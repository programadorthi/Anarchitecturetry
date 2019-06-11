package br.com.programadorthi.base.database

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

class BigDecimalTypeConverterTest {

    private val converter = BigDecimalTypeConverter()

    @Test
    fun `should parse BigDecimal 123_456 value to String 123_456`() {
        val stringValue = "123.456"
        val bigDecimalValue = BigDecimal(stringValue)
        val stringVersion = converter.fromBigDecimal(bigDecimalValue)
        assertThat(stringVersion).isEqualTo(stringValue)
    }

    @Test
    fun `should parse String 123_456 value to BigDecimal 123_456`() {
        val stringValue = "123.456"
        val bigDecimalValue = BigDecimal(stringValue)
        val bigDecimalResult = converter.toBigDecimal(stringValue)
        assertThat(bigDecimalResult).isEqualTo(bigDecimalValue)
    }
}