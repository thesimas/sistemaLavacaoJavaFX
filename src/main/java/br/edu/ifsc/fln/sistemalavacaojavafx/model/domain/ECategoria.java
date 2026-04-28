package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

public enum ECategoria {
    PEQUENO("Veículo de pequeno porte, como hatch ou compacto. Ex: Fiat Uno, Gol, Onix."),
    MEDIO("Veículo de médio porte, como sedã ou SUV pequeno. Ex: Corolla, Compass."),
    GRANDE("Veículo de grande porte, como caminhonete ou SUV grande. Ex: Hilux, SW4, Trailblazer."),
    MOTO("Motocicleta de qualquer cilindrada. Ex: CG 160, Fazer, XRE."),
    PADRAO("Categoria padrão para serviços genéricos.");

    // Detalhando o Enum conforme solicitado no Mão na Massa 1.
    private final String descricao;

    ECategoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
