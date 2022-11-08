// xmark function
function fClose(){
    var inputbar = document.getElementById('input');
    inputbar.value = ""; //검색바 내용 삭제
}

// 서버시간 호출
setInterval(function request_Time(){
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/getTime",
        data:{
            timezone: "Asia/Seoul",
            datetime: "http://localhost:8080/getTime?timezone=Asia/Seoul"
        },
        success: function(res){
            const time = document.getElementById("time");
            time.innerHTML = res.datetime;
        },
        error: function(){
            alert("TimeZone Fail");
        }
    })
},1000) // 1초마다 한번씩

// select 선택할때마다 다른 placeholder
function ddlselect(){  
    var d = $("select").val();
    if(d == 0){
        document.getElementById("d1").style.display = "inline-block";
        document.getElementById("d2").style.display = "none";
    }
    else{
        document.getElementById("d1").style.display = "none";
        document.getElementById("d2").style.display = "inline-block";
    }
}

