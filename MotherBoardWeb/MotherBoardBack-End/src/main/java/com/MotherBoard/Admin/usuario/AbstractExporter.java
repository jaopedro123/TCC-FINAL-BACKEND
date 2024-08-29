package com.MotherBoard.Admin.usuario;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date; 
import java.util.List;

import com.MotherBoard.entidade.comum.Usuario;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {

    public void setReponseHeader(HttpServletResponse response, String contentType, String extension) throws IOException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());

        String fileName = "Usuarios_" + timestamp + extension;

        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

}
