package br.com.programadorthi.anarchtecturetry.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val registeredCreator = creators[modelClass]
        if (registeredCreator != null) {
            return getOrThrow(registeredCreator)
        }

        val creator = creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }
        if (creator != null) {
            return getOrThrow(creator.value)
        }

        throw IllegalArgumentException("unknown model class $modelClass")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getOrThrow(provider: Provider<ViewModel>): T {
        try {
            return provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}