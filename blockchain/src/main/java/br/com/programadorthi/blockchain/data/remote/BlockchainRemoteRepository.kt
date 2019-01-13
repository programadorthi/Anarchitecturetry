package br.com.programadorthi.blockchain.data.remote

import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Single

interface BlockchainRemoteRepository {

    fun getCurrentMarketPrice(): Single<Blockchain>

    fun getAllMarketPrices(): Single<List<Blockchain>>

}