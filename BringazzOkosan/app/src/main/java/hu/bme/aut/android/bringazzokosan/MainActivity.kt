package hu.bme.aut.android.bringazzokosan

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.aut.android.bringazzokosan.databinding.ActivityMainBinding
import hu.bme.aut.android.bringazzokosan.ui.settings.SettingsActivity
import java.util.*


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var sP: SharedPreferences
    private lateinit var language: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sP = PreferenceManager.getDefaultSharedPreferences(baseContext)
        language = sP.getString("key_selectedLanguage","1")!!
        setLocale(language)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsToolbar -> {
                val exerciseIntent = Intent(this, SettingsActivity::class.java)
                startActivity(exerciseIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        if(language != sP.getString("key_selectedLanguage","1")){
            language = sP.getString("key_selectedLanguage","1")!!
            sP.getString("key_selectedLanguage","1")?.let { setLocale(it) }
            finish()
            startActivity(intent)
        }
        super.onResume()
    }

    fun setLocale(languageCode: String) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        resources.updateConfiguration(config, resources.displayMetrics)
    }


}