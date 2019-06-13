package br.com.programadorthi.anarchtecturetry.blockchain.domain

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class BlockchainInteractorImplTest {

    private val blockchainRepository = mockk<BlockchainRepository>()

    private lateinit var blockchainInteractor: BlockchainInteractor

    @Before
    fun setUp() {
        blockchainInteractor = BlockchainInteractorImpl(blockchainRepository)
    }

    @Test
    fun `should get a empty blockchain when get current market price`() {
        every { blockchainRepository.getCurrentMarketPrice() } returns Flowable.just(Blockchain.EMPTY)

        val testObserver = blockchainInteractor.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(Blockchain.EMPTY)
    }

    @Test
    fun `should get a empty blockchain list when there is no blockchain history`() {
        every { blockchainRepository.getAllMarketPrices() } returns Flowable.just(listOf(Blockchain.EMPTY))

        val testObserver = blockchainInteractor.getAllMarketPrices().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { it.isNotEmpty() }
    }

    @Test
    fun `should complete when fetch current market price`() {
        val subject = PublishSubject.create<Blockchain>()

        every { blockchainRepository.fetchCurrentMarketPrice() } answers { subject.ignoreElements() }

        val testObserver = blockchainInteractor.fetchCurrentMarketPrice().test()

        subject.onComplete()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertOf { subject.hasComplete() }
    }

    @Test
    fun `should complete when fetch all market prices`() {
        val subject = PublishSubject.create<Blockchain>()

        every { blockchainRepository.fetchAllMarketPrices() } answers { subject.ignoreElements() }

        val testObserver = blockchainInteractor.fetchAllMarketPrices().test()

        subject.onComplete()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertOf { subject.hasComplete() }
    }
}