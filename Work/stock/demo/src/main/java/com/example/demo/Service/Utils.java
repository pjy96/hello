package com.example.demo.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.StockData;
import com.example.demo.DTO.DeleteDTO;
import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.repository.stockrepository;

@Service
public class Utils {

    @Autowired
    stockrepository repo; // repository

    // 삭제버튼
    final String formatDel = "<button class=del onclick=\"delResult(%d)\"><i class=\"fa-solid fa-delete-left\"></i></button><br>";

    public GetTimeDTO getTime(String timeZone){
        GetTimeDTO gettimeparams = new GetTimeDTO();
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tz = TimeZone.getTimeZone(timeZone);
        df.setTimeZone(tz);

        gettimeparams.setTimezone(timeZone);
        gettimeparams.setDatetime(df.format(date));
        return gettimeparams;
    }
    
    // 입력데이터 출력 & select data & save data
    public StockDTO getStockPrices(String params, List<String> searchArray, HttpServletRequest req){

        StockDTO stkDto = new StockDTO();
        StockData stockData = new StockData(); // DB에 저장
        stkDto.setCp_code(params); // 입력값 회사명에 보내기(?)

        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css

        // select data & save data
        searchArray.add(0, time + params); // 출력할 배열에 저장
        stockData.setName(params); // DB에 저장

        // 5개 이상일 때 가장 먼저 입력된 데이터 삭제
        if(searchArray.size()>5){
            searchArray.remove(5);
        }

        // String으로 배열 출력
        for(int i=0; i<searchArray.size(); i++){
            str += searchArray.get(i) + String.format(formatDel, i);
        }

        stkDto.setResult(str); // DTO에 결과 저장
        
        return stkDto;
    }
    
    // delete
    public DeleteDTO deleteResult(int idx, List<String> searchArray) {
        
        DeleteDTO delDto = new DeleteDTO();
        String str = "";
        searchArray.remove(idx); // 입력받은 idx에 해당하는 배열 삭제
        for(int i=0; i<searchArray.size(); i++){ // 삭제 후 배열 재출력
            str += searchArray.get(i) + String.format(formatDel, i);
        }
        // delDto.setResult(str);

        return delDto;
    }

    // 회사명으로 입력한 데이터 DB에 있는지 검색
    public StockDTO searchName(){
        StockDTO stkDto = new StockDTO();
        List<StockData> resultName = repo.findByCode(123456); // List에 repo에서 code가 123456인 Data 가져오기

        return stkDto;
    }

    // 회사번호으로 입력한 데이터 DB에 있는지 검색
    public StockDTO searchCode(){
        StockDTO stkDto = new StockDTO();
        List<StockData> resultCode = repo.findByCode(123456); // List에 repo에서 code가 123456인 Data 가져오기

        return stkDto;
    }

}

