package br.com.mybar.project.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping
public class UsuarioController {

    @GetMapping("/TelaDeRegistroDeContas")
    public String texto(){
    return "Tela de Registro de Contas";
    }
    
}

