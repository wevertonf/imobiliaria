package com.example.demo.repository;

import com.example.demo.model.ImoveisModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImoveisRepository extends JpaRepository<ImoveisModel, Integer> {
    // Exemplos de métodos customizados
    List<ImoveisModel> findByTipoImovelId(Integer tipoImovelId);
    List<ImoveisModel> findByBairroId(Integer bairroId);
    List<ImoveisModel> findByUsuarioId(Integer usuarioId);
    List<ImoveisModel> findByDestaqueTrue(); // Imóveis em destaque
}