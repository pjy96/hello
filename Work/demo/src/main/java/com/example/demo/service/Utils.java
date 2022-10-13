package com.example.demo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.*;

import com.example.demo.dto.DeleteDTO;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.example.demo.dto.IpEmailCountRESDTO;
import com.example.demo.dto.RegexDTO;
import com.example.demo.model.TestData;
import com.example.demo.repository.testRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Service
@EnableWebSocket // websocket annotation
@EnableWebSocketMessageBroker
public class Utils {

    @Autowired
    testRepository repo; // DAO
    
    // 지우기 버튼에 대한 format 정의
    final String formatStr="<button class=del onclick=\"delResult(%d)\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";

    // world time api
    public GetTimeAPIRESParams getTimeWithZone(String timeZone){ // api/getTime
        GetTimeAPIRESParams resParams = new GetTimeAPIRESParams();
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // datetime 출력형식
        tz = TimeZone.getTimeZone(timeZone);
        df.setTimeZone(tz);

        // api/getTime에 저장할 정보
        resParams.setTimezone(timeZone); // timezone    
        resParams.setDatetime(df.format(date)); // datetime
        return resParams;
    }

    // Regex api & print array & save repo
    public RegexDTO getVaildation(String params, List<String> regArray, HttpServletRequest request) {

        RegexDTO resRegex = new RegexDTO();
        TestData testData = new TestData();
        resRegex.setInput(params); // regex 
        testData.setInput(resRegex.getInput());

        // host_ip
        String ip = request.getHeader("X-Forwarded-For"); 
        if (ip == null) {
            ip = request.getRemoteAddr(); // 클라이언트 접속 IP 불러오기
        }
        testData.setHostip(ip);

        // TimeZone 
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>"+time+"</p>"; // 입력받은 timezone css
        // IP Regex
        Pattern regIp = Pattern.compile("^((([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$");  
        Matcher mIp = regIp.matcher(params);
        boolean bIp = mIp.matches();
        // Email Regex
        Pattern regEmail = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+$"); 
        Matcher mEmail = regEmail.matcher(params);
        boolean bEmail = mEmail.matches();

        //for(String str1 : arr1){print str}
        //=> arr의 값을 차례대로 가져와서 str에 넣기

        // insert regex array
        if(bIp){
            regArray.add(0, time + params + " is IP");
            testData.setResult("IP");
        }else if(bEmail){
            regArray.add(0, time + params + " is Email");
            testData.setResult("EMAIL");
        }else{
            regArray.add(0, time + params + " is Invalid Format");
            testData.setResult("INVALID");
        }
        // 5개 이상일때 처음 항목 삭제
        if(regArray.size()>5){
            regArray.remove(5);
        }
        // string으로 배열 출력
        for(int i=0; i<regArray.size(); i++){
            str += regArray.get(i) + String.format(formatStr, i);
        }
        resRegex.setResult(str);
        repo.save(testData); // testData에 set한 data -> repo에 저장

        return resRegex;
    }

    //delete api
    public DeleteDTO deleteArray(int idx,  List<String> regArray) {

        DeleteDTO delResult = new DeleteDTO();
        String str = "";
        regArray.remove(idx); // 입력받은 idx에 해당하는 배열 삭제
        for(int i=0; i<regArray.size(); i++){ // 삭제 후 배열 재출력
            str += regArray.get(i) +String.format(formatStr, i);
        }
        delResult.setResult(str);
        return delResult;
    }

    // count ip, email
    public IpEmailCountRESDTO count(){
        IpEmailCountRESDTO ieDto = new IpEmailCountRESDTO();
        List<TestData> resultIp = repo.findByResult("IP"); // List에 repo에서 result = IP 인 Data 가져오기
        List<TestData> resultEmail = repo.findByResult("EMAIL"); // List에 repo에서 result = Email 인 Data 가져오기
        int countIP = resultIp.size(); // result = IP 인 Data 개수
        int countEMAIL = resultEmail.size(); // result = Email 인 Data 개수
        ieDto.setCountIP(countIP);
        ieDto.setCountEMAIL(countEMAIL);
        return ieDto;
    }

    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic", "/direct");
    }
}