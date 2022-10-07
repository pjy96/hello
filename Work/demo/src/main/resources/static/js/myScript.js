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

// scheduler API========================================================================================================
function schedule_API() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/count", // api count
        success: function(data){
            const scheduler1 = document.getElementById("scheduler1"); // scheduler1 출력창
            scheduler1.innerHTML = "TODAY | IP: " + data.countIP + ", EMAIL: " + data.countEMAIL; 
            //console.log("IP: " + data.countIP + ", EMAIL: " + data.countEMAIL); // 로그 찍어보기
        }, 
        error: function(){
            alert("schedule_API Fail");
        }
    })
}
//========================================================================================================================

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
