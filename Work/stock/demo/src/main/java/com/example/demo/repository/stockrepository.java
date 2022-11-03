package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DAO.StockData;

public interface stockrepository extends JpaRepository<StockData, Integer>{

}
