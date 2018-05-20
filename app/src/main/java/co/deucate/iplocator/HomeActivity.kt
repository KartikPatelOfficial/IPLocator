package co.deucate.iplocator

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.shashank.sony.fancydialoglib.Animation
import com.shashank.sony.fancydialoglib.FancyAlertDialog
import okhttp3.*
import java.io.IOException


class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    var googleMap: GoogleMap? = null
    var latitude = 0.0
    var longitude = 0.0

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val url = "http://ip-api.com/json/203.77.200.35"
        getData(url)
    }

    private fun getData(url: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
                .url(url)
                .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {

            override fun onFailure(call: Call?, e: IOException?) {
                showError(e)
            }

            override fun onResponse(call: Call?, response: Response?) {

                if (response!!.isSuccessful) {
                    val jsonData = response.body()!!.string()
                    parseJSON(jsonData)
                } else {
                    showError()
                }
            }
        })
    }

    private fun parseJSON(jsonData: String) {
        val data = jsonData.replace("as", "dat")
        val mainDetail = Klaxon().parse<Details>(data)
        showResult(mainDetail)
    }

    private fun showResult(mainDetail: Details?) {
        val newDetail = ArrayList<String>()
        newDetail.add(mainDetail!!.dat)
        newDetail.add(mainDetail.city)
        newDetail.add(mainDetail.country)
        newDetail.add(mainDetail.isp)
        newDetail.add(mainDetail.query)
        newDetail.add(mainDetail.regionName)
        newDetail.add(mainDetail.timeZone)
        newDetail.add(mainDetail.zip)

        runOnUiThread({
            val recycler: RecyclerView = findViewById(R.id.recyclerView)
            recycler.layoutManager = LinearLayoutManager(this)
            recycler.adapter = Adapter(Values = newDetail)

            showMap(mainDetail.lat, mainDetail.lon)
        })

    }

    private fun showMap(lat: Double, lon: Double) {

        if (googleMap != null) {
            val point = LatLng(lat, lon)
            setMarker(point)
        } else {
            latitude = lat
            longitude = lon
        }

    }

    private fun showError(e: IOException? = null) {

        FancyAlertDialog.Builder(this).setTitle("Error")
                .setBackgroundColor(Color.RED)
                .setMessage(e?.localizedMessage ?: "Something went wrong while getting details.")
                .setAnimation(Animation.POP).build()

    }

    override fun onMapReady(p0: GoogleMap?) {
        checkPermission()
        googleMap = p0

        if (latitude != 0.0) {
            val point = LatLng(latitude, longitude)
            setMarker(point)
        }

    }

    private fun setMarker(point: LatLng) {
        googleMap!!.addMarker(MarkerOptions().position(point).title("Location"))
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(point))

        val cameraPosition = CameraPosition.Builder()
                .target(point)
                .bearing(45f)
                .tilt(90f)
                .zoom(9F)
                .build()

        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
    }

}
