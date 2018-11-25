package com.medinet.security

import android.content.Context
import android.content.SharedPreferences
import com.medinet.servicelocator.SL
import org.spongycastle.util.encoders.Hex
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils

class WalletStore(context: Context) : SL.IService {
    private var prefs: SharedPreferences

    companion object {
        const val SHARED_PREF_KEY = "WalletStore"
    }

    init {
        prefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
    }

    fun findCredentialsByPassword(password: String): Credentials? {
        val mnemonic = prefs.getString(password, null)
        if (mnemonic === null) {
            return null
        }

        return this.generateCredentialsFromPasswordAndMnemonic(password, mnemonic)
    }

    fun generateCredentialsFromPasswordAndMnemonic(password: String, mnemonic: String): Credentials {
        return WalletUtils.loadBip39Credentials(password, mnemonic)
    }

    fun saveMnemonicWithPassword(password: String, mnemonic: String) {
        prefs.edit().putString(password, mnemonic).apply()
    }

}