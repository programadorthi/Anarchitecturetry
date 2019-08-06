package br.com.programadorthi.anarchtecturetry.blockchain.domain

import java.math.BigDecimal
import java.util.*

data class Blockchain(
    val date: Date,
    val value: BigDecimal
) {
    companion object {
        val EMPTY = Blockchain(Date(), BigDecimal.ZERO)
    }
}