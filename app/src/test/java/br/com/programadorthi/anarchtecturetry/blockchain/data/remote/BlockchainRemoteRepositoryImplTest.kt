package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.shared.LayerResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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

        coEvery { blockchainService.getCurrentMarketPrice() } returns raw

        blockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
            blockchainCurrentValueMapper, blockchainMapper, blockchainService, remoteExecutor
        )
    }

    @Test
    fun `should get a LayerResult Failure with NoInternetConnectionException when there is no internet connection`() {
        coEvery {
            remoteExecutor.checkConnectionMapperAndThenReturn(
                mapper = any<BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>>(),
                action = any()
            )
        } returns LayerResult.failure(BaseException.NoInternetConnectionException)

        runBlocking {
            val result = blockchainRemoteRepository.getCurrentMarketPrice()
            assert(result is LayerResult.Failure && result.exception is BaseException.NoInternetConnectionException)
        }
    }

    @Test
    fun `should get a LayerResult Failure with EssentialParamMissingException when all API response is invalid`() {
        val expected = BaseException.EssentialParamMissingException(
            missingParam = listOf("field").joinToString(prefix = "[", postfix = "]"),
            rawObject = raw
        )

        coEvery {
            remoteExecutor.checkConnectionMapperAndThenReturn(
                mapper = any<BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>>(),
                action = any()
            )
        } returns LayerResult.failure(expected)

        runBlocking {
            val result = blockchainRemoteRepository.getCurrentMarketPrice()
            assert(
                result is LayerResult.Failure &&
                        result.exception is BaseException.EssentialParamMissingException &&
                        result.exception.message == "[field] are missing in received object: $raw"
            )
        }
    }

}