package com.MotherBoard.Admin.InventarioProduto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.InventarioProduto;
import com.MotherBoard.entidade.comum.Usuario;

import jakarta.transaction.Transactional;

@Service
public class InventarioProdutoService {

    @Autowired
    private InventarioProdutoRepository repository;

    public static final int INVENTARIO_PRODUTOS_PER_PAGE = 5;

    public void salvaRegistroInventario(InventarioProduto inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("O objeto InventarioProduto não pode ser nulo");
        }
        repository.save(inventario);
    }

    @Transactional
    public void deleteByProdutoId(Integer produtoId) {
        repository.deleteByProdutoId(produtoId);
    }

    public List<InventarioProduto> listarTodos() {
        return repository.findAll();
    }

    public Page<InventarioProduto> listByPage(int pageNum, String sortField, String sortDir, String keyword, String startDate, String endDate) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNum - 1, INVENTARIO_PRODUTOS_PER_PAGE, sort);

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

    public List<InventarioProduto> listAll() {
        return (List<InventarioProduto>) repository.findAll();
    }



}
