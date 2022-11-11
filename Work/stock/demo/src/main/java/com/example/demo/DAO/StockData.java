package com.example.demo.DAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stock_db")
@Data
public class StockData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(name = "cp_name", columnDefinition = "varchar(25) comment '종목명'")
    private String name; // 회사 이름(종목명)

    @Column(name = "cp_code", columnDefinition = "char(6) comment '종목코드'")
    private String code; // 회사 주식 코드(종목코드)

    @Column(name = "st_price", columnDefinition = "int(10) comment '가격'")
    private int price; // 가격(종가)

    @Column(name = "st_rate", columnDefinition = "varchar(11) comment '등락률'")
    private String change_rate; // 등락률

}
