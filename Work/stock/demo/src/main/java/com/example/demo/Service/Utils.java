package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.StockData;
import com.example.demo.DTO.DeleteDTO;
import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.common.Common;
import com.example.demo.repository.stockrepository;

@Service
public class Utils{

    @Autowired
    stockrepository repo; // repository
   
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
    public StockDTO getStockPrices(String param) {

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

        str += time + param + Common.formatDel;

        stockDTO.setResult(str);
        repo.save(stockData); // stockData에 set한 data -> repo에 저장

        return stockDTO;
    }

    public DeleteDTO delResult(int idx){

        DeleteDTO delDto = new DeleteDTO();

        return delDto;
    }

    /*
     * 1.회사명 (C->S)
     * 2.회사명으로 코드 찾기 -> DB
     * 2.1 있을때
     *  2.1.1 KRX 요청하기 외부 api. 현재가 주가 S->KRX
          
     * 2.2 없을때
     *  2.2 KRX 회사명 요청해서 회사코드 받아오기 -> S->KRX
     *  2.3 받아온 회사코드 내부 db에 저장하기
     *  2.1.1 하기
     * 
     * 3. 받아온 결과 정리
     * 4. 정리된 값 전달 (S->C) 
     * 
     * 
     *  회사정보를 가져오는 API
     * 
     * 
     */





    // open api
    public StockDTO openAPIStock(String params){
        StockDTO stockDTO = new StockDTO();
        BufferedReader br = null;
        try{
            String apiBaseUrl = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo"; // KRX에 상장된 종목에 대한 정보 조회 API
            String reqParams = "?serviceKey=" + Common.KrxKey;
            reqParams += "&numOfRows=1"; // 날짜별로 많은 데이터가 나오는데 가장 최신 데이터로 설정
            reqParams += "&itmsNm=" + params; // 종목명으로 입력

            apiBaseUrl = apiBaseUrl+ URLEncoder.encode(reqParams,"UTF-8");

            URL url = new URL(apiBaseUrl);

            // 요청하고자 하는 url과 통신하기위한 connection 생성
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 통신을 위한 메소드 set
            urlConnection.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF_8"));
            
            String result = "";
            String line;
            while((line = br.readLine()) != null){
                result = result + line + "\n";
            }
            stockDTO.setCp_name(params);
            stockDTO.setCp_code(result);
            // stockDTO.setResult(result);

        }catch(Exception e){
            e.printStackTrace();
        }

        return stockDTO;
    }

    // 이름 검색했을 때 종목코드 나오게
    public StockDTO searchKRX(String params){
        StockDTO stockDTO = new StockDTO();

        return stockDTO;
    }


}
