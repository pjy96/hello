package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.dto.GetTimeAPIREQParams;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.example.demo.service.Utils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Log
public class controller{

    @Autowired
    Utils utils;

    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){

        return String.format("hello %s!!", name);
    }

    @RequestMapping(value="/", method={RequestMethod.GET, RequestMethod.POST})
    public String index(){
        return "index";
    }

    @RequestMapping(value="/api/getTime", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GetTimeAPIRESParams getTime(GetTimeAPIREQParams params) {
        log.info("Enter getTime :" + params.toString()); // 터미널에 파라미터가 잘 들어오는지 로그 찍기
        GetTimeAPIRESParams resParams = new GetTimeAPIRESParams();
        resParams.setTimezone(params.getTimezone());
        resParams.setDatetime("Asia/Seoul Time");
        return resParams;
    }
    
}