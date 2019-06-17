package br.com.programadorthi.base.formatter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatterTest {

    private val textFormatter = DateTimeFormatter()

    @Test
    fun `should get Brazilian dd_MM_yyyy_HH_mm date time format`() {
        val locale = Locale("pt", "BR")
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
        val date = Date()
        val expected = dateFormatter.format(date)
        val result = textFormatter.format(date, locale)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get USA MMM_dcomma_yyyy_h_mm_a date time format`() {
        val locale = Locale.US
        val dateFormatter = SimpleDateFormat("MMM d, yyyy h:mm a", locale)
        val date = Date()
        val expected = dateFormatter.format(date)
        val result = textFormatter.format(date, locale)
        assertThat(result).isEqualTo(expected)
    }
}