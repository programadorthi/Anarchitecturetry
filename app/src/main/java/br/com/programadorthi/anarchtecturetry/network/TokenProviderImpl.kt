package br.com.programadorthi.anarchtecturetry.network

class TokenProviderImpl : TokenProvider {

    override fun getToken(): String {
        // Put here where you get the token. E.g: shared preferences
        return "bearer abcdefghijklmnopqrstuvwxyz1234567890="
    }

    override fun saveToken(token: String) {
        // Put here where you save the token. E.g: shared preferences
    }
}