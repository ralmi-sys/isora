package net.isora.vpn.database.preference

import androidx.preference.PreferenceDataStore

interface OnPreferenceDataStoreChangeListener {
    fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String)
}
