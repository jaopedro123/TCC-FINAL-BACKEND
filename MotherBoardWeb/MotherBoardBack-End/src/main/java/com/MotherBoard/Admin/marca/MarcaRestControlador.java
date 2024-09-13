package com.MotherBoard.Admin.marca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.Marca;

@RestController
public class MarcaRestControlador {

	@Autowired
	private MarcaServico servico;

	
	@PostMapping("/marcas/check_unique")
	public String checkUniqueMarca(@Param("id") Integer id, @Param("nome") String nome) {
	    return servico.checkUniqueMarca(id, nome) ? "OK" : "Duplicate";
	}
    
	@GetMapping("/marcas/{id}/categorias")
     public List<CategoriaDTO> listCategoriasByMarca(@PathVariable(name = "id") Integer marcaId) throws MarcaNotFoundRestException{
		List<CategoriaDTO> listaCategorias = new ArrayList<>();
	  try { Marca marca = servico.get(marcaId);
	  Set<Categoria> categorias = marca.getCategorias();
	  for(Categoria categoria : categorias) {
		  CategoriaDTO dto = new CategoriaDTO(categoria.getId(), categoria.getNome());
		  listaCategorias.add(dto);
	  }
	  return listaCategorias;
	  }
	catch (MarcaNotFoundException e) {
throw new MarcaNotFoundRestException(); 
	}
	}
}
