/* xmark function */
function fClose(){
    var inputbar = document.getElementById('inputbar');
    inputbar.value = ""; // 검색바 내용 삭제
}

// ajax로 서버시간 호출하기
setInterval(function request_time() {
    $.ajax({
        type: "GET", 
        url: "http://localhost:8080/api/getTime", // api getTime
        data:{  // url 요청 시 보낼 파라미터
            timezone:"Asia/Seoul", 
            datetime:"http://localhost:8080/api/getTime?timezone=Asia/Seoul"
        },
        success: function(res){ //호출 성공했을때
            const time = document.getElementById("time"); // time 출력창
            time.innerHTML = res.datetime; // api/getTime 중 datetime 추출 & 출력
        },
        error:  function(){
            alert("TimeZone Fail");
        } 
    })
}, 1000); //1초마다 한번씩

// scheduler API
setInterval(function schedule_API() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/count", // api count
        data:{ // default 값
            countIP:0,
            countEMAIL:0
        },
        success: function(res){ // 성공했을 때
            const scheduler1 = document.getElementById("scheduler1"); // scheduler1 출력창
            scheduler1.innerHTML = "TODAY | IP: " + res.countIP + ", EMAIL: " + res.countEMAIL;
        }, 
        error: function(){
            alert("schedule_API Fail");
        }
    })
}, 5000); // 5초마다 한번씩

// setInterval(function websocket(){
//     $.ajax({
//         url: ""
//     })
// })
// ajax로 result 호출하기
function reqIPEmail() { // 정규식 test api 호출
    if(window.event.keyCode == 13){ // enter키 눌렀을 때
        var input = $('#inputbar').val(); // inputbar 입력 값 받기
        $.ajax({
            type: "GET", 
            url: "http://localhost:8080/api/reg?params=" + input, // api/reg 에 input param 던지기
            success: function(res){
                const result = document.getElementById("result"); // result 출력창
                result.innerHTML = res.result;
            },
            error:  function(){
                alert("Regex Fail");
            } 
        })

        // websocket
        sendWebsocket({'text': input});
    }
}

// delete API
function delResult(idx){
    $.ajax({
        type: "GET", 
        url: "http://localhost:8080/api/del?idx=" + idx, // array(idx) 삭제
        success: function(idx){
            const result = document.getElementById("result"); // result 출력창
            result.innerHTML = idx.result; // 삭제 후 재출력
        },
        error:  function(){
            alert("Delete Fail");
        } 
    })
}

// websocket 기본 세팅
var stompClient = null;
function setConnected(connected) { 
    $("#from").prop("disabled", connected);
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

 // window onload와 동시에 websocket에 connect
 window.onload = function connect() { // 생성된 소켓과 연결
    var url = 'ws://localhost:8080/broadcast';
    stompClient = Stomp.client(url); 
    stompClient.connect({}, function () {
        setConnected(true);
    }, function (err) {
        alert('error : ' + err);
    });                
}

// disconnect 눌렀을 때
function disconnect() {
    if (stompClient != null) { // 입력받은 from이 비었을 때
        stompClient.disconnect(function() {
            setConnected(false);
        });                    
    }                
}

// 입력 내용 console에 출력
function sendWebsocket(json) {
    stompClient.send("/app/broadcast", {}, JSON.stringify(json));
}
