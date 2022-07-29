package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

@Service
public class Utils {

    public void getTime() { // datetime 불러오는 function

        // 입력 받은 timezone 있으면 그 도시 return datetime
        //                    없으면 default seoul time

        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-mm-dd a hh:mm:ss"); // 시간 형식 지정
        String nowString = now.format(dateformat);

        System.out.println("Now in Seoul id " + now);


        
    }
    
}
