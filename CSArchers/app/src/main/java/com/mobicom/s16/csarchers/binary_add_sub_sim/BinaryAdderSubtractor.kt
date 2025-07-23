package com.mobicom.s16.csarchers.binary_add_sub_sim

import com.mobicom.s16.csarchers.Size
import kotlin.math.pow

enum class OpType {
    ADD,
    SUBTRACT
}

class BinaryAdderSubtractor {
    /*
    private var leftOperandSigned: Long = 0
    private var rightOperandSigned: Long = 0
    private var leftOperandUnsigned: ULong = 0u
    private var rightOperandUnsigned: ULong = 0u
    */

    private var current_input1: String = ""
    private var current_input2: String = ""
    private var current_output: String = ""

    var signed_min: Long = 0
        private set // setter can be used inside the class.
    var signed_max: Long = 0
        private set // setter can be used inside the class.
    var unsigned_min: ULong = 0u
        private set // setter can be used inside the class.
    var unsigned_max: ULong = 0u
        private set // setter can be used inside the class.

    var operation: OpType = OpType.ADD
    var isSigned = true

    var isOverflowed = false
        private set // isOverflowed can be modified inside the class.

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

    private fun convertToSignedDecimal(input: String): Long {
        if (input[0] == '1') {
            // Negative number in two's complement
            val value = input.toLong(2)
            return value - (1L shl size.value)
        } else {
            // Positive number
            return input.toLong(2)
        }
    }

    fun operate(input1: String, input2: String): String {
        current_input1 = input1
        current_input2 = input2

        // Validate binary string
        require(input1.matches(Regex("[01]+"))) { "Invalid binary string for input 1" }
        require(input1.length == size.value) { "input 1 length != ${size.value}" }
        require(input2.matches(Regex("[01]+"))) { "Invalid binary string for input 2" }
        require(input2.length == size.value) { "input 2 length != ${size.value}" }

        try {
            if (isSigned) { // is signed
                // Handle string inputs
                val signed_decimal_input1 =  try {
                    convertToSignedDecimal(input1)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Invalid binary format for input 1")
                }

                val signed_decimal_input2 = try {
                    convertToSignedDecimal(input2)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Invalid binary format for input 2")
                }

                var signed_decimal_output = when (operation) {
                    OpType.ADD -> {
                        val sum = signed_decimal_input1 + signed_decimal_input2
                        when {
                            sum > signed_max -> sum - (1L shl size.value)
                            sum < signed_min -> sum + (1L shl size.value)
                            else -> sum
                        }
                    }
                    OpType.SUBTRACT -> {
                        val diff = signed_decimal_input1 - signed_decimal_input2
                        when {
                            diff > signed_max -> diff - (1L shl size.value)
                            diff < signed_min -> diff + (1L shl size.value)
                            else -> diff
                        }
                    }
                }

                when (operation) {
                    OpType.ADD -> {
                        if (signed_decimal_input1 > 0 && signed_decimal_input2 > 0) {
                            isOverflowed = signed_decimal_output <= 0
                        } else if (signed_decimal_input1 < 0 && signed_decimal_input2 < 0) {
                            isOverflowed = signed_decimal_output >= 0
                        } else {
                            isOverflowed = false
                        }
                    }

                    // Overflow in subtraction: https://courses.cs.washington.edu/courses/cse370/00wi/sections/qs1.pdf
                    OpType.SUBTRACT -> {
                        if (signed_decimal_input1 > 0 && signed_decimal_input2 < 0) {
                            isOverflowed = signed_decimal_output <= 0
                        } else if (signed_decimal_input1 < 0 && signed_decimal_input2 > 0) {
                            isOverflowed = signed_decimal_output >= 0
                        } else {
                            isOverflowed = false
                        }
                    }
                }

                // Convert output into string.
                current_output = if (signed_decimal_output < 0) {
                    (signed_decimal_output.toLong() and ((1L shl size.value) - 1))
                        .toString(2)
                        .padStart(size.value, '0')
                } else {
                    signed_decimal_output.toString(2).padStart(size.value, '0')
                }
                current_output = current_output.slice((current_output.length - size.value)..(current_output.length - 1))
            } else { // is unsigned
                // Handle string inputs
                val unsigned_decimal_input1 =  try {
                    input1.toULong(2)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Invalid binary format for input 1")
                }

                val unsigned_decimal_input2 = try {
                    input2.toULong(2)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Invalid binary format for input 2")
                }

                // Perform operation with the converted inputs.
                val unsigned_decimal_output = when (operation) {
                    OpType.ADD -> unsigned_decimal_input1 + unsigned_decimal_input2
                    OpType.SUBTRACT -> unsigned_decimal_input1 - unsigned_decimal_input2
                } and unsigned_max

                // Check if overflow has occurred.
                when (operation) {
                    OpType.ADD -> {
                        isOverflowed = (unsigned_decimal_output < unsigned_decimal_input1 || unsigned_decimal_output < unsigned_decimal_input2)
                    }

                    // Overflow in subtraction: https://courses.cs.washington.edu/courses/cse370/00wi/sections/qs1.pdf
                    OpType.SUBTRACT -> {
                        isOverflowed = unsigned_decimal_input1 < unsigned_decimal_input2
                    }
                }

                // Convert output into string.
                current_output = unsigned_decimal_output.toString(2).padStart(size.value, '0')
                current_output = current_output.slice((current_output.length - size.value)..(current_output.length - 1))
            }
        } catch (e: NumberFormatException) {
            throw e
        }

        return current_output
    }
}