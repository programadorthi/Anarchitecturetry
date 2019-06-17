package br.com.programadorthi.base.formatter

import java.text.DateFormat
import java.util.*

class DateTimeFormatter : TextFormatter<Date> {
    override fun format(value: Date, locale: Locale): String {
        val formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale)
        return formatter.format(value)
    }
}