package br.com.programadorthi.blockchain.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.programadorthi.base.exception.CrashlyticsConsumer
import br.com.programadorthi.base.network.NetworkHandlerImpl
import br.com.programadorthi.base.network.RemoteExecutorImpl
import br.com.programadorthi.base.presentation.ViewActionState
import br.com.programadorthi.blockchain.R
import br.com.programadorthi.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.blockchain.data.local.BlockchainCurrentValueEntity
import br.com.programadorthi.blockchain.data.local.BlockchainDao
import br.com.programadorthi.blockchain.data.local.BlockchainEntity
import br.com.programadorthi.blockchain.data.local.BlockchainLocalRepositoryImpl
import br.com.programadorthi.blockchain.data.remote.*
import br.com.programadorthi.blockchain.domain.BlockchainInteractorImpl
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BlockchainActivity : AppCompatActivity() {

    private val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = BlockchainViewModel(
            blockchainInteractor = BlockchainInteractorImpl(
                repository = BlockchainRepositoryImpl(
                    localRepository = BlockchainLocalRepositoryImpl(
                        blockchainDao = object : BlockchainDao() {
                            override fun insertCurrentValue(entity: BlockchainCurrentValueEntity) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun insertMarketPrices(prices: List<BlockchainEntity>) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun deleteCurrentValue() {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun deleteMarketPricesLessThanTimestamp(timestamp: Long) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun getCurrentValue(): Flowable<List<BlockchainCurrentValueEntity>> {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun getHistoricalMarketPrices(): Flowable<List<BlockchainEntity>> {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        }
                    ),
                    remoteRepository = BlockchainRemoteRepositoryImpl(
                        blockchainCurrentValueMapper = BlockchainCurrentValueMapper(),
                        blockchainMapper = BlockchainMapper(),
                        blockchainService = object : BlockchainService {
                            override fun getCurrentMarketPrice(): Single<BlockchainCurrentValueRaw> {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun getMarketPrices(timespan: String): Single<BlockchainResponseRaw> {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        },
                        remoteExecutor = RemoteExecutorImpl(
                            crashlyticsConsumer = CrashlyticsConsumer(),
                            networkHandler = NetworkHandlerImpl(
                                context = this@BlockchainActivity
                            ),
                            scheduler = Schedulers.io()
                        )
                    )
                )
            ),
            scheduler = Schedulers.computation()
        ) as T
    }

    private lateinit var viewModel: BlockchainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blockchain)

        viewModel = ViewModelProviders
            .of(this@BlockchainActivity, viewModelFactory)
            .get(BlockchainViewModel::class.java)

        initCurrentMarketPrice()
        initMarketPrices()
    }

    private fun initCurrentMarketPrice() {
        viewModel.currentMarketPrice.observe(this@BlockchainActivity, Observer { state ->
            when (state) {
                is ViewActionState.Complete -> {
                    Timber.d(">>>>>> Current Complete: ${state.result}")
                }
                is ViewActionState.Error -> {
                    Timber.d(state.error, ">>>>>> Current Error")
                }
            }
            Timber.d(">>>> Current Loading? ${state is ViewActionState.Loading}")
        })
    }

    private fun initMarketPrices() {
        viewModel.marketPrices.observe(this@BlockchainActivity, Observer { state ->
            when (state) {
                is ViewActionState.Complete -> {
                    Timber.d(">>>>>> Prices Complete: ${state.result}")
                }
                is ViewActionState.Error -> {
                    Timber.d(state.error, ">>>>>> Prices Error")
                }
            }
            Timber.d(">>>> Prices Loading? ${state is ViewActionState.Loading}")
        })
    }

}
