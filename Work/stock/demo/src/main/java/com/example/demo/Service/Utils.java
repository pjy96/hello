package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.StockData;
import com.example.demo.DTO.GetTimeDTO;
import com.example.demo.DTO.StockDTO;
import com.example.demo.common.Common;
import com.example.demo.repository.stockrepository;

@Service
public class Utils{

    @Autowired
    stockrepository repo; // repository
   
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
     */

    // 회사명 입력 받아서 open api에서 데이터 추출
    public StockDTO openAPIStockName(String params, List<String> searchArray){

        StockDTO stockDTO = new StockDTO(); // DTO
        StockData stockData = new StockData(); // DAO
        stockDTO.setCp_name(params);

        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css

        List<StockData> searchCPName = repo.findByName(params); // repo에서 name = params인 Data -> List에 가져오기
        if(searchCPName.size() > 0){ // 1. 저장된 데이터가 있을 때 (DB에서 가져와서 DTO에 전달)
            for(int i=0; i<searchCPName.size(); i++){
                String dCode = searchCPName.get(i).getCode();
                String dPrice = searchCPName.get(i).getPrice();
                str += "DB출력 " + time + params + "(" + dCode  + ")  " + dPrice + Common.formatDel;
                stockDTO.setResult(str);
            }
        }else{ // 2. open api 호출하기
            try{
                // URL을 만들기 위한 StringBuilder
                StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"); // URL
                
                // 오픈 API의 요청 규격에 맞게 parameter 생성
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + Common.KrxKey); // 서비스 키
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 수
                urlBuilder.append("&" + URLEncoder.encode("itmsNm", "UTF-8") + "=" + URLEncoder.encode(params, "UTF-8")); // 회사명
                urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // 결과 타입 JSON

                // URL 객체 생성 (String으로 변환)
                URL url = new URL(urlBuilder.toString());
                // 요청하고자 하는 url과 통신하기위한 connection 생성
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // 통신을 위한 메소드 set
                con.setRequestMethod("GET");
                // 통신을 위한 Content-type SET (JSON)
                con.setRequestProperty("Content-type", "application/json");
                
                // 전달받은 데이터를 BufferedReader 객체로 저장
                BufferedReader br;
                if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300){
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
                }

                // 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // 문자열로 바꾸기
                String result = sb.toString();

                // 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성 & 문자열을 JSON 형태로 JSONObject 객체에 저장
                JSONParser jParser = new JSONParser();
                JSONObject jObj = (JSONObject)jParser.parse(result);

                // response키를 가지고 데이터 파싱
                JSONObject parse_response = (JSONObject) jObj.get("response");
                // response로부터 body 찾아오기
                JSONObject parse_body = (JSONObject) parse_response.get("body");
                // body로부터 items 찾아오기
                JSONObject parse_items = (JSONObject) parse_body.get("items");
                // items로부터 list 안에 item 받아오기
                JSONArray parse_item = (JSONArray) parse_items.get("item");

                JSONObject stock;
                // 회사코드 이름 추출해서 stockDTO에 저장
                for(int i=0; i<parse_item.size(); i++){
                    stock = (JSONObject) parse_item.get(i);
                    String cpCode = (String)stock.get("srtnCd"); // 회사 단축 코드
                    String rate = (String)stock.get("fltRt"); // 등락률
                    String price = (String)stock.get("clpr"); // 가격(종가)

                    if(rate.contains("-")){ // 등락률이 음수이면 파란색으로 표시
                        price = "<p class=blueprice>" + price + "<i class=\"fa-solid fa-caret-down\"></i></p>";
                    }else if(rate.equals("0")) { // 등락률이 0이면 검정색으로 표시
                        price = "<p class=blackprice>" + price + "</p>";
                    }else{ // 등락률이 양수이면 빨간색으로 표시
                        price = "<p class=redprice>" + price + "<i class=\"fa-solid fa-caret-up\"></i></p>";
                    }
                    str += time + params + "(" + cpCode  + ")  " + price + Common.formatDel;

                    // DTO에 전달
                    stockDTO.setCp_code(cpCode);
                    stockDTO.setSt_rate(rate);
                    stockDTO.setSt_price(price);
                    stockDTO.setResult(str);

                    if(cpCode != null){ // null값은 저장하지 않기
                        // DB에 저장
                        stockData.setName(stockDTO.getCp_name());
                        stockData.setCode(stockDTO.getCp_code());
                        stockData.setRate(stockDTO.getSt_rate());
                        stockData.setPrice(stockDTO.getSt_price());
                    }  
                    repo.save(stockData);
                }
                // 객체 해제
                br.close();
                con.disconnect();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return stockDTO;
    }

