package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class RemoteExecutorImplTest {

    private val crashConsumer = mockk<Consumer<Throwable>>()

    private val networkHandler = mockk<NetworkHandler>()

    private lateinit var remoteExecutor: RemoteExecutor

    @Before
    fun setUp() {
        every { networkHandler.hasInternetConnection() } returns true

        every { crashConsumer.accept(any()) } answers { nothing }

        remoteExecutor = RemoteExecutorImpl(
            crashConsumer = crashConsumer,
            networkHandler = networkHandler,
            scheduler = Schedulers.io()
        )
    }

    @Test
    fun `should throw NoInternetConnectionException when there is no internet connection`() {
        every { networkHandler.hasInternetConnection() } returns false

        val testObserver = remoteExecutor.checkConnectionAndThenComplete(
            action = Completable.complete()
        ).test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError { err -> err is BaseException.NoInternetConnectionException }
    }

    @Test
    fun `should report crash using crash consumer when there is any exception`() {
        val expected = Throwable("this is an exception")

        val testObserver = remoteExecutor.checkConnectionAndThenComplete(
            action = Completable.error(expected)
        ).test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertNotComplete()
            .assertError { err -> err.message == expected.message }
            .assertOf { verify { crashConsumer.accept(expected) } }
    }

    @Test
    fun `should API call complete when there is internet connection`() {
        val testObserver = remoteExecutor.checkConnectionAndThenComplete(
            action = Completable.complete()
        ).test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `should API call get single without mapper when there is internet connection`() {
        val response = "this is a response"

        val testObserver = remoteExecutor.checkConnectionAndThenSingle(
            action = Single.just(response)
        ).test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(response)
    }

    @Test
    fun `should API call get single with mapper when there is internet connection`() {
        val input = "1234"
        val output = 1234

        val mapper = mockk<Function<String, Int>>()

        every { mapper.apply(input) } returns output

        val testObserver = remoteExecutor.checkConnectionAndThenMapper(
            mapper = mapper,
            action = Single.just(input)
        ).test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(output)
    }

}