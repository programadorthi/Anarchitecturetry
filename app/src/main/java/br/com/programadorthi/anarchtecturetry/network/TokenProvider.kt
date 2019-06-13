package br.com.programadorthi.anarchtecturetry.network

interface TokenProvider {
    fun getToken(): String
    fun saveToken(token: String)
}