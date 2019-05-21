package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainLocalRepositoryImplTest {

    private val blockchainDao = mockk<BlockchainDao>()

    private val blockchainCurrentValueLocalMapper = BlockchainCurrentValueLocalMapper()

    private val blockchainLocalMapper = BlockchainLocalMapper()

    private lateinit var blockchainLocalRepository: BlockchainLocalRepository

    @Before
    fun setUp() {
        blockchainLocalRepository =
            BlockchainLocalRepositoryImpl(blockchainDao, blockchainCurrentValueLocalMapper, blockchainLocalMapper)
    }

    @Test
    fun `should get a empty blockchain when there is no current blockchain value in the database`() {
        every { blockchainDao.getCurrentValue() } returns Flowable.just(emptyList())

        val testObserver = blockchainLocalRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(Blockchain.EMPTY)
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

        every { blockchainDao.getCurrentValue() } returns Flowable.just(listOf(entity))

        val testObserver = blockchainLocalRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(expected)
    }

    @Test
    fun `should get a empty blockchain list when there is no blockchain history in the database`() {
        every { blockchainDao.getHistoricalMarketPrices() } returns Flowable.just(emptyList())

        val testObserver = blockchainLocalRepository.getAllMarketPrices().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValue { it.isEmpty() }
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

        every { blockchainDao.getHistoricalMarketPrices() } returns Flowable.just(listOf(entity))

        val testObserver = blockchainLocalRepository.getAllMarketPrices().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValue { it.isNotEmpty() }
            .assertValue(listOf(expected))
    }
}