    // 회사 코드 입력 받아서 open api에서 데이터 추출
    public StockDTO openAPIStockCode(String params, List<String> searchArray){

        StockDTO stockDTO = new StockDTO(); // DTO
        StockData stockData = new StockData(); // DAO
        stockDTO.setCp_code(params);

        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css

        List<StockData> searchCPCode = repo.findByCode(params); // repo에서 code = params인 Data -> List에 가져오기
        if(searchCPCode.size() > 0){ // 1. 저장된 데이터가 있을 때
            for(int i=0; i<searchCPCode.size(); i++){
                String dName = searchCPCode.get(i).getName();
                String dPrice = searchCPCode.get(i).getPrice();
                str += "DB출력 " + time + dName + "(" + params + ")  " + dPrice + Common.formatDel;
                stockDTO.setResult(str);
            }
        }else{ // 2. open api 호출하기
            try{
                // URL을 만들기 위한 StringBuilder
                StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"); // URL
                
                // 오픈 API의 요청 규격에 맞게 parameter 생성
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + Common.KrxKey); // 서비스 키
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 수
                urlBuilder.append("&" + URLEncoder.encode("likeSrtnCd", "UTF-8") + "=" + URLEncoder.encode(params, "UTF-8")); // 회사코드
                urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // 결과 타입 JSON

                // URL 객체 생성 (String으로 변환)
                URL url = new URL(urlBuilder.toString());
                // 요청하고자 하는 url과 통신하기위한 connection 생성
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // 통신을 위한 메소드 set
                con.setRequestMethod("GET");
                // 통신을 위한 Content-type SET (JSON)
                con.setRequestProperty("Content-type", "application/json");
                
                // 전달받은 데이터를 BufferedReader 객체로 저장
                BufferedReader br;
                if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300){
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader((con.getErrorStream())));
                }

                // 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                // 문자열로 바꾸기
                String result = sb.toString();

                // 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성 & 문자열을 JSON 형태로 JSONObject 객체에 저장
                JSONParser jParser = new JSONParser();
                JSONObject jObj = (JSONObject)jParser.parse(result);

                // response키를 가지고 데이터 파싱
                JSONObject parse_response = (JSONObject) jObj.get("response");
                // response로부터 body 찾아오기
                JSONObject parse_body = (JSONObject) parse_response.get("body");
                // body로부터 items 찾아오기
                JSONObject parse_items = (JSONObject) parse_body.get("items");
                // items로부터 list 안에 item 받아오기
                JSONArray parse_item = (JSONArray) parse_items.get("item");

                JSONObject stock;
                // 회사코드 이름 추출해서 stockDTO에 저장
                for(int i=0; i<parse_item.size(); i++){
                    stock = (JSONObject) parse_item.get(i);
                    String cpName = (String)stock.get("itmsNm"); // 회사 단축 코드
                    String rate = (String)stock.get("fltRt"); // 등락률
                    String price = (String)stock.get("clpr"); // 가격(종가)

                    if(rate.contains("-")){ // 등락률이 음수이면 파란색으로 표시
                        price = "<p class=blueprice>" + price + "<i class=\"fa-solid fa-caret-down\"></i></p>";
                    }else if(rate.equals("0")) { // 등락률이 0이면 검정색으로 표시
                        price = "<p class=blackprice>" + price + "</p>";
                    }else{ // 등락률이 양수이면 빨간색으로 표시
                        price = "<p class=redprice>" + price + "<i class=\"fa-solid fa-caret-up\"></i></p>";
                    }
                    str += time + cpName + "(" + params  + ")  " + price + Common.formatDel;

                    // DTO에 전달
                    stockDTO.setCp_name(cpName);
                    stockDTO.setSt_rate(rate);
                    stockDTO.setSt_price(price);
                    stockDTO.setResult(str);

                    if(cpName != null){ // null값은 저장하지 않기
                        // DB에 저장
                        stockData.setName(stockDTO.getCp_name());
                        stockData.setCode(stockDTO.getCp_code());
                        stockData.setRate(stockDTO.getSt_rate());
                        stockData.setPrice(stockDTO.getSt_price());
                    }
                    repo.save(stockData);
                }
                
                // 객체 해제
                br.close();
                con.disconnect();

            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return stockDTO;
    }

}
