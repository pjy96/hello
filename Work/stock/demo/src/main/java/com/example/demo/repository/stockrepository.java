package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.DAO.StockData;

public interface stockrepository extends JpaRepository<StockData, Integer>{
    List<StockData> findByName(String name); // 이름으로 검색하기
    List<StockData> findByCode(int code); // 번호로 검색하기
        

}
