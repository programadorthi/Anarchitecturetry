package br.com.programadorthi.anarchtecturetry.blockchain.domain

import br.com.programadorthi.base.shared.LayerResult

class BlockchainInteractorImpl(
    private val repository: BlockchainRepository
) : BlockchainInteractor {

    override suspend fun getCurrentMarketPrice(): LayerResult<Blockchain> {
        return repository.getCurrentMarketPrice()
    }

    override suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>> {
        return repository.getAllMarketPrices()
    }

}