package com.MotherBoard.Admin.InventarioProduto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.InventarioProduto;

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

    public Page<InventarioProduto> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNum - 1, INVENTARIO_PRODUTOS_PER_PAGE, sort);

        if (keyword != null) {
            return repository.pesquisar(keyword, pageable);
        }
        return repository.findAll(pageable);
    }
}
