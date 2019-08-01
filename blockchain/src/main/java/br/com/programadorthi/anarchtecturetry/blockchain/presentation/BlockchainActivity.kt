package br.com.programadorthi.anarchtecturetry.blockchain.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.programadorthi.anarchtecturetry.blockchain.R
import br.com.programadorthi.anarchtecturetry.blockchain.di.BlockchainComponent
import br.com.programadorthi.anarchtecturetry.blockchain.di.BlockchainInjector
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.adapter.BlockchainAdapter
import br.com.programadorthi.anarchtecturetry.utils.getOrCreateViewModel
import br.com.programadorthi.base.extension.setVisible
import br.com.programadorthi.base.presentation.ViewState
import br.com.programadorthi.base.shared.FailureType
import kotlinx.android.synthetic.main.activity_blockchain.*
import br.com.programadorthi.anarchtecturetry.R as appResources

class BlockchainActivity : AppCompatActivity() {

    private val blockchainAdapter = BlockchainAdapter()

    private lateinit var blockchainViewModel: BlockchainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = BlockchainInjector.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_blockchain)

        initRecyclerView()
        initViewModel(component)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId ?: return false
        if (itemId == R.id.refreshMenu) {
            blockchainViewModel.refresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        marketPricesRecyclerView.adapter = blockchainAdapter
    }

    private fun initViewModel(component: BlockchainComponent) {
        blockchainViewModel = getOrCreateViewModel { component.blockchainViewModel() }

        blockchainViewModel.currentMarketPrice.observe(this, Observer { state ->
            updateCurrentMarketPrice(state)
        })

        blockchainViewModel.marketPrices.observe(this, Observer { state ->
            updateMarketPrices(state)
        })

        blockchainViewModel.initialize()
    }

    private fun updateCurrentMarketPrice(state: ViewState<BlockchainViewData>?) {
        when (state) {
            is ViewState.Complete -> {
                state.result.apply {
                    currentMarketPriceValue.text = value
                    currentMarketPriceDate.text = getString(R.string.market_price_today, date)
                }
            }
            is ViewState.Failure -> checkCurrentMarketError(state.type)
        }
        updateCurrentMarketPriceFields(state)
    }

    private fun updateMarketPrices(state: ViewState<List<BlockchainViewData>>?) {
        when (state) {
            is ViewState.Complete -> {
                blockchainAdapter.changeDataSet(state.result)
            }
            is ViewState.Failure -> checkMarketPricesError(state.type)
        }
        updateMarketPricesFields(state)
    }

    private fun updateCurrentMarketPriceFields(state: ViewState<BlockchainViewData>?) {
        currentMarketPriceValue.setVisible(state is ViewState.Complete)
        currentMarketPriceDate.setVisible(state is ViewState.Complete)
        currentMarketPriceErrorText.setVisible(state is ViewState.Failure)
        currentMarketPricesProgressBar.setVisible(state is ViewState.Loading)
    }

    private fun updateMarketPricesFields(state: ViewState<List<BlockchainViewData>>?) {
        marketPricesRecyclerView.setVisible(state is ViewState.Complete)
        marketPricesErrorText.setVisible(state is ViewState.Failure)
        marketPricesProgressBar.setVisible(state is ViewState.Loading)
    }

    private fun checkCurrentMarketError(failureType: FailureType) {
        val text = when (failureType) {
            is FailureType.NoInternetConnection ->
                getString(appResources.string.shared_text_no_internet_connection)
            else -> getString(appResources.string.shared_text_unknown_exception)
        }
        currentMarketPriceErrorText.text = text
    }

    private fun checkMarketPricesError(failureType: FailureType) {
        val text = when (failureType) {
            is FailureType.NoInternetConnection ->
                getString(appResources.string.shared_text_no_internet_connection)
            else -> getString(appResources.string.shared_text_unknown_exception)
        }
        marketPricesErrorText.text = text
    }

}