package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DAO.NStockData;


public interface stockrepository extends JpaRepository<NStockData, Integer>{
    List<NStockData> findByName(String name); // 이름으로 검색하기
}
