package br.com.programadorthi.anarchtecturetry.blockchain.di.modules

import androidx.fragment.app.FragmentActivity
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.anarchtecturetry.utils.getOrCreateViewModel
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import java.math.BigDecimal
import java.util.*
import javax.inject.Named

@Module
class BlockchainPresentationModule(private val activity: FragmentActivity) {

    @Provides
    fun provideBlockchainViewModel(
        blockchainInteractor: BlockchainInteractor,
        coroutineScope: CoroutineScope,
        @Named(DATE_FORMATTER) dateFormatter: TextFormatter<Date>,
        @Named(MONEY_FORMATTER) moneyFormatter: TextFormatter<BigDecimal>
    ): BlockchainViewModel {
        return activity.getOrCreateViewModel {
            BlockchainViewModel(blockchainInteractor, dateFormatter, moneyFormatter, coroutineScope)
        }
    }

}