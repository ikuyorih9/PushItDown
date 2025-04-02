package com.pushitdown.pushitdown.services;

import java.time.LocalDate;
import java.util.List;

import com.pushitdown.pushitdown.models.ExpedienteDTO;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.RegistroDTO;

public interface RegistroService {
    public Registro registrar(String username, String tipo);
    public List<Registro> retornaRegistrosPorData(String username, LocalDate data);
    public Long obtemTempoDeTrabalhoPorData(String username, LocalDate data);
    public List<LocalDate> retornaDiasDeTrabalho(String username);
    public List<Registro> retornaRegistrosPorUsername(String username);
    public List<RegistroDTO> retornaRegistrosFiltrados(String username, LocalDate data, String tipo, Integer periodo);
    public List<RegistroDTO> retornaRegistrosFiltradosPorMes(String username, Integer mes, Integer ano, Integer periodo);
    public List<ExpedienteDTO> retornaExpedientes(String username, LocalDate data, Integer periodo);
}
