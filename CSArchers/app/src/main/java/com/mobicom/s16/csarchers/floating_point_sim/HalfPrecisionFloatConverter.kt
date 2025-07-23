package com.mobicom.s16.csarchers.floating_point_sim

import java.math.BigDecimal

/*
This is still work in progress.
There are still bugs in this converter.
Do not use it for real half-precision floating point conversions.
Integrated to its simulation activity to show that it works.
*/
class HalfPrecisionFloatConverter {

    companion object {
        private const val EXP_MAX = "11111"
        private const val EXP_ZERO = "00000"
    }

    fun convert(mantissaInput: String, exponentInput: String, isBase10: Boolean): ConversionResult {
        val mantissa = mantissaInput.trim()
        val exponent = exponentInput.trim()

        val binary = if (isBase10) handleBase10(mantissa, exponent) else handleBase2(mantissa, exponent)
        val hex = binaryToHex(binary)
        val msg = feedbackMessage(hex)
        return ConversionResult(binary, hex, msg)
    }

    private fun binaryToHex(bin: String): String {
        val bits = bin.replace(" ", "").padEnd(16, '0')
        return bits.chunked(4).joinToString("") { chunk ->
            val n = chunk.foldIndexed(0) { i, acc, c -> acc + (c.digitToInt() shl (3 - i)) }
            if (n < 10) "$n" else ('A' + (n - 10)).toString()
        }
    }

    private fun handleBase2(mantissa: String, exponent: String): String {
        if (!isValidBase2(mantissa, exponent)) return "0 $EXP_MAX 0100000000" // sNaN

        if (mantissa.equals("snan", true)) return "0 $EXP_MAX 0100000000"
        if (mantissa.equals("qnan", true)) return "0 $EXP_MAX 1000000000"

        var msb = "0"
        var cleanMantissa = mantissa
        if (mantissa.startsWith("-")) {
            msb = "1"
            cleanMantissa = mantissa.substring(1)
        }

        if (cleanMantissa == "0" || cleanMantissa == "0.0") {
            return "$msb $EXP_ZERO 0000000000"
        }

        val (formattedMantissa, adjustedExp) = toOneFFormat(cleanMantissa, exponent)

        val mantissaBits = formattedMantissa.drop(2)
        if (adjustedExp.toInt() > 15 || mantissaBits.length > 30) {
            return "$msb $EXP_MAX 0000000000"
        }

        val (mant, exp) = if (adjustedExp.toInt() < -14)
            denormalize(formattedMantissa, adjustedExp) else Pair(formattedMantissa, adjustedExp)

        return encode(msb, mant, exp)
    }

    private fun encodeMantissa(mant: String, exponent: String): String {
        val bits = mant.drop(2) + "0000000000000000" // pad extra to avoid index issues
        val base = bits.take(10)
        val guard = bits[10]
        val sticky = bits.drop(11).any { it == '1' }
        val needsRounding = toRoundUp(base, guard, sticky)
        return if (needsRounding) handleCarry(base) else base
    }

    private fun toRoundUp(lsb10: String, guard: Char, sticky: Boolean): Boolean {
        val lsbEven = lsb10.last() == '0'
        return guard == '1' && (!lsbEven || sticky)
    }

    private fun handleBase10(mantissa: String, exponent: String): String {
        if (!isValidBase10(mantissa, exponent)) return "0 $EXP_MAX 0100000000" // sNaN

        if (mantissa.equals("snan", true)) return "0 $EXP_MAX 0100000000"
        if (mantissa.equals("qnan", true)) return "0 $EXP_MAX 1000000000"

        var sign = "0"
        var absMantissa = mantissa
        if (mantissa.startsWith("-")) {
            sign = "1"
            absMantissa = mantissa.substring(1)
        }

        if (absMantissa == "0" || absMantissa == "0.0") {
            return "$sign $EXP_ZERO 0000000000"
        }

        // ✅ FIX: Evaluate true value and compare to max half-float (65504)
        return try {
            val base = BigDecimal(exponent)
            val scaled = BigDecimal(absMantissa).multiply(BigDecimal.TEN.pow(base.toInt()))

            if (scaled > BigDecimal("65504")) {
                "$sign $EXP_MAX 0000000000" // overflow
            } else {
                // Convert to binary
                val binary = convertDecimalToBinary(scaled.toString())
                // Treat result as base-2 input
                val (formatted, exp) = toOneFFormat(binary, "0")
                val (mant, adjExp) = if (exp.toInt() < -14) denormalize(formatted, exp) else Pair(formatted, exp)
                encode(sign, mant, adjExp)
            }
        } catch (e: Exception) {
            "$sign $EXP_MAX 0000000000" // fallback to +∞
        }
    }

