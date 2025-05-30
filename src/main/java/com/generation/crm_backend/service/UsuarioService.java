package com.generation.crm_backend.service;

import com.generation.crm_backend.model.Usuario;
import com.generation.crm_backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Buscar todos os usuários
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuário por email (usuário)
    public Optional<Usuario> findByUsuario(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email);
    }

    // Criar um novo usuário
    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
    	  // Verifica se já existe o usuário
        if (usuarioRepository.findByEmailIgnoreCase(usuario.getEmail()).isPresent()) {
            return Optional.empty();
    }
        // Salva no banco (senha sem criptografia)
        return Optional.of(usuarioRepository.save(usuario)); 
    }

    // Atualizar um usuário existente
    public Optional<Usuario> atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(usuarioAtualizado.getSenha());
            usuario.setTelefone(usuarioAtualizado.getTelefone());
            usuario.setFoto(usuarioAtualizado.getFoto());
            usuario.setCargo(usuarioAtualizado.getCargo());
            return usuarioRepository.save(usuario);
        });
    }
}