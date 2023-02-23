package hu.bme.aut.android.bringazzokosan.ui.exercise

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.google.android.gms.location.*
import hu.bme.aut.android.bringazzokosan.R
import hu.bme.aut.android.bringazzokosan.adapter.ExerciseAdapter
import hu.bme.aut.android.bringazzokosan.database.ExerciseDatabase
import hu.bme.aut.android.bringazzokosan.database.ExerciseItem
import hu.bme.aut.android.bringazzokosan.databinding.ActivityExerciseBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.properties.Delegates


class ExerciseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks,
    ExerciseAdapter.NewExerciseSaveListener {


    private lateinit var binding: ActivityExerciseBinding

    private val LOCATIONPERM = 124

    private lateinit var database: ExerciseDatabase
    private lateinit var adapter: ExerciseAdapter

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var preferenceSettings: SharedPreferences

    private var listOfSpeed: MutableList<Double> = mutableListOf()
    private var maxSpeed = 0.0
    private var avgSpeed = 0.0
    private var distance = 0.0
    private var startTime = 0
    private var lastlastLocation: Location? = null

    private var isDone:Boolean by Delegates.observable(false){property, oldValue, newValue ->
        if (newValue){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ExerciseDatabase.getDatabase(applicationContext)
        adapter = ExerciseAdapter(null) //just save unable to delete

        startTime = System.currentTimeMillis().toInt()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askForLocationPermission()
        createLocationRequest()

        binding.btnStop.setOnClickListener{
            onExerciseCreated(getExerciseData())
            finish()
        }

        preferenceSettings = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        //use imperial units instead of metric
        var unit1 = getString(R.string.km_h)
        var unit2 = getString(R.string.km)
        var unitMultiplier = 1.0
        if(preferenceSettings.getBoolean("key_imperialFormat",false)){
            unitMultiplier = 0.621371
            unit1 = getString(R.string.mi_h)
            unit2 = getString(R.string.mi)
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                //super.onLocationResult(p0)
                p0 ?: return
                if(!isDone){
                    //speed
                    var speed = ((p0.lastLocation.speed * 3.6 * unitMultiplier) * 100.0).roundToInt() / 100.0
                    //remove small gps errors
                    if(speed >= 1.0){
                        //max speed
                        if(speed > maxSpeed){
                            maxSpeed = speed
                        }
                        //avg speed
                        listOfSpeed.add(speed)
                        avgSpeed = (listOfSpeed.average() * 100.0).roundToInt() / 100.0

                        //distance
                        lastlastLocation?.let{
                            distance += p0.lastLocation.distanceTo(lastlastLocation) / 1000
                            distance = (distance * unitMultiplier * 100).roundToInt() / 100.0
                        }

                        lastlastLocation = p0.lastLocation
                    }
                    else{
                        speed = 0.0
                    }
                    //display
                    binding.currentSpeed.text = speed.toString().plus( " ").plus(unit1)
                    binding.maxSpeed.text = maxSpeed.toString().plus( " ").plus(unit1)
                    binding.avgSpeed.text = avgSpeed.toString().plus( " ").plus(unit1)
                    binding.distance.text = distance.toString().plus( " ").plus(unit2)

                    val rawDurInSeconds = (System.currentTimeMillis().toInt() - startTime)/1000
                    val hour = rawDurInSeconds / 3600
                    val min = rawDurInSeconds / 60 % 60
                    val sec = rawDurInSeconds % 60

                    binding.duration.text = String.format("%02d:%02d:%02d", hour, min, sec)



                }
            }
        }
    }

    override fun onExerciseCreated(newItem: ExerciseItem) {
        thread {
            val insertId = database.exerciseItemDao().insert(newItem)
            newItem.id = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    private fun getExerciseData(): ExerciseItem{
        val dateFormat = SimpleDateFormat("yyyy/M/dd hh:mm:ss")
        return ExerciseItem(
        time = dateFormat.format(Date()).toString(),
        maxspeed = binding.maxSpeed.text.toString(),
        avgspeed = binding.avgSpeed.text.toString(),
        distance = binding.distance.text.toString(),
        duration = binding.duration.text.toString()
        )
    }

    /*private fun getExerciseData() = ExerciseItem(

    )*/

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.exitMsg))
            .setPositiveButton(getString(R.string.exit)) { _, _ -> finish() }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun askForLocationPermission() {
        if (hasLocationPermissions()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->

            }
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Need location",
                LOCATIONPERM,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onResume(){
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Toast.makeText(this, "Auuu", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }
    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

}