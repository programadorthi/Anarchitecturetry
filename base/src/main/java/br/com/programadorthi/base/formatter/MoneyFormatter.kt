package br.com.programadorthi.base.formatter

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class MoneyFormatter : TextFormatter<BigDecimal> {
    override fun format(value: BigDecimal, locale: Locale): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(value)
    }
}