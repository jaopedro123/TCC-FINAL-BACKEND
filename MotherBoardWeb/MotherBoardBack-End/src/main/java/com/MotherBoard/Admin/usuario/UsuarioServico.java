package com.MotherBoard.Admin.usuario;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioServico {
	
	public static final int USUARIOS_POR_PAG = 2;

    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodosUsuarios() {
        return (List<Usuario>) usuarioRepo.findAll();
    }

    public Page<Usuario> listByPage(int pagNum, String sortField, String sortDir, String keyword, String filterBy) {
        
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        
        Pageable pageable = PageRequest.of(pagNum - 1, USUARIOS_POR_PAG, sort);
        
        if (keyword != null && !keyword.isEmpty()) {
            if ("cpf".equals(filterBy)) {
                return usuarioRepo.findAllByCpfContaining(keyword, pageable);
            } else {
                return usuarioRepo.findAllByNomeCompletoContaining(keyword, pageable);
            }
        }
        
        return usuarioRepo.findAll(pageable);
    }

    
    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    public Usuario salva(Usuario usuario) {
        boolean isUpdatingUser = (usuario.getId() != null);

        if (isUpdatingUser) {
            usuarioRepo.findById(usuario.getId()).ifPresent(existingUsuario -> {
                if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                    usuario.setSenha(existingUsuario.getSenha());
                } else {
                    encodePassword(usuario);
                }
            });
        } else {
            encodePassword(usuario);
        }

       return usuarioRepo.save(usuario);
    }

    private void encodePassword(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        Usuario usuarioByEmail = usuarioRepo.getUserByEmail(email);
        if (usuarioByEmail == null) return true;

        return id != null && usuarioByEmail.getId().equals(id);
    }

    public boolean isCpfUnique(Integer id, String cpf) {
        Usuario usuarioByCpf = usuarioRepo.getUserByCpf(cpf);
        if (usuarioByCpf == null) return true;

        return id != null && usuarioByCpf.getId().equals(id);
    }

    public Usuario get(Integer id) throws UserNotFoundException {
        return usuarioRepo.findById(id).orElseThrow(() -> 
         new UserNotFoundException("Não foi possível encontrar nenhum usuário com o ID " + id));
    }
    
    public void delete(Integer id) throws UserNotFoundException {
    	Long countById = usuarioRepo.countById(id);
    	
    	if (countById == null || countById == 0) {
    		throw new UserNotFoundException("Não foi possível encontrar nenhum usuário com o ID " + id);
    	}
    	
    	usuarioRepo.deleteById(id);
    }
    
    public void updateUserPermStatus(Integer id, boolean permitido) {
    	usuarioRepo.updateEnabledStatus(id, permitido);
    }

    public List<Usuario> listAll() {
        return (List<Usuario>) usuarioRepo.findAll();
    }


	

}
