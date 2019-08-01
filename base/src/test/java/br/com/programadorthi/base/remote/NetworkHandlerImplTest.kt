package br.com.programadorthi.base.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class NetworkHandlerImplTest {

    private val context = mockk<Context>()

    private val connectivityManager = mockk<ConnectivityManager>()

    private val networkInfo = mockk<NetworkInfo>()

    @Before
    fun setUp() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        every { connectivityManager.activeNetworkInfo } returns networkInfo
    }

    @Test
    fun `should returns false when ConnectivityManager is null`() {
        // Override setUp definition
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns null

        val networkHandler = NetworkHandlerImpl(context)

        val result = networkHandler.hasInternetConnection()

        assertThat(result).isFalse
    }

    @Test
    fun `should returns false when NetworkInfo is null`() {
        // Override setUp definition
        every { connectivityManager.activeNetworkInfo } returns null

        val networkHandler = NetworkHandlerImpl(context)

        val result = networkHandler.hasInternetConnection()

        assertThat(result).isFalse
    }

    @Test
    fun `should returns false when there is no connection`() {
        every { networkInfo.isConnectedOrConnecting } returns false

        val networkHandler = NetworkHandlerImpl(context)

        val result = networkHandler.hasInternetConnection()

        assertThat(result).isFalse
    }

    @Test
    fun `should returns true when there is connection`() {
        every { networkInfo.isConnectedOrConnecting } returns true

        val networkHandler = NetworkHandlerImpl(context)

        val result = networkHandler.hasInternetConnection()

        assertThat(result).isTrue
    }
}