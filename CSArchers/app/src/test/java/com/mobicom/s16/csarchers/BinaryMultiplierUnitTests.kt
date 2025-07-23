package com.mobicom.s16.csarchers

import com.mobicom.s16.csarchers.multiplier_sim.BinaryMultiplier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BinaryMultiplicationIn8bits {
    companion object {
        lateinit var multiplier: BinaryMultiplier
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            multiplier = BinaryMultiplier()
            multiplier.size = Size._8BITS
        }
    }

    @Test
    fun testing1digitFactors() {
        assertEquals("0000000001001000", multiplier.operate("00001001", "00001000"))
    }

    @Test
    fun testing2digitFactors() {
        assertEquals("0000000101010100", multiplier.operate("00010001", "00010100"))
    }

    @Test
    fun testing3digitFactors() {
        assertEquals("0110001001011011", multiplier.operate("11100111", "01101101"))
    }

    @Test
    fun testingDifferentDigitFactors() {
        assertEquals("0000000010000111", multiplier.operate("00001111", "00001001"))
        assertEquals("0100111000110100", multiplier.operate("01011011", "11011100"))
        assertEquals("0000001001000011", multiplier.operate("11000001", "00000011"))
    }

    @Test
    fun testingCommutativeProperty() {
        assertEquals("0000000000001111", multiplier.operate("00000101", "00000011"))
        assertEquals("0000000000001111", multiplier.operate("00000011", "00000101"))
    }

    @Test
    fun testingIdentityProperty() {
        assertEquals("0000000000111010", multiplier.operate("00000001", "00111010"))
    }

    @Test
    fun testingZeroProperty() {
        assertEquals("0000000000000000", multiplier.operate("01111111", "00000000"))
    }

    @Test
    fun testingMaxFactors() {
        assertEquals("1111111000000001", multiplier.operate("11111111", "11111111"))
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            multiplier.operate("001", "00000010")
        }

        assertEquals(exception.message, "Multiplicand length != 8")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            multiplier.operate("00000011", "0000000100")
        }

        assertEquals(exception.message, "Multiplier length != 8")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            multiplier.operate("ABC", "00010000")
        }
        assertEquals(exception.message, "Invalid binary string for multiplicand")

        exception = assertThrows(IllegalArgumentException::class.java) {
            multiplier.operate("00010000", "ABC")
        }
        assertEquals(exception.message, "Invalid binary string for multiplier")
    }
}