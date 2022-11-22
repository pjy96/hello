package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.Service.Utils;

@Controller
public class controller {

    @Autowired
    Utils utils;

    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }

    // getTimeZone
    @GetMapping("/getTime")
    @ResponseBody
    public GetTimeDTO getTime(GetTimeDTO gt) {
        String getTimeZone = gt.getTimezone();
        if (getTimeZone == null) {
            getTimeZone = "Asia/Seoul";
        }
        return utils.getTime(getTimeZone);
    }

    // 회사 이름 검색
    @GetMapping("/searchName")
    @ResponseBody
    public StockDTO getStockName(@RequestParam String param, HttpSession ses) {
        Object obj = ses.getAttribute("searchArray"); // 배열 생성
        List<String> arrList = (List<String>)obj; // List<Object> to List<String> 형변환
        return utils.openAPIStockName(param, arrList);
    }

    // 회사 단축코드 검색
    @GetMapping("/searchCode")
    @ResponseBody
    public StockDTO getStockCode(@RequestParam String param, HttpSession ses) {
        Object obj = ses.getAttribute("searchArray"); // 배열 생성
        List<String> arrList = (List<String>)obj; // List<Object> to List<String> 형변환
        return utils.openAPIStockCode(param, arrList);
    }
}
