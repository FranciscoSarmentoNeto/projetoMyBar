package br.com.mybar.project.model.DefinedTypes;

public enum UserType {
    ADMIN("admin"),
    COZINHA("cozinha"),
    GARCOM("garcom"),
    ATENDENTE_BALCAO("atendente");


    private String role;
    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
