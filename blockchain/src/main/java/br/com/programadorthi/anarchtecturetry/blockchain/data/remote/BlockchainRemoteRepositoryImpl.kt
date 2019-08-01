package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.shared.LayerResult

class BlockchainRemoteRepositoryImpl(
    private val blockchainCurrentValueMapper: BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>,
    private val blockchainMapper: BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>,
    private val blockchainService: BlockchainService,
    private val remoteExecutor: RemoteExecutor
) : BlockchainRemoteRepository {

    override suspend fun getCurrentMarketPrice(): LayerResult<Blockchain> {
        return remoteExecutor.checkConnectionMapperAndThenReturn(blockchainCurrentValueMapper) {
            blockchainService.getCurrentMarketPrice()
        }
    }

    override suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>> {
        return remoteExecutor.checkConnectionMapperAndThenReturn(blockchainMapper) {
            blockchainService.getMarketPrices()
        }
    }

}