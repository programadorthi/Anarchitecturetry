package br.com.programadorthi.base.exception

/**
 * Interface used to report exceptions in the application.
 * You can implement this to create a custom crash report like using Crashlytics
 */
interface CrashConsumer {
    /**
     * Called to report the captured exception
     */
    fun report(throwable: Throwable)
}