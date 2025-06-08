package com.sobremesas.PokeCafe.Service;

import com.sobremesas.PokeCafe.Domain.Usuario; // Importa a classe Usuario
import com.sobremesas.PokeCafe.Repository.UsuarioRepository; // Importa o UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa UsernameNotFoundException
import org.springframework.stereotype.Service; // Anotação @Service

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // NOVO: Método para salvar um usuário (Questão 12)
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}