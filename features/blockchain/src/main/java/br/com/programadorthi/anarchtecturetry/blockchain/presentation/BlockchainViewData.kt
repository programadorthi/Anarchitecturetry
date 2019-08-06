package br.com.programadorthi.anarchtecturetry.blockchain.presentation

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