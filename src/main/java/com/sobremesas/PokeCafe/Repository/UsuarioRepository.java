package com.sobremesas.PokeCafe.Repository;

import com.sobremesas.PokeCafe.Domain.Usuario; // Importa a classe Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Para retornar Optional

@Repository // Repositório Spring Data JPA
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para encontrar um usuário pelo username. O Spring Data JPA cria a query automaticamente.
    Optional<Usuario> findByUsername(String username);
}