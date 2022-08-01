package com.example.demo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.stereotype.Service;
import com.example.demo.dto.GetTimeAPIRESParams;

@Service
public class Utils {

    public GetTimeAPIRESParams getTimeWithZone(String timeZone){

        GetTimeAPIRESParams resParams = new GetTimeAPIRESParams();
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        tz = TimeZone.getTimeZone(timeZone);
        df.setTimeZone(tz);

        resParams.setTimezone(timeZone);
        resParams.setDatetime(df.format(date));
        return resParams;
    }
    
}
