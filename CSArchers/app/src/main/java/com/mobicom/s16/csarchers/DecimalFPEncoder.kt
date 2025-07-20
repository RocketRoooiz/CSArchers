package com.mobicom.s16.csarchers

import java.math.BigDecimal
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

object DecimalFPEncoder {
    fun encodeDecimalFP(value: BigDecimal, bits: Int): ByteArray {
        val sign = if (value.signum() < 0) 0x80 else 0x00 // sign bit
        //val exponent: Int
        val coefficient: Long

        val scaledValue = value.stripTrailingZeros()
        val plainStr = scaledValue.abs().toPlainString().replace(".", "")
        coefficient = plainStr.toLong()

        //exponent = -scaledValue.scale()

        return when (bits) {
            32 -> {
                val buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
                buffer.putInt(encodeDecimal32(sign, 0, coefficient))
                buffer.array()
            }
            64 -> {
                val buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN)
                buffer.putLong(encodeDecimal64(sign, 0, coefficient))
                buffer.array()
            }
            else -> throw IllegalArgumentException("Unsupported decimal format")
        }
    }

    private fun encodeDecimal32(sign: Int, exponent: Int, coefficient: Long): Int {
        val biasedExp = exponent + 101 // decimal32 bias is 101
        val coeffStr = coefficient.toString().padStart(7, '0') // Ensure 7 digits
        val msd = coeffStr[0].digitToInt() // Most significant digit
        val remainingDigits = coeffStr.substring(1) // 6 digits for DPD

        // Extract top 2 bits of biased exponent
        val expHigh = (biasedExp shr 6) and 0b11 // bits 7 and 6
        val expLow = biasedExp and 0b0011_1111    // lower 6 bits (bits 5 to 0)

        // --- Build combination field ---
        val comboField = when (msd) {
            in 0..7 -> { // minor MSD: combo = ee ddd
                (expHigh shl 3) or msd
            }
            8, 9 -> {    // major MSD: combo = 11 ee d
                val d = msd - 8
                0b11000 or (expHigh shl 1) or d
            }
            else -> throw IllegalArgumentException("Invalid MSD: $msd")
        }

        // --- Convert 6-digit coefficient remainder to DPD ---
        val dpdBits = denselyPackedBCD32(remainingDigits) // returns Int (17 bits)

        // --- Construct full decimal32 layout ---
        return (sign shl 31) or           // 1 bit
                (comboField shl 26) or     // 5 bits
                (expLow shl 20) or         // 6 bits
                dpdBits                    // 20 bits
    }


    private fun encodeDecimal64(sign: Int, exponent: Int, coefficient: Long): Long {
        val biasedExp = exponent + 398 // Decimal64 bias is 398

        // Pad coefficient to 16 digits
        val coeffStr = coefficient.toString().padStart(16, '0')
        val msd = coeffStr[0].digitToInt() // Most significant digit
        val remainingDigits = coeffStr.substring(1) // 15 digits for DPD

        // Extract top 2 bits of exponent
        val expHigh = (biasedExp shr 8) and 0b11 // bits 9 and 8
        val expLow = biasedExp and 0xFF          // lower 8 bits (bits 7–0)

        // --- Build combination field ---
        val comboField = when (msd) {
            in 0..7 -> { // Minor MSD
                (expHigh shl 3) or msd
            }
            8, 9 -> { // Major MSD
                val d = msd - 8
                0b11000 or (expHigh shl 1) or d
            }
            else -> throw IllegalArgumentException("Invalid MSD: $msd")
        }

        // --- Convert remaining 15 digits to DPD ---
        val dpdBits = denselyPackedBCD64(remainingDigits) // Returns Long (50 bits)

        // --- Construct full decimal64 layout ---
        return (sign.toLong() shl 63) or         // 1 bit
                (comboField.toLong() shl 58) or   // 5 bits
                (expLow.toLong() shl 50) or       // 8 bits
                dpdBits                           // 50 bits
    }

    private fun denselyPackedBCD32(numberStr: String): Int {
        val padded = numberStr.padStart(6, '0')
        var result = 0

        for (i in 0 until padded.length step 3) {
            val chunk = padded.substring(i, i + 3)
            val dpdBits = convertToDenselyPackedBCDChunk(chunk)
            result = (result shl 10) or dpdBits
        }

        return result
    }

    private fun denselyPackedBCD64(numberStr: String): Long {
        val padded = numberStr.padStart(15, '0')
        var result = 0L

        for (i in 0 until padded.length step 3) {
            val chunk = padded.substring(i, i + 3)
            val dpdBits = convertToDenselyPackedBCDChunk(chunk)
            result = (result shl 10) or dpdBits.toLong()
        }

        return result
    }

    private fun convertToDenselyPackedBCDChunk(chunk: String): Int {
        val digits = chunk.map { it.digitToInt() }
        val binary = digits.map { it.toString(2).padStart(4, '0') }.joinToString("")

        val a = binary[0]; val b = binary[1]; val c = binary[2]; val d = binary[3]
        val e = binary[4]; val f = binary[5]; val g = binary[6]; val h = binary[7]
        val i = binary[8]; val j = binary[9]; val k = binary[10]; val l = binary[11]

        val key = "" + a + e + i

        val dpd = when (key) {
            "000" -> "$b$c$d$f$g$h 0$j$k$l"
            "001" -> "$b$c$d$f$g$h 100$l"
            "010" -> "$b$c$d$j$k$h 101$l"
            "011" -> "$b$c$d 10$h 111$l"
            "100" -> "$j$k$d$f$g$h 110$l"
            "101" -> "$f$g$d 01$h 111$l"
            "110" -> "$j$k$d 00$h 111$l"
            "111" -> "00$d 11$h 111$l"
            else -> throw IllegalArgumentException("Invalid DPD key")
        }.replace(" ", "")

        return dpd.toInt(2)
    }
}
