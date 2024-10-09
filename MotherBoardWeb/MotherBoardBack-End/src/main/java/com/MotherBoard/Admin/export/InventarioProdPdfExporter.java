package com.MotherBoard.Admin.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.MotherBoard.Admin.usuario.AbstractExporter;
import com.MotherBoard.entidade.comum.InventarioProduto;
import com.MotherBoard.entidade.comum.Produto;
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

public class InventarioProdPdfExporter extends AbstractExporter{

	public void export(List<InventarioProduto> listInventarioProduto, HttpServletResponse response) throws IOException {
	    
	    super.setReponseHeader(response, "application/pdf", ".pdf");
	    Document document = new Document(PageSize.A4);
	    //Document document = new Document(PageSize.A1); "se quiser deixar bonito"
	    PdfWriter.getInstance(document, response.getOutputStream());
	    
	    document.open();

	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setSize(18);
	    font.setColor(Color.MAGENTA);

	    Paragraph paragraph = new Paragraph("Lista do Inventario do produto: ", font);
	    paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	    document.add(paragraph);
	    
	    Paragraph spacer = new Paragraph();
	    spacer.setSpacingAfter(20f);
	    document.add(spacer);

	    PdfPTable table = new PdfPTable(10);
	    table.setWidthPercentage(100f);
	    table.setSpacingAfter(25);
	    table.setWidths(new float[] {3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.6f});
	    
	    writeTableHeader(table);
	    writeTableData(table, listInventarioProduto);

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
	    
	    cell.setPhrase(new Phrase("Funcionário", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("ID do Funcionário", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Função", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Produto", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("ID do Produto", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Quantidade no Estoque", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Quantidade Anterior", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Ação", font));
	    table.addCell(cell);
	    
	    cell.setPhrase(new Phrase("Data de Modificação", font));
	    table.addCell(cell);
	}

	
	private void writeTableData(PdfPTable table, List<InventarioProduto> listInventarioProduto) {
	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setColor(Color.BLACK);

	    for (InventarioProduto inventarioProduto : listInventarioProduto) {
	        PdfPCell cell;

	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getId()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);
	        
	        Usuario usuario = inventarioProduto.getUsuarioId();
	        
	        cell = new PdfPCell(new Phrase(String.valueOf(usuario.getId()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(String.valueOf(usuario.getNomeCompleto()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getRoleUsuario()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	        
	        Produto produto = inventarioProduto.getProduto();

	        cell = new PdfPCell(new Phrase(String.valueOf(produto.getId()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(String.valueOf(produto.getNome()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204));
	        table.addCell(cell);

	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getEstoque()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getQuantidadeEstoqueAnterior()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getAcao()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(String.valueOf(inventarioProduto.getDataModificacao()), font));
	        cell.setBackgroundColor(new Color(255, 255, 204)); 
	        table.addCell(cell);
	    }
	}


}
