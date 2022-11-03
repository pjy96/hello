package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.DTO.DeleteDTO;
import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.Service.Utils;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class controller {

    @Autowired
    Utils utils;

    @RequestMapping(value = "/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/getTime")
    @ResponseBody
    public GetTimeDTO getTime(GetTimeDTO gt){
        String getTimeZone = gt.getTimezone();
        if(getTimeZone == null){
            getTimeZone="Asia/Seoul";
        }
        return utils.getTime(getTimeZone);
    }

    // 입력받은 데이터 저장 & 가격과 등락률 검색 & 화면에 5개씩 출력
    @RequestMapping(value="/searchStock", method = {RequestMethod.GET, RequestMethod.POST})
    public StockDTO getStockPrice(@RequestParam String param, HttpServletRequest req) {
        Object obj = req.getSession().getAttribute("searchArray"); // searchArray 배열
        List<String> arrList = (List<String>)obj; // List<object> to List<String> 형변환
        return utils.getStockPrices(param, arrList, req);
    }
    
    // 삭제
    @RequestMapping(value="delResult", method=RequestMethod.GET)
    public DeleteDTO delResult(@RequestParam int idx, HttpSession session) {
        Object obj = session.getAttribute("searchArray"); // searchArray 배열
        List<String> arrList = (List<String>)obj; // List<object> to List<String> 형변환
        return utils.deleteResult(idx, arrList);
    }
    

}
