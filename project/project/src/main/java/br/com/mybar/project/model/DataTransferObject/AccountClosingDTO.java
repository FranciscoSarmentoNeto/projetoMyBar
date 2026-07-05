package br.com.mybar.project.model.DataTransferObject;

import br.com.mybar.project.model.Account;
import br.com.mybar.project.model.AccountItem;
import br.com.mybar.project.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public record AccountClosingDTO(
        Account conta,
        List<AccountItem> itens,
        BigDecimal valorTotalItens,
        BigDecimal valorGorjeta,
        BigDecimal valorTotalComGorjeta,
        BigDecimal valorPago,
        List<Payment> pagamentos
) {
}