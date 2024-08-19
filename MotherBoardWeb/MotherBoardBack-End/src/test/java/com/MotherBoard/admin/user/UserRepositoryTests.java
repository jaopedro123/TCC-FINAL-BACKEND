package com.MotherBoard.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.MotherBoard.Admin.usuario.UsuarioRepository;
import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UsuarioRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCriaUsuario1Role() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		Usuario uga = new Usuario("namem@haha.com", "senha1", "nome", "111.111.111-22");
		uga.addRole(roleAdmin);
		
		Usuario usuarioSalvo = repo.save(uga);
		assertThat(usuarioSalvo.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCriaUsuario2Roles() {
		Usuario usuarioSeila = new Usuario("pokemon@gmail.com", "se2222", "nome2", "223.232.322-11");
		Role roleEditor = new Role(1);
		Role roleAssistente = new Role(5);
		
		usuarioSeila.addRole(roleEditor);
		usuarioSeila.addRole(roleAssistente);
		
		Usuario salvo = repo.save(usuarioSeila);
		
		assertThat(salvo.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListaTodosUsuarios() {
		Iterable<Usuario> listarUsuarios = repo.findAll();
		listarUsuarios.forEach(usuarios -> System.out.println(usuarios));
		
	}
	
	@Test
	public void testGetUsuarioPeloID() {
		Usuario usuarioId = repo.findById(1).get();
		System.out.println(usuarioId);
		assertThat(usuarioId).isNotNull();
	}
	
	@Test
	public void testAtualizaDetalhesDoUsuario() {
		Usuario usuar = repo.findById(1).get();
		usuar.setPermitido(true);
		usuar.setNomeCompleto("PAIA");
		
		repo.save(usuar);
	}
	
	@Test
	public void testAtualizaRoles() {
		Usuario usuar = repo.findById(1).get();
		Role roleEditor = new Role(5);
		Role roleAssistente = new Role(7);

		usuar.getRoles().remove(roleAssistente);
		usuar.addRole(roleEditor);
		
		repo.save(usuar);
	}
	
	@Test
	public void testDeletaUsuario() {
		Integer usuarioid = 2;
		repo.deleteById(usuarioid);
	}
	
	@Test
	public void testGetUsuarioByEmail() {
		String email = "felipe13lixo@gmail.com";
		Usuario usuario = repo.getUserByEmail(email);	
		assertThat(usuario).isNotNull();
	}
	
	@Test
	public void testGetUsuarioByCpf() {
		String cpf = "111.111.111-22";
		Usuario usuario = repo.getUserByCpf(cpf);	
		assertThat(usuario).isNotNull();
	}
	
	@Test
	public void testCountById() {
	    Integer id = 1;
	    Long countById = repo.countById(id);
	    
	    assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser() {
	    Integer id = 73;
	    repo.updateEnabledStatus(id, false);
	}

}
