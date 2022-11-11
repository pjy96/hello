package com.example.demo.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.*;

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
    public StockDTO getStockPrices(String params, List<String> searchArray, HttpServletRequest req) {

        StockDTO stkDto = new StockDTO(); // DTO
        StockData stockData = new StockData(); // DB

        // 만약에 입력받은 param이 회사명인지 회사코드인지 확인 후 DTO에 저장
        Pattern p = Pattern.compile("^\\d{6}$"); // Regex : 6자리의 숫자
        Matcher m = p.matcher(params);
        if (m.matches() == false) { // 입력받은 params가 회사명일때
            stkDto.setCp_name(params);
            stockData.setName(stkDto.getCp_name()); // repository에 저장
        } else if (m.matches()) { // 입력받은 params가 회사코드일때
            stkDto.setCp_code(params);
            stockData.setCode(stkDto.getCp_code());
        }

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
        if (searchArray.size() > 5) {
            searchArray.remove(5);
        }

        // String으로 배열 출력
        for (int i = 0; i < searchArray.size(); i++) {
            str += searchArray.get(i) + String.format(formatDel, i);
        }

        return stkDto;
    }

    // delete
    public DeleteDTO deleteResult(int idx, List<String> searchArray) {

        DeleteDTO delDto = new DeleteDTO();
        String str = "";
        searchArray.remove(idx); // 입력받은 idx에 해당하는 배열 삭제
        for (int i = 0; i < searchArray.size(); i++) { // 삭제 후 배열 재출력
            str += searchArray.get(i) + String.format(formatDel, i);
        }
        // delDto.setResult(str);

        return delDto;
    }

    // 회사명으로 입력한 데이터 DB에 있는지 검색
    public StockDTO searchName() {
        StockDTO stkDto = new StockDTO(); // DTO
        // StockData stkData = new StockData(); // repository
        List<StockData> resultName = repo.findByCode(stkDto.getCp_code()); // repo에서 dto에 저장된 cp_code Data 가져와서 List에 저장

        String str = "";

        for (StockData stk : resultName) {

        }
        // stkData.setName(stkDto.getCp_name()); // DTO에서 repo로 저장

        return stkDto;
    }

    // 회사번호으로 입력한 데이터 DB에 있는지 검색

    public StockDTO searchCode() {
        StockDTO stkDto = new StockDTO(); // DTO
        StockData stkData = new StockData(); // repository

        List<StockData> resultCode = repo.findByName(stkDto.getCp_name()); // repo에서 dto에 저장된 cp_name Data 가져와서 List에 저장

        return stkDto;
    }

}
