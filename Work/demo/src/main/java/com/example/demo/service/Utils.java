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

    static ArrayList<String> resultArr = new ArrayList<String>(5);

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
        resRegex.setInput(params); // input : 

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
            resultArr.add(params + " is IP" + "<br>");
        }else if(bEmail){
            resultArr.add(params + " is Email" + "<br>");
        }else{
            resultArr.add(params + " is Invalid Format" + "<br>");
        }

        // 5개 이상일때 처음 항목 삭제
        if(resultArr.size()>5){
            resultArr.remove(0);
        }

        String[] strArr = resultArr.toArray(new String[0]); // arrayList to array
        for(String n:strArr){
            resRegex.setResult(n);
        }
        //배열에 저장하고 출력
       
        // for(int i=0; i<resultArr.size(); i++){

        // }

        // for(String n: resultArr){ //for-each문 같은 역할
        //     resRegex.setResult(n);
        // }
        
        for(int i=0; i<resultArr.size(); i++){
            str = resultArr + "<button class=del onclick=\"deleteArray("+i+")\"><i class=\"fa-solid fa-delete-left\"></i></button>";
            resRegex.setResult(str);
        }
        return resRegex;

    }
}
