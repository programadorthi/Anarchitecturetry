package br.com.programadorthi.base.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.base.presentation.ViewActionState
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface ViewModelHelper {

    fun addToComposite(disposable: Disposable)

    fun <T> onNext(liveData: MutableLiveData<ViewActionState<T>>): Consumer<Resource<T>>

    fun release()

}