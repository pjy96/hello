package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
    String cp_name; // 회사 이름(종목명)
    String cp_code; // 회사 주식 코드(종목코드)
    String result; // 결과
}
