package com.pushitdown.pushitdown.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pushitdown.pushitdown.exceptions.NotFoundException;
import com.pushitdown.pushitdown.id.RegistroId;
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


}
