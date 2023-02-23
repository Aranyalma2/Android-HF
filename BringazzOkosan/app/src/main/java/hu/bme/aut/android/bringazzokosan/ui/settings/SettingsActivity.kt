package hu.bme.aut.android.bringazzokosan.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.bringazzokosan.R
import hu.bme.aut.android.bringazzokosan.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.settings)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.idFrameLayout, SettingsFragment())
            .commit()
    }
}