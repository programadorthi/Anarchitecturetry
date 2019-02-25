package br.com.programadorthi.blockchain.presentation

import java.math.BigDecimal
import java.util.*

data class BlockchainViewData(
    val date: Date,
    val value: BigDecimal
) {
    companion object {
        val EMPTY = BlockchainViewData(Date(), BigDecimal.ZERO)
    }
}