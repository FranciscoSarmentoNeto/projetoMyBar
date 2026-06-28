package br.com.mybar.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("1234")
    private String adminPassword;

    @Value("1234")
    private String garcomPassword;

    public boolean isAdmin(String senha) {
        return adminPassword.equals(senha);
    }

    public boolean isGarcom(String senha) {
        return garcomPassword.equals(senha);
    }
}
