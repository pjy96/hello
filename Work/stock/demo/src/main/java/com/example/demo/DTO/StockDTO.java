package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
    String cp_name; // 회사 이름(종목명)
    String cp_code; // 회사 주식 코드(단축코드)
    int st_price; // 가격(종가)
    String st_rate; // 등락률

    String result; // 출력할 결과물
}
