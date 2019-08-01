package br.com.programadorthi.anarchtecturetry.di

import android.app.Activity
import br.com.programadorthi.anarchtecturetry.MainApplication

fun Activity.mainComponent(): MainComponent = MainApplication.mainComponent(this)

interface BaseComponent<Target> {
    fun inject(target: Target)
}

interface BaseActivityComponent<Target : Activity> : BaseComponent<Target>