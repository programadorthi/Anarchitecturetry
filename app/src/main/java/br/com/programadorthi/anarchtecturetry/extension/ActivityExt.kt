package br.com.programadorthi.anarchtecturetry.extension

import android.app.Activity
import br.com.programadorthi.anarchtecturetry.MainApplication

fun Activity.mainComponent() = MainApplication.mainComponent(this)