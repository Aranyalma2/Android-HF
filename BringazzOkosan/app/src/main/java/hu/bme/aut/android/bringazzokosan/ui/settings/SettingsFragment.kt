package hu.bme.aut.android.bringazzokosan.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hu.bme.aut.android.bringazzokosan.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}