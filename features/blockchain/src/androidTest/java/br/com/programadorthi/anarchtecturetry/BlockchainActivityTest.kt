package br.com.programadorthi.anarchtecturetry

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import br.com.programadorthi.anarchtecturetry.blockchain.R
import br.com.programadorthi.anarchtecturetry.blockchain.di.inject
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewData
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.base.presentation.ViewState
import br.com.programadorthi.base.shared.FailureType
import io.mockk.*
import org.junit.*

class BlockchainActivityTest : BaseEspressoTest() {

    @get:Rule
    val activityTestRule = IntentsTestRule(BlockchainActivity::class.java, true, false)

    private val viewModel = mockk<BlockchainViewModel>()

    private val currentMarketPrice = MutableLiveData<ViewState<BlockchainViewData>>()

    private val marketPrices = MutableLiveData<ViewState<List<BlockchainViewData>>>()

    companion object {
        private const val INJECTOR_CLASS =
            "br.com.programadorthi.anarchtecturetry.blockchain.di.BlockchainInjector.kt"
    }

    @BeforeClass
    fun initialize() {
        mockkStatic(INJECTOR_CLASS)
    }

    @AfterClass
    fun clean() {
        unmockkStatic(INJECTOR_CLASS)
    }

    @Before
    fun setUp() {
        every { viewModel["onCleared"]() } answers { nothing }

        every { viewModel.currentMarketPrice } returns currentMarketPrice
        every { viewModel.marketPrices } returns marketPrices
        every { viewModel.initialize() } just Runs

        every { inject(any()) } answers {
            activityTestRule.activity.blockchainViewModel = viewModel
        }

        activityTestRule.launchActivity(null)
    }

    @Test
    fun whenStartActivity_shouldShowCurrentAndHistoricalTitle() {
        val context = activityTestRule.activity
        val marketPriceTitle = context.getString(R.string.market_price_title)
        val marketPriceHistoricalTitle = context.getString(R.string.market_price_historical_title)

        onViewWithId(resId = R.id.currentMarketPriceTitle)
            .matchText(marketPriceTitle)

        onViewWithId(resId = R.id.currentMarketPriceHistoricalTitle)
            .matchText(marketPriceHistoricalTitle)
    }

    @Test
    fun whenStartActivity_shouldShowCurrentMarketPriceLoadingState() {
        activityTestRule.activity.runOnUiThread {
            currentMarketPrice.value = ViewState.Loading()
        }

        onViewWithId(resId = R.id.currentMarketPricesProgressBar)
            .isDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceErrorText)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceValue)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceDate)
            .isNotDisplayed()
    }

    @Test
    fun whenStartActivity_shouldShowMarketPricesLoadingState() {
        activityTestRule.activity.runOnUiThread {
            marketPrices.value = ViewState.Loading()
        }

        onViewWithId(resId = R.id.marketPricesProgressBar)
            .isDisplayed()

        onViewWithId(resId = R.id.marketPricesErrorText)
            .isNotDisplayed()

        onViewWithId(resId = R.id.marketPricesRecyclerView)
            .isNotDisplayed()
    }

    @Test
    fun whenStartActivityAndHasCurrentMarketPrice_shouldShowCurrentMarket() {
        activityTestRule.activity.runOnUiThread {
            currentMarketPrice.value =
                ViewState.Complete(BlockchainViewData(date = "dd/MM/yyyy HH:mm", value = "R$ 1,00"))
        }

        onViewWithId(resId = R.id.currentMarketPricesProgressBar)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceErrorText)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceValue)
            .isDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceDate)
            .isDisplayed()
    }

    @Test
    fun whenStartActivityAndHasHistoricalMarketPrices_shouldShowMarketPrices() {
        activityTestRule.activity.runOnUiThread {
            marketPrices.value = ViewState.Complete(emptyList())
        }

        onViewWithId(resId = R.id.marketPricesProgressBar)
            .isNotDisplayed()

        onViewWithId(resId = R.id.marketPricesErrorText)
            .isNotDisplayed()

        onViewWithId(resId = R.id.marketPricesRecyclerView)
            .isDisplayed()
    }

    @Test
    fun whenStartActivityAndCurrentMarketPriceHasError_shouldShowCurrentMarketErrorText() {
        activityTestRule.activity.runOnUiThread {
            currentMarketPrice.value = ViewState.Failure(FailureType.NoInternetConnection)
        }

        onViewWithId(resId = R.id.currentMarketPricesProgressBar)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceErrorText)
            .isDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceValue)
            .isNotDisplayed()

        onViewWithId(resId = R.id.currentMarketPriceDate)
            .isNotDisplayed()
    }

    @Test
    fun whenStartActivityAndHistoricalMarketPricesHasError_shouldShowMarketPricesErrorText() {
        activityTestRule.activity.runOnUiThread {
            marketPrices.value = ViewState.Failure(FailureType.NoInternetConnection)
        }

        onViewWithId(resId = R.id.marketPricesProgressBar)
            .isNotDisplayed()

        onViewWithId(resId = R.id.marketPricesErrorText)
            .isDisplayed()

        onViewWithId(resId = R.id.marketPricesRecyclerView)
            .isNotDisplayed()
    }

}