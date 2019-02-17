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
        val disposable = mapCurrentValueAndShowLoadingAfter(
            mutableLiveData = mutableCurrentMarketPrice,
            defaultValue = Blockchain.EMPTY
        ).flatMapCompletable { result ->
            blockchainInteractor
                .fetchCurrentMarketPrice()
                .observeOn(scheduler)
                .doOnError { err ->
                    mutableCurrentMarketPrice.postValue(
                        ViewActionState.failure(
                            err,
                            result
                        )
                    )
                }
        }.subscribe()

        compositeDisposable.add(disposable)
    }

    fun fetchAllMarketPrices() {
        val disposable = mapCurrentValueAndShowLoadingAfter(
            mutableLiveData = mutableMarketPrices,
            defaultValue = emptyList()
        ).flatMapCompletable { result ->
            blockchainInteractor
                .fetchAllMarketPrices()
                .doOnError { err ->
                    mutableMarketPrices.postValue(
                        ViewActionState.failure(
                            err,
                            result
                        )
                    )
                }
                .observeOn(scheduler)
        }.subscribe()

        compositeDisposable.add(disposable)
    }

    fun getCurrentMarketPrice() {
        mutableCurrentMarketPrice.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.previousData)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .subscribe(mutableCurrentMarketPrice::postValue)

        compositeDisposable.add(disposable)
    }

    fun getAllMarketPrices() {
        mutableMarketPrices.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .map { result ->
                when (result) {
                    is Resource.Error -> ViewActionState.failure(result.error, result.previousData)
                    is Resource.Success -> ViewActionState.complete(result.data)
                }
            }
            .observeOn(scheduler)
            .subscribe(mutableMarketPrices::postValue)

        compositeDisposable.add(disposable)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> mapCurrentValueAndShowLoadingAfter(
        mutableLiveData: MutableLiveData<ViewActionState<T>>,
        defaultValue: T
    ): Observable<T> {
        return Observable
            .just(mutableLiveData.value)
            .map { currentValue ->
                when (currentValue) {
                    is ViewActionState.Complete<*> -> currentValue.result as T
                    is ViewActionState.Error<*> -> currentValue.previousData as T
                    else -> defaultValue
                }
            }
            .doOnNext { mutableLiveData.value = ViewActionState.loading() }
    }

}