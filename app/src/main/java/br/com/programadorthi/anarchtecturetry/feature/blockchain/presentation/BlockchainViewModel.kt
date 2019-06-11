package br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.presentation.ViewState
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

    private val mutableCurrentMarketPrice = MutableLiveData<ViewState<BlockchainViewData>>()
    val currentMarketPrice: LiveData<ViewState<BlockchainViewData>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewState<List<BlockchainViewData>>>()
    val marketPrices: LiveData<ViewState<List<BlockchainViewData>>>
        get() = mutableMarketPrices

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun initialize() {
        getLocalMarketPrice()
        getLocalMarketPrices()
        refresh()
    }

    fun refresh() {
        fetchCurrentMarketPrice()
        fetchAllMarketPrices()
    }

    private fun fetchCurrentMarketPrice() {
        mutableCurrentMarketPrice.value = ViewState.Loading()

        val disposable = blockchainInteractor
            .fetchCurrentMarketPrice()
            .observeOn(scheduler)
            .onErrorComplete { err ->
                mutableCurrentMarketPrice.postValue(ViewState.Error(err))
                return@onErrorComplete true
            }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun fetchAllMarketPrices() {
        mutableMarketPrices.value = ViewState.Loading()

        val disposable = blockchainInteractor
            .fetchAllMarketPrices()
            .observeOn(scheduler)
            .onErrorComplete { err ->
                mutableMarketPrices.postValue(ViewState.Error(err))
                return@onErrorComplete true
            }
            .subscribe()

        compositeDisposable.add(disposable)
    }

    private fun getLocalMarketPrice() {
        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .map<ViewState<BlockchainViewData>> { result -> ViewState.Complete(mapToBlockchainViewData(result)) }
            .onErrorReturn { err -> ViewState.Error(err) }
            .observeOn(scheduler)
            .subscribe(mutableCurrentMarketPrice::postValue)

        compositeDisposable.add(disposable)
    }

    private fun getLocalMarketPrices() {
        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .flatMapSingle(this::mapItemsToBlockchainViewData)
            .onErrorReturn { err -> ViewState.Error(err) }
            .observeOn(scheduler)
            .subscribe(mutableMarketPrices::postValue)

        compositeDisposable.add(disposable)
    }

    private fun mapToBlockchainViewData(blockchain: Blockchain): BlockchainViewData {
        return BlockchainViewData(
            date = dateFormatter.format(blockchain.date, Locale.getDefault()),
            value = moneyFormatter.format(blockchain.value, Locale.getDefault())
        )
    }

    private fun mapItemsToBlockchainViewData(blockchains: List<Blockchain>): Single<ViewState<List<BlockchainViewData>>> {
        if (blockchains.isEmpty()) {
            return Single.just(ViewState.Complete(emptyList()))
        }

        return Observable
            .fromIterable(blockchains)
            .map(this::mapToBlockchainViewData)
            .toList()
            .map { result -> ViewState.Complete(result) }
    }

}