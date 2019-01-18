package br.com.programadorthi.blockchain.presentation

import androidx.lifecycle.ViewModel
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import io.reactivex.Scheduler

class BlockchainViewModel(
    private val blockchainInteractor: BlockchainInteractor,
    private val scheduler: Scheduler
) : ViewModel() {

    /*private val mutableCurrentMarketPrice = MutableLiveData<ViewActionState<Blockchain>>()
    val currentMarketPrice: LiveData<ViewActionState<Blockchain>>
        get() = mutableCurrentMarketPrice

    private val mutableMarketPrices = MutableLiveData<ViewActionState<List<Blockchain>>>()
    val marketPrices: LiveData<ViewActionState<List<Blockchain>>>
        get() = mutableMarketPrices

    override fun onCleared() {
        super.onCleared()
        viewModelHelper.release()
    }

    fun getCurrentMarketPrice() {
        mutableCurrentMarketPrice.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getCurrentMarketPrice()
            .observeOn(scheduler)
            .subscribe(viewModelHelper.onNext(mutableCurrentMarketPrice))

        viewModelHelper.addToComposite(disposable)
    }

    fun getAllMarketPrices() {
        mutableMarketPrices.value = ViewActionState.loading()

        val disposable = blockchainInteractor
            .getAllMarketPrices()
            .observeOn(scheduler)
            .subscribe(viewModelHelper.onNext(mutableMarketPrices))

        viewModelHelper.addToComposite(disposable)
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
            .observeOn(scheduler)
            .subscribe({}, { ex ->
                mutableCurrentMarketPrice.postValue(ViewActionState.failure(ex, result))
            })

        viewModelHelper.addToComposite(disposable)
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
            .observeOn(scheduler)
            .subscribe({}, { ex ->
                mutableMarketPrices.postValue(ViewActionState.failure(ex, result))
            })

        viewModelHelper.addToComposite(disposable)
    }*/

}