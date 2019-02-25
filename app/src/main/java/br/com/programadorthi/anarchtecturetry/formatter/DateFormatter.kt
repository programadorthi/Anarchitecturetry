package br.com.programadorthi.anarchtecturetry.formatter

import br.com.programadorthi.base.presentation.TextFormatter
import java.text.DateFormat
import java.util.*

class DateFormatter : TextFormatter<Date> {
    override fun format(value: Date): String {
        val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return formatter.format(value)
    }
}