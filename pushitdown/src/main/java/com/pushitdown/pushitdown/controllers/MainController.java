package com.pushitdown.pushitdown.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.pushitdown.pushitdown.models.ExpedienteDTO;
import com.pushitdown.pushitdown.models.Registro;
import com.pushitdown.pushitdown.models.RegistroDTO;
import com.pushitdown.pushitdown.repositories.RegistroRepository;
import com.pushitdown.pushitdown.services.RegistroService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.channels.Pipe.SourceChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("home")
@CrossOrigin(origins = "client.local:8080") // Permite requisições do frontend
public class MainController {

    private RegistroService registroService;

    public MainController(RegistroService registroService){
        this.registroService = registroService;
    }

    @GetMapping("{username}/lastsession")
    public String getMethodName(@PathVariable String username) {
        return new String();
    }

    @PostMapping("{username}/entrance")
    public ResponseEntity<String> entrar(@PathVariable String username) {
        registroService.registrar(username, "ENTRADA");
        return ResponseEntity.status(HttpStatus.OK).body("Entrada registrada.");
    }
    
    @PostMapping("{username}/exit")
    public ResponseEntity<String> sair(@PathVariable String username) {
        registroService.registrar(username, "SAIDA");
        return ResponseEntity.status(HttpStatus.OK).body("Saída registrada.");
    }

    @GetMapping("{username}/total-horas")
    public Long obtemTotalHoras(@PathVariable String username) {
        long time = registroService.obtemTempoDeTrabalhoPorData(username, LocalDate.now());
        return time;
    }

    @GetMapping("{username}/dias-trabalhados")
    public List<LocalDate> obtemDiasDeTrabalho(@PathVariable String username) {
        return registroService.retornaDiasDeTrabalho(username);
    }

    @GetMapping("{username}/expedientes")
    public List<ExpedienteDTO> obtemExpedientes(@PathVariable String username){
        List<LocalDate> diasTrabalhados = registroService.retornaDiasDeTrabalho(username);
        List<ExpedienteDTO> expedientes =  new ArrayList<>();
        for(LocalDate dia : diasTrabalhados){
            Long horas = registroService.obtemTempoDeTrabalhoPorData(username, dia);
            expedientes.add(new ExpedienteDTO(dia, horas));
        }

        return expedientes;
    }   
    
    @GetMapping("{username}/registros")
    public List<RegistroDTO> obtemRegistros(@PathVariable String username){
        List<Registro> registros = registroService.retornaRegistrosPorUsername(username);
        List<RegistroDTO> dto = new ArrayList<>();
        for(Registro r : registros){
            dto.add(new RegistroDTO(r.getId().getData(), r.getId().getHora(), r.getTipo()));
        }
        return dto;
    }
    
}
