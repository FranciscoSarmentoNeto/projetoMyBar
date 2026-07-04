package br.com.mybar.project.model.DataTransferObject;

import br.com.mybar.project.model.DefinedTypes.UserType;

public record RegisterDTO(String login, String password,String nome, int codigo,UserType role) {


}
