package br.com.mybar.project.controllers;

import br.com.mybar.project.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/admin")
    public ResponseEntity<String> loginAdmin(@RequestBody String senha) {
        if (authService.isAdmin(senha)) {
            return ResponseEntity.ok("ADMIN AUTORIZADO");
        }

        System.out.printf(senha);
        return ResponseEntity.status(403).body("Senha incorreta");
    }

    @PostMapping("/login/garcom")
    public ResponseEntity<String> loginGarcom(@RequestBody String senha) {
        if (authService.isGarcom(senha)) {
            return ResponseEntity.ok("GARÇOM AUTORIZADO");
        }
        return ResponseEntity.status(403).body("Senha incorreta");
    }
}
