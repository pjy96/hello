package com.example.demo.service;

import java.security.DrbgParameters.Reseed;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.*;

import javax.naming.spi.DirStateFactory.Result;

import com.example.demo.dto.GetIPEmailRESParams;
import com.example.demo.dto.GetTimeAPIRESParams;

import lombok.var;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

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

    public static String[] append(String[] arr, String element){
        List<String> resArray = new ArrayList<>(Arrays.asList(arr));
        resArray.add(element);
        return resArray.toArray(new String[5]);
    }
    public static String[] removeElement(String[] arr, int index){
        String[] removeResult = new String[arr.length - 1];
        System.arraycopy(arr, 0, removeResult, 0, index);
        if(arr.length != index){
            System.arraycopy(arr, index+1, removeResult, index, arr.length - index - 1);
        }
        return removeResult;
    }

    // Regex api & print array
    public GetIPEmailRESParams getVaildation(String params) {
        
        GetIPEmailRESParams resRegex = new GetIPEmailRESParams();
        
        //IP Regex
        Pattern regIp = Pattern.compile("^((([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(([0-9]{1,2})|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$");  
        Matcher mIp = regIp.matcher(params);
        boolean bIp = mIp.matches();
        //Email Regex
        Pattern regEmail = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+$"); 
        Matcher mEmail = regEmail.matcher(params);
        boolean bEmail = mEmail.matches();

        String[] arr = {};
        //Regular expresson test
        if(bIp){
            // resRegex.setInput(params);
            // resRegex.setResult(params + " is IP");
            arr = append(arr, params+" is IP");
        }else if(bEmail){
            // resRegex.setInput(params);
            // resRegex.setResult(params + " is Email");
            arr = append(arr, params+" is Email");
        }else{
            // resRegex.setInput(params);
            // resRegex.setResult(params + " is Invalid Format");
            arr = append(arr, params+" is Invalid Format");
        }
        int index = 5;
        if(arr.length > 5){
            arr = removeElement(arr, index);
        }
        
        resRegex.setInput(params);
        resRegex.setResult(Arrays.toString(arr));
        
        return resRegex;
    }


}
