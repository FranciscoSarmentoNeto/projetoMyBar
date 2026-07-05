package br.com.mybar.project.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DataTransferObject.RegisterDTO;
import br.com.mybar.project.model.DefinedTypes.UserType;
import br.com.mybar.project.repository.UserRepositoryInterface;

import java.util.List;

@Service
public class UserService {
    private UserRepositoryInterface repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public UserService(UserRepositoryInterface repository)
    {
        this.repository = repository;
    }

    public List<User> listarUsuario(){
        List<User> lista = repository.findAll();
        return lista;
    }

    public User criarUsuario(Integer id, RegisterDTO data){
        if (repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código de usuário está em uso");
        }
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.password());
        User usuario = new User(
                data.codigo(),
                data.nome(),
                data.login(),
                senhaCriptografada,
                data.role()
        );
        return repository.save(usuario);

    }

    public User editarUsuario(Integer codigo, RegisterDTO data) {
        User usuarioExistente = repository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        usuarioExistente.setNome(data.nome());
        usuarioExistente.setEmail(data.login());
        usuarioExistente.setTipo(data.role());

        if (data.password() != null && !data.password().trim().isEmpty()) {
            String senhaCriptografada = new BCryptPasswordEncoder().encode(data.password());
            usuarioExistente.setSenha(senhaCriptografada);
        }

        return repository.save(usuarioExistente);
    }

    public Boolean excluirUsuario(Integer id){
        repository.deleteById(id);
        return true;
    }

    public User autenticarGarcom(String codigoStr, String senha) {
        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Código de garçom inválido.");
        }

        User usuario = repository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Garçom não encontrado."));

        if (!encoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha do garçom inválida.");
        }

        return usuario;
    }

    public void verificarSenhaGarcom(String codigoStr, String senha) {
        autenticarGarcom(codigoStr, senha);
    }

    public void verificarSenhaAdmin(String email, String senha) {
        User usuario = (User) repository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException("Administrador não encontrado.");
        }

        if (usuario.getTipo() != UserType.ADMIN) {
            throw new IllegalArgumentException("Usuário não tem permissão de administrador.");
        }

        if (!encoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha do administrador inválida.");
        }
    }

    public User buscarPorCodigo(String codigoStr) {
    int codigo;
    try {
        codigo = Integer.parseInt(codigoStr);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Código de garçom inválido.");
    }

    return repository.findByCodigo(codigo)
            .orElseThrow(() -> new IllegalArgumentException("Garçom não encontrado com o código: " + codigo));
}
}
