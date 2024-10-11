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
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.Usuario;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class InventarioCategoriaExcelExporter extends AbstractExporter{
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public InventarioCategoriaExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
	    sheet = workbook.createSheet("InventarioCategorias");
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
	    createCell(row, 1, "Funcionário", cellStyle);
	    createCell(row, 2, "ID do Funcionário", cellStyle);
	    createCell(row, 3, "Função", cellStyle);
	    createCell(row, 4, "Categoria", cellStyle);
	    createCell(row, 5, "ID da Categoria", cellStyle);
	    createCell(row, 6, "Ação", cellStyle);
	    createCell(row, 7, "Data de Modificação", cellStyle);
	}

	private void writeDataLines(List<InventarioCategoria> listInventarioCategorias) {
	    int rowIndex = 1;
	    XSSFCellStyle cellStyle = workbook.createCellStyle();
	    XSSFFont font = workbook.createFont();
	    font.setFontHeight(14);
	    font.setColor(new XSSFColor(new java.awt.Color(0, 0, 0))); 
	    cellStyle.setFont(font);
	    cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204))); 
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    for (InventarioCategoria inventarioCategoria : listInventarioCategorias) {
	        XSSFRow row = sheet.createRow(rowIndex++);
	        int columnIndex = 0;

	        createCell(row, columnIndex++, inventarioCategoria.getId(), cellStyle);
	        Usuario usuario = inventarioCategoria.getUsuarioId(); 
	        
	        createCell(row, columnIndex++, usuario != null ? usuario.getNomeCompleto() : "", cellStyle);
	        createCell(row, columnIndex++, usuario != null ? usuario.getId() : "", cellStyle);
	        createCell(row, columnIndex++, inventarioCategoria.getRoleUsuario(), cellStyle);
	        
	        Categoria categoria = inventarioCategoria.getCategoria(); 
	        createCell(row, columnIndex++, categoria != null ? categoria.getNome() : "", cellStyle);
	        createCell(row, columnIndex++, categoria != null ? categoria.getId() : "", cellStyle);
	        
	        createCell(row, columnIndex++, inventarioCategoria.getAcao(), cellStyle);
	        createCell(row, columnIndex++, inventarioCategoria.getDataModificacao(), cellStyle);
	    }
	}


	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
	    XSSFCell cell = row.createCell(columnIndex);
	    sheet.autoSizeColumn(columnIndex);
	    
	    if (value instanceof Integer) {
	        cell.setCellValue((Integer) value);
	    } else if (value instanceof Boolean) {
	        cell.setCellValue((Boolean) value);
	    } else if (value instanceof String) {
	        cell.setCellValue((String) value);
	    } else {
	        cell.setCellValue(value != null ? value.toString() : "");
	    }
	    
	    cell.setCellStyle(style);
	}

	
	public void export(List<InventarioCategoria> listInventarioCategorias, HttpServletResponse response) throws IOException {
	    if (listInventarioCategorias == null) {
	    	listInventarioCategorias = new ArrayList<>();
	      
	    }

	    super.setReponseHeader(response, "application/octet-stream", ".xlsx");

	    writeHeaderLine();
	    writeDataLines(listInventarioCategorias);

	    ServletOutputStream outputStream = response.getOutputStream();
	    workbook.write(outputStream);
	    workbook.close();
	    outputStream.close();
	}



}
