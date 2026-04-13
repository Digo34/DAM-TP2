package dam_a51743.coolweatherapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import android.content.res.Configuration
import androidx.core.content.edit
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    var day = true // to swap between day(true) and night(false) theme

    // Start with Lisbon as default
    private var lastLat = 38.76f
    private var lastLon = -9.12f

    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)
        day = prefs.getBoolean("day", true)
        lastLat = prefs.getFloat("lastLat", 38.76f)
        lastLon = prefs.getFloat("lastLon", -9.12f)

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                if (day) setTheme(R.style.Theme_Day)
                else setTheme(R.style.Theme_Night)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                if (day) setTheme(R.style.Theme_Day_Land)
                else setTheme(R.style.Theme_Night_Land)
            }
            else -> {
                if (day) setTheme(R.style.Theme_Day)
                else setTheme(R.style.Theme_Night)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<EditText>(R.id.latInput).setText(lastLat.toString())
        findViewById<EditText>(R.id.lonInput).setText(lastLon.toString())

        val isRecreating = prefs.getBoolean("isRecreating", false)
        if (isRecreating) {
            prefs.edit(commit = true) { putBoolean("isRecreating", false) }
            fetchWeatherData(lastLat, lastLon).start()
        } else {
            getDeviceLocation()
        }

        // UPDATE button
        val btnUpdate = findViewById<Button>(R.id.updateButton)
        btnUpdate.setOnClickListener {
            lastLat = findViewById<EditText>(R.id.latInput).text.toString().toFloatOrNull() ?: 38.76f
            lastLon = findViewById<EditText>(R.id.lonInput).text.toString().toFloatOrNull() ?: -9.12f
            val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)
            prefs.edit(commit = true) {
                putFloat("lastLat", lastLat)
                putFloat("lastLon", lastLon)
            }
            fetchWeatherData(lastLat, lastLon).start()
        }
    }

    private fun weatherAPICall(lat: Float, long: Float): WeatherData {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${long}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m&")
            append("daily=sunrise,sunset&")
            append("timezone=auto")
        }
        val url = URL(reqString)
        url.openStream().use {
            return Gson().fromJson(InputStreamReader(it, "UTF-8"), WeatherData::class.java)
        }
    }

    private fun fetchWeatherData(lat: Float, long: Float): Thread {
        return Thread {
            val weather = weatherAPICall(lat, long)
            updateUI(weather)
        }
    }

    private fun updateUI(request: WeatherData) {
        runOnUiThread {
            val currentTime = request.current_weather.time
            val sunrise = request.daily.sunrise[0]
            val sunset = request.daily.sunset[0]
            val wasDay = day
            day = isDaytime(currentTime, sunrise, sunset)

            val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)

            if (wasDay != day) {
                prefs.edit(commit = true) {
                    putBoolean("day", day)
                    putFloat("lastLat", lastLat)
                    putFloat("lastLon", lastLon)
                    putBoolean("isRecreating", true)  // ADD THIS
                }
                recreate()
                return@runOnUiThread
            }

            val weatherImage = findViewById<ImageView>(R.id.weatherImage)
            val pressureValue = findViewById<TextView>(R.id.pressureValue)
            val windDirectionValue = findViewById<TextView>(R.id.windDirectionValue)
            val windSpeedValue = findViewById<TextView>(R.id.windSpeedValue)
            val temperatureValue = findViewById<TextView>(R.id.temperatureValue)
            val timeValue = findViewById<TextView>(R.id.timeValue)

            pressureValue.text = getString(R.string.pressure_value, request.hourly.pressure_msl[12].toString())
            windDirectionValue.text = getString(R.string.wind_direction_value, request.current_weather.winddirection.toString())
            windSpeedValue.text = getString(R.string.wind_speed_value, request.current_weather.windspeed.toString())
            temperatureValue.text = getString(R.string.temperature_value, request.current_weather.temperature.toString())
            timeValue.text = request.current_weather.time

            val weatherMap = getWeatherCodeMap()
            val wCode = weatherMap[request.current_weather.weathercode]
            val resID = if (day) wCode?.dayImage else wCode?.nightImage
            if (resID != null) weatherImage.setImageResource(resID)
        }
    }

    private fun isDaytime(currentTime: String, sunrise: String, sunset: String): Boolean {
        val current = currentTime.substringAfter("T")
        val rise = sunrise.substringAfter("T")
        val set = sunset.substringAfter("T")
        android.util.Log.d("WEATHER", "onCreate: day=$day, lat=$lastLat, lon=$lastLon")
        return current in rise..set
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private fun getDeviceLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLat = location.latitude.toFloat()
                lastLon = location.longitude.toFloat()
            }
            findViewById<EditText>(R.id.latInput).setText(lastLat.toString())
            findViewById<EditText>(R.id.lonInput).setText(lastLon.toString())
            fetchWeatherData(lastLat, lastLon).start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation()
        } else {
            fetchWeatherData(lastLat, lastLon).start()
        }
    }
}