package com.mobicom.s16.csarchers

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
    DecimalToBinaryConversionTests::class, BinaryToDecimalConversionTests::class,
)
class DecimalBinaryConversionTests

@RunWith(Suite::class)
@SuiteClasses(
    DecimalToBinaryConversionIn8BitsUnsigned::class, DecimalToBinaryConversionIn8BitsSigned::class,
    DecimalToBinaryConversionIn16BitsUnsigned::class, DecimalToBinaryConversionIn16BitsSigned::class
)
class DecimalToBinaryConversionTests

@RunWith(JUnit4::class)
class DecimalToBinaryConversionIn8BitsUnsigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._8BITS
            converter.isSigned = false
        }
    }

    @Test
    fun testingUnsignedInput() {
        assertEquals(converter.convertDecimal2Binary("9"), "00001001")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("A91")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("256")
        }

        assertEquals(exception.message, "Input out-of-range")
    }

    @Test
    fun testingNegativeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("-1")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertDecimal2Binary("0"), "00000000")
    }
}

@RunWith(JUnit4::class)
class DecimalToBinaryConversionIn8BitsSigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._8BITS
            converter.isSigned = true
        }
    }

    @Test
    fun testingPositiveInput() {
        assertEquals(converter.convertDecimal2Binary("65"), "01000001")
    }

    @Test
    fun testingNegativeInput() {
        assertEquals(converter.convertDecimal2Binary("-70"), "10111010")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertDecimal2Binary("0"), "00000000")
    }

    @Test
    fun testingNegativeZeroInput() {
        assertEquals(converter.convertDecimal2Binary("-0"), "00000000")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("-ABCDE")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingOutOfRangePositiveInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("128")
        }

        assertEquals(exception.message, "Input out-of-range")
    }

    @Test
    fun testingOutOfRangeNegativeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("-129")
        }

        assertEquals(exception.message, "Input out-of-range")
    }
}

@RunWith(JUnit4::class)
class DecimalToBinaryConversionIn16BitsUnsigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._16BITS
            converter.isSigned = false
        }
    }

    @Test
    fun testingUnsignedInput() {
        assertEquals(converter.convertDecimal2Binary("35627"), "1000101100101011")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("2136A")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("65536")
        }

        assertEquals(exception.message, "Input out-of-range")
    }

    @Test
    fun testingNegativeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("-32768")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertDecimal2Binary("0"), "0000000000000000")
    }
}

@RunWith(JUnit4::class)
class DecimalToBinaryConversionIn16BitsSigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter
        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._16BITS
            converter.isSigned = true
        }
    }

    @Test
    fun testingPositiveInput() {
        assertEquals(converter.convertDecimal2Binary("12345"), "0011000000111001")
    }

    @Test
    fun testingNegativeInput() {
        assertEquals(converter.convertDecimal2Binary("-30000"), "1000101011010000")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertDecimal2Binary("0"), "0000000000000000")
    }

    @Test
    fun testingNegativeZeroInput() {
        assertEquals(converter.convertDecimal2Binary("-0"), "0000000000000000")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("ABCDEFGHIJKL")
        }

        assertEquals(exception.message, "Invalid number format")
    }

    @Test
    fun testingOutOfRangePositiveInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("32768")
        }

        assertEquals(exception.message, "Input out-of-range")
    }

    @Test
    fun testingOutOfRangeNegativeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertDecimal2Binary("-32769")
        }

        assertEquals(exception.message, "Input out-of-range")
    }
}

@RunWith(Suite::class)
@SuiteClasses(
    BinaryToDecimalConversionIn8BitsUnsigned::class, BinaryToDecimalConversionIn8BitsSigned::class,
    BinaryToDecimalConversionIn16BitsUnsigned::class, BinaryToDecimalConversionIn16BitsSigned::class
)
class BinaryToDecimalConversionTests

@RunWith(JUnit4::class)
class BinaryToDecimalConversionIn8BitsUnsigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter

        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._8BITS
            converter.isSigned = false
        }
    }

    @Test
    fun testingUnsignedInput() {
        assertEquals(converter.convertBinary2Decimal("11111111"), "255")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertBinary2Decimal("00000000"), "0")
    }

    @Test
    fun testingShortInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("0011")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingLongInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("000000001")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("111111111111")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("00A")
        }

        assertEquals(exception.message, "Invalid binary string")
    }
}

@RunWith(JUnit4::class)
class BinaryToDecimalConversionIn8BitsSigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter

        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._8BITS
            converter.isSigned = true
        }
    }

    @Test
    fun testingPositiveInput() {
        assertEquals(converter.convertBinary2Decimal("00011100"), "28")
    }

    @Test
    fun testingNegativeInput() {
        assertEquals(converter.convertBinary2Decimal("10000001"), "-127")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertBinary2Decimal("00000000"), "0")
    }

    @Test
    fun testingShortInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("11111")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingLongInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("0000000000")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("1100000000")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("010ABCDE")
        }

        assertEquals(exception.message, "Invalid binary string")
    }
}

@RunWith(JUnit4::class)
class BinaryToDecimalConversionIn16BitsUnsigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter

        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._16BITS
            converter.isSigned = false
        }
    }

    @Test
    fun testingUnsignedInput() {
        assertEquals(converter.convertBinary2Decimal("0011101010011000"), "15000")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertBinary2Decimal("0000000000000000"), "0")
    }

    @Test
    fun testingShortInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("0000000000011")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingLongInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("10000000000000000001")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("11111111111111111")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("ABCDEFGHIJKLMOPQ")
        }

        assertEquals(exception.message, "Invalid binary string")
    }
}

@RunWith(JUnit4::class)
class BinaryToDecimalConversionIn16BitsSigned {
    companion object {
        lateinit var converter: DecimalBinaryConverter

        @BeforeClass // Runs first before all of the unit tests in this class.
        @JvmStatic
        fun setup() {
            converter = DecimalBinaryConverter()
            converter.size = Size._16BITS
            converter.isSigned = true
        }
    }

    @Test
    fun testingPositiveInput() {
        assertEquals(converter.convertBinary2Decimal("0110010011010100"), "25812")
    }

    @Test
    fun testingNegativeInput() {
        assertEquals(converter.convertBinary2Decimal("1011001001011111"), "-19873")
    }

    @Test
    fun testingZeroInput() {
        assertEquals(converter.convertBinary2Decimal("0000000000000000"), "0")
    }

    @Test
    fun testingShortInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("0000010101")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingLongInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("100000000000000000000")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingOutOfRangeInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("100000000000000000001")
        }

        assertEquals(exception.message, "Binary string length doesn't match selected size")
    }

    @Test
    fun testingInvalidInput() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            converter.convertBinary2Decimal("1111ABCD0000EFGH")
        }

        assertEquals(exception.message, "Invalid binary string")
    }
}