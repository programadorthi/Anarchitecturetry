package br.com.programadorthi.anarchtecturetry.firebase

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.exception.CrashConsumer

/**
 * Consumer implementation used to catch errors in the RxJava flow.
 * <br/>
 * It'll be used in the RxJava <strong>doOnError()</strong> method
 */
class CrashlyticsConsumer : CrashConsumer {
    override fun report(throwable: Throwable) {
        if (BaseException.isAnExceptionToReport(throwable)) {
            // Here we send the Throwable to the Crashlytics
        }
    }
}