package com.MotherBoard.Admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    SecurityFilterChain configureHttp(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.csrf().disable() 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/Usuarios/**").hasAuthority("Admin")
                .requestMatchers("/categorias/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/marcas/**").hasAnyAuthority("Admin", "Editor", "Assistente")
                .requestMatchers("/produtos/editar/**", "/produtos/salvar/**", "/produtos/deletar/**", "/produtos/new", "/produtos/check_uniquePorduto", "/produtos/detalhes/**").hasAnyAuthority("Admin", "Editor", "Assistente")
                .requestMatchers("/inventarioCategorias/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/inventarioMarcas/**", "/inventarioProdutos/**").hasAnyAuthority("Admin", "Editor", "Assistente")
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")  
                .usernameParameter("email") 
                .permitAll() 
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  
                .logoutSuccessUrl("/login?logout") 
                .invalidateHttpSession(true) 
                .permitAll()
                
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            );

        return http.build();
    }


    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception {
        return (web) -> web.ignoring().requestMatchers("/imagens/**", "/css/**", "/scripts/**", "/webjars/**");
    }


    
}
