package com.pushitdown.pushitdown.services;

import com.pushitdown.pushitdown.models.Usuario;
import com.pushitdown.pushitdown.models.UsuarioDTO;

public interface UsuarioService {
    public Usuario cadastrar(UsuarioDTO dto);
    public Usuario autenticar(UsuarioDTO dto);
}
