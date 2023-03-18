package com.jh.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var stationEditText: EditText
    private lateinit var subwayListView: ListView
    private lateinit var noResultTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var subwayList: ArrayList<String>
    private lateinit var subwayArrayAdapter: ArrayAdapter<String>

    private var startIndex = 1
    private var endIndex = 10

    private val API_KEY = "API_KEY"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stationEditText = findViewById(R.id.stationEditText)
        subwayListView = findViewById(R.id.subway_list_view)
        noResultTextView = findViewById(R.id.no_result_text_view)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        subwayList = ArrayList()
        subwayArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subwayList)
        subwayListView.adapter = subwayArrayAdapter

        subwayListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val subway = subwayList[position]
            //지하철 정보 클릭시, 처리
        }

        swipeRefreshLayout.setOnRefreshListener {
            startIndex = 1
            endIndex = 10
            subwayList.clear()
            subwayArrayAdapter.notifyDataSetChanged()
            searchSubway()
        }
        searchSubway()
    }

    fun onCreateOptionsMenu(menu: Menu?, ture: Boolean): Boolean {
        val inflater = MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return ture
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_menu -> {
                swipeRefreshLayout.isRefreshing = true
                swipeRefreshLayout.postDelayed({
                    swipeRefreshLayout.isRefreshing = false
                    startIndex = 1
                    endIndex = 10
                    subwayList.clear()
                    subwayArrayAdapter.notifyDataSetChanged()
                    searchSubway()
                }, 2000)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchSubway() {
        val station = stationEditText.text.toString()
        if (station.isEmpty()) {
            return
        }

        val encodingStation = URLEncoder.encode(station, "UTF-8")

        val urlStr =
            "https://swopenapi.seoul.go.kr/api/subway/${API_KEY}/json/realtimeStationArrival/0/${startIndex}/${endIndex}/${encodingStation}"

        Thread {
            val response = getHttpResponse(urlStr)
            val subwayInfoList = parseSubwayInfoList(response)

            runOnUiThread {
                if (subwayInfoList.isEmpty()) {
                    noResultTextView.visibility = View.VISIBLE
                } else {
                    noResultTextView.visibility = View.GONE
                    subwayInfoList.forEach {
                        subwayList.add(it)
                    }
                    subwayArrayAdapter.notifyDataSetChanged()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }.start()
    }

    private fun parseSubwayInfoList(response: String): Any {

    }

    private fun getHttpResponse(urlStr: String): String {

        val url = URL(urlStr)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.readTimeout = 15000
        connection.connectTimeout = 15000
        connection.doInput = true

        try {
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuffer()

                var line: String? = reader.readLine()
                while (line != null) {
                    response.append(line)
                    line = reader.readLine()
                }
                reader.close()
                inputStream.close()

                return response.toString()
            } else {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } finally {
            connection.disconnect()
        }
    }
}

private fun Any.forEach(function: () -> Boolean) {

}

private fun Any.isEmpty(): Boolean {

}



