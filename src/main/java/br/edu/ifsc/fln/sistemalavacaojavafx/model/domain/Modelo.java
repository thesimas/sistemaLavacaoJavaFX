package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

public class Modelo {
    private int id;
    private String descricao;
    private Motor motor; // Associação Unidirecional.
    private ECategoria categoria;
    private Marca marca;  // Associação Unidirecional.

    // Construtor sem os seus atributos padrões, mas para manter a Composição, deverá ser passado ao menos as informações do Motor.
    public Modelo(int pontenciaRecebida, ETipoCombustivel tipoCombustivelRecebido) {
        this.motor = new Motor(pontenciaRecebida, tipoCombustivelRecebido);
    }

    public Modelo (int id, String descricao,int pontenciaRecebida, ETipoCombustivel tipoCombustivelRecebido, ECategoria categoria,  Marca marca) {
        this.id = id;
        this.descricao = descricao;
        this.motor = new Motor(pontenciaRecebida, tipoCombustivelRecebido); // Ao criar o construtor, deverá ser feito o motor, respeitando a COMPOSIÇÃO.
        this.categoria = categoria;
        this.marca = marca;
    }
    // Set domain.Modelo não deverá existir por causa da COMPOSIÇÃO.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Motor getMotor() {
        return motor;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Informações do Modelo - " +
                "Identificação: " + id +
                " | Descrição do Modelo: " + descricao +
                " | Motor: " + motor +
                " | Categoria do Veículo: " + categoria +
                " | Marca - " + marca ;
    }
}