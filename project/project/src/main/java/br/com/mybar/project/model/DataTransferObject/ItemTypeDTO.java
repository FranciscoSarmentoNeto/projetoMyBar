package br.com.mybar.project.model.DataTransferObject;

import java.math.BigDecimal;

public record ItemTypeDTO(String descricao, boolean cozinha, BigDecimal gorjeta) {
}
