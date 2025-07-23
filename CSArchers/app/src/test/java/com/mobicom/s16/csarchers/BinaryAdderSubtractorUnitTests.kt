package com.mobicom.s16.csarchers

import com.mobicom.s16.csarchers.binary_add_sub_sim.BinaryAdderSubtractor
import com.mobicom.s16.csarchers.binary_add_sub_sim.OpType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    BinaryAdditionTests::class, BinarySubtractionTests::class
)
class BinaryAdderSubtractorUnitTests

@RunWith(Suite::class)
@SuiteClasses(
    BinaryAdditionIn8BitsUnsigned::class, BinaryAdditionIn8BitsSigned::class,
    BinaryAdditionIn16BitsUnsigned::class, BinaryAdditionIn16BitsSigned::class
)
class BinaryAdditionTests

@RunWith(JUnit4::class)
class BinaryAdditionIn8BitsUnsigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._8BITS
            adderSubtractor.operation = OpType.ADD
            adderSubtractor.isSigned = false
        }
    }

    @Test
    fun testingUnsignedSum() {
        assertEquals("01011011", adderSubtractor.operate("00110101", "00100110"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingOverflowedSum() {
        assertEquals("01001101", adderSubtractor.operate("10101111", "10011110"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroSum() {
        assertEquals("00000000", adderSubtractor.operate("00000000", "00000000"))
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("001", "00000010")
        }

        assertEquals(exception.message, "input 1 length != 8")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000011", "0000000100")
        }

        assertEquals(exception.message, "input 2 length != 8")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABC", "00010000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00010000", "ABC")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinaryAdditionIn8BitsSigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._8BITS
            adderSubtractor.operation = OpType.ADD
            adderSubtractor.isSigned = true
        }
    }

    @Test
    fun testingPositiveSum() {
        assertEquals("01001000", adderSubtractor.operate("00110111", "00010001"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeSum() {
        assertEquals("00101110", adderSubtractor.operate("01110011", "10111011"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroSum() {
        assertEquals("00000000", adderSubtractor.operate("00000000", "00000000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingPositiveOverflow() {
        assertEquals("10001010", adderSubtractor.operate("01101110", "00011100"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeOverflow() {
        assertEquals("01101010", adderSubtractor.operate("11001110", "10011100"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("001", "11000010")
        }

        assertEquals(exception.message, "input 1 length != 8")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000001", "1000011100")
        }

        assertEquals(exception.message, "input 2 length != 8")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDE", "11111111")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("11111111", "ABCDE")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinaryAdditionIn16BitsUnsigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._16BITS
            adderSubtractor.operation = OpType.ADD
            adderSubtractor.isSigned = false
        }
    }

    @Test
    fun testingUnsignedSum() {
        assertEquals("1111001000110000", adderSubtractor.operate("0111010100110000", "0111110100000000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingOverflowedSum() {
        assertEquals("0000000000000000", adderSubtractor.operate("0111111111111111", "1000000000000001"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroSum() {
        assertEquals("0000000000000000", adderSubtractor.operate("0000000000000000", "0000000000000000"))
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000000001", "0000000000000010")
        }

        assertEquals(exception.message, "input 1 length != 16")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0000000000000011", "00000001000000000")
        }

        assertEquals(exception.message, "input 2 length != 16")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDEFGHIJKLMNOP", "0001000000000000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0001000000000000", "ABCDEFGHIJKLMNOP")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinaryAdditionIn16BitsSigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._16BITS
            adderSubtractor.operation = OpType.ADD
            adderSubtractor.isSigned = true
        }
    }

    @Test
    fun testingPositiveSum() {
        assertEquals("0001011101110000", adderSubtractor.operate("0000001111101000", "0001001110001000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeSum() {
        assertEquals("1000010010000010", adderSubtractor.operate("1011100010100000", "1100101111100010"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroSum() {
        assertEquals("0000000000000000", adderSubtractor.operate("0000000000000000", "0000000000000000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingPositiveOverflow() {
        assertEquals("1000111001011000", adderSubtractor.operate("0110101000011010", "0010010000111110"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeOverflow() {
        assertEquals("0111111111111111", adderSubtractor.operate("1000000000000001", "1111111111111110"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000000001", "0000000000000010")
        }

        assertEquals(exception.message, "input 1 length != 16")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0000000000000011", "00000001000000000")
        }

        assertEquals(exception.message, "input 2 length != 16")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDEFGHIJKLMNOP", "0001000000000000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0001000000000000", "ABCDEFGHIJKLMNOP")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(Suite::class)
@SuiteClasses(
    BinarySubtractionIn8BitsUnsigned::class, BinarySubtractionIn8BitsSigned::class,
    BinarySubtractionIn16BitsUnsigned::class, BinarySubtractionIn16BitsSigned::class
)
class BinarySubtractionTests

@RunWith(JUnit4::class)
class BinarySubtractionIn8BitsUnsigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._8BITS
            adderSubtractor.operation = OpType.SUBTRACT
            adderSubtractor.isSigned = false
        }
    }

    @Test
    fun testingUnsignedDifference() {
        assertEquals("00001001", adderSubtractor.operate("00001011", "00000010"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingOverflowedDifference() {
        assertEquals("11111110", adderSubtractor.operate("00000001", "00000011"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroDifference() {
        assertEquals("00000000", adderSubtractor.operate("00000000", "00000000"))
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("001", "00000010")
        }

        assertEquals(exception.message, "input 1 length != 8")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000011", "0000000100")
        }

        assertEquals(exception.message, "input 2 length != 8")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABC", "00010000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00010000", "ABC")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinarySubtractionIn8BitsSigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._8BITS
            adderSubtractor.operation = OpType.SUBTRACT
            adderSubtractor.isSigned = true
        }
    }

    @Test
    fun testingPositiveDifference() {
        assertEquals("00000011", adderSubtractor.operate("11111110", "11111011"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeDifference() {
        assertEquals("11100101", adderSubtractor.operate("01100100", "01111111"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroDifference() {
        assertEquals("00000000", adderSubtractor.operate("00000000", "00000000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingPositiveOverflow() {
        assertEquals("10000000", adderSubtractor.operate("01100100", "11100100"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeOverflow() {
        assertEquals("01111001", adderSubtractor.operate("11011101", "01100100"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("001", "11000010")
        }

        assertEquals(exception.message, "input 1 length != 8")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("00000001", "1000011100")
        }

        assertEquals(exception.message, "input 2 length != 8")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDE", "11111111")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("11111111", "ABCDE")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinarySubtractionIn16BitsUnsigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._16BITS
            adderSubtractor.operation = OpType.SUBTRACT
            adderSubtractor.isSigned = false
        }
    }

    @Test
    fun testingUnsignedDifference() {
        assertEquals("0100100000011000", adderSubtractor.operate("1101111100100111", "1001011100001111"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingOverflowedDifference() {
        assertEquals("1111111110101110", adderSubtractor.operate("0010001011010110", "0010001100101000"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroDifference() {
        assertEquals("0000000000000000", adderSubtractor.operate("0000000000000000", "0000000000000000"))
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("000011", "0000001000000011")
        }

        assertEquals(exception.message, "input 1 length != 16")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0011010010000011", "00000111111001111")
        }

        assertEquals(exception.message, "input 2 length != 16")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDEFGHIJ", "0000000000010000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0000000000010000", "ABCDEFGHIJ")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}

@RunWith(JUnit4::class)
class BinarySubtractionIn16BitsSigned {
    companion object {
        lateinit var adderSubtractor: BinaryAdderSubtractor
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            adderSubtractor = BinaryAdderSubtractor()
            adderSubtractor.size = Size._16BITS
            adderSubtractor.operation = OpType.SUBTRACT
            adderSubtractor.isSigned = true
        }
    }

    @Test
    fun testingPositiveDifference() {
        assertEquals("0111110100001111", adderSubtractor.operate("0111000011001000", "1111001110111001"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeDifference() {
        assertEquals("1100010001100110", adderSubtractor.operate("1110100011010101", "0010010001101111"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingZeroDifference() {
        assertEquals("0000000000000000", adderSubtractor.operate("0000000000000000", "0000000000000000"))
        assertEquals(false, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingPositiveOverflow() {
        assertEquals("1000011111101010", adderSubtractor.operate("0110110101000011", "1110010101011001"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingNegativeOverflow() {
        assertEquals("0111111111111111", adderSubtractor.operate("1000000000000001", "0000000000000010"))
        assertEquals(true, adderSubtractor.isOverflowed)
    }

    @Test
    fun testingShortInputs() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("000011", "0000001000000011")
        }

        assertEquals(exception.message, "input 1 length != 16")
    }

    @Test
    fun testingLongInputs() { // or out of range
        val exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0011010010000011", "00000111111001111")
        }

        assertEquals(exception.message, "input 2 length != 16")
    }

    @Test
    fun testingInvalidInputs() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("ABCDEFGHIJ", "0000000000010000")
        }
        assertEquals(exception.message, "Invalid binary string for input 1")

        exception = assertThrows(IllegalArgumentException::class.java) {
            adderSubtractor.operate("0000000000010000", "ABCDEFGHIJ")
        }
        assertEquals(exception.message, "Invalid binary string for input 2")
    }
}