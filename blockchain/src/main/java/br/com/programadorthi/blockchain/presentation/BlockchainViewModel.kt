package br.com.programadorthi.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.domain.Blockchain
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class BlockchainViewModel(
    private val blockchainInteractor: BlockchainInteractor,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val mutableCurrentMarketPrice = MutableLiveData<ViewActionState<Blockchain>>()
    val currentMarketPrice: LiveData<ViewActionState<Blockchain>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewActionState<List<Blockchain>>>()
    val marketPrices: LiveData<ViewActionState<List<Blockchain>>>
        get() = mutableMarketPrices

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getCurrentMarketPrice() {
        mutableCurrentMarketPrice.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .observeOn(Schedulers.computation())
            .subscribe(onNext(mutableCurrentMarketPrice))

        compositeDisposable.add(disposable)
    }

    fun getAllMarketPrices() {
        mutableMarketPrices.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .observeOn(Schedulers.computation())
            .subscribe(onNext(mutableMarketPrices))

        compositeDisposable.add(disposable)
    }


    fun fetchCurrentMarketPrice() {
        val currentValue = mutableCurrentMarketPrice.value
        val result = when (currentValue) {
            is ViewActionState.Complete -> currentValue.result
            is ViewActionState.Error -> currentValue.data
            else -> Blockchain.EMPTY
        }

        mutableCurrentMarketPrice.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .fetchCurrentMarketPrice()
            .observeOn(Schedulers.computation())
            .subscribe({}, { ex ->
                mutableCurrentMarketPrice.postValue(ViewActionState.failure(ex, result))
            })

        compositeDisposable.add(disposable)
    }

    fun fetchAllMarketPrices() {
        val currentValue = mutableMarketPrices.value
        val result = when (currentValue) {
            is ViewActionState.Complete -> currentValue.result
            is ViewActionState.Error -> currentValue.data
            else -> emptyList()
        }

        mutableMarketPrices.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .fetchAllMarketPrices()
            .observeOn(Schedulers.computation())
            .subscribe({}, { ex ->
                mutableMarketPrices.postValue(ViewActionState.failure(ex, result))
            })

        compositeDisposable.add(disposable)
    }

    private fun <T> onNext(liveData: MutableLiveData<ViewActionState<T>>): Consumer<Resource<T>> =
        Consumer { result ->
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