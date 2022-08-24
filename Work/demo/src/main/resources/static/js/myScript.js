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
                // const time = document.getElementById("time"); // enter 기준 현재 시간
                // const eTime = document.getElementById("eTime"); // time 출력창
                // eTime.innerHTML = time.innerHTML + " | ";
            },
            error:  function(){ // 호출 error시 alert 오류발생
                alert("Regex 호출 실패");
            } 
        })
    }
}

/* 삭제버튼 function */
function deleteArray(idx){

    // 배열의 idx번째 내용 삭제
    arr.splice(idx,1); 
    arrElement.splice(idx,1);

     // 출력창 초기화
    result.innerHTML = ""; 
    eTime.innerHTML = "";

    for(var i=0; i<arr.length; i++){ // 재출력
        eTime.innerHTML += arrElement[i] + "<br>";
        result.innerHTML += arr[i] + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";
    }
}

/* 입력받은 값 배열에 저장 -> 양식에 맞게 출력 */
// function printArray(resultString){

//     var inputbar = document.getElementById("inputbar").value; // 입력 받은 내용*
//     var result = document.getElementById("result"); // 검색결과 출력창
//     var element = document.getElementById("time"); // div time (현재시간 innerHTML)

//     // 출력창 초기화
//     result.innerHTML = ""; 
//     eTime.innerHTML = "";

//     t = element.innerHTML + " | "; // 저장할 문자열 중 현재 시간
//     s = inputbar + resultString; // 저장할 문자열 중 유효성검사 결과

//     arrElement.unshift(t);
//     arr.unshift(s); // 문자열을 배열에 저장(가장 최근 데이터가 위로 오게)

//     if(arr.length > 5){ // 배열의 길이가 5를 넘지 않게
//         arr.pop(); // 가장 오래된 데이터 삭제
//         arrElement.pop();
//     }
//     for(var i=0; i<arr.length; i++){ // 문자열 저장한 배열 + 삭제버튼 + 개행
//         eTime.innerHTML += arrElement[i] + "<br>";
//         result.innerHTML += arr[i] + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";
//     }

// }
