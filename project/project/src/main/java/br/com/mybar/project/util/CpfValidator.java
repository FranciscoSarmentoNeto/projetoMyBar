package br.com.mybar.project.util;

public final class CpfValidator {

    private CpfValidator() {
    }

    public static String limpar(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValid(String cpf) {
        String numeros = limpar(cpf);

        if (numeros == null || numeros.length() != 11 || numeros.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int[] digitos = numeros.chars().map(c -> c - '0').toArray();

            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += digitos[i] * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }
            if (primeiroDigito != digitos[9]) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += digitos[i] * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }

            return segundoDigito == digitos[10];
        } catch (Exception e) {
            return false;
        }
    }
}