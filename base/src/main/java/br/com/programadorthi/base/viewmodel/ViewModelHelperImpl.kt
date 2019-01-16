package br.com.programadorthi.base.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.base.presentation.ViewActionState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class ViewModelHelperImpl : ViewModelHelper {

    private val compositeDisposable = CompositeDisposable()

    override fun addToComposite(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun <T> onNext(liveData: MutableLiveData<ViewActionState<T>>): Consumer<Resource<T>> {
        return Consumer { result ->
            when (result) {
                is Resource.Error -> liveData.postValue(
                    ViewActionState.failure(
                        result.error,
                        result.data
                    )
                )
                is Resource.Success -> liveData.postValue(ViewActionState.complete(result.data))
            }
        }
    }

    override fun release() {
        compositeDisposable.dispose()
    }
}