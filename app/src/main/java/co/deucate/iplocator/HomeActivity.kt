package co.deucate.iplocator

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.shashank.sony.fancydialoglib.Animation
import com.shashank.sony.fancydialoglib.FancyAlertDialog
import okhttp3.*
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        })

    }

    private fun showError(e: IOException? = null) {

        FancyAlertDialog.Builder(this).setTitle("Error")
                .setBackgroundColor(Color.RED)
                .setMessage(e?.localizedMessage ?: "Something went wrong while getting details.")
                .setAnimation(Animation.POP).build()

    }

}
