<h1>RealtimeMetroSVC</h1>

<h2>Code description



<h4>해당 코드에 대한 설명
안드로이드 APP의 MainActivity를 정의하는 Kotlin 코드로,

1 ~ 16: 사용할 패키지와 라이브러리를 import 한다.

18 ~ 44: AppCompatActivity 클래스를 상속 받아, MainActivity를 정의하고, EditText, ListView, TextView, SwipeRefreshLayout 등의 View 객체 / 변수 / method 가 선언되어 있다

46 ~ 53: 지하철 역 이름을 입력받는 EditText, 지하철 역 정보를 보여주는 ListView, 결과가 없을 경우 보여 줄 TextView, SwipeRefreshLayout 객체를 초기화 한다.

55 ~ 58: ListView에 표시할 지하철 역 정보를 저장할, subwayList 객체와 이 객체를 ListView와 연결하는 subwayArrayAdapter 객체를 선언합니다.

60 ~ 66: ListView에 역 정보를 클릭 시 때 실행될 이벤트 리스너를 정의한다.

68 ~ 74: SwipeRefreshLayout 객체에 새로고침 이벤트 리스너를 정의한다

76: searchSubway() method를 호출한다.

78 ~ 86: onCreateOptionsMenu() method를 정의한다. 해당 method는, activity에서 옵션 메뉴를 만들어 주는 역할을 한다.

88 ~ 99: onOptionsItemSelected() method를 정의한다. 여기선 옵션에서 선택한 항목에 대하여 event처리를 진행 한다.

101 ~ 121: 역 정보를 검색하는 searchSubway() method를 정의한다.

123 ~ 153: api server 측에서 받아온 역 정보를 파싱, subwayInfoList에 저장하는 parseSubwayInfoList() method를 정의한다

155 ~ 181: server에 GET 요청을 보내고, 응답을 받는 getHttpResponse() method를 정의, 이 method 서버와 통신하는 HTTPURLConnection 객체를 생성하고 연결한다. 이후, InputStream으로부터 데이터를 읽어 StringBuffer에 저장하여 반환하게 된다. 마지막으로, HTTPURLConnection 객체를 연결을 끊는다.



