package com.example.demo.services;

import com.example.demo.dto.ImoveisDTO;
import com.example.demo.model.ImoveisModel;
import com.example.demo.model.TiposImoveisModel;
import com.example.demo.model.BairrosModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.ImoveisRepository;
import com.example.demo.repository.TiposImoveisRepository;
import com.example.demo.repository.BairrosRepository;
import com.example.demo.repository.UserRepository; // Você já tem isso
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class ImoveisServices {

    @Autowired
    private ImoveisRepository repositorio;

    @Autowired
    private TiposImoveisRepository tiposImoveisRepository;

    @Autowired
    private BairrosRepository bairrosRepository;

    @Autowired
    private UserRepository usuariosRepository; // Para buscar o usuário

    public List<ImoveisModel> getAll() {
        return repositorio.findAll();
    }

    public Page<ImoveisModel> getAll(Pageable pageable) {
        Page<ImoveisModel> list = repositorio.findAll(pageable);
        return list;
    }

    public ImoveisModel find(Integer id) {
        Optional<ImoveisModel> model = repositorio.findById(id);
        return model.orElse(null);
    }

    public ImoveisModel insert(ImoveisModel model) {
        // Verificar se as entidades relacionadas existem
        if (model.getTipoImovel() != null) {
            TiposImoveisModel tipo = tiposImoveisRepository.findById(model.getTipoImovel().getId()).orElse(null);
            if (tipo == null) throw new RuntimeException("Tipo de imóvel não encontrado");
            model.setTipoImovel(tipo);
        }
        if (model.getBairro() != null) {
            BairrosModel bairro = bairrosRepository.findById(model.getBairro().getId()).orElse(null);
            if (bairro == null) throw new RuntimeException("Bairro não encontrado");
            model.setBairro(bairro);
        }
        if (model.getUsuario() != null) {
            UserModel usuario = usuariosRepository.findById(model.getUsuario().getId()).orElse(null);
            if (usuario == null) throw new RuntimeException("Usuário não encontrado");
            model.setUsuario(usuario);
        }
        return repositorio.save(model);
    }

    public ImoveisModel insert(ImoveisDTO dto) {
        ImoveisModel model = new ImoveisModel();
        model.setTitulo(dto.getTitulo());
        model.setDescricao(dto.getDescricao());
        model.setPreco_venda(dto.getPreco_venda());
        model.setPreco_aluguel(dto.getPreco_aluguel());
        model.setFinalidade(dto.getFinalidade());
        model.setStatus(dto.getStatus());
        model.setDormitorios(dto.getDormitorios());
        model.setBanheiros(dto.getBanheiros());
        model.setGaragem(dto.getGaragem());
        model.setArea_total(dto.getArea_total());
        model.setArea_construida(dto.getArea_construida());
        model.setEndereco(dto.getEndereco());
        model.setNumero(dto.getNumero());
        model.setComplemento(dto.getComplemento());
        model.setCep(dto.getCep());
        model.setCaracteristicas(dto.getCaracteristicas());
        model.setDestaque(dto.getDestaque());

        // Relacionamentos
        if (dto.getTipoImovelId() != null) {
            TiposImoveisModel tipo = tiposImoveisRepository.findById(dto.getTipoImovelId()).orElse(null);
            if (tipo == null) throw new RuntimeException("Tipo de imóvel não encontrado");
            model.setTipoImovel(tipo);
        }
        if (dto.getBairroId() != null) {
            BairrosModel bairro = bairrosRepository.findById(dto.getBairroId()).orElse(null);
            if (bairro == null) throw new RuntimeException("Bairro não encontrado");
            model.setBairro(bairro);
        }
        if (dto.getUsuarioId() != null) {
            UserModel usuario = usuariosRepository.findById(dto.getUsuarioId()).orElse(null);
            if (usuario == null) throw new RuntimeException("Usuário não encontrado");
            model.setUsuario(usuario);
        }

        return repositorio.save(model);
    } 
 
    public ImoveisModel update(ImoveisModel model) {
        try {
            if (find(model.getId()) != null) {
                // Verificar relacionamentos como no insert
                if (model.getTipoImovel() != null) {
                    TiposImoveisModel tipo = tiposImoveisRepository.findById(model.getTipoImovel().getId()).orElse(null);
                    if (tipo == null) throw new RuntimeException("Tipo de imóvel não encontrado");
                    model.setTipoImovel(tipo);
                }
                if (model.getBairro() != null) {
                    BairrosModel bairro = bairrosRepository.findById(model.getBairro().getId()).orElse(null);
                    if (bairro == null) throw new RuntimeException("Bairro não encontrado");
                    model.setBairro(bairro);
                }
                if (model.getUsuario() != null) {
                    UserModel usuario = usuariosRepository.findById(model.getUsuario().getId()).orElse(null);
                    if (usuario == null) throw new RuntimeException("Usuário não encontrado");
                    model.setUsuario(usuario);
                }
                return repositorio.save(model);
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