package com.example.demo.dto;

import com.example.demo.model.UserModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int id;
    private String nome;
    private String email;
    private String tipo;

    public UserDTO() {
    }

    public UserDTO(int id, String nome, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    public UserDTO(UserModel userModel) {
        this.id = userModel.getId();
        this.nome = userModel.getNome();
        this.email = userModel.getEmail();
        this.tipo = userModel.getTipo();   
    }
}
