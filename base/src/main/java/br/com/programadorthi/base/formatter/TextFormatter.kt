package br.com.programadorthi.base.formatter

import java.util.*

interface TextFormatter<T> {
    fun format(value: T, locale: Locale): String
}