package br.com.mybar.project.model.DataTransferObject;

import br.com.mybar.project.model.DefinedTypes.UserType;

public record UserResponseDTO(int codigo, String nome, String email, UserType tipo) {

}