package com.pushitdown.pushitdown.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.sql.Date;


import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pushitdown.pushitdown.exceptions.NotFoundException;
import com.pushitdown.pushitdown.id.RegistroId;
import com.pushitdown.pushitdown.models.ExpedienteDTO;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.RegistroDTO;
import com.pushitdown.pushitdown.models.Usuario;
import com.pushitdown.pushitdown.models.UsuarioDTO;
import com.pushitdown.pushitdown.repositories.RegistroRepository;
import com.pushitdown.pushitdown.repositories.UsuarioRepository;

@Service
public class RegistroServiceImplementation implements RegistroService{
    private RegistroRepository registroRepository;
    private UsuarioRepository usuarioRepository;

    public RegistroServiceImplementation(RegistroRepository registroRepository, UsuarioRepository usuarioRepository){
        this.registroRepository = registroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Registro registrar(String username, String tipo) {
        Usuario found = usuarioRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("Usuário não está cadastrado no sistema."));

        Optional<Registro> lastSession = registroRepository.findLastSessionByUsername(username);

        if(lastSession.isPresent() && lastSession.get().getTipo().equals(tipo)){
            throw new RuntimeException("Usuário não finalizou a última sessão");
        }

        RegistroId id = new RegistroId(found.getUsername(), LocalDate.now(), LocalTime.now());
        return registroRepository.save(new Registro(id, found, tipo));
    }

    @Override
    public List<Registro> retornaRegistrosPorData(String username, LocalDate data) {
        return registroRepository.findByUsernameAndData(username, data);
    }

    @Override
    public Long obtemTempoDeTrabalhoPorData(String username, LocalDate data) {
        List<Registro> regs = registroRepository.findByUsernameAndData(username, data);
		Duration total = Duration.ZERO;
		LocalTime inicio = null;
		for(Registro r : regs){
			String tipo = r.getTipo();
			LocalTime hora = r.getId().getHora();
			if(tipo.equals("ENTRADA")){
				inicio = hora;
			}
			else{
				total = total.plus(Duration.between(inicio, hora));
				inicio = null;
			}
		}
        return total.toMillis();
    }

    @Override
    public List<LocalDate> retornaDiasDeTrabalho(String username) {
        return registroRepository.findAllDaysByUsername(username);
    }

    @Override
    public List<Registro> retornaRegistrosPorUsername(String username) {
        return registroRepository.findRegistrosByUsername(username);
    }

    @Override
    public List<RegistroDTO> retornaRegistrosFiltrados(String username, LocalDate data, String tipo, Integer periodo){
        List<Registro> registros = registroRepository.findByParams(username, data, tipo);
        List<RegistroDTO> dto = new ArrayList<>();
        
        periodo = (periodo == null || periodo > registros.size()) ? registros.size() : periodo;
        
        for(int i = 0; i < periodo; i++){
            Registro r = registros.get(i);
            dto.add(new RegistroDTO(r.getId().getData(), r.getId().getHora(), r.getTipo()));
        }

        return dto;
    }

    @Override
    public List<RegistroDTO> retornaRegistrosFiltradosPorMes(String username, Integer mes, Integer ano, Integer periodo){
        List<Registro> registros = registroRepository.findByMonth(username, mes, ano);
        List<RegistroDTO> dto = new ArrayList<>();

        periodo = (periodo == null || periodo > registros.size()) ? registros.size() : periodo;

        for(int i = 0; i < periodo; i++){
            Registro r = registros.get(i);
            dto.add(new RegistroDTO(r.getId().getData(), r.getId().getHora(), r.getTipo()));
        }

        return dto;
    }

    @Override
    public List<ExpedienteDTO> retornaExpedientes(String username, LocalDate data, Integer periodo){
        Integer mes = null;
        Integer ano = null;
        if(data != null){
            mes = data.getMonthValue();
            ano = data.getYear();
        }

        List<Object[]> resultado = registroRepository.findExpedientesPorData(username, mes, ano);

        List<ExpedienteDTO> e = new ArrayList<>();
        for (Object[] row : resultado) {
            LocalDate day = ((Date) row[0]).toLocalDate();
            long millis = ((BigDecimal) row[1]).longValue();
            if(millis < 0){
                millis = 0;
            }
            e.add(new ExpedienteDTO(day, millis));
        }        

        List<ExpedienteDTO> expedientes = new ArrayList<>();

        periodo = (periodo == null || periodo > e.size()) ? e.size() : periodo;

        for(int i = 0; i < periodo; i++){
            ExpedienteDTO expediente = e.get(i);
            expedientes.add(new ExpedienteDTO(expediente.getData(), expediente.getHoras()));
        }

        return expedientes;
    }
}
