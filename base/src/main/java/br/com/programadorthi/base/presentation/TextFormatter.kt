package br.com.programadorthi.base.presentation

interface TextFormatter<T> {
    fun format(value: T): String
}