package br.com.mybar.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DefinedTypes.UserType;
import br.com.mybar.project.repository.UserRepositoryInterface;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepositoryInterface repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${api.security.admin.nome:Administrador}")
    private String adminNome;

    @Value("${api.security.admin.email:administrador@gmail.com}")
    private String adminEmail;

    @Value("${api.security.admin.senha:1234}")
    private String adminSenha;

    public DatabaseSeeder(UserRepositoryInterface repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            User admin = new User(
                    1,
                    adminNome,
                    adminEmail,
                    passwordEncoder.encode(adminSenha),
                    UserType.ADMIN
            );
            repository.save(admin);
            System.out.println("Usuário admin padrão criado: " + adminEmail);
        }
    }
}