    private fun encode(sign: String, mantissa: String, exponent: String): String {
        val (m, exp) = encodeExponent(mantissa, exponent)
        val encodedMant = encodeMantissa(m, exp)
        return "$sign $exp $encodedMant"
    }

    private fun encodeExponent(mantissa: String, exponent: String): Pair<String, String> {
        if (mantissa == "0.0") return mantissa to EXP_ZERO
        val biasExp = exponent.toInt() + 15
        if (biasExp < 0 || biasExp >= 32) return "0.0" to EXP_MAX
        val expBin = biasExp.toString(2).padStart(5, '0')
        return mantissa to expBin
    }

    private fun handleCarry(mant: String): String {
        val bits = mant.toCharArray()
        for (i in 9 downTo 0) {
            if (bits[i] == '0') {
                bits[i] = '1'
                for (j in i + 1..9) bits[j] = '0'
                return bits.concatToString()
            }
        }
        return "1000000000"
    }

    private fun toOneFFormat(m: String, e: String): Pair<String, String> {
        var mant = m
        var exp = e.toInt()

        if (mant.startsWith("1.")) return mant to exp.toString()

        if (mant.startsWith("0.")) {
            val idx = mant.indexOf('1')
            if (idx == -1) return "0.0" to "0"
            return "1.${mant.drop(idx + 1)}" to (exp - (idx - 1)).toString()
        }

        val dotIndex = mant.indexOf('.')
        return if (dotIndex != -1) {
            val move = dotIndex - 1
            "1.${mant.replace(".", "").drop(1)}" to (exp + move).toString()
        } else {
            "1.${mant.drop(1)}" to (exp + mant.length - 1).toString()
        }
    }

    private fun denormalize(m: String, e: String): Pair<String, String> {
        val raw = m.replace(".", "")
        val zerosToAdd = kotlin.math.abs(e.toInt() + 14) - 1
        val padded = "0".repeat(zerosToAdd) + raw
        return "0.${padded}" to "-15"
    }

    private fun base10Shift(d: String, e: String): String {
        var dec = if ("." !in d) "$d." else d
        var exp = e.toInt()

        while (exp != 0) {
            val idx = dec.indexOf('.')
            dec = dec.replace(".", "")
            dec = when {
                exp > 0 -> (dec + "0").replaceRange(idx + 1, idx + 1, ".")
                else -> "0." + dec.padStart(dec.length + 1, '0')
            }
            exp += if (exp > 0) -1 else 1
        }
        return dec
    }

    private fun convertDecimalToBinary(dec: String): String {
        val (whole, frac) = if ("." in dec) dec.split(".") else listOf(dec, "0")
        val intPart = whole.toBigInteger().toString(2)
        val fracPart = buildString {
            var f = BigDecimal("0.$frac")
            repeat(30) {
                f = f.multiply(BigDecimal(2))
                append(if (f >= BigDecimal.ONE) {
                    f -= BigDecimal.ONE
                    '1'
                } else '0')
            }
        }
        return "$intPart.$fracPart"
    }

    private fun safeToInt(str: String): Int = try {
        BigDecimal(str).toInt()
    } catch (e: Exception) {
        0
    }

    private fun isValidBase2(m: String, e: String): Boolean =
        Regex("^-?(0|1)+(\\.(0|1)+)?").matches(m) && Regex("^-?\\d+").matches(e) ||
                m.lowercase() in listOf("snan", "qnan")

    private fun isValidBase10(m: String, e: String): Boolean =
        Regex("^-?\\d+(\\.\\d+)?").matches(m) && Regex("^-?\\d+").matches(e) ||
                m.lowercase() in listOf("snan", "qnan")

    private fun feedbackMessage(hex: String): String {
        val cases = mapOf(
            "0001" to "Smallest Positive Denormalized Number",
            "8001" to "Smallest Negative Denormalized Number",
            "03FF" to "Largest Denormalized Number",
            "0400" to "Smallest Positive Normalized Number",
            "8400" to "Smallest Negative Normalized Number",
            "3BFF" to "Largest Number less than One",
            "3C01" to "Smallest Number larger than One",
            "7BFF" to "Largest Normal Number",
            "FBFF" to "Largest Negative Normal Number",
            "8000" to "Negative Zero",
            "0000" to "Positive Zero",
            "7C00" to "Special Case: Positive Infinity",
            "FC00" to "Special Case: Negative Infinity",
            "7D00" to "Special Case: Signalling NaN",
            "7E00" to "Special Case: Quiet NaN"
        )
        val hexUpper = hex.uppercase()
        return cases[hexUpper] ?: if (hexUpper > "0001" && hexUpper < "03FF") "Denormalized Number" else "Normalized Finite"
    }

    data class ConversionResult(val binary: String, val hex: String, val message: String)
}