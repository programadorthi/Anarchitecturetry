package br.com.programadorthi.blockchain.data.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.blockchain.domain.Blockchain
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.functions.Function
import org.junit.Before
import org.junit.Test

class BlockchainRemoteRepositoryImplTest {

    private val blockchainCurrentValueMapper = BlockchainCurrentValueRemoteMapper()

    private val blockchainMapper = BlockchainRemoteMapper()

    private val blockchainService = mockk<BlockchainService>()

    private val remoteExecutor = mockk<RemoteExecutor>()

    private val raw = BlockchainCurrentValueRaw(
        timestamp = null,
        value = null
    )

    private lateinit var blockchainRemoteRepository: BlockchainRemoteRepository

    @Before
    fun setUp() {

        every { blockchainService.getCurrentMarketPrice() } returns Single.just(raw)

        blockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
            blockchainCurrentValueMapper, blockchainMapper, blockchainService, remoteExecutor
        )
    }

    @Test
    fun `should throw NoInternetConnectionException when there is no internet connection`() {

        every {
            remoteExecutor.checkConnectionAndThenMapper(
                mapper = any<Function<BlockchainCurrentValueRaw, Blockchain>>(),
                action = any()
            )
        } returns Single.error(BaseException.NoInternetConnectionException())

        val testObserver = blockchainRemoteRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError(BaseException.NoInternetConnectionException::class.java)

    }

    @Test
    fun `should throw EssentialParamMissingException when all API call current mar response is invalid`() {

        every {
            remoteExecutor.checkConnectionAndThenMapper(
                mapper = any<Function<BlockchainCurrentValueRaw, Blockchain>>(),
                action = any()
            )
        } returns Single.error(BaseException.EssentialParamMissingException(
            missingParam = listOf("field").joinToString(prefix = "[", postfix = "]"),
            rawObject = raw)
        )

        val testObserver = blockchainRemoteRepository.getCurrentMarketPrice().test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError(BaseException.EssentialParamMissingException::class.java)
            .assertErrorMessage("[field] are missing in received object: $raw")

    }

}