package com.pushitdown.pushitdown.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index.html"; // O Spring Boot servirá este arquivo da pasta static
    }

    @GetMapping("/historico")
    public String history(){
        return "logs.html";
    }
    
}
