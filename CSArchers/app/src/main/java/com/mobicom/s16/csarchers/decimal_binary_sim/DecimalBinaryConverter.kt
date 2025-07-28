package com.mobicom.s16.csarchers.decimal_binary_sim

import com.mobicom.s16.csarchers.Size
import kotlin.math.pow

class DecimalBinaryConverter {
    var current_input: String = ""
    var current_output: String = ""
    var signed_min: Long = 0
        private set // setter can be used inside the class.
    var signed_max: Long = 0
        private set // setter can be used inside the class.
    var unsigned_min: ULong = 0u
        private set // setter can be used inside the class.
    var unsigned_max: ULong = 0u
        private set // setter can be used inside the class.

    var isSigned = true
    var size: Size = Size._8BITS
        set(value) {
            field = value
            updateRanges()
        }

    var unsigned_decimal_to_binary_steps: MutableList<UnsignedDecimalToBinaryStep>
    = mutableListOf<UnsignedDecimalToBinaryStep>()
        private set
    var unsigned_binary_to_decimal_steps: MutableList<UnsignedBinaryToDecimalStep>
    = mutableListOf<UnsignedBinaryToDecimalStep>()
        private set

    init {
        updateRanges()
    }

    private fun updateRanges() {
        signed_min = (-1 * (2.0.pow(size.value - 1))).toLong()
        signed_max = ((2.0.pow(size.value - 1)) - 1).toLong()
        unsigned_min = 0u
        unsigned_max = (2.0.pow(size.value) - 1).toULong()
    }

    fun convertDecimal2Binary(input: String): String {
        current_input = input

        unsigned_decimal_to_binary_steps.clear()

        try {
            if (isSigned) { // is signed
                val signed_decimal = input.toLong()
                require(signed_decimal in signed_min..signed_max) { "Input out-of-range" }

                // Handle negative numbers properly
                current_output = if (signed_decimal < 0) {
                    val twosComplement = (1L shl size.value) + signed_decimal
                    twosComplement.toString(2).takeLast(size.value) // Ensure exactly N bits
                } else {
                    signed_decimal.toString(2).padStart(size.value, '0')
                }

            } else { // is unsigned
                val unsigned_decimal = input.toULong()
                require(unsigned_decimal in unsigned_min..unsigned_max) { "Input out-of-range" }
                current_output = unsigned_decimal.toString(2).padStart(size.value, '0')

                var bit = 0
                var dividend = unsigned_decimal.toLong()
                var quotient = 0L
                var remainder = 0L
                while (dividend > 0) {
                    quotient = dividend / 2L
                    remainder = dividend % 2L
                    bit++

                    unsigned_decimal_to_binary_steps.add(UnsignedDecimalToBinaryStep(dividend, quotient, remainder, bit))

                    dividend = quotient
                }
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid number format")
        }

        return current_output
    }

    fun convertBinary2Decimal(input: String): String {
        current_input = input

        unsigned_binary_to_decimal_steps.clear()

        // Validate binary string
        require(input.matches(Regex("[01]+"))) { "Invalid binary string" }
        require(input.length == size.value) { "Binary string length doesn't match selected size" }

        try {
            if (isSigned) { // is signed
                // For signed numbers, we need to handle two's complement
                val signed_decimal = if (input[0] == '1') {
                    // Negative number in two's complement
                    val value = input.toLong(2)
                    value - (1L shl size.value)
                } else {
                    // Positive number
                    input.toLong(2)
                }
                require(signed_decimal in signed_min..signed_max) { "Input out-of-range" }
                current_output = signed_decimal.toString()
            } else { // is unsigned
                val unsigned_decimal = input.toULong(2)
                require(unsigned_decimal in unsigned_min..unsigned_max) { "Input out-of-range" }
                current_output = unsigned_decimal.toString()

                for (i in 0 until size.value) {
                    val bit = if (current_input[size.value - i - 1] == '1') 1L else 0L

                    val powerOfTwo = 1L shl i

                    val product = powerOfTwo * bit

                    unsigned_binary_to_decimal_steps.add(UnsignedBinaryToDecimalStep(bit, i, powerOfTwo, product))
                }
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid binary format")
        }

        return current_output
    }
}