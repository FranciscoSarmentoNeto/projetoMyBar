package br.com.mybar.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DataTransferObject.RegisterDTO;
import br.com.mybar.project.model.DataTransferObject.UserResponseDTO;
import br.com.mybar.project.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UserController {


    @Autowired
    private UserService usuarioService;

    public UserController(UserService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> lista = usuarioService.listarUsuario()
                .stream()
                .map(u -> new UserResponseDTO(u.getCodigo(), u.getNome(), u.getEmail(), u.getTipo()))
                .toList();
        return ResponseEntity.ok(lista);
    }
    @PostMapping("/{id}")
    public ResponseEntity<User> criarUsuario(@PathVariable Integer id, @RequestBody RegisterDTO user)
    {
        return ResponseEntity.status(201).body(usuarioService.criarUsuario(id,user));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> editarUsuario(@PathVariable Integer id, @RequestBody RegisterDTO user)
    {
        return ResponseEntity.ok(usuarioService.editarUsuario(id, user));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerUsuario(@PathVariable Integer id)
    {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.status(204).build();
    }
}
