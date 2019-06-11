package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import io.reactivex.Single

class BlockchainRemoteRepositoryImpl(
    private val blockchainCurrentValueMapper: BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>,
    private val blockchainMapper: BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>,
    private val blockchainService: BlockchainService,
    private val remoteExecutor: RemoteExecutor
) : BlockchainRemoteRepository {

    override fun getCurrentMarketPrice(): Single<Blockchain> {
        return remoteExecutor.checkConnectionAndThenMapper(
            mapper = blockchainCurrentValueMapper,
            action = blockchainService.getCurrentMarketPrice()
        )
    }

    override fun getAllMarketPrices(): Single<List<Blockchain>> {
        return remoteExecutor.checkConnectionAndThenMapper(
            mapper = blockchainMapper,
            action = blockchainService.getMarketPrices()
        )
    }

}