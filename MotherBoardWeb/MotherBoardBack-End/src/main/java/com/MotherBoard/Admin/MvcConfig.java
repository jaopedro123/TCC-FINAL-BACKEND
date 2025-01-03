package com.MotherBoard.Admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    String dirName = "fotos-usuario";
	    Path userPhotosDir = Paths.get(dirName);
	    String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
	    
	    registry.addResourceHandler("/" + dirName + "/**")
	            .addResourceLocations("file:/" + userPhotosPath + "/");

		String dirNameCategoria = "categoria-imagens";
		Path categoriaImagensDir = Paths.get(dirNameCategoria);
		String categoriaImagensPath = categoriaImagensDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirNameCategoria + "/**")
				.addResourceLocations("file:/" + categoriaImagensPath + "/");
		
		String brandLogosDirName = "Marca-logos";
		Path brandLogosDir = Paths.get(brandLogosDirName);
		String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/" + brandLogosDirName + "/**") 
		        .addResourceLocations("file:/" + brandLogosPath + "/");

		String dirNameProduto = "produto-imagens";
		Path produtoImagensDir = Paths.get(dirNameProduto);
		String produtoImagensPath = produtoImagensDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirNameProduto + "/**")
				.addResourceLocations("file:/" + produtoImagensPath + "/");
		}	
	}
