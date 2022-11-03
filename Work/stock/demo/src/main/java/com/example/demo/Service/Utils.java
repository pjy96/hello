package com.example.demo.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.DeleteDTO;
import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;

@Service
public class Utils {

    public GetTimeDTO getTime(String timeZone){
        GetTimeDTO gettimeparams = new GetTimeDTO();
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tz = TimeZone.getTimeZone(timeZone);
        df.setTimeZone(tz);

        gettimeparams.setTimezone(timeZone);
        gettimeparams.setDatetime(df.format(date));
        return gettimeparams;
    }
    
    public StockDTO getStockPrices(String params, List<String> searchArray, HttpServletRequest req){

        StockDTO stkDto = new StockDTO();

        return stkDto;
    }
        
    public DeleteDTO deleteResult(int idx, List<String> searchArray) {
        
        DeleteDTO delDto = new DeleteDTO();

        return delDto;
    }
}

