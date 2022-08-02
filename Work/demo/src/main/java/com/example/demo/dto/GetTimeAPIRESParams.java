package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetTimeAPIRESParams { // 응답할 parameter 정의
    String timezone;
    String datetime;
}
