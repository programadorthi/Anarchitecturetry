package br.com.programadorthi.anarchtecturetry.crashlytics

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.exception.CrashConsumer

/**
 * Consumer implementation used to catch errors in the RxJava flow
 */
class CrashlyticsConsumer : CrashConsumer {
    override fun report(throwable: Throwable) {
        if (BaseException.isAnExceptionToReport(throwable)) {
            // Here we send the Throwable to the Crashlytics
        }
    }
}