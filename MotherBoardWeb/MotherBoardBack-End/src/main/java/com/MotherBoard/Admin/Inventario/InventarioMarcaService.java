package com.MotherBoard.Admin.Inventario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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


    
}