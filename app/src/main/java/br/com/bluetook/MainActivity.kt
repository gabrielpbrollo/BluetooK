package br.com.bluetook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val btClient: Button by lazy {
        findViewById(R.id.btClient)
    }
    private val btServer: Button by lazy {
        findViewById(R.id.btServer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        btClient.setOnClickListener { openClientScreen() }
        btServer.setOnClickListener { openServerScreen() }
    }

    private fun openClientScreen() {
        startActivity(Intent(this, BluetooKClientSample::class.java))
    }

    private fun openServerScreen() {
        startActivity(Intent(this, BluetooKServerSample::class.java))
    }
}