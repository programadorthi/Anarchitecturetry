package br.com.programadorthi.base.formatter

import br.com.programadorthi.base.presentation.TextFormatter
import java.math.BigDecimal
import java.text.NumberFormat

class MoneyFormatter : TextFormatter<BigDecimal> {
    override fun format(value: BigDecimal): String {
        val formatter = NumberFormat.getCurrencyInstance()
        return formatter.format(value)
    }
}