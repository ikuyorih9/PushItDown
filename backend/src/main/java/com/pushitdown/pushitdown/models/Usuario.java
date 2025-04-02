package com.pushitdown.pushitdown.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private String username;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String senha;
    
    public Usuario() {
    }

    public Usuario(String username, String nome, String senha) {
        this.username = username;
        this.nome = nome;
        this.senha = senha;
    }
    
    public String getUsername() {
        return username;
    }
    public String getNome() {
        return nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
