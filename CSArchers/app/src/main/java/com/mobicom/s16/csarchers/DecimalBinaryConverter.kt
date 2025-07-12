package com.mobicom.s16.csarchers

import kotlin.math.pow
class DecimalBinaryConverter {
    var current_input: String = ""
    var current_output: String = ""
    private var signed_min: Long = 0
    private var signed_max: Long = 0
    private var unsigned_min: ULong = 0u
    private var unsigned_max: ULong = 0u

    var isSigned = true
    var size: Size = Size._8BITS
        set(value) {
            field = value
            updateRanges()
        }

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
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid number format")
        }

        return current_output
    }

    fun convertBinary2Decimal(input: String): String {
        current_input = input

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
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid binary format")
        }

        return current_output
    }
}