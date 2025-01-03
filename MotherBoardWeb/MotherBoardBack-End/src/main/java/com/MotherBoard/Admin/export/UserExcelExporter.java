package com.MotherBoard.Admin.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.MotherBoard.Admin.usuario.AbstractExporter;
import com.MotherBoard.entidade.comum.Usuario;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter{
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
	    sheet = workbook.createSheet("Usuarios");
	    XSSFRow row = sheet.createRow(0);
	    
	    XSSFCellStyle cellStyle = workbook.createCellStyle();
	    XSSFFont font = workbook.createFont();
	    font.setBold(true);
	    font.setFontHeight(20);
	    font.setColor(new XSSFColor(new java.awt.Color(128, 0, 128)));
	    cellStyle.setFont(font);
	    cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(221, 160, 221))); 
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    createCell(row, 0, "ID", cellStyle);
	    createCell(row, 1, "Nome Completo", cellStyle);
	    createCell(row, 2, "E-mail", cellStyle);
	    createCell(row, 3, "CPF", cellStyle);
	    createCell(row, 4, "Roles", cellStyle);
	    createCell(row, 5, "Permitido", cellStyle);
	}

	private void writeDataLines(List<Usuario> listUsers) {
	    int rowIndex = 1;
	    XSSFCellStyle cellStyle = workbook.createCellStyle();
	    XSSFFont font = workbook.createFont();
	    font.setFontHeight(14);
	    font.setColor(new XSSFColor(new java.awt.Color(0, 0, 0))); 
	    cellStyle.setFont(font);
	    cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204))); 
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    for (Usuario usuario : listUsers) {
	        XSSFRow row = sheet.createRow(rowIndex++);
	        int columnIndex = 0;

	        createCell(row, columnIndex++, usuario.getId(), cellStyle);
	        createCell(row, columnIndex++, usuario.getNomeCompleto(), cellStyle);
	        createCell(row, columnIndex++, usuario.getEmail(), cellStyle);
	        createCell(row, columnIndex++, usuario.getCpf(), cellStyle);
	        createCell(row, columnIndex++, usuario.getRoles().toString(), cellStyle);
	        createCell(row, columnIndex++, usuario.isPermitido(), cellStyle);
	    }
	}

	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if (value instanceof Integer) {
		    cell.setCellValue((Integer) value);
		} 
		else if (value instanceof Boolean) {
		    cell.setCellValue((Boolean) value);
		} 
		else {
		    cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
	}

	
	public void export(List<Usuario> listUsers, HttpServletResponse response) throws IOException {
	    if (listUsers == null) {
	        listUsers = new ArrayList<>();
	      
	    }

	    super.setReponseHeader(response, "application/octet-stream", ".xlsx");

	    writeHeaderLine();
	    writeDataLines(listUsers);

	    ServletOutputStream outputStream = response.getOutputStream();
	    workbook.write(outputStream);
	    workbook.close();
	    outputStream.close();
	}



}
