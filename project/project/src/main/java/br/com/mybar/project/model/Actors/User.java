package br.com.mybar.project.model.Actors;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.mybar.project.model.DefinedTypes.UserType;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")

public class User implements UserDetails {
    @Id
    @Column(name = "codigo", nullable = false, unique = true)
    private int codigo;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private UserType tipo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    public User(int codigo, String nome, String email, String senhaCriptografada, UserType role) {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        this.senha = senhaCriptografada;
        this.tipo = role;
    }

    public User() {

    }


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int id) {
        this.codigo = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UserType getTipo() {
        return tipo;
    }

    public void setTipo(UserType tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipo == UserType.ADMIN)
        {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_GARCOM"));
        } else if (this.tipo == UserType.GARCOM) {
            return List.of(new SimpleGrantedAuthority("ROLE_GARCOM"));
        } else if (this.tipo == UserType.COZINHA) {
            return List.of(new SimpleGrantedAuthority("ROLE_COZINHA"));
        }else{
            return List.of(new SimpleGrantedAuthority("ROLE_ATENDENTE"));
        }}

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

