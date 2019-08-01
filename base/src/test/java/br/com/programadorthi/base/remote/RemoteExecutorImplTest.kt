package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.shared.FailureType
import br.com.programadorthi.base.shared.LayerResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoteExecutorImplTest {

    private val crashConsumer = mockk<CrashConsumer>()

    private val networkHandler = mockk<NetworkHandler>()

    private lateinit var remoteExecutor: RemoteExecutor

    @Before
    fun setUp() {
        every { networkHandler.hasInternetConnection() } returns true

        every { crashConsumer.report(any()) } answers { nothing }

        remoteExecutor = RemoteExecutorImpl(
            crashConsumer = crashConsumer,
            networkHandler = networkHandler,
            dispatcher = Dispatchers.Default
        )
    }

    @Test
    fun `should get a LayerResult Failure with a NoInternetConnectionException when there is no internet connection`() {
        every { networkHandler.hasInternetConnection() } returns false

        runBlocking {
            val result = remoteExecutor.checkConnectionAndThenDone {}
            assert(result is LayerResult.Failure && result.type is FailureType.NoInternetConnection)
        }
    }

    @Test
    fun `should report crash using crash consumer when there is any exception`() {
        runBlocking {
            val result = remoteExecutor.checkConnectionAndThenDone(action = {
                throw BaseException.HttpCallException(code = 500)
            })

            verify(exactly = 1) { crashConsumer.report(ofType(BaseException.HttpCallException::class)) }

            assert(
                result is LayerResult.Failure &&
                        result.type is FailureType.HttpCall &&
                        (result.type as FailureType.HttpCall).code == 500
            )
        }
    }

    @Test
    fun `should API call complete when there is internet connection`() {
        runBlocking {
            val result = remoteExecutor.checkConnectionAndThenDone { }

            verify(exactly = 0) { crashConsumer.report(any()) }

            assert(result is LayerResult.Success && result.data)
        }
    }

    @Test
    fun `should API call get single without mapper when there is internet connection`() {
        val expected = "this is a response"

        runBlocking {
            val result = remoteExecutor.checkConnectionAndThenReturn { expected }

            verify(exactly = 0) { crashConsumer.report(any()) }

            assert(result is LayerResult.Success && result.data == expected)
        }
    }

    @Test
    fun `should API call get single with mapper when there is internet connection`() {
        val input = "1234"
        val expected = 1234

        val mapper = mockk<BaseRemoteMapper<String, Int>>()

        every { mapper.apply(input) } returns expected

        runBlocking {
            val result = remoteExecutor.checkConnectionMapperAndThenReturn(mapper) { input }

            verify(exactly = 0) { crashConsumer.report(any()) }

            assert(result is LayerResult.Success && result.data == expected)
        }

    }

}