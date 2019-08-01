package br.com.programadorthi.anarchtecturetry.blockchain.data

import br.com.programadorthi.anarchtecturetry.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.anarchtecturetry.blockchain.data.remote.BlockchainRemoteRepository
import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainRepository
import br.com.programadorthi.base.shared.LayerResult

class BlockchainRepositoryImpl(
    private val localRepository: BlockchainLocalRepository,
    private val remoteRepository: BlockchainRemoteRepository
) : BlockchainRepository {

    override suspend fun getCurrentMarketPrice(): LayerResult<Blockchain> {
        val remoteResult = remoteRepository.getCurrentMarketPrice()
        if (remoteResult is LayerResult.Success) {
            localRepository.insertCurrentValueInTransaction(remoteResult.data)
            return remoteResult
        }

        val localResult = localRepository.getCurrentMarketPrice()
        if (localResult is LayerResult.Success && localResult.data != Blockchain.EMPTY) {
            return localResult
        }
        return remoteResult
    }

    override suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>> {
        val remoteResult = remoteRepository.getAllMarketPrices()
        if (remoteResult is LayerResult.Success) {
            localRepository.updateMarketPricesInTransaction(remoteResult.data)
            return remoteResult
        }

        val localResult = localRepository.getAllMarketPrices()
        if (localResult is LayerResult.Success && localResult.data.isNotEmpty()) {
            return localResult
        }
        return remoteResult
    }

}