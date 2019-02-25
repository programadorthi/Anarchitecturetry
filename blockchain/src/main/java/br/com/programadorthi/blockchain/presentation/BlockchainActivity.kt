package br.com.programadorthi.blockchain.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.base.extension.createViewModel
import br.com.programadorthi.base.extension.observe
import br.com.programadorthi.base.extension.setVisible
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_blockchain.*
import timber.log.Timber
import javax.inject.Inject

class BlockchainActivity : AppCompatActivity() {

    @Inject
    lateinit var blockchainViewModel: BlockchainViewModel

    private val blockchainAdapter = BlockchainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blockchain)

        initViewModel()
    }

    private fun initViewModel() {
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
                val viewData = state.result
                currentMarketPriceValue.text = viewData.value
                currentMarketPriceDate.text = viewData.date
            }
            is ViewActionState.Error -> {
                Timber.d(state.error, ">>>>>> Current Error")
            }
        }
        currentMarketPriceValue.setVisible(state !is ViewActionState.Loading)
        currentMarketPriceDate.setVisible(state !is ViewActionState.Loading)
        currentMarketPricesProgressBar.setVisible(state is ViewActionState.Loading)
    }

    private fun updateMarketPrices(state: ViewActionState<List<BlockchainViewData>>?) {
        when (state) {
            is ViewActionState.Complete -> {
                blockchainAdapter.changeDataSet(state.result)
            }
            is ViewActionState.Error -> {
                Timber.d(state.error, ">>>>>> Prices Error")
            }
        }
        marketPricesRecyclerView.setVisible(
            state is ViewActionState.Complete && state.result.isNotEmpty()
        )
        marketPricesEmptyList.setVisible(
            state is ViewActionState.Complete && state.result.isEmpty()
        )
        marketPricesProgressBar.setVisible(state is ViewActionState.Loading)
    }

}
