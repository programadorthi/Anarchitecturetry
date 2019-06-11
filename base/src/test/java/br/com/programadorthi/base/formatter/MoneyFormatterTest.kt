package br.com.programadorthi.base.formatter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class MoneyFormatterTest {

    private val textFormatter = MoneyFormatter()

    @Test
    fun `should get Brazilian real number format`() {
        val tenReal = BigDecimal.TEN
        val expected = "R$ 10,00"
        val result = textFormatter.format(tenReal, Locale("pt", "BR"))
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get USA dolar number format`() {
        val tenDolar = BigDecimal.TEN
        val expected = "$10.00"
        val result = textFormatter.format(tenDolar, Locale.US)
        assertThat(result).isEqualTo(expected)
    }
}