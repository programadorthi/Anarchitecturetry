package br.com.programadorthi.base.di

interface BaseComponent<in T> {
    fun inject(targe: T)
}