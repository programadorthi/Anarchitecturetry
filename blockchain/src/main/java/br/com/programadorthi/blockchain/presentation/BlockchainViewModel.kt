package br.com.programadorthi.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.domain.Blockchain
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import io.reactivex.Observable
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun fetchCurrentMarketPrice() {
        val disposable = Observable
            .just(mutableCurrentMarketPrice.value)
            .map { currentValue ->
                when (currentValue) {
                    is ViewActionState.Complete -> currentValue.result
                    is ViewActionState.Error -> currentValue.previousData
                    else -> Blockchain.EMPTY
                }
            }
            .doOnNext { mutableCurrentMarketPrice.value = ViewActionState.loading() }
            .flatMapCompletable { currentValue ->
                blockchainInteractor
                    .fetchCurrentMarketPrice()
                    .observeOn(scheduler)
                    .doOnError { err ->
                        mutableCurrentMarketPrice.postValue(
                            ViewActionState.failure(err, currentValue)
                        )
                    }
            }.subscribe()

        compositeDisposable.add(disposable)
    }

    fun fetchAllMarketPrices() {
        val disposable = Observable
            .just(mutableMarketPrices.value)
            .map { currentValue ->
                when (currentValue) {
                    is ViewActionState.Complete -> currentValue.result
                    is ViewActionState.Error -> currentValue.previousData
                    else -> emptyList()
                }
            }
            .doOnNext { mutableMarketPrices.value = ViewActionState.loading() }
            .flatMapCompletable { currentValue ->
                blockchainInteractor
                    .fetchAllMarketPrices()
                    .observeOn(scheduler)
                    .doOnError { err ->
                        mutableMarketPrices.postValue(
                            ViewActionState.failure(err, currentValue)
                        )
                    }
            }.subscribe()

        compositeDisposable.add(disposable)
    }

    fun getCurrentMarketPrice() {
        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.previousData)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .doOnSubscribe { mutableCurrentMarketPrice.value = ViewActionState.loading() }
            .subscribe(mutableCurrentMarketPrice::postValue)

        compositeDisposable.add(disposable)
    }

    fun getAllMarketPrices() {
        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.previousData)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .doOnSubscribe { mutableMarketPrices.value = ViewActionState.loading() }
            .subscribe(mutableMarketPrices::postValue)

        compositeDisposable.add(disposable)
    }

}