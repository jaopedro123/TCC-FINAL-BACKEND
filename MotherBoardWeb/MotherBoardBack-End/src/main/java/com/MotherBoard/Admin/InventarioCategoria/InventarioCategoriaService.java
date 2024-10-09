package com.MotherBoard.Admin.InventarioCategoria;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.InventarioProduto;

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

    public Page<InventarioCategoria> listByPage(int pageNum, String sortField, String sortDir, String keyword, String startDate, String endDate) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    	PageRequest pageable = PageRequest.of(pageNum - 1, INVENTARIO_CATEGORIA_PER_PAGE, sort);

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasDateRange = (startDate != null && !startDate.trim().isEmpty() && endDate != null && !endDate.trim().isEmpty());
        DateTimeFormatter formatterBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterISO = DateTimeFormatter.ofPattern("yyyyMMdd");

        if (hasDateRange) {
            try {
                LocalDate startLocalDate = LocalDate.parse(startDate, formatterBR);
                LocalDate endLocalDate = LocalDate.parse(endDate, formatterBR);
                startDate = startLocalDate.format(formatterISO);
                endDate = endLocalDate.format(formatterISO);
            } catch (DateTimeParseException e) {
                System.out.println("Erro ao formatar datas: " + e.getMessage());
            }
        }

        if (hasKeyword && hasDateRange) {
            return repository.pesquisarPorPeriodo(keyword, startDate, endDate, pageable);
        } else if (hasDateRange) {
            return repository.pesquisarPorPeriodoSemKeyword(startDate, endDate, pageable);
        } else if (hasKeyword) {
            return repository.pesquisar(keyword, pageable);
        }

        return repository.findAll(pageable);
    }

	
    public List<InventarioCategoria> listAll() {
        return (List<InventarioCategoria>) repository.findAll();
    }
    
}
    