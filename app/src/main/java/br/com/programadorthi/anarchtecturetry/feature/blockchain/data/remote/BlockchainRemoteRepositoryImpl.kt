package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor

class BlockchainRemoteRepositoryImpl(
    private val blockchainCurrentValueMapper: BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>,
    private val blockchainMapper: BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>,
    private val blockchainService: BlockchainService,
    private val remoteExecutor: RemoteExecutor
) : BlockchainRemoteRepository {

    override suspend fun getCurrentMarketPrice(): Blockchain {
        return remoteExecutor.checkConnectionMapperAndThenReturn(blockchainCurrentValueMapper) {
            blockchainService.getCurrentMarketPrice()
        }
    }

    override suspend fun getAllMarketPrices(): List<Blockchain> {
        return remoteExecutor.checkConnectionMapperAndThenReturn(blockchainMapper) {
            blockchainService.getMarketPrices()
        }
    }

}