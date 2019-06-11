package br.com.programadorthi.anarchtecturetry

import android.app.Activity
import android.app.Application
import br.com.programadorthi.anarchtecturetry.di.DaggerMockMainComponent
import br.com.programadorthi.anarchtecturetry.di.MockMainComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MockApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    val component: MockMainComponent by lazy {
        val mockMainComponent = DaggerMockMainComponent.create()
        mockMainComponent.inject(this@MockApplication)
        return@lazy mockMainComponent
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

}