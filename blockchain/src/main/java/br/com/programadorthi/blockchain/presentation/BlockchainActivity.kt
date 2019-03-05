package br.com.programadorthi.blockchain.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        initRecyclerView()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId ?: return false
        if (itemId == R.id.refreshMenu) {
            blockchainViewModel.apply {
                fetchCurrentMarketPrice()
                fetchAllMarketPrices()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(this@BlockchainActivity, RecyclerView.VERTICAL, false)
        marketPricesRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = blockchainAdapter
        }
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
        var result = emptyList<BlockchainViewData>()
        when (state) {
            is ViewActionState.Complete -> {
                blockchainAdapter.changeDataSet(state.result)
                result = state.result
            }
            is ViewActionState.Error -> {
                Timber.d(state.error, ">>>>>> Prices Error")
                result = state.previousData
            }
        }
        marketPricesRecyclerView.setVisible(result.isNotEmpty())
        marketPricesEmptyList.setVisible(
            state !is ViewActionState.Loading && result.isEmpty()
        )
        marketPricesProgressBar.setVisible(state is ViewActionState.Loading)
    }

}
