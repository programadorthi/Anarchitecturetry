package br.com.programadorthi.blockchain.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.base.extension.createViewModel
import br.com.programadorthi.base.extension.observe
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.R
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class BlockchainActivity : AppCompatActivity() {

    @Inject
    lateinit var blockchainViewModel: BlockchainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blockchain)

        createViewModel(blockchainViewModel) {
            observe(currentMarketPrice, ::updateCurrentMarketPrice)
            observe(marketPrices, ::updateMarketPrices)
        }.apply {
            getLocalMarketPrice()
            getLocalMarketPrices()
        }
    }

    private fun updateCurrentMarketPrice(state: ViewActionState<BlockchainViewData>?) {
        when (state) {
            is ViewActionState.Complete -> {
                Timber.d(">>>>>> Current Complete: ${state.result}")
            }
            is ViewActionState.Error -> {
                Timber.d(state.error, ">>>>>> Current Error")
            }
        }
        Timber.d(">>>> Current Loading? ${state is ViewActionState.Loading}")
    }

    private fun updateMarketPrices(state: ViewActionState<List<BlockchainViewData>>?) {
        when (state) {
            is ViewActionState.Complete -> {
                Timber.d(">>>>>> Prices Complete: ${state.result}")
            }
            is ViewActionState.Error -> {
                Timber.d(state.error, ">>>>>> Prices Error")
            }
        }
        Timber.d(">>>> Prices Loading? ${state is ViewActionState.Loading}")
    }

}
