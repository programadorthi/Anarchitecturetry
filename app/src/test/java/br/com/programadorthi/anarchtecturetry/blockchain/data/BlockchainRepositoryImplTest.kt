package br.com.programadorthi.anarchtecturetry.blockchain.data

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.BlockchainRemoteRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainRepository
import br.com.programadorthi.base.shared.FailureType
import br.com.programadorthi.base.shared.LayerResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainRepositoryImplTest {

    private val localRepository = mockk<BlockchainLocalRepository>()

    private val remoteRepository = mockk<BlockchainRemoteRepository>()

    private lateinit var blockchainRepository: BlockchainRepository

    @Before
    fun setUp() {
        blockchainRepository = BlockchainRepositoryImpl(localRepository, remoteRepository)
    }

    @Test
    fun `should get a empty blockchain when get current market price`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        coEvery { remoteRepository.getCurrentMarketPrice() } returns LayerResult.failure(FailureType.Unknown)

        coEvery { localRepository.getCurrentMarketPrice() } returns LayerResult.success(expected)

        runBlocking {
            val result = blockchainRepository.getCurrentMarketPrice()
            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should get the blockchain value when fetch current market price`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        coEvery { remoteRepository.getCurrentMarketPrice() } returns LayerResult.success(expected)

        coEvery { localRepository.insertCurrentValueInTransaction(any()) } returns LayerResult.success(true)

        runBlocking {
            val result = blockchainRepository.getCurrentMarketPrice()

            coVerify(exactly = 1) { localRepository.insertCurrentValueInTransaction(any()) }

            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should get the blockchain value from local when remote call throw an exception`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        coEvery { remoteRepository.getCurrentMarketPrice() } returns LayerResult.failure(FailureType.Unknown)

        coEvery { localRepository.getCurrentMarketPrice() } returns LayerResult.success(expected)

        runBlocking {
            val result = blockchainRepository.getCurrentMarketPrice()

            coVerify(exactly = 0) { localRepository.insertCurrentValueInTransaction(any()) }

            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should get a empty blockchain list from server when there is no blockchain history`() {
        coEvery { remoteRepository.getAllMarketPrices() } returns LayerResult.success(emptyList())

        coEvery { localRepository.updateMarketPricesInTransaction(any()) } returns LayerResult.success(true)

        runBlocking {
            val result = blockchainRepository.getAllMarketPrices()
            assert(result is LayerResult.Success && result.data.isEmpty())
        }
    }

    @Test
    fun `should get a blockchain history list from server only`() {
        val blockchainZero = Blockchain(
            date = Date(),
            value = BigDecimal.ZERO
        )
        val blockchainOne = blockchainZero.copy(value = BigDecimal.ONE)
        val blockchainTen = blockchainZero.copy(value = BigDecimal.TEN)

        val expected = listOf(blockchainZero, blockchainOne, blockchainTen)

        coEvery { remoteRepository.getAllMarketPrices() } returns LayerResult.success(expected)

        coEvery { localRepository.updateMarketPricesInTransaction(any()) } returns LayerResult.success(true)

        runBlocking {
            val result = blockchainRepository.getAllMarketPrices()

            coVerify(exactly = 1) { localRepository.updateMarketPricesInTransaction(any()) }

            assert(result is LayerResult.Success && result.data.isNotEmpty() && result.data.containsAll(expected))
        }
    }

    @Test
    fun `should get a blockchain history list from local only`() {
        val blockchainZero = Blockchain(
            date = Date(),
            value = BigDecimal.ZERO
        )
        val blockchainOne = blockchainZero.copy(value = BigDecimal.ONE)
        val blockchainTen = blockchainZero.copy(value = BigDecimal.TEN)

        val expected = listOf(blockchainZero, blockchainOne, blockchainTen)

        coEvery { remoteRepository.getAllMarketPrices() } returns LayerResult.failure(FailureType.Unknown)

        coEvery { localRepository.getAllMarketPrices() } returns LayerResult.success(expected)

        runBlocking {
            val result = blockchainRepository.getAllMarketPrices()

            coVerify(exactly = 0) { localRepository.updateMarketPricesInTransaction(any()) }

            assert(result is LayerResult.Success && result.data.isNotEmpty() && result.data.containsAll(expected))
        }
    }
}