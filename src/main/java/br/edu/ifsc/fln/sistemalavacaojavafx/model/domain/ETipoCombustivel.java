package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

public enum ETipoCombustivel {
    GASOLINA("Veículo movido a gasolina comum ou aditivada."),
    ETANOL("Veículo movido a etanol (álcool)."),
    FLEX("Flex, pode utilizar tanto gasolina quanto etanol ou mistura de ambos."),
    DIESEL("Veículo movido a diesel, geralmente de grande porte como caminhonetes, vans ou utilitários."),
    GNV("Veículo adaptado para Gás Natural Veicular (GNV), combustível mais econômico e menos poluente."),
    OUTRO("Outro tipo de combustível ou tecnologia, como elétrico, híbrido ou alternativo.");

    // Detalhando o Enum conforme solicitado no Mão na Massa 1.
    private final String descricao;

    ETipoCombustivel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}