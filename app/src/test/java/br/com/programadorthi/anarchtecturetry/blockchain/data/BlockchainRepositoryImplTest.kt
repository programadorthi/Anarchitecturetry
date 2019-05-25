package br.com.programadorthi.anarchtecturetry.blockchain.data

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.BlockchainRemoteRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
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
        every { localRepository.getCurrentMarketPrice() } returns Flowable.just(Blockchain.EMPTY)

        val testObserver = blockchainRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(Blockchain.EMPTY)
    }

    @Test
    fun `should get the blockchain value when fetch current market price`() {
        val expected = Blockchain(
            date = Date(),
            value = BigDecimal.ONE
        )

        val database = BehaviorSubject.create<Blockchain>()

        every { remoteRepository.getCurrentMarketPrice() } returns Single.just(Blockchain.EMPTY)

        every { localRepository.insertCurrentValueInTransaction(any()) } answers { database.onNext(expected) }

        val testObserver = blockchainRepository.fetchCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertOf { database.hasValue() }
            .assertOf { database.value == expected }
    }

    @Test
    fun `should get a empty blockchain list when there is no blockchain history`() {
        every { localRepository.getAllMarketPrices() } returns Flowable.just(listOf(Blockchain.EMPTY))

        val testObserver = blockchainRepository.getAllMarketPrices().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it.isNotEmpty() }
    }

    @Test
    fun `should get a blockchain list when insert or update history`() {
        val blockchain0 = Blockchain(
            date = Date(),
            value = BigDecimal.ZERO
        )
        val blockchain1 = blockchain0.copy(value = BigDecimal.ONE)
        val blockchain10 = blockchain0.copy(value = BigDecimal.TEN)

        val expected = listOf(blockchain0, blockchain1, blockchain10).toTypedArray()

        val database = ReplaySubject.create<Blockchain>()

        every { remoteRepository.getAllMarketPrices() } returns Single.just(listOf(Blockchain.EMPTY))

        every { localRepository.updateMarketPricesInTransaction(any()) } answers {
            database.onNext(blockchain0)
            database.onNext(blockchain1)
            database.onNext(blockchain10)
        }

        val testObserver = blockchainRepository.fetchAllMarketPrices().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertOf { database.hasValue() }
            .assertOf { expected.contentEquals(database.values) }
    }
}