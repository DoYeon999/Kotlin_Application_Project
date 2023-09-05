package com.example.myapplication.weather_imgfind.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBottomSheetDialogBinding
import com.example.myapplication.weather_imgfind.model.FirstDayWeatherDB
import com.example.myapplication.weather_imgfind.model.ForecastModel
import com.example.myapplication.weather_imgfind.model.Meta
import com.example.myapplication.weather_imgfind.model.OtherDayWeatherDB
import com.example.myapplication.weather_imgfind.model.PreTideInfo
import com.example.myapplication.weather_imgfind.model.Result
import com.example.myapplication.weather_imgfind.model.TideInfo
import com.example.myapplication.weather_imgfind.model.TideModel
import com.example.myapplication.weather_imgfind.model.TidePreModel
import com.example.myapplication.weather_imgfind.model.TidePreModelDB
import com.example.myapplication.weather_imgfind.model.TotalWeatherDB
import com.example.myapplication.weather_imgfind.model.WeatherModel
import com.example.myapplication.weather_imgfind.model.temper
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.github.mikephil.charting.data.Entry
import com.github.usingsky.calendar.KoreanLunarCalendar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    lateinit var binding : ActivityBottomSheetDialogBinding
    var bottomcheckcondition = false
    //lateinit var binding : ActivitySixthPracticeGoogleMapBinding

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    //private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private val defaultLocation = LatLng(35.2100, 129.0689)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    data class MapLocation (
        var regId : String,
        var obscode : String,
        var obsname : String,
        var latitude : Double,
        var longtitude : Double)

    data class XYGrid (var x : Double, var y : Double)



    // [START maps_current_place_on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = ActivitySixthPracticeGoogleMapBinding.inflate(layoutInflater)
        // [START_EXCLUDE silent]
        // Retrieve location and camera position from saved instance state.
        // [START maps_current_place_on_create_save_instance_state]
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // [END maps_current_place_on_create_save_instance_state]
        // [END_EXCLUDE]

