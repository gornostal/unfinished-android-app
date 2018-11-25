package com.medinet.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.medinet.R
import com.medinet.security.WalletStore
import com.medinet.servicelocator.SL
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                doLogin()
                return@OnEditorActionListener true
            }
            false
        })

        signInButton.setOnClickListener { doLogin() }
    }

    private fun doLogin() {

        // Reset errors.
        password.error = null
        mnemonic.error = null

        // Store values at the time of the login attempt.
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }

        if (!cancel) {
            password.isEnabled = false
            // ask for a seed phrase for a new password
            val walletStore = SL[WalletStore::class.java]
            if (walletStore.findCredentialsByPassword(passwordStr) !== null) {
                // wallet exists
                onLogin()
                return
            } else {

                signInButton.text = getString(R.string.action_use_and_sign_in)
                val mnemonicStr = mnemonic.text.toString()

                if (mnemonicStr.isBlank()) {
                    focusView = mnemonic
                    cancel = true
                    if (mnemonicSection.visibility == View.VISIBLE) {
                        mnemonic.error = getString(R.string.error_field_required)
                    }
                }

                // for debugging purposes only
                if (mnemonicSection.visibility == View.GONE) {
                    mnemonic.setText(getString(R.string.dummy_mnemonic))
                }

                mnemonicSection.visibility = View.VISIBLE
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            onLogin()
        }
    }

    private fun onLogin() {
        finish()
    }

}
