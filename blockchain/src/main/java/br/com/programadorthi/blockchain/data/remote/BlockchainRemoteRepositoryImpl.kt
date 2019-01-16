package br.com.programadorthi.blockchain.data.remote

import br.com.programadorthi.base.network.BaseMapper
import br.com.programadorthi.base.network.RemoteExecutor
import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Single

class BlockchainRemoteRepositoryImpl(
    private val blockchainCurrentValueMapper: BaseMapper<BlockchainCurrentValueRaw, Blockchain>,
    private val blockchainMapper: BaseMapper<BlockchainResponseRaw, List<Blockchain>>,
    private val blockchainService: BlockchainService,
    private val remoteExecutor: RemoteExecutor
) : BlockchainRemoteRepository {

    override fun getCurrentMarketPrice(): Single<Blockchain> {
        return remoteExecutor.checkConnectionAndThenMapper(blockchainCurrentValueMapper) {
            blockchainService.getCurrentMarketPrice()
        }
    }

    override fun getAllMarketPrices(): Single<List<Blockchain>> {
        return remoteExecutor.checkConnectionAndThenMapper(blockchainMapper) {
            blockchainService.getMarketPrices()
        }
    }

}