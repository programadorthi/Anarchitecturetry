@file:JvmName("ViewModelUtils")

package br.com.programadorthi.base.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import br.com.programadorthi.base.exception.BaseException

/**
 * Below are custom implementations based in the Gabor Varadi (aka Zhuinden, EpicPandaForce)
 * https://stackoverflow.com/a/50681021/2463035
 */

inline fun <reified T : ViewModel> viewModelFactory(viewModel: T) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == viewModel::class.java) {
                @Suppress("UNCHECKED_CAST")
                return viewModel as T
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }

inline fun <reified T : ViewModel> Fragment.createViewModel(
    viewModel: T,
    body: T.() -> Unit
): T {
    val vm = T::class.java.let { clazz ->
        ViewModelProviders.of(this, viewModelFactory(viewModel)).get(clazz)
    }
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> FragmentActivity.createViewModel(
    viewModel: T,
    body: T.() -> Unit
): T {
    val vm = T::class.java.let { clazz ->
        ViewModelProviders.of(this, viewModelFactory(viewModel)).get(clazz)
    }
    vm.body()
    return vm
}

/**
 * Below are custom implementations created by Cejas
 * https://fernandocejas.com/2018/05/07/architecting-android-reloaded/
 */

/**
 * Extension function to attach a behavior to any [LiveData]
 *
 * @param liveData The current [LiveData]
 * @param body The action to execute
 */
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

/**
 * Extension function to attach a behavior to fullbankException [LiveData]
 *
 * @param liveData The current [LiveData]
 * @param body The action to execute using the [BaseException]
 */
fun <L : LiveData<BaseException>> LifecycleOwner.failure(
    liveData: L,
    body: (BaseException?) -> Unit
) =
    liveData.observe(this, Observer(body))