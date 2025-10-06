package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotosImoveisDTO {
    private Integer id;
    private String nome_arquivo;
    private String caminho;
    private Boolean capa;
    private Integer ordem;

    // ID do imóvel ao qual a foto pertence
    private Integer imovelId;

    public FotosImoveisDTO() {}

    public FotosImoveisDTO(String nome_arquivo, String caminho, Boolean capa, Integer ordem, Integer imovelId) {
        this.nome_arquivo = nome_arquivo;
        this.caminho = caminho;
        this.capa = capa;
        this.ordem = ordem;
        this.imovelId = imovelId;
    }
}