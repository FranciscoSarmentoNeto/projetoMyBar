package br.com.mybar.project.request.AcccountRequest;

// Body for request of post Account
public class AccountPostRequest {
    private String cliente_cpf;
    private boolean open;
    private int peoples;

    public String getCliente_cpf() {
        return cliente_cpf;
    }

    public boolean getOpen() {
        return open;
    }

    public int getPeoples() {
        return peoples;
    }
}
