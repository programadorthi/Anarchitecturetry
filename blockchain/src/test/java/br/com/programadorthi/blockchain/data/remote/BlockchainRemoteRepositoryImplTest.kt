package br.com.programadorthi.blockchain.data.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.blockchain.domain.Blockchain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainRemoteRepositoryImplTest {

    private val blockchainCurrentValueMapper = BlockchainCurrentValueRemoteMapper()

    private val blockchainMapper = BlockchainRemoteMapper()

    private val blockchainService = mockk<BlockchainService>()

    private val remoteExecutor = mockk<RemoteExecutor>()

    private lateinit var blockchainRemoteRepository: BlockchainRemoteRepository

    @Before
    fun setUp() {
        blockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
            blockchainCurrentValueMapper, blockchainMapper, blockchainService, remoteExecutor
        )
    }

    /*@Test
    fun `should throw NoInternetConnectionException when there is no internet connection`() {
        every {
            remoteExecutor.checkConnectionAndThenMapper(blockchainCurrentValueMapper) {
                blockchainService.getCurrentMarketPrice()
            }
        } returns Single.error(BaseException.NoInternetConnectionException())

        val testObserver = blockchainRemoteRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError(BaseException.NoInternetConnectionException::class.java)

    }

    @Test
    fun `should throw EssentialParamMissingException when all API call current mar response is invalid`() {
        val raw = BlockchainCurrentValueRaw(
            timestamp = null,
            value = null
        )

        every { blockchainService.getCurrentMarketPrice() } returns Single.just(raw)

        every {
            remoteExecutor.checkConnectionAndThenMapper(blockchainCurrentValueMapper) {
                blockchainService.getCurrentMarketPrice()
            }
        } returns Single.just(Blockchain.EMPTY)

        val testObserver = blockchainRemoteRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError(BaseException.EssentialParamMissingException::class.java)
            .assertErrorMessage("[timestamp, market_price_usd] are missing in received object: $raw")

    }*/

}