package br.com.programadorthi.anarchtecturetry.blockchain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.presentation.ViewState
import br.com.programadorthi.base.shared.LayerResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*

class BlockchainViewModel(
    private val blockchainInteractor: BlockchainInteractor,
    private val dateFormatter: TextFormatter<Date>,
    private val moneyFormatter: TextFormatter<BigDecimal>,
    private val executionScope: CoroutineScope
) : ViewModel() {

    private val mutableCurrentMarketPrice = MutableLiveData<ViewState<BlockchainViewData>>()
    val currentMarketPrice: LiveData<ViewState<BlockchainViewData>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewState<List<BlockchainViewData>>>()
    val marketPrices: LiveData<ViewState<List<BlockchainViewData>>>
        get() = mutableMarketPrices

    override fun onCleared() {
        executionScope.cancel()
        super.onCleared()
    }

    fun initialize() {
        if (isInitialized()) return
        refresh()
    }

    fun refresh() {
        getLocalMarketPrice()
        getLocalMarketPrices()
    }

    private fun getLocalMarketPrice() {
        mutableCurrentMarketPrice.value = ViewState.Loading()

        executionScope.launch {
            val state: ViewState<BlockchainViewData> =
                when (val result = blockchainInteractor.getCurrentMarketPrice()) {
                    is LayerResult.Success -> ViewState.Complete(mapToBlockchainViewData(result.data))
                    is LayerResult.Failure -> ViewState.Failure(result.type)
                }
            mutableCurrentMarketPrice.postValue(state)
        }
    }

    private fun getLocalMarketPrices() {
        mutableMarketPrices.value = ViewState.Loading()

        executionScope.launch {
            val state: ViewState<List<BlockchainViewData>> =
                when (val result = blockchainInteractor.getAllMarketPrices()) {
                    is LayerResult.Success -> ViewState.Complete(result.data.map(::mapToBlockchainViewData))
                    is LayerResult.Failure -> ViewState.Failure(result.type)
                }
            mutableMarketPrices.postValue(state)
        }
    }

    private fun mapToBlockchainViewData(blockchain: Blockchain): BlockchainViewData {
        return BlockchainViewData(
            date = dateFormatter.format(blockchain.date, Locale.getDefault()),
            value = moneyFormatter.format(blockchain.value, Locale.getDefault())
        )
    }

    private fun isInitialized(): Boolean {
        return mutableCurrentMarketPrice.value != null && mutableMarketPrices.value != null
    }

}