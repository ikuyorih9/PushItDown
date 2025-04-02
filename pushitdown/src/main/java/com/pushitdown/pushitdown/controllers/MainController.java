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
import java.util.Comparator;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("home")
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
    public List<ExpedienteDTO> exp(
        @PathVariable String username,
        @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        @RequestParam(required = false) Integer periodo 
    ){
        return registroService.retornaExpedientes(username, data, periodo);
    }

    @GetMapping("{username}/registros")
    public List <RegistroDTO> obtemRegistrosFiltrados(
        @PathVariable String username,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) Integer periodo 
    ){
        tipo = tipo != null ? tipo.toUpperCase() : null;
        return registroService.retornaRegistrosFiltrados(username, data, tipo, periodo);
    }
    
}
