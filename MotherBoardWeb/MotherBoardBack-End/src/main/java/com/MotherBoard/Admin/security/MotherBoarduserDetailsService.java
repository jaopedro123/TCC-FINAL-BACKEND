
package com.MotherBoard.Admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.MotherBoard.Admin.usuario.UsuarioRepository;
import com.MotherBoard.entidade.comum.Usuario;

@Service
public class MotherBoarduserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.getUserByEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        
        return new MotherBoarduserDetails(usuario);
    }

}
