package com.sobremesas.PokeCafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import com.sobremesas.PokeCafe.Service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean // Bean para o PasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Provedor de Autenticação
    public AuthenticationProvider authenticationProvider(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Cria um provedor DAO
        authProvider.setUserDetailsService(usuarioService); // Define seu UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder); // Define seu PasswordEncoder
        return authProvider;
    }

    @Bean // AuthenticationManager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Obtém o AuthenticationManager configurado pelo Spring Security
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // Desabilita CSRF
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Cria sessão se necessária
                        .sessionFixation().migrateSession() // Migra a sessão existente para um novo ID de sessão
                )
                .authorizeHttpRequests(auth -> {
                    // RECURSOS ESTÁTICOS: Acesso público
                    auth.requestMatchers("/css/**", "/images/**", "/js/**").permitAll();

                    // ROTAS PÚBLICAS GERAIS: Acesso público (GET/POST)
                    // APENAS AS ROTAS REALMENTE PÚBLICAS ESTÃO AQUI. Rotas de ADMIN e USER_ROLE foram removidas.
                    auth.requestMatchers("/", "/index", "/login", "/cadusuario").permitAll();

                    // ROTAS DE AÇÃO PÚBLICAS ESPECÍFICAS (POST):
                    auth.requestMatchers(HttpMethod.POST, "/salvarusuario").permitAll(); // POST para salvar novo usuário

                    // ROTAS PROTEGIDAS POR ROLE (Papéis)

                    // ROLE_ADMIN:
                    auth.requestMatchers("/admin", "/cadastro", "/editar/**", "/deletar/**", "/restaurar/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/salvar").hasRole("ADMIN"); // POST para salvar/atualizar sobremesa

                    // ROLE_USER (e implicitamente ADMIN, se ADMIN tiver permissões de USER)
                    auth.requestMatchers("/verCarrinho", "/adicionarCarrinho/**", "/finalizarCompra").hasAnyRole("USER", "ADMIN");

                    // QUALQUER OUTRA REQUISIÇÃO (que não foi permitAll ou hasRole/hasAnyRole acima) exige autenticação.
                    auth.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Página de login personalizada
                        .permitAll() // A página de login em si deve ser acessível a todos
                        .defaultSuccessUrl("/index", true) // Redireciona para /index após login com sucesso
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL para logout
                        .logoutSuccessUrl("/index?logout") // Redireciona para /index com param 'logout' após logout
                        .permitAll() // A rota de logout deve ser acessível a todos
                        .invalidateHttpSession(true) // Invalida a sessão HTTP ao fazer logout
                        .deleteCookies("JSESSIONID") // Deleta o cookie de sessão
                )
                .build();
    }
}