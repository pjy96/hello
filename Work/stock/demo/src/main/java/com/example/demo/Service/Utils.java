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
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

    // open api에서 회사명으로 입력받은 데이터 추출
    public StockDTO openAPIStock(String params, List<String> searchArray){

        StockDTO stockDTO = new StockDTO();
        stockDTO.setCp_name(params);

        // timezone
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "";
        String time = df.format(date) + " | ";
        time = "<p class=csstime>" + time + "</p>"; // 입력받은 timezone css

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
                String strCode = (String)stock.get("srtnCd");
                String cpCode = strCode.substring(1,7); // 문자열 추출
                str += time + params + " " + cpCode + Common.formatDel;
                stockDTO.setCp_code(cpCode); // DTO에 cp_code 저장
                // searchArray.add(str); // 배열에 저장
                stockDTO.setResult(str); // DTO에 출력할 result 저장
            }

            // 객체 해제
            br.close();
            con.disconnect();

        
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
