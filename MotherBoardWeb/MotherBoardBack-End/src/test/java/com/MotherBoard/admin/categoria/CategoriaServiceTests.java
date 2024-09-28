package com.MotherBoard.admin.categoria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.MotherBoard.Admin.categoria.CategoriaRepository;
import com.MotherBoard.Admin.categoria.CategoriaServico;
import com.MotherBoard.entidade.comum.Categoria;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoriaServiceTests {

    @MockBean
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaServico servico;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName() {
        Integer id = null;
        String nome = "Hardware";
        String alias = "hardware";
        
        Categoria categoria = new Categoria(id, nome, alias);

        Mockito.when(repo.findByName(nome)).thenReturn(categoria);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        System.out.println(resultado);
        assertThat(resultado).isEqualTo("Nome Duplicado");
    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Integer id = null;
        String nome = "NameABC";
        String alias = "hardware";
        
        Categoria categoria = new Categoria(id, nome, alias);

        Mockito.when(repo.findByName(nome)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(categoria);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        assertThat(resultado).isEqualTo("Alias Duplicado");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String nome = "NameABC";
        String alias = "hardware";
        
        Mockito.when(repo.findByName(nome)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        assertThat(resultado).isEqualTo("OK");
    }
    
    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String nome = "Hardware";
        String alias = "hardware";
        
        Categoria categoria = new Categoria(2, nome, alias);

        Mockito.when(repo.findByName(nome)).thenReturn(categoria);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        assertThat(resultado).isEqualTo("Nome Duplicado");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Integer id = 1;
        String nome = "NameABC";
        String alias = "hardware";
        
        Categoria categoria = new Categoria(2, nome, alias);

        Mockito.when(repo.findByName(nome)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(categoria);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        System.out.println(resultado);
        assertThat(resultado).isEqualTo("Alias Duplicado");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String nome = "NameABC";
        String alias = "hardware";

        Categoria categoria = new Categoria(id, nome, alias);
        
        Mockito.when(repo.findByName(nome)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(categoria);

        String resultado = servico.checkUniqueCategoria(id, nome, alias);

        assertThat(resultado).isEqualTo("OK");
    }

}
