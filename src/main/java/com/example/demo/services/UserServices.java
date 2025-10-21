package com.example.demo.services;

//import java.lang.foreign.Linker.Option;
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

@Service
public class UserServices {

    @Autowired
    UserRepository repositorio;

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

    public UserModel insert(UserModel user) {
        return repositorio.save(user);
        
    }

    public UserModel insert(UserDTO dto) {
        UserModel model = new UserModel();
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setTipo(dto.getTipo());
        //criar a senha criptografada
        return repositorio.save(model);
    }

    public UserModel update(UserModel user) {
        try {
            if (find(user.getId()) != null) {
                return repositorio.save(user);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
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
