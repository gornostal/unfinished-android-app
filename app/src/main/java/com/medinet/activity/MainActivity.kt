package com.medinet.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.medinet.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanQrCodeButton.setOnClickListener { switchToQrScanner() }
    }

    private fun switchToQrScanner() {
        val intent = Intent(this, CodeScannerActivity::class.java)
        startActivity(intent)
    }

}
