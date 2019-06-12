package br.com.programadorthi.anarchtecturetry

import android.app.Application
import android.content.Context
import br.com.programadorthi.anarchtecturetry.di.DaggerMainComponent
import br.com.programadorthi.anarchtecturetry.di.MainComponent
import timber.log.Timber

class MainApplication : Application() {

    private val mainComponent: MainComponent by lazy {
        DaggerMainComponent.builder()
            .context(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        @JvmStatic
        fun mainComponent(context: Context): MainComponent {
            val application = context.applicationContext as MainApplication
            return application.mainComponent
        }
    }

}