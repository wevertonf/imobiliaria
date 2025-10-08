package com.example.demo.services;

import com.example.demo.dto.FotosImoveisDTO;
import com.example.demo.model.FotosImoveisModel;
import com.example.demo.model.ImoveisModel;
import com.example.demo.repository.FotosImoveisRepository;
import com.example.demo.repository.ImoveisRepository; // Para buscar imóvel
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class FotosImoveisServices {

    @Autowired
    private FotosImoveisRepository repositorio;

    @Autowired
    private ImoveisRepository imoveisRepository; // Para buscar imóvel

    public List<FotosImoveisModel> getAll() {
        return repositorio.findAll();
    }

    public Page<FotosImoveisModel> getAll(Pageable pageable) {
        Page<FotosImoveisModel> list = repositorio.findAll(pageable);
        return list;
    }

    public FotosImoveisModel find(Integer id) {
        Optional<FotosImoveisModel> model = repositorio.findById(id);
        return model.orElse(null);
    }

    public FotosImoveisModel insert(FotosImoveisModel model) {
        // Verificar se o imóvel existe
        if (model.getImovel() != null) {
            ImoveisModel imovel = imoveisRepository.findById(model.getImovel().getId()).orElse(null);
            if (imovel == null) throw new RuntimeException("Imóvel não encontrado");
            model.setImovel(imovel);
        }
        return repositorio.save(model);
    }

    public FotosImoveisModel insert(FotosImoveisDTO dto) {
        FotosImoveisModel model = new FotosImoveisModel();
        model.setNome_arquivo(dto.getNome_arquivo());
        model.setCaminho(dto.getCaminho());
        model.setCapa(dto.getCapa());
        model.setOrdem(dto.getOrdem());

        // Relacionamento com imóvel
        if (dto.getImovelId() != null) {
            ImoveisModel imovel = imoveisRepository.findById(dto.getImovelId()).orElse(null);
            if (imovel == null) throw new RuntimeException("Imóvel não encontrado");
            model.setImovel(imovel);
        }

        return repositorio.save(model);
    }

    public FotosImoveisModel update(FotosImoveisModel model) {
        try {
            if (find(model.getId()) != null) {
                // Verificar imóvel como no insert
                if (model.getImovel() != null) {
                    ImoveisModel imovel = imoveisRepository.findById(model.getImovel().getId()).orElse(null);
                    if (imovel == null) throw new RuntimeException("Imóvel não encontrado");
                    model.setImovel(imovel);
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