package com.example.springbatch_init.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.springbatch_init.springbatch.domain.StockData;

@Configuration
public class BathConfig {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public ItemReader<StockData> reader() {
		FlatFileItemReader<StockData> csvFileReader = new FlatFileItemReader<>();
		csvFileReader.setResource(new FileSystemResource("companylist.csv"));
		csvFileReader.setLinesToSkip(1);
		csvFileReader.setLineMapper(new DefaultLineMapper<StockData>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(","));
				setFieldSetMapper(new StockDataFieldSetMapper());
			}
		});
		return csvFileReader;
	}

	@Bean
	public ItemWriter<StockData> writer() {
		return new StockDataExcelWriter();
	}

	@Bean
	public Job StockProcess() {
		return jobs.get("StockProcess").incrementer(new RunIdIncrementer()).flow(sotckJob()).end().build();
	}

	@Bean
	public Step sotckJob() {
		return steps.get("step").<StockData, StockData>chunk(100).reader(reader()).writer(writer()).build();
	}

}