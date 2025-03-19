package com.pushitdown.pushitdown.models;

import java.time.Duration;
import java.time.LocalDate;

public class ExpedienteDTO {
    private LocalDate data;
    private Long horas;

    public ExpedienteDTO(LocalDate data, Long horas) {
        this.data = data;
        this.horas = horas;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public Long getHoras() {
        return horas;
    }
    public void setHoras(Long horas) {
        this.horas = horas;
    }
    
}
