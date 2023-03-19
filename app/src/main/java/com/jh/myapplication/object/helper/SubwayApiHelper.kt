
package com.jh.myapplication.`object`.helper

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

data class Train(val lineNm: String, val arvlMsg: String)

object SubwayApiHelper {
    private const val BASE_URL = "https://swopenapi.seoul.go.kr/api/subway/"
    private const val API_KEY = "인증키"

    //getRealtimeArrival : 지하철 api의 realtimeStationArrival 엔드포인트를 호출하여,
    //지정한 앱의 실시간 열차 도착 정보를 가져온다.
    //역 이름 (statNm) 과 api호출 결과의 callback 함수인, successCallback과, errorCallback
    //를 인자로 받는다.
    //호출 결과는 비동기적으로 처리하며 VolleySingleton 라이브러리를 사용 한다.
    fun getRealtimeArrival(
        //statNm : 역 이름
        statNm: String, successCallback: (List<Train>) -> Unit, errorCallback: () -> Unit, startNm: String
    ) {
        val url = "${BASE_URL}realtimeStationArrival/0/10/$statNm/xml/$API_KEY"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val trainList = mutableListOf<Train>()
            val itemList = response.getJSONObject("realtimeArrival").getJSONArray("row")
            for (i in 0 until itemList.length()) {
                val item = itemList.getJSONObject(i)
                val train = Train(item.getString("trainLineNm"), item.getString("arvlMsg2"))
                trainList.add(train)
            }
            successCallback(trainList)
        }, {
            errorCallback()
        })
        VolleySingleton.getInstance()?.addToRequestQueue(request)
    }
}

object VolleySingleton {
    private var instance: VolleySingleton? = null

    fun getInstance(): VolleySingleton? {
        if (instance == null) {
            instance = VolleySingleton
        }
        return instance
    }

    fun addToRequestQueue(request: JsonObjectRequest) {
        // implementation omitted
    }
}
