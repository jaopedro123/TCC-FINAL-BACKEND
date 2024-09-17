/* 
package com.MotherBoard.Admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

public class MotherBoarduserDetails implements UserDetails{


	private static final long serialVersionUID = 1L;
	private Usuario usuario;

	public MotherBoarduserDetails(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = usuario.getRoles();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getNome()));
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return usuario.isPermitido();
	}
	
	public String getNomeCompleto() {
	    return this.usuario.getNomeCompleto();
	}

	public void setNomeCompleto(String nome) {
	    this.usuario.setNomeCompleto(nome);
	}

}
 */