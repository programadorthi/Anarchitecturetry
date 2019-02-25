package br.com.programadorthi.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.base.presentation.TextFormatter
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.math.BigDecimal
import java.util.*

class BlockchainViewModel(
    private val blockchainInteractor: BlockchainInteractor,
    private val dateFormatter: TextFormatter<Date>,
    private val moneyFormatter: TextFormatter<BigDecimal>,
    private val scheduler: Scheduler
) : ViewModel() {

    private val mutableCurrentMarketPrice = MutableLiveData<ViewActionState<BlockchainViewData>>()
    val currentMarketPrice: LiveData<ViewActionState<BlockchainViewData>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewActionState<List<BlockchainViewData>>>()
    val marketPrices: LiveData<ViewActionState<List<BlockchainViewData>>>
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
                    else -> BlockchainViewData.EMPTY
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

    fun getLocalMarketPrice() {
        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .map { result ->
                ViewActionState.complete(
                    BlockchainViewData(
                        date = dateFormatter.format(result.date),
                        value = moneyFormatter.format(result.value)
                    )
                )
            }
            .onErrorReturn { err -> ViewActionState.failure(err, BlockchainViewData.EMPTY) }
            .observeOn(scheduler)
            .doOnSubscribe { mutableCurrentMarketPrice.value = ViewActionState.loading() }
            .subscribe(mutableCurrentMarketPrice::postValue)

        compositeDisposable.add(disposable)
    }

    fun getLocalMarketPrices() {
        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .flatMapSingle { result ->
                if (result.isEmpty()) {
                    val complete = ViewActionState.complete(emptyList<BlockchainViewData>())
                    return@flatMapSingle Single.just(complete)
                }
                return@flatMapSingle Observable
                    .fromIterable(result)
                    .map { item ->
                        BlockchainViewData(
                            date = dateFormatter.format(item.date),
                            value = moneyFormatter.format(item.value)
                        )
                    }
                    .toList()
                    .map { list -> ViewActionState.complete(list) }
            }
            .onErrorReturn { err -> ViewActionState.failure(err, emptyList()) }
            .observeOn(scheduler)
            .doOnSubscribe { mutableMarketPrices.value = ViewActionState.loading() }
            .subscribe(mutableMarketPrices::postValue)

        compositeDisposable.add(disposable)
    }

}