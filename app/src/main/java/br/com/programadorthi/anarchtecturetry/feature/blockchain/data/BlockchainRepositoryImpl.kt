package br.com.programadorthi.anarchtecturetry.feature.blockchain.data

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.BlockchainRemoteRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainRepository
import br.com.programadorthi.base.exception.BaseException

class BlockchainRepositoryImpl(
    private val localRepository: BlockchainLocalRepository,
    private val remoteRepository: BlockchainRemoteRepository
) : BlockchainRepository {

    override suspend fun getCurrentMarketPrice(): Blockchain {
        return try {
            remoteRepository.getCurrentMarketPrice().apply {
                localRepository.insertCurrentValueInTransaction(this)
            }
        } catch (ex: BaseException.NoInternetConnectionException) {
            localRepository.getCurrentMarketPrice().apply {
                if (this == Blockchain.EMPTY) {
                    throw ex
                }
            }
        }
    }

    override suspend fun getAllMarketPrices(): List<Blockchain> {
        return try {
            remoteRepository.getAllMarketPrices().apply {
                localRepository.updateMarketPricesInTransaction(this)
            }
        } catch (ex: BaseException.NoInternetConnectionException) {
            localRepository.getAllMarketPrices().apply {
                if (this.isEmpty()) {
                    throw ex
                }
            }
        }
    }

}