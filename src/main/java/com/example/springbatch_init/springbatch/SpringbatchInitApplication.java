package com.example.springbatch_init.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchInitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchInitApplication.class, args);
	}
}
