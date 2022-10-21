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



$(function () {
    var chatBox = $('.chat_box');
    var messageInput = $('input[name="message"]');
    var sendBtn = $('.send');
    var roomId = $('.content').data('room-id');
    var member = $('.content').data('member');

    var sock = new SockJS("/stomp-chat");
    var client = Stomp.over(sock); // 1. SockJS를 내부에 들고 있는 client를 내어준다.

    // 2. connection이 맺어지면 실행된다.
    client.connect({}, function () {
        // 3. send(path, header, message)로 메시지를 보낼 수 있다.
        client.send('/publish/chat/join', {}, JSON.stringify({chatRoomId: roomId, writer: member})); 
        // 4. subscribe(path, callback)로 메시지를 받을 수 있다. callback 첫번째 파라미터의 body로 메시지의 내용이 들어온다.
        client.subscribe('/subscribe/chat/room/' + roomId, function (chat) {
            var content = JSON.parse(chat.body);
            chatBox.append('<li>' + content.message + '(' + content.writer + ')</li>')
        });
    });

    sendBtn.click(function () {
        var message = messageInput.val();
        client.send('/publish/chat/message', {}, JSON.stringify({chatRoomId: roomId, message: message, writer: member}));
        messageInput.val('');
    });
});