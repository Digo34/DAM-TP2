package dam_a51743.coolweatherapp

data class Daily(
    var sunrise: ArrayList<String>,
    var sunset: ArrayList<String>
)

data class WeatherData(
    var latitude: String,
    var longitude: String,
    var timezone: String,
    var current_weather: CurrentWeather,
    var hourly: Hourly,
    var daily: Daily
)

data class CurrentWeather(
    var temperature: Float,
    var windspeed: Float,
    var winddirection: Int,
    var weathercode: Int,
    var time: String
)

data class Hourly(
    var time: ArrayList<String>,
    var temperature_2m: ArrayList<Float>,
    var weathercode: ArrayList<Int>,
    var pressure_msl: ArrayList<Double>
)

enum class WMOWeatherCode(var code: Int, var dayImage: Int, var nightImage: Int) {
    CLEAR_SKY(0, R.drawable.clear_day, R.drawable.clear_night),
    MAINLY_CLEAR(1, R.drawable.mostly_clear_day, R.drawable.mostly_clear_night),
    PARTLY_CLOUDY(2, R.drawable.partly_cloudy_day, R.drawable.partly_cloudy_night),
    OVERCAST(3, R.drawable.cloudy, R.drawable.cloudy),
    FOG(45, R.drawable.fog, R.drawable.fog),
    DEPOSITING_RIME_FOG(48, R.drawable.fog, R.drawable.fog),
    DRIZZLE_LIGHT(51, R.drawable.drizzle, R.drawable.drizzle),
    DRIZZLE_MODERATE(53, R.drawable.drizzle, R.drawable.drizzle),
    DRIZZLE_DENSE(55, R.drawable.drizzle, R.drawable.drizzle),
    FREEZING_DRIZZLE_LIGHT(56, R.drawable.freezing_drizzle, R.drawable.freezing_drizzle),
    FREEZING_DRIZZLE_DENSE(57, R.drawable.freezing_drizzle, R.drawable.freezing_drizzle),
    RAIN_SLIGHT(61, R.drawable.rain_light, R.drawable.rain_light),
    RAIN_MODERATE(63, R.drawable.rain, R.drawable.rain),
    RAIN_HEAVY(65, R.drawable.rain_heavy, R.drawable.rain_heavy),
    FREEZING_RAIN_LIGHT(66, R.drawable.freezing_rain_light, R.drawable.freezing_rain_light),
    FREEZING_RAIN_HEAVY(67, R.drawable.freezing_rain_heavy, R.drawable.freezing_rain_heavy),
    SNOW_FALL_SLIGHT(71, R.drawable.snow_light, R.drawable.snow_light),
    SNOW_FALL_MODERATE(73, R.drawable.snow, R.drawable.snow),
    SNOW_FALL_HEAVY(75, R.drawable.snow_heavy, R.drawable.snow_heavy),
    SNOW_GRAINS(77, R.drawable.snow, R.drawable.snow),
    RAIN_SHOWERS_SLIGHT(80, R.drawable.rain_light, R.drawable.rain_light),
    RAIN_SHOWERS_MODERATE(81, R.drawable.rain, R.drawable.rain),
    RAIN_SHOWERS_VIOLENT(82, R.drawable.rain_heavy, R.drawable.rain_heavy),
    SNOW_SHOWERS_SLIGHT(85, R.drawable.snow_light, R.drawable.snow_light),
    SNOW_SHOWERS_HEAVY(86, R.drawable.snow_heavy, R.drawable.snow_heavy),
    THUNDERSTORM_SLIGHT_MODERATE(95, R.drawable.tstorm, R.drawable.tstorm),
    THUNDERSTORM_HAIL_SLIGHT(96, R.drawable.tstorm, R.drawable.tstorm),
    THUNDERSTORM_HAIL_HEAVY(99, R.drawable.tstorm, R.drawable.tstorm)
}

fun getWeatherCodeMap(): Map<Int, WMOWeatherCode> {
    val weatherMap = HashMap<Int, WMOWeatherCode>()
    WMOWeatherCode.entries.forEach {
        weatherMap[it.code] = it
    }
    return weatherMap
}