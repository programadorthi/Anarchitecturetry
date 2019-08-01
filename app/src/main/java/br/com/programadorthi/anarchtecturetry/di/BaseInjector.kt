package br.com.programadorthi.anarchtecturetry.di

import android.app.Activity
import br.com.programadorthi.anarchtecturetry.MainApplication

fun Activity.mainComponent(): MainComponent = MainApplication.mainComponent(this)

interface BaseInjector<Target, Component> {
    fun inject(target: Target): Component
}

interface BaseActivityInjector<Target: Activity, Component> : BaseInjector<Target, Component>