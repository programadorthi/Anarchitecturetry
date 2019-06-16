package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

class BlockchainInteractorImpl(
    private val repository: BlockchainRepository
) : BlockchainInteractor {

    override suspend fun getCurrentMarketPrice(): Blockchain {
        return repository.getCurrentMarketPrice()
    }

    override suspend fun getAllMarketPrices(): List<Blockchain> {
        return repository.getAllMarketPrices()
    }

}