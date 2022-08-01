package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("/")
    @ResponseBody
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){

        return String.format("hello %s!!", name);
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value="/api/getTime", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GetTimeAPIRESParams getTime(GetTimeAPIREQParams params) {
       
        String getTimeZone = params.getTimezone();
        if(getTimeZone == null){ // default Asia/Seoul
            getTimeZone="Asia/Seoul";
        }
        return utils.getTimeWithZone(getTimeZone); // service 호출
    }
    
}