package br.com.programadorthi.base.formatter

import java.text.DateFormat
import java.util.*

class DateFormatter : TextFormatter<Date> {
    override fun format(value: Date, locale: Locale): String {
        val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
        return formatter.format(value)
    }
}