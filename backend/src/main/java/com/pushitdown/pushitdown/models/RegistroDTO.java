package com.pushitdown.pushitdown.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RegistroDTO {
    private LocalDate data;
    private LocalTime hora;
    private String tipo;
    
    public RegistroDTO(LocalDate data, LocalTime hora, String tipo) {
        this.data = data;
        this.hora = hora;
        this.tipo = tipo;
    }
    
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
