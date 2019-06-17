package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.shared.FailureType
import br.com.programadorthi.base.shared.LayerResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainLocalRepositoryImplTest {

    private val blockchainDao = mockk<BlockchainDao>()

    private val crashConsumer = mockk<CrashConsumer>()

    private lateinit var blockchainLocalRepository: BlockchainLocalRepository

    @Before
    fun setUp() {
        blockchainLocalRepository = BlockchainLocalRepositoryImpl(blockchainDao, crashConsumer, Dispatchers.Default)
    }

    @Test
    fun `should get a LayerResult Failure when current blockchain throws an exception in the database`() {
        val expected = Exception("this is a database exception")

        coEvery { blockchainDao.getCurrentValue() } throws expected

        coEvery { crashConsumer.report(any()) } just Runs

        runBlocking {
            val result = blockchainLocalRepository.getCurrentMarketPrice()
            assert(result is LayerResult.Failure && result.type == FailureType.Unknown)
        }
    }

    @Test
    fun `should get a empty blockchain when there is no current blockchain value in the database`() {
        coEvery { blockchainDao.getCurrentValue() } returns emptyList()

        runBlocking {
            val result = blockchainLocalRepository.getCurrentMarketPrice()
            assert(result is LayerResult.Success && result.data == Blockchain.EMPTY)
        }
    }

    @Test
    fun `should get the current blockchain value when insert or update the current value in the database`() {
        val entity = BlockchainCurrentValueEntity(
            timestamp = Date().time,
            value = BigDecimal.ONE
        )
        val expected = Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )

        coEvery { blockchainDao.getCurrentValue() } returns listOf(entity)

        runBlocking {
            val result = blockchainLocalRepository.getCurrentMarketPrice()
            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should get a LayerResult Failure when blockchain history throw an exception in the database`() {
        val expected = Exception("this is a database exception")

        coEvery { blockchainDao.getHistoricalMarketPrices() } throws expected

        coEvery { crashConsumer.report(any()) } just Runs

        runBlocking {
            val result = blockchainLocalRepository.getAllMarketPrices()
            assert(result is LayerResult.Failure && result.type == FailureType.Unknown)
        }
    }

    @Test
    fun `should get a empty blockchain list when there is no blockchain history in the database`() {
        coEvery { blockchainDao.getHistoricalMarketPrices() } returns emptyList()

        runBlocking {
            val result = blockchainLocalRepository.getAllMarketPrices()
            assert(result is LayerResult.Success && result.data.isEmpty())
        }
    }

    @Test
    fun `should get a blockchain list when insert or update a blockchain history in the database`() {
        val entity = BlockchainEntity(
            timestamp = Date().time,
            value = BigDecimal.ONE
        )
        val expected = Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )

        coEvery { blockchainDao.getHistoricalMarketPrices() } returns listOf(entity)

        runBlocking {
            val result = blockchainLocalRepository.getAllMarketPrices()
            assert(result is LayerResult.Success && result.data.isNotEmpty() && result.data[0] == expected)
        }
    }
}