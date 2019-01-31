package br.com.programadorthi.blockchain.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.programadorthi.blockchain.R
import timber.log.Timber

class BlockchainActivity : AppCompatActivity() {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blockchain)

        Timber.d(">>>>>>>>>>>>>>>> ViewModelFactory: $viewModelFactory")
    }

}
