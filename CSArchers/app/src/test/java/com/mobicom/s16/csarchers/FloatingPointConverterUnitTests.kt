package com.mobicom.s16.csarchers

import com.mobicom.s16.csarchers.floating_point_sim.HalfPrecisionFloatConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class HalfPrecisionFloatConverterTest {

    private val converter = HalfPrecisionFloatConverter()

    @Test
    fun testOnePointFive() {
        val result = converter.convert("1.5", "0", true)
        assertEquals("0 01111 1000000000", result.binary)
        assertEquals("3E00", result.hex)
    }

    @Test
    fun testZero() {
        val result = converter.convert("0", "0", false)
        assertEquals("0 00000 0000000000", result.binary)
        assertEquals("0000", result.hex)
    }

    @Test
    fun testNegativeZero() {
        val result = converter.convert("-0", "0", false)
        assertEquals("1 00000 0000000000", result.binary)
        assertEquals("8000", result.hex)
    }

    @Test
    fun testOne() {
        val result = converter.convert("1", "0", false)
        assertEquals("0 01111 0000000000", result.binary)
        assertEquals("3C00", result.hex)
    }

    @Test
    fun testTwo() {
        val result = converter.convert("2", "0", true)
        assertEquals("0 10000 0000000000", result.binary)
        assertEquals("4000", result.hex)
    }

    @Test
    fun testSmallestPositiveDenorm() {
        val result = converter.convert("1", "-24", false)
        assertEquals("0 00000 0000000001", result.binary)
        assertEquals("0001", result.hex)
    }

    @Test
    fun testLargestDenormalized() {
        val result = converter.convert("0.0000609756", "0", true)
        assertEquals("0 00000 1111111111", result.binary)
        assertEquals("03FF", result.hex)
    }

    @Test
    fun testPositiveInfinity() {
        val result = converter.convert("65520", "0", true)
        assertEquals("0 11111 0000000000", result.binary)
        assertEquals("7C00", result.hex)
    }

    @Test
    fun testSNan() {
        val result = converter.convert("snan", "0", false)
        assertEquals("0 11111 0100000000", result.binary)
        assertEquals("7D00", result.hex)
    }

    @Test
    fun testQNaN() {
        val result = converter.convert("qnan", "0", false)
        assertEquals("0 11111 1000000000", result.binary)
        assertEquals("7E00", result.hex)
    }

    @Test
    fun testRoundToEven() {
        val result = converter.convert("1.00097656", "0", true) // exactly between 1.0 and next representable
        assertEquals("0 01111 0000000001", result.binary)
        assertEquals("3C01", result.hex)
    }

    @Test
    fun testNegativeOnePointFive() {
        val result = converter.convert("-1.5", "0", true)
        assertEquals("1 01111 1000000000", result.binary)
        assertEquals("BE00", result.hex)
    }@Test fun testRandomDecimal1() {
        val result = converter.convert("3.25", "0", true)
        assertEquals("0 10000 0100000000", result.binary)
        assertEquals("4100", result.hex)
    }

    @Test fun testRandomDecimal2() {
        val result = converter.convert("0.03125", "0", true)
        assertEquals("0 01001 0000000000", result.binary)
        assertEquals("1200", result.hex)
    }

    @Test fun testRandomDecimal3() {
        val result = converter.convert("7.75", "0", true)
        assertEquals("0 10001 1110000000", result.binary)
        assertEquals("47C0", result.hex)
    }

    @Test fun testRandomDecimal4() {
        val result = converter.convert("15.875", "0", true)
        assertEquals("0 10011 1111000000", result.binary)
        assertEquals("4FF0", result.hex)
    }

    @Test fun testDecimalWithExponent1() {
        val result = converter.convert("6.25", "2", true)
        assertEquals("0 10011 1001000000", result.binary)
        assertEquals("4E40", result.hex)
    }

    @Test fun testDecimalWithNegativeExponent() {
        val result = converter.convert("3.25", "-1", true)
        assertEquals("0 01111 1010000000", result.binary)
        assertEquals("3E80", result.hex)
    }

    @Test fun testBinaryBaseInput1() {
        val result = converter.convert("101.1", "0", false)
        assertEquals("0 10000 1011000000", result.binary)
        assertEquals("42C0", result.hex)
    }

    @Test fun testBinaryBaseInput2() {
        val result = converter.convert("11.01", "-2", false)
        assertEquals("0 00000 0000110100", result.binary)
        assertEquals("0034", result.hex)
    }

    @Test fun testBinaryBaseInputLargeExponent() {
        val result = converter.convert(".11", "10", false)
        assertEquals("0 11111 0000000000", result.binary)
        assertEquals("7C00", result.hex)
    }

    @Test fun testBinaryUnderflowDenorm() {
        val result = converter.convert("110", "-11", false)
        assertEquals("0 00000 0000011101", result.binary)
        assertEquals("001D", result.hex)
    }

    @Test fun testNegativeBinaryDenorm() {
        val result = converter.convert("-1.001", "-10", false)
        assertEquals("1 00000 1111100000", result.binary)
        assertEquals("81F0", result.hex)
    }

    @Test fun testBinaryOverflow() {
        val result = converter.convert("1.1", "20", false)
        assertEquals("0 11111 0000000000", result.binary)
        assertEquals("7C00", result.hex)
    }

    @Test fun testTinyDecimal() {
        val result = converter.convert("0.00000006", "0", true)
        assertEquals("0 00000 0000000001", result.binary)
        assertEquals("0001", result.hex)
    }

    @Test fun testLargestNormal() {
        val result = converter.convert("65504", "0", true)
        assertEquals("0 11110 1111111111", result.binary)
        assertEquals("7BFF", result.hex)
    }
}
