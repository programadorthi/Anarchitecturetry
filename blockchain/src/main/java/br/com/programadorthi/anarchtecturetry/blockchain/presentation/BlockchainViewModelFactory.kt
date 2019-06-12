package br.com.programadorthi.anarchtecturetry.blockchain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BlockchainViewModelFactory(private val blockchainViewModel: BlockchainViewModel) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockchainViewModel::class.java)) {
            return blockchainViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}