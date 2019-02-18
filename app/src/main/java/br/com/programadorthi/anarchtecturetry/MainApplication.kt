package br.com.programadorthi.anarchtecturetry

import android.app.Activity
import android.app.Application
import br.com.programadorthi.anarchtecturetry.di.DaggerMainComponent
import br.com.programadorthi.anarchtecturetry.di.modules.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initTimber()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    private fun initDagger() {
        DaggerMainComponent.builder()
            .context(this@MainApplication)
            .networkModule(NetworkModule)
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}