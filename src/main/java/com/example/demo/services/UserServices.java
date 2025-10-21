package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.ImoveisModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.SenhaUtil;

// Se usar o Spring Security PasswordEncoder, injete-o:
// import org.springframework.security.crypto.password.PasswordEncoder;
// @Autowired private PasswordEncoder passwordEncoder;

@Service
public class UserServices {

    @Autowired
    UserRepository repositorio;

    // P/ PasswordEncoder do Spring Security:
    // @Autowired
    // private PasswordEncoder passwordEncoder;


    //HTTP -> Controller -> Service (getAll()) -> Repository -> Model -> Banco de Dados
    //Banco de Dados -> Model -> Repository -> Service -> Controller -> HTTP
    public List<UserModel> getAll() {
        List<UserModel> lista = repositorio.findAll();
        return lista;
    }

    public Page<UserModel> getAll(Pageable pageable) {
        Page<UserModel> list = repositorio.findAll(pageable);
        return list;
    }

    public UserModel find(Integer id) {
        Optional<UserModel> model = repositorio.findById(id);
        return model.orElse(null);
    }

    /* public UserModel insert(UserModel user) {
        return repositorio.save(user);
        
    } */

    public UserModel insert(UserDTO dto) {
        UserModel model = new UserModel();
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setTipo(dto.getTipo());

        // Criptografar a senha antes de salvar
        String senhaCriptografada = null;
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            senhaCriptografada = SenhaUtil.hashSenha(dto.getSenha());// Usando utilitário standalone

            // Se usando Spring Security PasswordEncoder:
            // senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        }
        model.setSenha(senhaCriptografada); // Pode ser null se dto.getSenha() for vazia

        return repositorio.save(model);
    }

    public UserModel update(Integer id, UserDTO dto) {
        Optional<UserModel> optionalModel = repositorio.findById(id);
        if (optionalModel.isPresent()) {
            UserModel model = optionalModel.get();

            // Atualizar campos básicos
            model.setNome(dto.getNome());
            model.setEmail(dto.getEmail());
            model.setTipo(dto.getTipo());

            // Atualizar senha SOMENTE se uma nova foi fornecida
            if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
                String senhaCriptografada = SenhaUtil.hashSenha(dto.getSenha());
                // Ou com Spring Security: passwordEncoder.encode(dto.getSenha());
                model.setSenha(senhaCriptografada);
            }
            // Se dto.getSenha() for null/vazia, a senha existente é mantida.

            return repositorio.save(model);
        } else {
            return null; // Ou lançar exceção
        }
    }

    public boolean delete(Integer id) {
        try {
            if (find(id) != null) {
                repositorio.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    
}
