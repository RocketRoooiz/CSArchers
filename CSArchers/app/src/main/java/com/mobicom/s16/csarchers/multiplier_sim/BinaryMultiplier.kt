package com.mobicom.s16.csarchers.multiplier_sim

import com.mobicom.s16.csarchers.Size
import kotlin.math.pow

class BinaryMultiplier {
    private var current_multiplicand: String = ""
    private var current_multiplier: String = ""
    private var current_product: String = ""

    var unsigned_min: ULong = 0u
        private set
    var unsigned_mid: ULong = 0u
        private set
    var unsigned_max: ULong = 0u
        private set

    var constant_m = 0uL
    var steps: MutableList<MultiplierStep> = mutableListOf<MultiplierStep>()
        private set

    var size: Size = Size._8BITS
        set(value) {
            field = value
            updateRanges()
        }

    init {
        updateRanges()
    }

    private fun updateRanges() {
        unsigned_min = 0u
        unsigned_mid = (2.0.pow(size.value - 1)).toULong()
        unsigned_max = (2.0.pow(size.value).toULong() - 1u) // Fixed potential overflow
    }

    fun operate(multiplicand: String, multiplier: String): String {
        current_multiplicand = multiplicand
        current_multiplier = multiplier

        // Validate binary string
        require(multiplicand.matches(Regex("^[01]+\$"))) { "Invalid binary string for multiplicand" } // Added ^ and $ for exact match
        require(multiplicand.length == size.value) { "Multiplicand length != ${size.value}" }
        require(multiplier.matches(Regex("^[01]+\$"))) { "Invalid binary string for multiplier" }
        require(multiplier.length == size.value) { "Multiplier length != ${size.value}" }

        var a = 0uL // Accumulator
        val m = try {
            multiplicand.toULong(2) // Multiplicand
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid binary format for multiplicand")
        }
        constant_m = m

        var q = try {
            multiplier.toULong(2) // Multiplier
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid binary format for multiplier")
        }

        var carry: Boolean

        steps.clear()

        for (i in 0 until size.value) {
            val a_initial = a
            val q_initial = q

            // Add multiplicand to accumulator if LSB of q is 1
            val add_m = q and 1uL == 1uL
            a += if (q and 1uL == 1uL) m else 0uL

            // Check for carry
            carry = a > unsigned_max
            val c = carry

            a = a and unsigned_max
            val a_plus_m = a

            // Shift q right and bring in LSB from a
            q = q shr 1
            q = q or ((a and 1uL) shl (size.value - 1)) // Fixed: properly set MSB of q
            val q_shr = q

            // Shift a right and handle carry
            a = a shr 1
            if (carry) {
                a = a or unsigned_mid // Set the MSB if there was a carry
            }
            val a_shr = a

            // Add a current step to the step list.
            steps.add(MultiplierStep(i + 1, add_m, c, a_initial, a_plus_m, a_shr, q_initial, q_shr))
        }

        // Combine a and q to form the product
        val product = (a shl size.value) or q // Using OR instead of ADD for bit concatenation

        current_product = product.toString(2).padStart(size.value * 2, '0')

        return current_product
    }
}