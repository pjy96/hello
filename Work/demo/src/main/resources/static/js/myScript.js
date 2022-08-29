var arrElement = []; // 시간 출력할 배열 생성 
var arr = []; // 검사 결과 출력할 배열 생성
var arrR = []; // R test용 배열

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
        error:  function(){ //호출 error시 alert Fail
            alert("Fail");
        } 
    })
}, 1000); //1초마다 한번씩

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
            error:  function(){ // 호출 error시 alert 오류발생
                alert("Regex 호출 실패");
            } 
        })
    }
}
//삭제 하기!!!!---------------------------
function delResult(idx){
    $.ajax({
        type: "GET", 
        url: "http://localhost:8080/api/del",
        success: function(idx){
            const result = document.getElementById("result"); // result 출력창
            result.innerHTML = "";
        },
        error:  function(){ // error시 alert 실패
            alert("삭제 실패");
        } 
    })
}
/* 삭제버튼 function */
// function deleteArray(idx){
//     resultArr.splice(idx,1); 
//     result.innerHTML = ""; 
//     for(var i=0; i<resultArr.size(); i++){ // 재출력
//         str += resultArr.get(i) + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";
//     }
// }