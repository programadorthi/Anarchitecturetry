package br.com.programadorthi.anarchtecturetry.utils

import br.com.programadorthi.anarchtecturetry.BuildConfig
import br.com.programadorthi.base.utils.StartableActivity

object Activities {
    object BlockchainActivity : StartableActivity {
        override val className = "${BuildConfig.PACKAGE_NAME}.blockchain.presentation.BlockchainActivity"
    }
}