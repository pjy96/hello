// 회사명 검색바 내용 삭제
function fClose() {
    var inputbar = document.getElementById('input');
    inputbar.value = "";
}
// 회사코드 검색바 내용 삭제
function fClose1() {
    var inputbar1 = document.getElementById('input1');
    inputbar1.value = "";
}

// 서버시간 호출
setInterval(function request_Time() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/getTime",
        data: {
            timezone: "Asia/Seoul",
            datetime: "http://localhost:8080/getTime?timezone=Asia/Seoul"
        },
        success: function (res) {
            const time = document.getElementById("time");
            time.innerHTML = res.datetime;
        },
        error: function () {
            alert("TimeZone Fail");
        }
    })
}, 1000) // 1초마다 한번씩

// select 선택할때마다 다른 placeholder
function ddlselect() {
    var d = $("select").val();
    if (d == 0) {
        document.getElementById("d1").style.display = "inline-block";
        document.getElementById("d2").style.display = "none";
    }
    else {
        document.getElementById("d1").style.display = "none";
        document.getElementById("d2").style.display = "inline-block";
    }
}


// result 호출하기
function searchStock() {
    if (window.event.keyCode == 13) { // enter 키를 눌렀을 때
        var input = $('#input').val(); // input에서 회사명 입력 받은 것
        var input1 = $('#input1').val(); // input에서 회사코드를 입력 받은 것
        if (input != null) { // input으로 회사명 입력 받았을 때
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/searchStock?params=" + input, // input
                success: function (res) {
                    const result = document.getElementById("result"); // result 출력창
                    result.innerHTML = res.result; // result 화면에 출력
                },
                error: function () {
                    alert("Search Fail");
                }
            })
        } else if (input1 != null) {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/searchStock?params=" + input1, // input
                success: function (res) {
                    const result = document.getElementById("result"); // result 출력창
                    result.innerHTML = res.result; // result 화면에 출력
                },
                error: function () {
                    alert("Search Fail");
                }
            })
        }

    }
}

// result delete
function delResult(idx) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/delResult?idx=" + idx, // array(idx) 삭제
        success: function (idx) {
            const result = document.getElementById("result"); // result 출력창
            result.innerHTML = idx.result; // 삭제후 재출력
        },
        error: function () {
            alert("Delete Fail");
        }
    })
}

// open api 불어오기 test
function getAPITest() {
    $.ajax({
        type: "GET",
        url: "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService",
        data: {
            serviceKey: "serviceKey", // 서비스키
            numOfRows: "numOfRows", // 한 페이지 결과 수
            pageNo: "pageNo", // 페이지 번호
            resultType: "resultType", // 구분(xml, json) Default xml

        },
        success: function (msg) {
            alert("Success! message : " + msg)
            
        },
        error: function () {
            alert("API Test Fail");
        }
    })
}
