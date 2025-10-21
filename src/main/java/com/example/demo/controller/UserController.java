package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.ImoveisModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserServices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;




@RestController
@RequestMapping(value = "/users")

/* .../users
GET /users
GET /users/1
POST /users
PUT /users/1
DELETE /users/1 */

public class UserController {

    @Autowired
    private UserServices service;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserModel> usuarios = service.getAll();
        List<UserDTO> usuariosDTO = usuarios.stream()// Mapeia para DTOs (sem senha)
            .map(user -> new UserDTO(user))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(usuariosDTO);
    }

    @GetMapping("/users-page")
    public Page<UserDTO> getPosts(Pageable pageable) {
        return service.getAll(pageable).map(UserDTO::new);// Page.map com construtor sem senha
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserModel user = service.find(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);// Retorna DTO sem senha
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody UserDTO dto) {
        try {
            UserModel model = service.update(id, dto);
            if (model != null) {
                 UserDTO userDTO = new UserDTO(model);
                return ResponseEntity.status(HttpStatus.OK).body(userDTO);// Retorna DTO sem senha
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO dto) {
        try {
            UserModel model = service.insert(dto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(model.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {// Tratar erros de validação, unicidade de email, etc.
            e.printStackTrace(); // Log do erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }       
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


/* 
✅ Dicas Importantes
Senha: Atualizar senhas diretamente via PUT pode ser inseguro. Considere um endpoint separado para alteração de senha.
Validação: Use @Valid e anotações do Bean Validation (como @NotNull, @Email) nos campos do UserModel para validar automaticamente os dados recebidos.
Tratamento Global de Erros: Use @ControllerAdvice para tratar exceções globais de forma padronizada.
Segurança: Para um projeto real, adicione Spring Security para proteger os endpoints. 
*/


