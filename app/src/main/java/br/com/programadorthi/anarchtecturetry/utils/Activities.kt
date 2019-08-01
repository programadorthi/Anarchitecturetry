package br.com.programadorthi.anarchtecturetry.utils

import br.com.programadorthi.anarchtecturetry.BuildConfig
import br.com.programadorthi.base.utils.AddressableActivity

object Activities {
    object BlockchainActivity : AddressableActivity {
        override val className = "${BuildConfig.PACKAGE_NAME}.blockchain.presentation.BlockchainActivity"
    }
}