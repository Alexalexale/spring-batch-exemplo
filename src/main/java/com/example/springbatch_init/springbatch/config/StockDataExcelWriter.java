package com.example.springbatch_init.springbatch.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbatch_init.springbatch.domain.StockData;
import com.example.springbatch_init.springbatch.repository.StockDataRepository;

public class StockDataExcelWriter implements ItemWriter<StockData> {

	private static final String FILE_NAME = "C:/excel/StockData";
	private static final String[] HEADERS = { "Symbol", "Name", "Last Sale", "Market Cap", "ADR TSO", "IPO Year",
			"Sector", "Industry", "Summary URL" };

	private String outputFilename;
	private XSSFWorkbook workbook = new XSSFWorkbook();
	private XSSFCellStyle dataCellStyle;
	private int currRow = 0;

	@Autowired
	private StockDataRepository stockDataRepository;

	private void addHeaders(XSSFSheet sheet) {

		XSSFWorkbook wb = sheet.getWorkbook();

		XSSFCellStyle style = wb.createCellStyle();
		XSSFFont font = wb.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);

		XSSFRow row = sheet.createRow(2);
		int col = 0;

		for (String header : HEADERS) {
			XSSFCell cell = row.createCell(col);
			cell.setCellValue(header);
			cell.setCellStyle(style);
			col++;
		}
		currRow++;
	}

	private void addTitleToSheet(XSSFSheet sheet) {

		XSSFWorkbook wb = sheet.getWorkbook();

		XSSFCellStyle style = wb.createCellStyle();
		XSSFFont font = wb.createFont();

		font.setFontHeightInPoints((short) 14);
		font.setFontName("Arial");
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);

		XSSFRow row = sheet.createRow(currRow);
		row.setHeightInPoints(16);

		XSSFCell cell = row.createCell(0, CellType.STRING);
		cell.setCellValue("Stock Data as of " + new Date());
		cell.setCellStyle(style);

		CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
		sheet.addMergedRegion(range);
		currRow++;

	}

	@AfterStep
	public void afterStep(StepExecution stepExecution) throws IOException {
		FileOutputStream fos = new FileOutputStream(outputFilename);
		workbook.write(fos);
		fos.close();
		System.out.println(System.currentTimeMillis());
		System.out.println(new Date());
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Calling beforeStep");
		System.out.println(System.currentTimeMillis());
		System.out.println(new Date());

		outputFilename = FILE_NAME + "_teste.xls";

		XSSFSheet sheet = workbook.createSheet("Testing");
		sheet.createFreezePane(0, 3, 0, 3);
		sheet.setDefaultColumnWidth(20);

		addTitleToSheet(sheet);
		currRow++;
		addHeaders(sheet);
		initDataStyle();

	}

	private void initDataStyle() {
		dataCellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
		dataCellStyle.setFont(font);
	}

	@Override
	public void write(List<? extends StockData> items) throws Exception {

		XSSFSheet sheet = workbook.getSheetAt(0);

		stockDataRepository.saveAll(items);

		items.stream().forEach(data -> {
			currRow++;
			XSSFRow row = sheet.createRow(currRow);
			createStringCell(row, data.getSymbol(), 0);
			createStringCell(row, data.getName(), 1);
			createNumericCell(row, data.getLastSale().doubleValue(), 2);
			createStringCell(row, data.getMarketCap(), 3);
			createStringCell(row, data.getIpoYear(), 4);
			createStringCell(row, data.getSector(), 5);
			createStringCell(row, data.getIndustry(), 6);
			createStringCell(row, data.getSummaryUrl(), 7);
		});
	}

	private void createStringCell(XSSFRow row, String val, int col) {
		XSSFCell cell = row.createCell(col);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(val);
	}

	private void createNumericCell(XSSFRow row, Double val, int col) {
		XSSFCell cell = row.createCell(col);
		cell.setCellType(CellType.NUMERIC);
		cell.setCellValue(val);
	}

}
