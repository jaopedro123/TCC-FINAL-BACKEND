package com.MotherBoard.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.MotherBoard.Admin.usuario.RoleRepository;
import com.MotherBoard.entidade.comum.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Gerenciar e coordenar todas as atividades administrativas da empresa. Manter registros precisos e atualizados. Organizar reuniões e eventos. Supervisionar a equipe administrativa e garantir que os procedimentos sejam seguidos corretamente.");
		Role roleSalva = repo.save(roleAdmin);
		
		assertThat(roleSalva.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
	    Role roleVendedor = new Role("Vendedor", "Realizar vendas de produtos ou serviços. Atender clientes, entender suas necessidades e oferecer soluções adequadas. Manter um relacionamento positivo com os clientes. Atingir metas de vendas estabelecidas pela empresa. Realizar demonstrações de produtos e serviços.");
	    Role roleEditor = new Role("Editor", "Revisar e editar conteúdo para publicações. Garantir a precisão e a clareza do conteúdo. Trabalhar com escritores e outros colaboradores para melhorar a qualidade do material publicado. Manter o estilo e o tom editorial da empresa.");
	    Role roleExportador = new Role("Exportador", "Gerenciar o processo de exportação de produtos. Coordenar com fornecedores e transportadoras. Garantir que toda a documentação necessária esteja correta e completa. Supervisionar o cumprimento das normas internacionais de exportação.");
	    Role roleAssistente = new Role("Assistente", "nem pessoa e");

	    repo.saveAll(List.of(roleVendedor, roleEditor, roleExportador, roleAssistente));
	}

	
	
	
}
