package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

    // 회사 이름 검색시 회사 코드를 가져오는 API
    @GetMapping("/search")
    @ResponseBody
    public StockDTO getStockPrice(@RequestParam String param, HttpServletRequest req) {
        Object obj = req.getSession().getAttribute("searchArray"); // 배열 생성
        List<String> arrList = (List<String>)obj; // List<Object> to List<String> 형변환
        return utils.openAPIStock(param, arrList);
    }



}
