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
        //return ResponseEntity.status(200).body(usuarios);
        List<UserDTO> usuariosDTO = usuarios.stream()
            .map(user -> new UserDTO(user))
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(usuariosDTO);
    }

    @GetMapping("/users-page")
    public Page<UserDTO> getPosts(Pageable pageable) {
        return service.getAll(pageable).map(UserDTO::new);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserModel user = service.find(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> update(@PathVariable int id, @RequestBody UserModel model) {
        model.setId(id); // Garantir que o ID do path seja usado
        model = service.update(model);
        return ResponseEntity.noContent().build();
    }

    /* @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        UserModel savedUser = service.insert(dto);
        //converter para DTO
        UserDTO savedUserDTO = new UserDTO(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
        //trocar o retorno p/ uri(PENDENTE)
    } */

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO dto) {
        UserModel model = service.insert(dto);
        // return new ResponseEntity(model, HttpStatus.CREATED);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(model.getId()).toUri();
        return ResponseEntity.created(uri).build();
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

/* @GetMapping("/users")
    public String getAllUsers() {
        return "Lista de todos os usuários";
    } */

    /* @GetMapping
    public List<UserModel> getAllUsers() {
        List<UserModel> lista = new ArrayList<>();
        //users.add(new UserModel(1, "Alice", "alice@example.com"));

        UserModel user1 = new UserModel(1, "Alice", "alice@example.com");
        UserModel user2 = new UserModel(2, "Bob", "bob@example.com");

        lista.add(user1);
        lista.add(user2);

        return lista;

    } */
/* 
    @Autowired
    UserRepository repositorio;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> lista = repositorio.findAll();
        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable int id) {
        Optional<UserModel> userOptional = repositorio.findById(id);
        if (userOptional.isPresent()) {
            //UserModel usuario = user.get();
            //return ResponseEntity.status(200).body(usuario);
            return ResponseEntity.ok(userOptional.get());
        } else {
            //return ResponseEntity.status(404).body(null);// Retorna 404 Not Found se o ID não existir
            return ResponseEntity.notFound().build(); // ou .body(null) como feito acima
        }
    }
    
    @PostMapping
    public ResponseEntity<UserModel> criarUsuario(@RequestBody UserModel user) {
        // O JPA cuida do ID (gerado automaticamente), então não precisa se preocupar com ID duplicado aqui
        // O save() faz INSERT se for novo (id == null ou não existir no banco) ou UPDATE se já existir
        UserModel usuarioSalvo = repositorio.save(user);
        
        //return ResponseEntity.status(201).body(usuarioSalvo);
        // Retorna 201 Created com o usuário recém-criado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    // PUT /users/{id} - Atualizar usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<UserModel> atualizarUsuario(@PathVariable int id, @RequestBody UserModel userDetails) {
        Optional<UserModel> userOptional = repositorio.findById(id);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            // Atualiza os campos com base no que veio no body
            user.setNome(userDetails.getNome()); // Exemplo: ajuste os campos conforme sua classe UserModel
            user.setEmail(userDetails.getEmail());
            //user.setSenha(userDetails.getSenha()); // Cuidado com senhas! Talvez não deva ser atualizada assim
            
            UserModel usuarioAtualizado = repositorio.save(user);
            // Retorna 200 OK com o usuário atualizado
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            // Retorna 404 Not Found se o ID não existir
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /users/{id} - Deletar usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id) {
        Optional<UserModel> userOptional = repositorio.findById(id);

        if (userOptional.isPresent()) {
            repositorio.deleteById(id); // Executa o DELETE no banco
            // Retorna 204 No Content (corpo vazio é o padrão para 204)
            return ResponseEntity.noContent().build();
        } else {
            // Retorna 404 Not Found se o ID não existir
            return ResponseEntity.notFound().build();
        }
    }*/

