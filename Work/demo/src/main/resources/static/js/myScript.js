var arrElement = []; // 시간 출력할 배열 생성 
var arr = []; // 검사 결과 출력할 배열 생성

/* xmark function */
function fClose(){
    var inputbar = document.getElementById('inputbar');
    inputbar.value = ""; /* 검색바 내용 삭제 */
}

/* Enter 입력 -> 유효성 검사 */
function printResult(inputbar){
    var inputbar = document.getElementById("inputbar").value; /* 입력 받은 내용*/

    /* IP 정규식 : 0~255사이의 숫자 + "." + 0~255사이의 숫자 + "." + 0~255사이의 숫자 + "." 0~255사이의 숫자 */
    const regIp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/; 
    /* Email 정규식 : \w(=[0-9a-zA-Z_]) 1회이상 @ \w 1회이상 "." 숫자&알파벳 */
    const regEmail = /^\w+@\w+\.[0-9a-zA-Z]+$/;

    if(window.event.keyCode == 13){ /* Enter 입력 시 */
        if(regIp.test(inputbar)){ /* regIp */
            printArray(" is IP");
        }else if(regEmail.test(inputbar)){ /* regEmail */
            printArray(" is Email");
        }else{ /* invalid */
            printArray(" is invalid format");
        }
    }
}

/* 입력받은 값 배열에 저장 -> 양식에 맞게 출력 */
function printArray(resultString){

    var inputbar = document.getElementById("inputbar").value; /* 입력 받은 내용*/
    var result = document.getElementById("result"); /* 검색결과 출력창*/
    var element = document.getElementById("time"); /* div time (현재시간) */

    // 출력창 초기화
    result.innerHTML = ""; 
    eTime.innerHTML = "";

    t = element.innerHTML + " | "; // 저장할 문자열 중 현재 시간
    s = inputbar + resultString; // 저장할 문자열 중 유효성검사 결과

    arrElement.unshift(t);
    arr.unshift(s); // 문자열을 배열에 저장(가장 최근 데이터가 위로 오게)

    if(arr.length > 5){ // 배열의 길이가 5를 넘지 않게
        arr.pop(); // 가장 오래된 데이터 삭제
        arrElement.pop();
    }
    for(var i=0; i<arr.length; i++){ // 문자열 저장한 배열 + 삭제버튼 + 개행
        eTime.innerHTML += arrElement[i] + "<br>";
        result.innerHTML += arr[i] + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";
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

// ajax로 서버시간 호출하기
setInterval(function request_time() {
    $.ajax({
        type: "GET", 
        url: "http://localhost:8080/api/getTime",
        success: function(res){ //호출 성공했을때
            const time = document.getElementById("time"); //time 출력창
            var getTime = res.datetime; //worldtimeapi 중 datetime 추출
            var setDay = getTime.substring(0,10); // 전체 문자열 중 날짜에 해당
            var setTime = getTime.substring(11,19); // 전체 문자열 중 시간에 해당
            time.innerText = setDay + " " + setTime; // 최종 time 출력
        },
        error:  function(){ //호출 error
            alert("Fail");
        } 
    })
}, 1000); //1초마다 한번씩

/* fetch로 좌상단 서버타임 출력하기 */
// setInterval(function serverTime(){ 
//     const config = {
//         method: "get"
//     }; /* GET request */
//     fetch("http://worldtimeapi.org/api/timezone/Asia/Seoul", config) /* url 주소로 get 요청 보내기 */
//         .then(response => response.json())    /* json 형식으로 응답받음 */
//         .then(data => {
//             const time = document.getElementById("time"); /* time 출력창 */
//             var getTime = data.datetime;
//             var setDay = getTime.substring(0,10); /* 전체 문자열 중 날짜에 해당 */
//             var setTime = getTime.substring(11,19); /* 전체 문자열 중 시간에 해당 */
//             time.innerText = setDay + " " + setTime; /* 최종 time 출력 */
//     })
//     .catch(error => console.log(error)); /* error 발생시 */
// },1000);
