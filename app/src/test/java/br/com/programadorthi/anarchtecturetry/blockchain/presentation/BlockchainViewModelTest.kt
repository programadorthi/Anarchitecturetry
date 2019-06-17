package br.com.programadorthi.anarchtecturetry.blockchain.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewData
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.presentation.ViewState
import br.com.programadorthi.base.shared.LayerResult
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val blockchainInteractor = mockk<BlockchainInteractor>()

    private val dateFormatter = mockk<TextFormatter<Date>>()

    private val moneyFormatter = mockk<TextFormatter<BigDecimal>>()

    private val currentMarketPriceLoadingClass = ViewState.Loading<BlockchainViewData>().javaClass.kotlin
    private val allMarketPricesLoadingClass = ViewState.Loading<List<BlockchainViewData>>().javaClass.kotlin

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val viewModel = BlockchainViewModel(blockchainInteractor, dateFormatter, moneyFormatter, coroutineScope)

    @Before
    fun setUp() {
        every { dateFormatter.format(any(), any()) } returns ""
        every { moneyFormatter.format(any(), any()) } returns ""

        coEvery { blockchainInteractor.getCurrentMarketPrice() } returns LayerResult.success(Blockchain.EMPTY)
        coEvery { blockchainInteractor.getAllMarketPrices() } returns LayerResult.success(emptyList())
    }

    @Test
    fun `should initialize with empty blockchain, empty history and fetch new data`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        viewModel.initialize()

        verify(exactly = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(exactly = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Complete(BlockchainViewData.EMPTY)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Complete(emptyList())) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should initialize with a blockchain and empty history only`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        val expected = BlockchainViewData(
            date = "01/01/2000",
            value = "R$ 0,00"
        )

        every { dateFormatter.format(any(), any()) } returns expected.date
        every { moneyFormatter.format(any(), any()) } returns expected.value

        viewModel.initialize()

        verify(exactly = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(exactly = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Complete(expected)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Complete(emptyList())) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should initialize with a blockchain and a history`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        val expected = BlockchainViewData(
            date = "01/01/2000",
            value = "R$ 0,00"
        )

        every { dateFormatter.format(any(), any()) } returns expected.date
        every { moneyFormatter.format(any(), any()) } returns expected.value

        coEvery { blockchainInteractor.getAllMarketPrices() } returns LayerResult.success(listOf(Blockchain.EMPTY))

        viewModel.initialize()

        verify(exactly = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(exactly = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Complete(expected)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Complete(listOf(expected))) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should initialize with a local database exception`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        val expected = IllegalStateException("this is an exception")

        coEvery { blockchainInteractor.getCurrentMarketPrice() } returns LayerResult.failure(expected)
        coEvery { blockchainInteractor.getAllMarketPrices() } returns LayerResult.failure(expected)

        viewModel.initialize()

        verify(exactly = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(exactly = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Error(expected)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Error(expected)) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should initialize with an API exception`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        val expected = IllegalStateException("this is an exception")

        coEvery { blockchainInteractor.getCurrentMarketPrice() } returns LayerResult.failure(expected)
        coEvery { blockchainInteractor.getAllMarketPrices() } returns LayerResult.failure(expected)

        viewModel.initialize()

        verify(exactly = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(exactly = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Error(expected)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Error(expected)) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should refresh data from API`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        viewModel.refresh()

        verify(atMost = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(atMost = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    @Test
    fun `should catch exception on refreshing data from API`() {
        val observerCurrentMarketPrice = mockCurrentMarketPriceObserver()
        val observerAllMarketPrices = mockAllMarketPricesObserver()

        val expected = IllegalStateException("this is an exception")

        coEvery { blockchainInteractor.getCurrentMarketPrice() } returns LayerResult.failure(expected)
        coEvery { blockchainInteractor.getAllMarketPrices() } returns LayerResult.failure(expected)

        viewModel.refresh()

        verify(atMost = 2) { observerCurrentMarketPrice.onChanged(any()) }
        verify(atMost = 2) { observerAllMarketPrices.onChanged(any()) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ViewState.Error(expected)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ViewState.Error(expected)) }

        verify(exactly = 1) { observerCurrentMarketPrice.onChanged(ofType(currentMarketPriceLoadingClass)) }
        verify(exactly = 1) { observerAllMarketPrices.onChanged(ofType(allMarketPricesLoadingClass)) }

        confirmVerified(observerCurrentMarketPrice, observerAllMarketPrices)
    }

    private fun mockCurrentMarketPriceObserver(): Observer<ViewState<BlockchainViewData>> {
        val observerCurrentMarketPrice = mockk<Observer<ViewState<BlockchainViewData>>>()
        every { observerCurrentMarketPrice.onChanged(any()) } just Runs
        viewModel.currentMarketPrice.observeForever(observerCurrentMarketPrice)
        return observerCurrentMarketPrice
    }

    private fun mockAllMarketPricesObserver(): Observer<ViewState<List<BlockchainViewData>>> {
        val observerAllMarketPrices = mockk<Observer<ViewState<List<BlockchainViewData>>>>()
        every { observerAllMarketPrices.onChanged(any()) } just Runs
        viewModel.marketPrices.observeForever(observerAllMarketPrices)
        return observerAllMarketPrices
    }

}