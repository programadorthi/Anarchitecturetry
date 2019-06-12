package br.com.programadorthi.anarchtecturetry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.programadorthi.anarchtecturetry.utils.Activities
import br.com.programadorthi.anarchtecturetry.utils.createIntent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = createIntent(Activities.BlockchainActivity)
        startActivity(intent)
    }
}
