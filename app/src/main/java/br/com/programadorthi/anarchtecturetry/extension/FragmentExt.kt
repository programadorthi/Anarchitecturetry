package br.com.programadorthi.anarchtecturetry.extension

import androidx.fragment.app.Fragment
import br.com.programadorthi.anarchtecturetry.MainApplication
import br.com.programadorthi.anarchtecturetry.di.MainComponent

fun Fragment.mainComponent(): MainComponent {
    val context = activity ?: throw IllegalStateException("Fragment has no a host activity")
    return MainApplication.mainComponent(context)
}