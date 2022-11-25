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

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.NStockData;
import com.example.demo.DTO.DeleteDTO;
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
     * 1.회사명 (C->S) 클라이언트 -> 서버
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
     */

    // 실시간 주식 받는 메소드
    public StockDTO naverAPIstockName(String params, List<String> searchArray){

        StockDTO stockDTO = new StockDTO(); // DTO
        NStockData nstockData = new NStockData(); // DAO
        stockDTO.setCp_name(params);
        String str = ""; // time + 회사명 + (단축코드) + 종가(하락세인지/상승세인지) + 삭제버튼
        String strResult = ""; // 결과창
        List<NStockData> searchCode = repo.findByName(params); // repo에서 name = params인 Data -> List에 가져오기
        
        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css
        
        // 1. 입력한 데이터가 DB에 있을 때 -> DB에서 회사코드 가져와서 / naverAPI에 넣어서 실시간 현재가 가져오기
        // 2. 입력한 데이터가 DB에 없을 때 -> KRX open api에서 회사코드 가져와서 / naverAPI에 넣어서 실시간 현재가 가져오기
        
        if(searchCode.size() > 0){ // 입력한 회사명이 DB에 있을 때
            for(int i=0; i<searchCode.size(); i++){
                String nCode = searchCode.get(i).getCode(); // DB에서 회사코드 가져오기
                try {
                    // URL을 만들기 위한 StringBuilder
                    StringBuilder uBuilder = new StringBuilder("https://polling.finance.naver.com/api/realtime/domestic/stock"); // 네이버 증권 실시간 주식 URL
                    // 오픈 API의 요청 규격에 맞게 parameter 생성
                    uBuilder.append("/" + nCode); // params = 회사코드 입력
        
                    // URL 객체 생성 (String으로 변환)
                    URL url = new URL(uBuilder.toString());
                    // 요청하고자 하는 url과 통신하기위한 connection 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 통신을 위한 메소드 set
                    conn.setRequestMethod("GET");
                    // 통신을 위한 Content-type SET (JSON)
                    conn.setRequestProperty("Content-Type", "application/json");
        
                    // 전달받은 데이터를 BufferedReader 객체로 저장
                    BufferedReader br;
                    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    }else{
                        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
        
                    // 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null){
                        sb.append(line);
                    }
                    String result = sb.toString(); // 문자열로 바꾸기
        
                    // 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성 & 문자열을 JSON 형태로 JSONArray에 저장
                    JSONParser jParser = new JSONParser();
                    JSONObject jObj = (JSONObject)jParser.parse(result);
        
                    // datas 데이터 파싱
                    JSONArray parse_datas = (JSONArray) jObj.get("datas");
                    JSONObject nStock;
        
                    // 실시간 가격과 등락률 가져오기
                    for(int k=0; k<parse_datas.size(); k++){
                        nStock = (JSONObject) parse_datas.get(k);
                        String nStockPrice = (String)nStock.get("closePrice"); // 현재가
                        String nStockRatio = (String)nStock.get("fluctuationsRatio"); // 등락률
        
                        if(nStockRatio.contains("-")){ // 등락률: 음수 -> 파란색
                            nStockPrice = "<p class=blueprice>" + nStockPrice + "<i class=\"fa-solid fa-caret-down\"></i></p>";
                        }else if(nStockRatio.equals("0")) { // 등락률: 0 -> 검정색
                            nStockPrice = "<p class=blackprice>" + nStockPrice + "</p>";
                        }else{ // 등락률: 양수 -> 빨간색
                            nStockPrice = "<p class=redprice>" + nStockPrice + "<i class=\"fa-solid fa-caret-up\"></i></p>";
                        }

                        str += time + "(DB)" + params + "(" + nCode + ")" + nStockPrice;
                        searchArray.add(0, str); // 배열에 저장
                        if(searchArray.size()>5){ // 5개까지 저장
                            searchArray.remove(5);
                        }
                        for(int n=0; n<searchArray.size(); n++){ // 출력
                            strResult += searchArray.get(n) + String.format(Common.formatDel, n);
                        }
                        // DTO에 전달
                        stockDTO.setResult(strResult);
                    }

                    // 객체 해제
                    br.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }else{ // 입력한 회사명이 DB에 없을 때

            // 입력한 회사명 -> KRX API 불러와서 회사단축코드 가져오기
            try{
                // URL을 만들기 위한 StringBuilder
                StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo"); // URL
                
                // 오픈 API의 요청 규격에 맞게 parameter 생성
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + Common.KrxKey); // 서비스 키
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 수
                urlBuilder.append("&" + URLEncoder.encode("itmsNm", "UTF-8") + "=" + URLEncoder.encode(params, "UTF-8")); // 회사명
                urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // 결과 타입 JSON

                // URL 객체 생성 (String으로 변환)
                URL url = new URL(urlBuilder.toString());
                // 요청하고자 하는 url과 통신하기위한 connection 생성
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
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
                // 회사코드 추출해서 stockDTO에 저장
                for(int i=0; i<parse_item.size(); i++){
                    stock = (JSONObject) parse_item.get(i);
                    String code = (String)stock.get("srtnCd"); // 회사 단축 코드
                    String cpCode = code.substring(1); // 앞 문자 자르기
                    stockDTO.setCp_code(cpCode); // stockDTO에 cpCode 저장
                }
                // 객체 해제
                br.close();
                con.disconnect();
            }catch(Exception e){
                e.printStackTrace();
            }

            String pCode = stockDTO.getCp_code(); // DTO에서 회사 단축코드 가져오기

            // NaverAPI에 회사코드를 입력해서 실시간으로 현재가격 가져오기
            try {
                StringBuilder uBuilder = new StringBuilder("https://polling.finance.naver.com/api/realtime/domestic/stock"); // 네이버 증권 실시간 주식 URL
                uBuilder.append("/" + pCode); // params = 회사코드 입력
    
                URL url = new URL(uBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
    
                BufferedReader br;
                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }else{
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
    
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }
                String result = sb.toString();
    
                JSONParser jParser = new JSONParser();
                JSONObject jObj = (JSONObject)jParser.parse(result);
    
                JSONArray parse_datas = (JSONArray) jObj.get("datas");
                JSONObject nStock;
    
                for(int i=0; i<parse_datas.size(); i++){
                    nStock = (JSONObject) parse_datas.get(i);
                    String nStockPrice = (String)nStock.get("closePrice"); // 현재가
                    String nStockRatio = (String)nStock.get("fluctuationsRatio"); // 등락률
    
                    if(nStockRatio.contains("-")){ // 등락률: 음수 -> 파란색
                        nStockPrice = "<p class=blueprice>" + nStockPrice + "<i class=\"fa-solid fa-caret-down\"></i></p>";
                    }else if(nStockRatio.equals("0")) { // 등락률: 0 -> 검정색
                        nStockPrice = "<p class=blackprice>" + nStockPrice + "</p>";
                    }else{ // 등락률: 양수 -> 빨간색
                        nStockPrice = "<p class=redprice>" + nStockPrice + "<i class=\"fa-solid fa-caret-up\"></i></p>";
                    }
                    str += time + params + "(" + pCode  + ")  " + nStockPrice; // 시간 + 회사명(종목코드) + 가격(등락률)
                    
                    searchArray.add(0, str); // 배열에 저장
                    if(searchArray.size()>5){ // 5개까지 저장
                        searchArray.remove(5);
                    }
                    for(int k=0; k<searchArray.size(); k++){ // 출력
                        strResult += searchArray.get(k) + String.format(Common.formatDel, k);
                    }
        
                    // DTO에 전달
                    stockDTO.setResult(strResult);

                    // DB에 저장
                    nstockData.setName(params);
                    nstockData.setCode(pCode);
                }
                // 객체 해제
                br.close();
                conn.disconnect();
                repo.save(nstockData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stockDTO;
    }

    // 삭제 버튼
    public DeleteDTO deleteArray(int idx, List<String> searchArray){
        DeleteDTO delDTO = new DeleteDTO();
        String str = ""; // 결과창
        searchArray.remove(idx); // 입력받은 idx에 해당하는 배열 삭제
        for(int i=0; i<searchArray.size(); i++){ // 삭제 후 배열 재출력
            str += searchArray.get(i) + String.format(Common.formatDel, i);
        }
        delDTO.setResult(str);
        return delDTO;
    }
}