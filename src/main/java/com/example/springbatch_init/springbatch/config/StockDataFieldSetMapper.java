package com.example.springbatch_init.springbatch.config;

import java.math.BigDecimal;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.example.springbatch_init.springbatch.domain.StockData;

public class StockDataFieldSetMapper implements FieldSetMapper<StockData> {

	public StockData mapFieldSet(FieldSet fieldSet) throws BindException {

		StockData data = new StockData();
		data.setSymbol(fieldSet.readString(0));
		data.setName(fieldSet.readString(1));

		String lastSaleVal = fieldSet.readString(2);
		if ("n/a".equals(lastSaleVal)) {
			data.setLastSale(BigDecimal.ZERO);
		} else {
			data.setLastSale(new BigDecimal(lastSaleVal));
		}

		data.setMarketCap(fieldSet.readString(3));
		data.setIpoYear(fieldSet.readString(4));
		data.setSector(fieldSet.readString(5));
		data.setIndustry(fieldSet.readString(6));
		data.setSummaryUrl(fieldSet.readString(7));
		return data;
	}
}