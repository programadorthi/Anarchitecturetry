package br.com.programadorthi.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.domain.Blockchain
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class BlockchainViewModel(
    private val blockchainInteractor: BlockchainInteractor,
    private val scheduler: Scheduler
) : ViewModel() {

    private val mutableCurrentMarketPrice = MutableLiveData<ViewActionState<Blockchain>>()
    val currentMarketPrice: LiveData<ViewActionState<Blockchain>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewActionState<List<Blockchain>>>()
    val marketPrices: LiveData<ViewActionState<List<Blockchain>>>
        get() = mutableMarketPrices

    private val compositeDisposable = CompositeDisposable()

    init {
        getCurrentMarketPrice()
        getAllMarketPrices()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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
            .doOnError { mutableCurrentMarketPrice.postValue(ViewActionState.failure(it, result)) }
            .observeOn(scheduler)
            .subscribe()

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
            .doOnError { mutableMarketPrices.postValue(ViewActionState.failure(it, result)) }
            .observeOn(scheduler)
            .subscribe()

        compositeDisposable.add(disposable)
    }

    private fun getCurrentMarketPrice() {
        mutableCurrentMarketPrice.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.data)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .subscribe(mutableCurrentMarketPrice::postValue)

        compositeDisposable.add(disposable)
    }

    private fun getAllMarketPrices() {
        mutableMarketPrices.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.data)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .subscribe(mutableMarketPrices::postValue)

        compositeDisposable.add(disposable)
    }

}