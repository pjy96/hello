package com.example.demo.model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.controller.TestData;
import com.example.demo.dto.DeleteDTO;
import com.example.demo.dto.GetTimeAPIREQParams;
import com.example.demo.dto.GetTimeAPIRESParams;
import com.example.demo.dto.RegexDTO;
import com.example.demo.dto.SaveDTO;
import com.example.demo.repository.testRepository;
import com.example.demo.service.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = "/")
    public String index(HttpSession session, HttpServletRequest request) {
        log.info("[Index HTML Call] | " + session.getId()); // getId
        if (session.getAttribute("regArray") == null){ // regArray에 바인딩 된 객체를 돌려주고, 없다면 null
            ArrayList<String> regArray = new ArrayList<String>(); // browser마다 다른 배열에 저장 
            session.setAttribute("regArray", regArray); // 객체 생성
        }
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
    // @RequestMapping(value = "/api/reg", method = RequestMethod.GET)
    // @ResponseBody
    // public RegexDTO TestIPEmail(@RequestParam String params, HttpSession session){ // 
    //     Object obj = session.getAttribute("regArray"); // regArray
    //     List<String> arrList = (List<String>)obj; // List<object> to List<String> 형변환
    //     return utils.getVaildation(params, arrList);
    // }

    // 삭제 API
    @RequestMapping(value = "/api/del", method = RequestMethod.GET)
    @ResponseBody
    public DeleteDTO DeleteArray(@RequestParam int idx, HttpServletRequest request) {
        Object obj = request.getSession().getAttribute("regArray"); // regArray
        List<String> arrList = (List<String>)obj; // List<object> to List<String> 형변환
        return utils.deleteArray(idx, arrList);
    }

    @Autowired
    testRepository repo; 

    @RequestMapping(value="/api/db", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void SaveData(TestData testData, HttpServletRequest request) {
        // ip 불러오기
        String ip = request.getHeader("X-Forwarded-For"); 
        if (ip == null) {
            ip = request.getRemoteAddr(); // 클라이언트 접속 IP 불러오기
        }
        // String result;
        testData.setHostip(ip);
        // testData.setResult(result);
        repo.save(testData);
    }

    @RequestMapping(value = "/api/reg", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public SaveDTO saveData(@RequestParam String params, TestData testData, HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For"); 
        if (ip == null) {
            ip = request.getRemoteAddr(); // 클라이언트 접속 IP 불러오기
        }
        testData.setHostip(ip);
        return utils.saveData(params);
    }

}