package com.example.springbatch_init.springbatch.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springbatch_init.springbatch.domain.StockData;

@Repository
public interface StockDataRepository extends CrudRepository<StockData, Long> {

}