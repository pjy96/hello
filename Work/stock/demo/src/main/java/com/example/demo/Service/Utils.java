package com.example.demo.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.repository.stockrepository;

@Service
public class Utils {

    @Autowired
    stockrepository repo; // repository

    // 삭제버튼
    final String formatDel = "<button class=del onclick=\"delResult(%d)\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";

    // 가격 price = "<p class=cssprice>" + price + "</p>";

    // timezone
    public GetTimeDTO getTime(String timeZone) {
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

    public StockDTO getStockPrices(String Param){

        StockDTO stockDTO = new StockDTO();

        stockDTO.setCp_name(Param);
        stockDTO.setResult("Success! " + Param + " test중입니다.");

        return stockDTO;
    }


}
