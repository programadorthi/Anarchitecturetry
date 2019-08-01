package br.com.programadorthi.anarchtecturetry.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * This is a version created by Gabor Varadi
 *
 * https://stackoverflow.com/questions/50673266/viewmodelproviders-with-dagger-2-not-able-to-grasp-the-concept/50681021#50681021
 */
inline fun <reified T : ViewModel> FragmentActivity.getOrCreateViewModel(
    crossinline factory: () -> T
): T = T::class.java.let { clazz ->
    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == clazz) {
                @Suppress("UNCHECKED_CAST")
                return factory() as T
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }).get(clazz)
}