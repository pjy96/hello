package com.example.demo.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="test_data")
@Data // Getter & Setter 포함
public class TestData{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "input", columnDefinition = "varchar(25) not null comment '입력값'")
    private String input;

    @Column(name = "result", columnDefinition = "varchar(60) comment '결과값'")
    private String result;

    @CreationTimestamp
    @Column(name = "datetime", columnDefinition = "datetime(6) not null comment '입력한 시간'")
    private LocalDateTime datetime;
    
    @Column(name = "host_ip", columnDefinition = "varchar(15) not null comment '입력한 ip 주소'")
    private String hostip;
    
}