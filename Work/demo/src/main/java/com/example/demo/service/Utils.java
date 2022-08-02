package com.example.demo.service;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.Validate;

import com.example.demo.dto.GetIPEmailREQParams;
import com.example.demo.dto.GetIPEmailRESPArams;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.fasterxml.jackson.annotation.JacksonInject.Value;

import lombok.var;

@Service
public class Utils {

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

    public GetIPEmailRESPArams TestValidation(String input) {

        GetIPEmailRESPArams resinput = new GetIPEmailRESPArams();

        /* IP 정규식 */
        String regIp = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"; 
        /* Email 정규식 */
        String regEmail = "^\\w+@\\w+\\.[0-9a-zA-Z]+$";

        var resultIP = Pattern.matches(regIp, input); // regIP 와 input 비교
        var resultEmail = Pattern.matches(regEmail, input); // regEmail 과 input 비교

        //@ModelAttribute : View 에서 넘어온 parameter를 DTO로 받을 수 있음

        return resinput;
    }

}
