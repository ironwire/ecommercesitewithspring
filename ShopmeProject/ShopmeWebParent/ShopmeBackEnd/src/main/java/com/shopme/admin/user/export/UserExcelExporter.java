package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	public void export(List<User> users, HttpServletResponse response) throws IOException {
		super.setRespondHeader(response, "application/octet-stream", ".xlsx");
		
		writeHeaderLine();
		writeDataLines(users);
		
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
	}
	
	private void writeDataLines(List<User> users) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		
		for(User user: users) {
			XSSFRow row =sheet.createRow(rowIndex++);
			
			int columnIndex = 0;
			
			createCell(row, columnIndex, user.getId(),cellStyle);
			createCell(row, columnIndex+1, user.getEmail(),cellStyle);
			createCell(row, columnIndex+2, user.getFirstName(),cellStyle);
			createCell(row, columnIndex+3, user.getLastName(),cellStyle);
			createCell(row, columnIndex+4, user.getRoles().toString(),cellStyle);
			createCell(row, columnIndex+5, user.isEnabled(),cellStyle);
		}
		
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		sheet.setColumnWidth(0, 10*256);
		sheet.setColumnWidth(1, 15*256);
		sheet.setColumnWidth(2, 20*256);
		sheet.setColumnWidth(3, 20*256);
		sheet.setColumnWidth(4, 25*256);
		sheet.setColumnWidth(5, 10*256);

		
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0, "User ID", cellStyle);
		createCell(row, 1, "E-mail", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enabled?", cellStyle);
		
		
		
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}else if(value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
		
		
	}
}
