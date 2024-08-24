package com.MotherBoard.Admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		    Path uploadPath = Paths.get(uploadDir);
		    
		    if (!Files.exists(uploadPath)) {
		        Files.createDirectories(uploadPath);
		    }
		    
		    try (InputStream inputStream = multipartFile.getInputStream()) {
		        Path filePath = uploadPath.resolve(fileName);
		        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		    } 
		    catch (IOException ex) {
		        throw new IOException("Nao foi possivel salvar a imagem: " + fileName, ex);
		    }
		}


	public static void cleanDir(String dir) {
	    Path dirPath = Paths.get(dir);
	    try {
	        Files.list(dirPath).forEach(file -> {
	            if (!Files.isDirectory(file)) {
	                try {
	                    Files.delete(file);
	                } 
	                catch (IOException ex) {
	                    System.out.println("Nao foi possivel deletar essa imagem: " + file);
	                }
	            }
	        });
	    } 
	    catch (IOException ex) {
	        System.out.println("Erro ao listar o diretório: " + dirPath);
	    }
	}
	
	public static void deleteDir(String dir) throws IOException {
	    Path dirPath = Paths.get(dir);
	    if (Files.exists(dirPath)) {
	        try {
	            Files.walk(dirPath)
	                .sorted(Comparator.reverseOrder())
	                .map(Path::toFile)
	                .forEach(File::delete);
	        } 
	        catch (IOException ex) {
	            System.out.println("Erro ao deletar o diretório: " + dirPath);
	        }
	    }
	}

	
}
