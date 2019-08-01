package br.com.programadorthi.base.formatter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateFormatterTest {

    private val textFormatter = DateTimeFormatter()

    @Test
    fun `should get Brazilian dd_MM_yyyy date format`() {
        val locale = Locale("pt", "BR")
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", locale)
        val date = Date()
        val expected = dateFormatter.format(date)
        val result = textFormatter.format(date, locale)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get USA MMM_dcomma_yyyy date format`() {
        val locale = Locale.US
        val dateFormatter = SimpleDateFormat("MMM d, yyyy", locale)
        val date = Date()
        val expected = dateFormatter.format(date)
        val result = textFormatter.format(date, locale)
        assertThat(result).isEqualTo(expected)
    }
}