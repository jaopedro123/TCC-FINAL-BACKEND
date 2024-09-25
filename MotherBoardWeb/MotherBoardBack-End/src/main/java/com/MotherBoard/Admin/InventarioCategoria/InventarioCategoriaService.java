package com.MotherBoard.Admin.InventarioCategoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.InventarioMarca;

@Service
public class InventarioCategoriaService {

    @Autowired
    private InventarioCategoriaRepository repository;

    public void salvaRegistroInventario(InventarioCategoria inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("O objeto InventarioMarca não pode ser nulo");
        }
        repository.save(inventario);
        
    }
    public void deleteByCategoriaId(Integer categoriaId) {
    	repository.deleteById(categoriaId);
    }
    
    public List<InventarioCategoria> listarTodos() {
        return repository.findAll();
    }


    public static final int INVENTARIO_CATEGORIA_PER_PAGE = 3;

    public Page<InventarioCategoria> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        PageRequest pageable = PageRequest.of(pageNum - 1, INVENTARIO_CATEGORIA_PER_PAGE,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        if (keyword != null) {
            return repository.pesquisar(keyword, pageable);
        }
        return repository.findAll(pageable);
    }
}
    