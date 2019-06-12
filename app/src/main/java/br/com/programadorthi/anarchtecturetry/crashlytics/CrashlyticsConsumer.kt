package br.com.programadorthi.anarchtecturetry.crashlytics

import br.com.programadorthi.base.exception.BaseException
import io.reactivex.functions.Consumer

/**
 * Consumer implementation used to catch errors in the RxJava flow
 */
class CrashlyticsConsumer : Consumer<Throwable> {
    override fun accept(throwable: Throwable?) {
        if (BaseException.isAnExceptionToReport(throwable)) {
            // Here we send the Throwable to the Crashlytics
        }
    }
}