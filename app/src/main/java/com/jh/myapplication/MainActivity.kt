//Build: HAN JEONGHUN
//DEV TOOL : Intellij IDEA ultimate
//OS: WINDOWS 11 AND MAC OS
//First Build: 17 mar 2023

//description:
//지하철 api 를 활용 한 실시간 열차 도착 알리미 앱


package com.jh.myapplication

import android.annotation.SuppressLint
import android.os.Build.VERSION_CODES.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import androidx.appcompat.app.*;

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
        setContentView(R.layout.activity_main);
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.main_menu, menu)
        return true
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
                true
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
                    subwayList.addAll(subwayInfoList)
                    subwayArrayAdapter.notifyDataSetChanged()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }.start()
    }

    private fun parseSubwayInfoList(response: String): ArrayList<String> {
        val subwayInfoList = ArrayList<String>()

        try {
            val jsonObject = JSONObject(response)
            val jsonArray: JSONArray = jsonObject.getJSONArray("realtimeArrivalList")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val subwayName = item.getString("subwayId")
                val arrivalMsg = item.getString("arvlMsg2")

                val subwayInfo = "$subwayName : $arrivalMsg"
                subwayInfoList.add(subwayInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return subwayInfoList
    }

    private fun getHttpResponse(urlStr: String): String {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        try {
            val input = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuffer()

            var line = input.readLine()
            while (line != null) {
                response.append(line)
                line = input.readLine()
            }

            return response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return ""
    }

}