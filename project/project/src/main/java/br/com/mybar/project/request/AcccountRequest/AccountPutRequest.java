package br.com.mybar.project.request.AcccountRequest;

public class AccountPutRequest {
    private String cliente_cpf;
    private int account_id;
    private Boolean open;
    private int peoples;

    public String getCliente_cpf() {
        return cliente_cpf;
    }

    public Boolean getOpen() {
        return open;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getPeoples() {
        return peoples;
    }
}
