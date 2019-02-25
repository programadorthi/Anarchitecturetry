package br.com.programadorthi.blockchain.presentation

data class BlockchainViewData(
    val date: String,
    val value: String
) {
    companion object {
        val EMPTY = BlockchainViewData(
            date = "",
            value = ""
        )
    }
}