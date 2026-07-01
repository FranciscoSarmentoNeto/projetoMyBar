package br.com.mybar.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("1234")
    private String adminPassword;

    @Value("1234")
    private String garcomPassword;

    @Value("1234")
    private String cozinhaPassword;

    @Value("1234")
    private String balcaoPassword;

    public boolean isAdmin(String senha) {
        return adminPassword.equals(senha);
    }

    public boolean isGarcom(String senha) {
        return garcomPassword.equals(senha);
    }

    public boolean isCozinha(String senha) {
        return cozinhaPassword.equals(senha);
    }

    public boolean isBalcao(String senha) {
        return balcaoPassword.equals(senha);
    }
}
