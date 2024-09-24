package com.MotherBoard.Admin.InventarioMarca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.InventarioMarca;

@Service
public class InventarioMarcaService {

    @Autowired
    private InventarioMarcaRepository repository;

    public void salvaRegistroInventario(InventarioMarca inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("O objeto InventarioMarca não pode ser nulo");
        }
        repository.save(inventario);
        
    }
    public void deleteByMarcaId(Integer marcaId) {
    	repository.deleteByMarcaId(marcaId);
    }
    
    public List<InventarioMarca> listarTodos() {
        return repository.findAll();
    }


    public static final int INVENTARIO_MARCAS_PER_PAGE = 1;

    public Page<InventarioMarca> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNum - 1, INVENTARIO_MARCAS_PER_PAGE, sort);

        if (keyword != null) {
            return repository.findAllByKeyword(keyword, pageable);
        }
        return repository.findAll(pageable);
    }
}
    