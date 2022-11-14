package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.Service.Utils;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestMethod;


@Log
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

    @RequestMapping(value="/search", method = {RequestMethod.GET, RequestMethod.POST})
    public StockDTO getStockPrice(@RequestParam String param) {
        log.info("PARAM : " + param);
        return utils.getStockPrices(param);
    }
    


}
