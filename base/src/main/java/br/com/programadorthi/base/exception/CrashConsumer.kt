package br.com.programadorthi.base.exception

interface CrashConsumer {
    fun report(throwable: Throwable)
}