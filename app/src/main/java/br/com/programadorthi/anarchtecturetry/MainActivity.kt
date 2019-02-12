package br.com.programadorthi.anarchtecturetry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.blockchain.presentation.BlockchainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this@MainActivity, BlockchainActivity::class.java))
        finish()
    }
}