//        myadapter = APISlidingAdapter(this)
        binding = ActivityBottomSheetDialogBinding.inflate(layoutInflater)
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = "날씨"


        // [START_EXCLUDE silent]
        // Construct a PlacesClient
        Places.initialize(applicationContext, "BuildConfig.MAPS_API_KEY")
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Build the map.
        // [START maps_current_place_map_fragment]
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        // [END maps_current_place_map_fragment]
        // [END_EXCLUDE]

    }
    // [END maps_current_place_on_create]

    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    // [END maps_current_place_on_save_instance_state]

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    // [START maps_current_place_on_options_item_selected]
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace()
        }
        return true
    }
    // [END maps_current_place_on_options_item_selected]

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */

    // [START maps_current_place_on_map_ready]
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        var locations = listOf(
            MapLocation("11H20301", "DT_0054", "진해", 35.147, 128.643),
            MapLocation("11G00201", "DT_0004", "제주", 33.527, 126.543),
            MapLocation("11H20201", "DT_0005", "부산", 35.096, 129.035),
            MapLocation("21F20801", "DT_0007", "목포", 34.779, 126.375),
            MapLocation("11D20401", "DT_0012", "속초", 38.207, 128.594),
            MapLocation("11F20401", "DT_0016", "여수", 34.747, 127.765),
            MapLocation("11F20301", "DT_0027", "완도", 34.315, 126.759),
            MapLocation("21F20201", "DT_0028", "진도", 34.377, 126.308),
            MapLocation("11H20403", "DT_0029", "거제도", 34.801, 128.699),
            MapLocation("11F20401", "DT_0031", "거문도", 34.028, 127.308),
            MapLocation("11F20701", "DT_0035", "흑산도", 34.684, 125.435),
            MapLocation("11C20102", "DT_0050", "태안", 36.913, 126.238),
            MapLocation("11H20201", "DT_0056", "부산항 신항", 35.077, 128.768)
        )

        val markers = mutableListOf<Marker>()

        locations.forEach {
            val marker = map?.addMarker(
                MarkerOptions()
                    .title(it.obsname)
                    .position(LatLng(it.latitude, it.longtitude)))
            marker?.tag = it
            Log.d("googel", marker?.tag.toString())
            markers.add(marker!!)
        }

        map.setOnCameraIdleListener {
            for(marker in markers) {
                if(map.projection.visibleRegion.latLngBounds.contains(marker.position))
                    marker.showInfoWindow()
                else
                    marker.hideInfoWindow()
            }
        }

        map.setOnMarkerClickListener {
                clickedMarker -> showMarkerInfo(clickedMarker)
            true
        }

    }
    // [END maps_current_place_on_map_ready]

    @RequiresApi(Build.VERSION_CODES.O)
    fun showMarkerInfo(marker : Marker) {
        Log.d("google22", marker.title!!)
        Log.d("google22", marker.position.latitude.toString())
        Log.d("google22", marker.position.longitude.toString())

        val apikey = "/FFdZti8UpV2Ku/EnEYvg=="
        val apikeyTemp = "0DZUAX87M9kJWvxPJWL3raL5m9BYWp2N%2FzlC8zZYrvAg6Lwvv7WqwI4%2Bvb729zpp8rxMBKyp29N7kJEzNwrdhQ%3D%3D"
        val apikeyTemp2 = "0DZUAX87M9kJWvxPJWL3raL5m9BYWp2N/zlC8zZYrvAg6Lwvv7WqwI4+vb729zpp8rxMBKyp29N7kJEzNwrdhQ=="
        val apiKeyFore = "0DZUAX87M9kJWvxPJWL3raL5m9BYWp2N%2FzlC8zZYrvAg6Lwvv7WqwI4%2Bvb729zpp8rxMBKyp29N7kJEzNwrdhQ%3D%3D"
        val apiKeyFore2 = "0DZUAX87M9kJWvxPJWL3raL5m9BYWp2N/zlC8zZYrvAg6Lwvv7WqwI4+vb729zpp8rxMBKyp29N7kJEzNwrdhQ=="
        //val obscode = "DT_0001"
        val resulttype = "json"
        lateinit var apiTime : String
        lateinit var apiDay : String
        lateinit var apiDayFore : String

        val today = LocalDateTime.now()
        val apiTimeFore1 = today.minusDays(3L).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0600"
        Log.d("google22", apiTimeFore1)
        lateinit var apiTimeFore2 : String
        val firstDay = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val firstTime = today.format(DateTimeFormatter.ofPattern("HHmm"))
        when(firstTime.toInt()) {
            in 215..514 -> {
                apiTime = "0200"
                apiDay = firstDay
            }
            in 515..814 -> {
                apiTime = "0500"
                apiDay = firstDay
            }
            in 815..1114 -> {
                apiTime = "0800"
                apiDay = firstDay
            }
            in 1115..1414 -> {
                apiTime = "1100"
                apiDay = firstDay
            }
            in 1415..1714 -> {
                apiTime = "1400"
                apiDay = firstDay
            }
            in 1715..2014 -> {
                apiTime = "1700"
                apiDay = firstDay
            }
            in 2015..2314 -> {
                apiTime = "2000"
                apiDay = firstDay
            }
            in 2315..2400 -> {
                apiTime = "2300"
                apiDay = firstDay
            }
            else -> {
                apiTime = "2300"
                apiDay = today.minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            }
        }
        when(firstTime.toInt()) {
            in 605..1804 -> {
                apiDayFore = firstDay
                apiTimeFore2 = apiDayFore + "0600"
            }
            in 1805..2359 -> {
                apiDayFore = firstDay
                apiTimeFore2 = apiDayFore + "1800"
            }
            else -> {
                apiDayFore = today.minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                apiTimeFore2 = apiDayFore + "1800"
            }
        }
        val secondDay = today.plusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val thirdDay = today.plusDays(2L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val fourthDay = today.plusDays(3L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val fifthDay = today.plusDays(4L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val sixthDay = today.plusDays(5L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val seventhDay = today.plusDays(6L).format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val firstLunar = checkLunar(firstDay.substring(0, 4).toInt(), firstDay.substring(4, 6).toInt(), firstDay.substring(6).toInt())
        val secondLunar = checkLunar(secondDay.substring(0, 4).toInt(), secondDay.substring(4, 6).toInt(), secondDay.substring(6).toInt())
        val thirdLunar = checkLunar(thirdDay.substring(0, 4).toInt(), thirdDay.substring(4, 6).toInt(), thirdDay.substring(6).toInt())
        val fourthLunar = checkLunar(fourthDay.substring(0, 4).toInt(), fourthDay.substring(4, 6).toInt(), fourthDay.substring(6).toInt())
        val fifthLunar = checkLunar(fifthDay.substring(0, 4).toInt(), fifthDay.substring(4, 6).toInt(), fifthDay.substring(6).toInt())
        val sixthLunar = checkLunar(sixthDay.substring(0, 4).toInt(), sixthDay.substring(4, 6).toInt(), sixthDay.substring(6).toInt())
        val seventhLunar = checkLunar(seventhDay.substring(0, 4).toInt(), seventhDay.substring(4, 6).toInt(), seventhDay.substring(6).toInt())

        val lunarlist = listOf<String>(firstLunar, secondLunar, thirdLunar, fourthLunar, fifthLunar, sixthLunar, seventhLunar)

        val tideService = (applicationContext as APIApplication).tideService
        val tempService = (applicationContext as APIApplication).temperService
        val tidelist = mutableListOf<TideModel>()
        //val myadapter = APIAdapter(this@SixPracticeGoogleMap, tidelist)
        //Log.d("google22", marker?.tag.toString())
        val mytag : MapLocation = (marker?.tag!! as MapLocation)
        val mytide1 = tideService.getTide(apikey, mytag.obscode, firstDay, resulttype)
        val mytide2 = tideService.getTide(apikey, mytag.obscode, secondDay, resulttype)
        val mytide3 = tideService.getTide(apikey, mytag.obscode, thirdDay, resulttype)
        val mytide4 = tideService.getTide(apikey, mytag.obscode, fourthDay, resulttype)
        val mytide5 = tideService.getTide(apikey, mytag.obscode, fifthDay, resulttype)
        val mytide6 = tideService.getTide(apikey, mytag.obscode, sixthDay, resulttype)
        val mytide7 = tideService.getTide(apikey, mytag.obscode, seventhDay, resulttype)
        val nowtide = tideService.getPreTide(apikey, mytag.obscode, firstDay, resulttype)
        val xy = mapToGrid(mytag.latitude, mytag.longtitude)
        val weather = tempService.getWeather(apikeyTemp2, 1, 900, resulttype, apiDay, apiTime, xy.x.toInt(), xy.y.toInt())
        //val forecast = tempService.getForecast(apiKeyFore2, 1, 10, resulttype, mytag.regId, apiTimeFore1)
        val forecast2 = tempService.getForecast(apiKeyFore2, 1, 10, resulttype, mytag.regId, apiTimeFore2)
        Log.d("google22", "$apiDay, $apiTime, ${xy.x.toInt()}, ${xy.y.toInt()}")

        val temperatures = mutableListOf<temper>()
        //val windlist = mutableListOf<wind>()
        //val windlist2 = mutableListOf<wind>()
        //val wavelist = mutableListOf<wave>()
        var levels = mutableListOf<Entry>()
        val scope = CoroutineScope(Dispatchers.Main)

        val fishService = (applicationContext as APIApplication).fishService
        val todayTideDB = fishService.getTodayTide(mytag.obscode)
        val preWeathDB   = fishService.getFirstDayForecast(mytag.obscode)
        val preWeathDB2  = fishService.getOtherDayForecast(mytag.obscode)
        val todayTideList : MutableList<Entry> = ArrayList()
        val totalTideList : MutableList<TideModel> = ArrayList()
        lateinit var firstdayForecast : FirstDayWeatherDB
        lateinit var otherdayForecast : List<OtherDayWeatherDB>

        var tidetemplist : MutableList<TideInfo> = ArrayList()
        var temperaturelist : MutableList<temper> = ArrayList()

        val totalWeather = fishService.getTotalWeatherForecast(mytag.obscode)
        totalWeather.enqueue(object : Callback<TotalWeatherDB> {
            override fun onResponse(
                call: Call<TotalWeatherDB>,
                response: Response<TotalWeatherDB>
            ) {
                Log.d("today::::", "***${response.body()}***")
                Log.d("today::::", "***${response.body()!!.todaytidelist}***")
                Log.d("today::::", "***${response.body()!!.firstDayWeather}***")
                Log.d("today::::", "***${response.body()!!.otherDayWeather}***")

                val todayTide = response.body()!!.todaytidelist
                var i = 0
                todayTide!!.forEach {
                    todayTideList.add(Entry(i.toFloat(), it.tidelevel.toFloat()))
                    i++
                }

                firstdayForecast = response.body()!!.firstDayWeather
                if(firstdayForecast!!.tidelevelone != null) {
                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelone, firstdayForecast!!.tidetimeone, firstdayForecast!!.tidetypeone))
                }
                if(firstdayForecast!!.tideleveltwo != null) {
                    tidetemplist.add(TideInfo(firstdayForecast!!.tideleveltwo, firstdayForecast!!.tidetimetwo, firstdayForecast!!.tidetypetwo))
                }
                if(firstdayForecast!!.tidelevelthree != null) {
                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelthree, firstdayForecast!!.tidetimethree, firstdayForecast!!.tidetypethree))
                }
                if(firstdayForecast!!.tidelevelfour != null) {
                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelfour, firstdayForecast!!.tidetimefour, firstdayForecast!!.tidetypefour))
                }
                val temptide = TideModel(Result(tidetemplist, Meta("", "")))
                totalTideList.add(temptide)
                temperaturelist.add(temper(firstDay, "TMP", firstdayForecast.nowtemp))

                otherdayForecast = response.body()!!.otherDayWeather
                for(i in 0..5) {
                    var tidetemplist : MutableList<TideInfo> = ArrayList()
                    if(otherdayForecast[i]!!.tidelevelone != null) {
                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelone, otherdayForecast[i]!!.tidetimeone, otherdayForecast[i]!!.tidetypeone))
                    }
                    if(otherdayForecast[i]!!.tideleveltwo != null) {
                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tideleveltwo, otherdayForecast[i]!!.tidetimetwo, otherdayForecast[i]!!.tidetypetwo))
                    }
                    if(otherdayForecast[i]!!.tidelevelthree != null) {
                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelthree, otherdayForecast[i]!!.tidetimethree, otherdayForecast[i]!!.tidetypethree))
                    }
                    if(otherdayForecast[i]!!.tidelevelfour != null) {
                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelfour, otherdayForecast[i]!!.tidetimefour, otherdayForecast[i]!!.tidetypefour))
                    }
                    val temptide = TideModel(Result(tidetemplist, Meta("", "")))
                    totalTideList.add(temptide)
                    if(i == 0) {
                        temperaturelist.add(temper(secondDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(secondDay, "TMX", otherdayForecast[i].maxtemp))
                    } else if (i == 1) {
                        temperaturelist.add(temper(thirdDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(thirdDay, "TMX", otherdayForecast[i].maxtemp))
                    } else if (i == 2) {
                        temperaturelist.add(temper(fourthDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(fourthDay, "TMX", otherdayForecast[i].maxtemp))
                    } else if (i == 3) {
                        temperaturelist.add(temper(fifthDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(fifthDay, "TMX", otherdayForecast[i].maxtemp))
                    } else if (i == 4) {
                        temperaturelist.add(temper(sixthDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(sixthDay, "TMX", otherdayForecast[i].maxtemp))
                    } else if (i == 5) {
                        temperaturelist.add(temper(seventhDay, "TMN", otherdayForecast[i].mintemp))
                        temperaturelist.add(temper(seventhDay, "TMX", otherdayForecast[i].maxtemp))
                    }
                }
                if(!bottomcheckcondition) {
                    Log.d("today::::", "****************************")
                    bottomcheckcondition = true
                    val bottomsheetdialog = BottomSheetDialog(totalTideList, todayTideList, temperaturelist, mytag.obsname, lunarlist)
                    bottomsheetdialog.show(supportFragmentManager, "bottomsheetdialog")
                    bottomcheckcondition = false
                }
            }

            override fun onFailure(call: Call<TotalWeatherDB>, t: Throwable) {
                Log.d("today::::", "total failed")
            }
        })

//        todayTideDB.enqueue(object : Callback<List<TidePreModelDB>> {
//            override fun onResponse(
//                call: Call<List<TidePreModelDB>>,
//                response: Response<List<TidePreModelDB>>
//            ) {
//                val todayTide = response.body()
//                var i = 0
//                todayTide!!.forEach {
//                    todayTideList.add(Entry(i.toFloat(), it.tidelevel.toFloat()))
//                    i++
//                }
//                preWeathDB.enqueue(object : Callback<FirstDayWeatherDB> {
//                    override fun onResponse(
//                        call: Call<FirstDayWeatherDB>,
//                        response: Response<FirstDayWeatherDB>
//                    ) {
//                        firstdayForecast = response.body()!!
//                        if(firstdayForecast!!.tidelevelone != null) {
//                            tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelone, firstdayForecast!!.tidetimeone, firstdayForecast!!.tidetypeone))
//                        }
//                        if(firstdayForecast!!.tideleveltwo != null) {
//                            tidetemplist.add(TideInfo(firstdayForecast!!.tideleveltwo, firstdayForecast!!.tidetimetwo, firstdayForecast!!.tidetypetwo))
//                        }
//                        if(firstdayForecast!!.tidelevelthree != null) {
//                            tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelthree, firstdayForecast!!.tidetimethree, firstdayForecast!!.tidetypethree))
//                        }
//                        if(firstdayForecast!!.tidelevelfour != null) {
//                            tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelfour, firstdayForecast!!.tidetimefour, firstdayForecast!!.tidetypefour))
//                        }
//                        val temptide = TideModel(Result(tidetemplist, Meta("", "")))
//                        totalTideList.add(temptide)
//                        temperaturelist.add(temper(firstDay, "TMP", firstdayForecast.nowtemp))
//                        preWeathDB2.enqueue(object : Callback<List<OtherDayWeatherDB>> {
//                            override fun onResponse(
//                                call: Call<List<OtherDayWeatherDB>>,
//                                response: Response<List<OtherDayWeatherDB>>
//                            ) {
//                                otherdayForecast = response.body()!!
//                                for(i in 0..5) {
//                                    var tidetemplist : MutableList<TideInfo> = ArrayList()
//                                    if(otherdayForecast[i]!!.tidelevelone != null) {
//                                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelone, otherdayForecast[i]!!.tidetimeone, otherdayForecast[i]!!.tidetypeone))
//                                    }
//                                    if(otherdayForecast[i]!!.tideleveltwo != null) {
//                                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tideleveltwo, otherdayForecast[i]!!.tidetimetwo, otherdayForecast[i]!!.tidetypetwo))
//                                    }
//                                    if(otherdayForecast[i]!!.tidelevelthree != null) {
//                                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelthree, otherdayForecast[i]!!.tidetimethree, otherdayForecast[i]!!.tidetypethree))
//                                    }
//                                    if(otherdayForecast[i]!!.tidelevelfour != null) {
//                                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelfour, otherdayForecast[i]!!.tidetimefour, otherdayForecast[i]!!.tidetypefour))
//                                    }
//                                    val temptide = TideModel(Result(tidetemplist, Meta("", "")))
//                                    totalTideList.add(temptide)
//                                    if(i == 0) {
//                                        temperaturelist.add(temper(secondDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(secondDay, "TMX", otherdayForecast[i].maxtemp))
//                                    } else if (i == 1) {
//                                        temperaturelist.add(temper(thirdDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(thirdDay, "TMX", otherdayForecast[i].maxtemp))
//                                    } else if (i == 2) {
//                                        temperaturelist.add(temper(fourthDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(fourthDay, "TMX", otherdayForecast[i].maxtemp))
//                                    } else if (i == 3) {
//                                        temperaturelist.add(temper(fifthDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(fifthDay, "TMX", otherdayForecast[i].maxtemp))
//                                    } else if (i == 4) {
//                                        temperaturelist.add(temper(sixthDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(sixthDay, "TMX", otherdayForecast[i].maxtemp))
//                                    } else if (i == 5) {
//                                        temperaturelist.add(temper(seventhDay, "TMN", otherdayForecast[i].mintemp))
//                                        temperaturelist.add(temper(seventhDay, "TMX", otherdayForecast[i].maxtemp))
//                                    }
//                                }
//                                if(!bottomcheckcondition) {
//                                    Log.d("today::::", "****************************")
//                                    bottomcheckcondition = true
//                                    val bottomsheetdialog = BottomSheetDialog(totalTideList, todayTideList, temperaturelist, mytag.obsname, lunarlist)
//                                    bottomsheetdialog.show(supportFragmentManager, "bottomsheetdialog")
//                                    bottomcheckcondition = false
//                                }
//                                Log.d("today::::", "3 : $temperaturelist")
//                            }
//                            override fun onFailure(call: Call<List<OtherDayWeatherDB>>, t: Throwable) {
//                                Log.d("today::::", "failed22")
//                            }
//                        })
//                    }
//                    override fun onFailure(call: Call<FirstDayWeatherDB>, t: Throwable) {
//                        Log.d("today::::", "failed22")
//                    }
//                })
//            }
//            override fun onFailure(call: Call<List<TidePreModelDB>>, t: Throwable) {
//                Log.d("today::::", "failed11")
//            }
//        })

        // api에서 다이렉트로 받아오기
//        lateinit var firstdayForecast : FirstDayWeatherDB
//        lateinit var otherdayForecast : List<OtherDayWeatherDB>
//
//        var tidetemplist : MutableList<TideInfo> = ArrayList()
//        var temperaturelist : MutableList<temper> = ArrayList()
//        preWeathDB.enqueue(object : Callback<FirstDayWeatherDB> {
//            override fun onResponse(
//                call: Call<FirstDayWeatherDB>,
//                response: Response<FirstDayWeatherDB>
//            ) {
//                firstdayForecast = response.body()!!
//                if(firstdayForecast!!.tidelevelone != null) {
//                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelone, firstdayForecast!!.tidetimeone, firstdayForecast!!.tidetypeone))
//                }
//                if(firstdayForecast!!.tideleveltwo != null) {
//                    tidetemplist.add(TideInfo(firstdayForecast!!.tideleveltwo, firstdayForecast!!.tidetimetwo, firstdayForecast!!.tidetypetwo))
//                }
//                if(firstdayForecast!!.tidelevelthree != null) {
//                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelthree, firstdayForecast!!.tidetimethree, firstdayForecast!!.tidetypethree))
//                }
//                if(firstdayForecast!!.tidelevelfour != null) {
//                    tidetemplist.add(TideInfo(firstdayForecast!!.tidelevelfour, firstdayForecast!!.tidetimefour, firstdayForecast!!.tidetypefour))
//                }
//                val temptide = TideModel(Result(tidetemplist, Meta("", "")))
//                totalTideList.add(temptide)
//                temperaturelist.add(temper(firstDay, "TMP", firstdayForecast.nowtemp))
//            }
//            override fun onFailure(call: Call<FirstDayWeatherDB>, t: Throwable) {
//                Log.d("today::::", "failed22")
//            }
//        })

//        preWeathDB2.enqueue(object : Callback<List<OtherDayWeatherDB>> {
//            override fun onResponse(
//                call: Call<List<OtherDayWeatherDB>>,
//                response: Response<List<OtherDayWeatherDB>>
//            ) {
//                otherdayForecast = response.body()!!
//                for(i in 0..5) {
//                    var tidetemplist : MutableList<TideInfo> = ArrayList()
//                    if(otherdayForecast[i]!!.tidelevelone != null) {
//                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelone, otherdayForecast[i]!!.tidetimeone, otherdayForecast[i]!!.tidetypeone))
//                    }
//                    if(otherdayForecast[i]!!.tideleveltwo != null) {
//                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tideleveltwo, otherdayForecast[i]!!.tidetimetwo, otherdayForecast[i]!!.tidetypetwo))
//                    }
//                    if(otherdayForecast[i]!!.tidelevelthree != null) {
//                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelthree, otherdayForecast[i]!!.tidetimethree, otherdayForecast[i]!!.tidetypethree))
//                    }
//                    if(otherdayForecast[i]!!.tidelevelfour != null) {
//                        tidetemplist.add(TideInfo(otherdayForecast[i]!!.tidelevelfour, otherdayForecast[i]!!.tidetimefour, otherdayForecast[i]!!.tidetypefour))
//                    }
//                    val temptide = TideModel(Result(tidetemplist, Meta("", "")))
//                    totalTideList.add(temptide)
//                    if(i == 0) {
//                        temperaturelist.add(temper(secondDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(secondDay, "TMX", otherdayForecast[i].maxtemp))
//                    } else if (i == 1) {
//                        temperaturelist.add(temper(thirdDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(thirdDay, "TMX", otherdayForecast[i].maxtemp))
//                    } else if (i == 2) {
//                        temperaturelist.add(temper(fourthDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(fourthDay, "TMX", otherdayForecast[i].maxtemp))
//                    } else if (i == 3) {
//                        temperaturelist.add(temper(fifthDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(fifthDay, "TMX", otherdayForecast[i].maxtemp))
//                    } else if (i == 4) {
//                        temperaturelist.add(temper(sixthDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(sixthDay, "TMX", otherdayForecast[i].maxtemp))
//                    } else if (i == 5) {
//                        temperaturelist.add(temper(seventhDay, "TMN", otherdayForecast[i].mintemp))
//                        temperaturelist.add(temper(seventhDay, "TMX", otherdayForecast[i].maxtemp))
//                    }
//                }
//                Log.d("today::::", "3 : $temperaturelist")
//            }
//            override fun onFailure(call: Call<List<OtherDayWeatherDB>>, t: Throwable) {
//                Log.d("today::::", "failed22")
//            }
//        })

//        if(!bottomcheckcondition) {
//            Log.d("today::::", "****************************")
//            bottomcheckcondition = true
//            val bottomsheetdialog = BottomSheetDialog(totalTideList, todayTideList, temperaturelist, mytag.obsname, lunarlist)
//            bottomsheetdialog.show(supportFragmentManager, "bottomsheetdialog")
//            bottomcheckcondition = false
//        }

//        scope.launch {
//            try {
//                Log.d("maptest", "$bottomcheckcondition")
//                if(!bottomcheckcondition) {
//                    bottomcheckcondition = true
//                    //Log.d("maptest", "***$bottomcheckcondition***")
//                    mytide1.enqueue(object : Callback<TideModel> {
//                        override fun onResponse(call: Call<TideModel>, response: Response<TideModel>) {
//                            tidelist.clear()
//                            val tide = response.body()
//                            if(tide != null) tidelist.add(tide!!)
//                            mytide2.enqueue(object : Callback<TideModel> {
//                                override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                    val tide = response.body()
//                                    if(tide != null) tidelist.add(tide!!)
//                                    mytide3.enqueue(object : Callback<TideModel> {
//                                        override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                            val tide = response.body()
//                                            if(tide != null) tidelist.add(tide!!)
//                                            mytide4.enqueue(object : Callback<TideModel> {
//                                                override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                                    val tide = response.body()
//                                                    if(tide != null) tidelist.add(tide!!)
//                                                    mytide5.enqueue(object : Callback<TideModel> {
//                                                        override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                                            val tide = response.body()
//                                                            if(tide != null) tidelist.add(tide!!)
//                                                            mytide6.enqueue(object :
//                                                                Callback<TideModel> {
//                                                                override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                                                    val tide = response.body()
//                                                                    if(tide != null) tidelist.add(tide!!)
//                                                                    mytide7.enqueue(object :
//                                                                        Callback<TideModel> {
//                                                                        override fun onResponse(call : Call<TideModel>, response : Response<TideModel>) {
//                                                                            val tide = response.body()
//                                                                            if(tide != null) tidelist.add(tide!!)
//                                                                            Log.d("google22", "$tidelist")
//                                                                            nowtide.enqueue(object : Callback<TidePreModel> {
//                                                                                override fun onResponse(call : Call<TidePreModel>, response: Response<TidePreModel>) {
//                                                                                    val pretide = response.body()
//                                                                                    Log.d("google22", "$pretide")
//                                                                                    var i = 0
//                                                                                    for(s in pretide?.result?.data!!) {
//                                                                                        levels.add(Entry(i.toFloat(), s.tidelevel!!.toFloat()))
//                                                                                        i++
//                                                                                    }
//                                                                                    weather.enqueue(object : Callback<WeatherModel> {
//                                                                                        override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
//                                                                                            val temps = response.body()
//                                                                                            val tempPres = temps?.response?.body?.items?.tempPre
//                                                                                            var checknowtmp = true
//                                                                                            tempPres!!.forEach {
//                                                                                                if(it.fcstDate == firstDay && it.category == "TMP" && checknowtmp) {
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//                                                                                                    checknowtmp = false
//                                                                                                }
//                                                                                                if(it.fcstDate == secondDay && it.category == "TMN")
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//                                                                                                if(it.fcstDate == secondDay && it.category == "TMX")
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//                                                                                                if(it.fcstDate == thirdDay && it.category == "TMN")
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//                                                                                                if(it.fcstDate == thirdDay && it.category == "TMX")
//                                                                                                    temperatures.add(
//                                                                                                        temper(it.fcstDate, it.category, it.fcstValue)
//                                                                                                    )
//
//                                                                                                if(temperatures.size == 6) return@forEach
//                                                                                            }
//                                                                                            Log.d("google22", "yy22")
//                                                                                            forecast2.enqueue(object : Callback<ForecastModel> {
//                                                                                                override fun onResponse(call: Call<ForecastModel>, response: Response<ForecastModel>) {
//                                                                                                    val fore = response.body()
//                                                                                                    val forecasts = fore?.response?.body?.items?.tempFore
//                                                                                                    temperatures.add(
//                                                                                                        temper(fourthDay, "TMN", forecasts?.get(0)?.taMin3!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(fourthDay, "TMX", forecasts?.get(0)?.taMax3!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(fifthDay, "TMN", forecasts?.get(0)?.taMin4!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(fifthDay, "TMX", forecasts?.get(0)?.taMax4!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(sixthDay, "TMN", forecasts?.get(0)?.taMin5!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(sixthDay, "TMX", forecasts?.get(0)?.taMax5!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(seventhDay, "TMN", forecasts?.get(0)?.taMin6!!)
//                                                                                                    )
//                                                                                                    temperatures.add(
//                                                                                                        temper(seventhDay, "TMX", forecasts?.get(0)?.taMax6!!)
//                                                                                                    )
//                                                                                                    Log.d("google22", "$temperatures")
//                                                                                                    Log.d("google22", "$lunarlist")
//                                                                                                    val bottomsheetdialog = BottomSheetDialog(tidelist, levels, temperatures, mytag.obsname, lunarlist)
//                                                                                                    bottomsheetdialog.show(supportFragmentManager, "bottomsheetdialog")
//                                                                                                    bottomcheckcondition = false
//                                                                                                }
//                                                                                                override fun onFailure(call: Call<ForecastModel>, t: Throwable
//                                                                                                ) { Log.d("google22", "failedForecast") }
//                                                                                            })
//                                                                                        }
//                                                                                        override fun onFailure(call: Call<WeatherModel>, t: Throwable)
//                                                                                        { Log.d("google22", "failedTemper") }
//                                                                                    })
//                                                                                }
//                                                                                override fun onFailure(call : Call<TidePreModel>, t : Throwable) {
//                                                                                    call.cancel()
//                                                                                }
//                                                                            })
//                                                                        }
//                                                                        override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                                                            Log.d("google22", "failed")
//                                                                            call.cancel()
//                                                                        } })
//                                                                }
//                                                                override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                                                    Log.d("google22", "failed")
//                                                                    call.cancel()
//                                                                } })
//                                                        }
//                                                        override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                                            Log.d("google22", "failed")
//                                                            call.cancel()
//                                                        }
//                                                    })
//                                                }
//                                                override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                                    Log.d("google22", "failed")
//                                                    call.cancel()
//                                                }
//                                            })
//                                        }
//                                        override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                            Log.d("google22", "failed")
//                                            call.cancel()
//                                        }
//                                    })
//                                }
//                                override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                                    Log.d("google22", "failed")
//                                    call.cancel()
//                                }
//                            })
//                        }
//                        override fun onFailure(call: Call<TideModel>, t: Throwable) {
//                            Log.d("google22", "failed")
//                            call.cancel()
//                        }
//                    })
//                }
//            } catch (e : Exception) {
//                Log.d("google22", "failed api")
//            }
//        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */

    fun checkLunar(year : Int, month : Int, day : Int) : String{
        var checkYoon : Boolean = false

        if(year % 4 == 0) {
            if(year % 100 == 0) {
                checkYoon = year % 400 == 0
            }
            checkYoon = true
        }
        val nowCal = KoreanLunarCalendar.getInstance()
        nowCal.setLunarDate(year, month, day, checkYoon)
        val tempLunar = nowCal.solarIsoFormat
        val checkMoolDDae = tempLunar.substring(8).toInt()
        var returnVal : String = ""
        when(checkMoolDDae) {
            1 -> returnVal = "7물"
            2 -> returnVal = "8물"
            3 -> returnVal = "9물"
            4 -> returnVal = "10물"
            5 -> returnVal = "11물"
            6 -> returnVal = "12물"
            7 -> returnVal = "13물"
            8 -> returnVal = "14물(조금)"
            9 -> returnVal = "15물(무시)"
            10 -> returnVal = "1물"
            11 -> returnVal = "2물"
            12 -> returnVal = "3물"
            13 -> returnVal = "4물"
            14 -> returnVal = "5물"
            15 -> returnVal = "6물"
            16 -> returnVal = "7물"
            17 -> returnVal = "8물"
            18 -> returnVal = "9물"
            19 -> returnVal = "10물"
            20 -> returnVal = "11물"
            21 -> returnVal = "12물"
            22 -> returnVal = "13물"
            23 -> returnVal = "14물(조금)"
            24 -> returnVal = "15물(무시)"
            25 -> returnVal = "1물"
            26 -> returnVal = "2물"
            27 -> returnVal = "3물"
            28 -> returnVal = "4물"
            29 -> returnVal = "5물"
            30 -> returnVal = "6물"
        }

        return returnVal
    }


    fun mapToGrid(lat : Double, lon : Double) : XYGrid {
        var RE = 6371.00877 // 지구 반경(km)
        var GRID = 5.0 // 격자 간격(km)
        var SLAT1 = 30.0 // 투영 위도1(degree)
        var SLAT2 = 60.0 // 투영 위도2(degree)
        var OLON = 126.0 // 기준점 경도(degree)
        var OLAT = 38.0 // 기준점 위도(degree)
        var XO = 43 // 기준점 X좌표(GRID)
        var YO = 136 // 기1준점 Y좌표(GRID)
        var DEGRAD = Math.PI / 180.0
        var RADDEG = 180.0 / Math.PI

        var re = RE / GRID
        var slat1 = SLAT1 * DEGRAD
        var slat2 = SLAT2 * DEGRAD
        var olon = OLON * DEGRAD
        var olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = lon * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        val y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return XYGrid(x, y)
    }

    // [START maps_current_place_get_device_location]
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_get_device_location]

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    // [END maps_current_place_location_permission]

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }
    // [END maps_current_place_on_request_permissions_result]

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            map?.addMarker(
                MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)))

            // Prompt the user for permission.
            getLocationPermission()
        }
    }
    // [END maps_current_place_show_current_place]

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    // [START maps_current_place_open_places_dialog]
    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
            }

            if (markerLatLng == null) {
                return@OnClickListener
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map?.addMarker(
                MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet))

            // Position the map's camera at the location of the marker.
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM.toFloat()))
        }

        // Display the dialog.
        AlertDialog.Builder(this)
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
    }
    // [END maps_current_place_open_places_dialog]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_update_location_ui]

    companion object {
        private val TAG = MapActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }
}