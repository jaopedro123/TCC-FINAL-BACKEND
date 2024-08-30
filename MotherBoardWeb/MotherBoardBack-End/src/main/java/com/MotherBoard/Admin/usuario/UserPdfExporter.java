package com.MotherBoard.Admin.usuario;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.MotherBoard.entidade.comum.Usuario;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class UserPdfExporter extends AbstractExporter{

	public void export(List<Usuario> listUsers, HttpServletResponse response) throws IOException {
	    
	    super.setReponseHeader(response, "application/pdf", ".pdf");
	    Document document = new Document(PageSize.A4);
	    PdfWriter.getInstance(document, response.getOutputStream());
	    
	    document.open();

	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setSize(18);
	    font.setColor(Color.MAGENTA);

	    Paragraph paragraph = new Paragraph("Lista de Funcionarios: ", font);
	    paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	    document.add(paragraph);
	    
	    Paragraph spacer = new Paragraph();
	    spacer.setSpacingAfter(20f);
	    document.add(spacer);

	    PdfPTable table = new PdfPTable(6);
	    table.setWidthPercentage(100f);
	    table.setSpacingAfter(10);
	    table.setWidths(new float[] {1.5f, 3.0f, 3.5f, 3.0f, 3.0f, 2.0f});
	    
	    writeTableHeader(table);
	    writeTableData(table, listUsers);

	    document.add(table);
	    
	    document.close();
	}

	private void writeTableHeader(PdfPTable table) {
	    PdfPCell cell = new PdfPCell();
	    cell.setBackgroundColor(new Color(221, 160, 221)); 
	    cell.setPadding(5);

	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setColor(Color.WHITE);

	    cell.setPhrase(new Phrase("ID", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Nome Completo", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("E-mail", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("CPF", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Roles", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Permitido", font));
	    table.addCell(cell);
	}

	
	private void writeTableData(PdfPTable table, List<Usuario> listUsers) {
	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setColor(Color.BLACK);

	    for (Usuario usuario : listUsers) {
	        PdfPCell cell;

	        cell = new PdfPCell(new Phrase(String.valueOf(usuario.getId()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(usuario.getNomeCompleto(), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(usuario.getEmail(), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(usuario.getCpf(), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(usuario.getRoles().toString(), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(String.valueOf(usuario.isPermitido()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	    }
	}


}
