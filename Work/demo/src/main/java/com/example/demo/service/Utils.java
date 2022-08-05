package com.example.demo.service;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.GetIPEmailRESPArams;
import com.example.demo.dto.GetTimeAPIRESParams;

@Service
public class Utils {

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

    // Regex api
    public GetIPEmailRESPArams getVaildation(String input) {

        GetIPEmailRESPArams resRegex = new GetIPEmailRESPArams();

        // IP Regex
        // Pattern p = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        // // Email Regex
        // //Pattern regEmail = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+$");
        // Matcher m = p.matcher(input);
        // boolean b = m.matches();



        resRegex.setInput(input);
        // resRegex.setResult(b);
        
        return resRegex;

    }
    // 삭제 API 만들기
    // @Test
    // public void removetest(){
    //     assertTrue(testService, numberCompare(2,1));
    // }

}
