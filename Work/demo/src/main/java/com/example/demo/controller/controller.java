package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.GetIPEmailRESPArams;
import com.example.demo.dto.GetTimeAPIREQParams;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.example.demo.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class controller{

    @Autowired
    Utils utils; //service

    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format("hello %s!!", name); // example
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index.html"; // index.html 호출
    }

    @GetMapping(value="/api/getTime")
    @ResponseBody
    public GetTimeAPIRESParams getTime(GetTimeAPIREQParams params){ // time api
        String getTimeZone = params.getTimezone();
        if(getTimeZone == null){ // default Asia/Seoul
            getTimeZone="Asia/Seoul";
        }
        return utils.getTimeWithZone(getTimeZone); // service 호출
    }

    // 정규식 validation 하는 API만들기
    /*
     * enter 눌렀을 때 api 실행
     * 배열 없이 일단 출력
     * service에서 return
     * 
     * param 던지는 방법 연습하는 것 
     * 
     */
    @RequestMapping(value = "/api/testipemail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GetIPEmailRESPArams TestIPEmail(String input){ // IP, Email Vaildation api

    
        return utils.TestValidation(input);

    }
    
}