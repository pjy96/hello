package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StompMessage {
    private String from; // 입력인
    private String text; // 전달 message
}
