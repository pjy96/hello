package com.example.demo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.*;

import com.example.demo.dto.GetIPEmailRESParams;
import com.example.demo.dto.GetTimeAPIRESParams;

import org.springframework.stereotype.Service;

@Service
public class Utils {

    static ArrayList<String> resultArr = new ArrayList<String>();

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

    // Regex api & print array
    public GetIPEmailRESParams getVaildation(String params) {
        
        GetIPEmailRESParams resRegex = new GetIPEmailRESParams();
        String str = "";
        resRegex.setInput(params);

        // IP Regex
        Pattern regIp = Pattern.compile("^((([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$");  
        Matcher mIp = regIp.matcher(params);
        boolean bIp = mIp.matches();
        // Email Regex
        Pattern regEmail = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+$"); 
        Matcher mEmail = regEmail.matcher(params);
        boolean bEmail = mEmail.matches();

        // insert array
        if(bIp){
            resultArr.add(0, params + " is IP");
        }else if(bEmail){
            resultArr.add(0, params + " is Email");
        }else{
            resultArr.add(0, params + " is Invalid Format");
        }
        // 5개 이상일때 처음 항목 삭제
        if(resultArr.size()>5){
            resultArr.remove(5);
        }
        for(int i=0; i<resultArr.size(); i++){
            // 앞에 현재 시간 불러와서 넣기
            str += resultArr.get(i) + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";
        }
        resRegex.setResult(str);
        return resRegex;

    }



}

// 삭제버튼 구현