package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TestData;

@Repository
public interface testRepository extends JpaRepository<TestData, Integer>{
    List<TestData> findByResult(String result);
}
