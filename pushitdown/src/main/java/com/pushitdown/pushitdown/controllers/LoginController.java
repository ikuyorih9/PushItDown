package com.pushitdown.pushitdown.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pushitdown.pushitdown.models.UsuarioDTO;
import com.pushitdown.pushitdown.services.*;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("auth")
public class LoginController {
    private final UsuarioService usuarioService;
    // private final RegistroService registroService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        // this.registroService = registroService;
    }

    @PostMapping("cadastrar")
    public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDTO usuario){
        System.out.println("[CADASTRO]: cadastrando " + usuario.getNome() + "... (auth/cadastrar)");
        usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("login")
    public ResponseEntity entrar(@RequestBody UsuarioDTO dto){
        System.out.println("[LOGIN] entrando como " + dto.getUsername() + ".");
        usuarioService.autenticar(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Login realizado com sucesso");
    }
    
}
