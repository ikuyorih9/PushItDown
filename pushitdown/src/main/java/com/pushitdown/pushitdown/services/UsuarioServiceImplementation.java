package com.pushitdown.pushitdown.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pushitdown.pushitdown.exceptions.BadFormsException;
import com.pushitdown.pushitdown.exceptions.NotFoundException;
import com.pushitdown.pushitdown.exceptions.UnauthorizedException;
import com.pushitdown.pushitdown.exceptions.UserRegisteredException;
import com.pushitdown.pushitdown.models.Usuario;
import com.pushitdown.pushitdown.models.UsuarioDTO;
import com.pushitdown.pushitdown.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImplementation implements UsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImplementation(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario cadastrar(UsuarioDTO dto) {
        // Verifica se o usuário está presente na base de dados.
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()){
            // Lança a exceção de usuário já registrado.
            throw new UserRegisteredException("Usuário " + dto.getUsername() + "já está cadastrado no sistema.");
        }
        
        // Cria um novo usuário do DTO.
        Usuario saved = new Usuario(dto.getUsername(), 
                                    dto.getNome(), 
                                    passwordEncoder.encode(dto.getSenha()));

        return usuarioRepository.save(saved);
    }

    @Override
    public Usuario autenticar(UsuarioDTO dto){
        String username = dto.getUsername();
        String senha = dto.getSenha();
        if(username == null || username.isEmpty())
            throw new BadFormsException("Username não fornecido.");
        if(senha == null || senha.isEmpty())
            throw new BadFormsException("Senha não fornecida.");

        Usuario found = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuário não está cadastrado."));

        if (!passwordEncoder.matches(senha, found.getSenha()))
            throw new UnauthorizedException("Senha incorreta");
        
        return found;
    }
}
