package com.example.demo.DAO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "Nstock_db")
@Data
public class NStockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(name = "name", columnDefinition = "varchar(25) not null comment '종목명'")
    private String name; // 회사 이름(종목명)

    @Column(name = "code", columnDefinition = "char(6) not null comment '종목코드'")
    private String code; // 회사 주식 코드(종목코드)
}
