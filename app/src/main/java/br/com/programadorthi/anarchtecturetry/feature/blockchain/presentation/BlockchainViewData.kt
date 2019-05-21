package br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation

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