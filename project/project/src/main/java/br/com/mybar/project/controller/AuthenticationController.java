package br.com.mybar.project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mybar.project.infra.security.TokenService;
import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DataTransferObject.AutheticationDTO;
import br.com.mybar.project.model.DataTransferObject.LoginResponseDTO;



@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AutheticationDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var usuario = (User) auth.getPrincipal();
            var token = tokenService.generateToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getNome(), usuario.getTipo().name()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Email ou senha inválidos.");
        }
    }

}
