package br.com.programadorthi.anarchtecturetry.blockchain.domain

import br.com.programadorthi.base.shared.LayerResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainInteractorImplTest {

    private val blockchainRepository = mockk<BlockchainRepository>()

    private lateinit var blockchainInteractor: BlockchainInteractor

    @Before
    fun setUp() {
        blockchainInteractor = BlockchainInteractorImpl(blockchainRepository)
    }

    @Test
    fun `should get a empty blockchain when get current market price`() {
        coEvery { blockchainRepository.getCurrentMarketPrice() } returns LayerResult.success(Blockchain.EMPTY)

        runBlocking {
            val result = blockchainInteractor.getCurrentMarketPrice()
            assert(result is LayerResult.Success && result.data == Blockchain.EMPTY)
        }
    }

    @Test
    fun `should get a empty blockchain list when there is no blockchain history`() {
        coEvery { blockchainRepository.getAllMarketPrices() } returns LayerResult.success(emptyList())

        runBlocking {
            val result = blockchainInteractor.getAllMarketPrices()
            assert(result is LayerResult.Success && result.data.isEmpty())
        }
    }

    @Test
    fun `should get a blockchain when get current market price`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        coEvery { blockchainRepository.getCurrentMarketPrice() } returns LayerResult.success(expected)

        runBlocking {
            val result = blockchainInteractor.getCurrentMarketPrice()
            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should get a blockchain list when there is no blockchain history`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        coEvery { blockchainRepository.getAllMarketPrices() } returns LayerResult.success(listOf(expected))

        runBlocking {
            val result = blockchainInteractor.getAllMarketPrices()
            assert(result is LayerResult.Success && result.data.isNotEmpty() && result.data[0] == expected)
        }
    }

}