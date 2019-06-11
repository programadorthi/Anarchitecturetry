package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import io.reactivex.Single

interface BlockchainRemoteRepository {

    fun getCurrentMarketPrice(): Single<Blockchain>

    fun getAllMarketPrices(): Single<List<Blockchain>>

}