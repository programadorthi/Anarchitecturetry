package br.com.programadorthi.anarchtecturetry

import android.app.Activity
import android.app.Application
import br.com.programadorthi.anarchtecturetry.di.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerMainComponent.create().inject(this@MainApplication)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}