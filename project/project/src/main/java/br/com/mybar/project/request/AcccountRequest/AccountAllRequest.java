package br.com.mybar.project.request.AcccountRequest;

public class AccountAllRequest {
    private int id;
    private String cpf;
    private int pessoas;
    private boolean couvert;
    private boolean open;

    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public int getPessoas() {
        return pessoas;
    }

    public boolean isCouvert() {
        return couvert;
    }

    public boolean isOpen() {
        return open;
    }

    public void setCouvert(boolean couvert) {
        this.couvert = couvert;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPessoas(int pessoas) {
        this.pessoas = pessoas;
    }
}
