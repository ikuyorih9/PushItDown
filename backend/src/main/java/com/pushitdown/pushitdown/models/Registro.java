package com.pushitdown.pushitdown.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.pushitdown.pushitdown.id.RegistroId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Registro {
    
    @EmbeddedId
    private RegistroId id;

    @ManyToOne
    @MapsId("usuario")
    @JoinColumn(name = "usuario", referencedColumnName = "username", nullable = false)
    private Usuario usuario;

    @Column(nullable=false)
    private String tipo;

    public Registro() {}
    
    public Registro(RegistroId id, Usuario usuario, String tipo) {
        this.id = id;
        this.usuario = usuario;
        this.tipo = tipo;
    }

    public Registro(Usuario usuario, LocalDate data, LocalTime hora, String tipo) {
        this.id = new RegistroId(usuario.getUsername(), data, hora);
        this.usuario = usuario;
        this.tipo = tipo;
    }

    public RegistroId getId() {
        return id;
    }

    public void setId(RegistroId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Registro [usuario=" + id.getUsuario() + ", dataHora= " + id.getData() + ":" + id.getHora() + ", tipo=" + tipo + "]";
    }

    
}
