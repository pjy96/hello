package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.json.JSONParser;
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

    // 가격 price = "<p class=cssprice>" + price + "</p>";

    // timezone
    public GetTimeDTO getTime(String timeZone) {
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

    // 회사명 입력데이터 출력 & select data & save data
    public StockDTO getStockPrices(String param, List<String> searchArray, HttpServletRequest req) {

        StockDTO stockDTO = new StockDTO(); // DTO
        StockData stockData = new StockData(); // repository

        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css

        // 만약에 입력받은 param이 회사명인지 회사코드인지 확인 후 DTO에 보내기
        Pattern p = Pattern.compile("^\\d{6}$"); // Regex
        Matcher m = p.matcher(param);
        if (m.matches() == false) { // 입력받은 param이 회사명일때
            stockDTO.setCp_name(param);
            stockData.setName(stockDTO.getCp_name()); // repository에 저장
        } else if (m.matches()) { // 입력받은 param이 회사코드일때
            stockDTO.setCp_code(param);
            stockData.setCode(stockDTO.getCp_code()); // repository에 저장
        }

        searchArray.add(0, time + param);


        stockDTO.setResult(str);
        repo.save(stockData); // stockData에 set한 data -> repo에 저장

        return stockDTO;
    }

    public DeleteDTO deleteResult(int idx, List<String> searchArray){
        DeleteDTO delDto = new DeleteDTO();
        String str = "";
        searchArray.remove(idx); // 입력받은 idx에 해당하는 배열 삭제
        str += idx + formatDel;
        delDto.setResult(str);
        return delDto;
    }

    // open api 불러오는 예시 소스
    public StockDTO openAPIStock(){
        StockDTO stockDTO = new StockDTO();
        String key = "rxfav9g20svzi6USPiNW6bNLrNi7Yk0nScVPsY8dLjMmud7sg%2FAx%2BNGk4V6WGIphulP4FxqxpKx1FU81bAml1A%3D%3D"; // 인증키
        String result = ""; // 파싱할 데이터를 저장할 변수
        String apiUrl = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService"; // open api url
        try{
            URL url = new URL(apiUrl + key);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();
            

            

        }catch(Exception e){
            e.printStackTrace();
        }



        return stockDTO;
    }
}
