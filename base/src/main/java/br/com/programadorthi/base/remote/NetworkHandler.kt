package br.com.programadorthi.base.remote

/**
 * Implements this interface to check if there is internet connection
 * before do an API request
 */
interface NetworkHandler {

    /**
     * Check if there is internet connection
     *
     * @return there is or there is no connection
     */
    fun hasInternetConnection(): Boolean

}