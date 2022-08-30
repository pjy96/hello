package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.DeleteDTO;
import com.example.demo.dto.GetTimeAPIREQParams;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.example.demo.dto.RegexDTO;
import com.example.demo.service.Utils;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Log
public class controller{

    @Autowired
    Utils utils;

    // Hello World example
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format("hello %s!!", name); 
    }

    // index.html 호출
    // @RequestMapping(value = "/")
    // public String index(HttpSession session) {
    //     log.info("[Index HTML Call] " + session.getId());
    //     return "index.html"; 
    // }

    // index.html 호출
    @RequestMapping(value = "/")
    public String index(HttpSession session, HttpServletRequest request) {

        if(object == null){}
        
        HttpSession session = request.getSession(); // 세션 있으면 있는 세션 반환, 없으면 신규 세션 생성
        
        log.info("[Index HTML Call] " + session.getId());
        return "index.html"; 
    }


    // worldtimeapi
    @GetMapping("/api/getTime")
    @ResponseBody
    public GetTimeAPIRESParams getTime(GetTimeAPIREQParams params){
        String getTimeZone = params.getTimezone();
        if(getTimeZone == null){ // default Asia/Seoul
            getTimeZone="Asia/Seoul";
        }
        return utils.getTimeWithZone(getTimeZone); // service 호출
    }

    // 정규식 validation 하는 API
    @RequestMapping(value = "/api/reg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RegexDTO TestIPEmail(@RequestParam String params){
        return utils.getVaildation(params);
    }

    // 삭제 API
    @RequestMapping(value = "/api/del", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public DeleteDTO DeleteArray(@RequestParam int idx) {
        return utils.deleteArray(idx);
    }

}