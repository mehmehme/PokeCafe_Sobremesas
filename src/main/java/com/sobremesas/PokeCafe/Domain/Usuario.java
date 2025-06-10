package com.sobremesas.PokeCafe.Domain;

import jakarta.persistence.*; // Para mapeamento JPA
import lombok.Data; // Para getters e setters
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority; // Importa GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importa SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collection; // Para a coleção de GrantedAuthority
import java.util.Collections; // Para Collections.singletonList

@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: gera construtor sem argumentos
@AllArgsConstructor // Lombok: gera construtor com todos os argumentos
@Entity // Marca como entidade JPA
@Table(name = "usuario") // Mapeia para a tabela 'usuario' no DB
public class Usuario implements UserDetails { // Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome de usuário é obrigatório") // Validação
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres") // Validação
    @Column(unique = true, nullable = false) // Garante que o username seja único e não nulo
    private String username; // Nome de usuário

    @NotBlank(message = "A senha é obrigatória") // Validação
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") // Validação
    @Column(nullable = false)
    private String password; // Senha (será criptografada)

    private Boolean isAdmin; // Indica se é admin (true) ou usuário comum (false)

    // --- Métodos da Interface UserDetails (obrigatórios) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna as permissões (roles) do usuário
        if (this.isAdmin != null && this.isAdmin) {
            // Usuário é admin: tem ROLE_ADMIN e ROLE_USER
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            // Usuário comum: tem apenas ROLE_USER
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // O Spring Security usará o 'password' atributo diretamente, mas este método é da interface.
    @Override
    public String getPassword() {
        return this.password;
    }

    // O Spring Security usará o 'username' atributo diretamente, mas este método é da interface.
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Contas nunca expiram para esta aplicação
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Contas nunca são bloqueadas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Conta sempre ativa/habilitada
    }

    // Você pode adicionar outros atributos aqui (ex: email, id_carrinho, ultima_compra, conforme sua tabela 'usuario' no SQL)
    // String email; // Se você tiver um 'email' na tabela, adicione aqui
    // Long idCarrinho; // Se você tiver um 'id_carrinho', adicione aqui
    // Date ultimaCompra; // Se você tiver 'ultima_compra', adicione aqui
}