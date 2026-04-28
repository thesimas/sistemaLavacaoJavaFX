package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

public class Marca {
    private int id;
    private String nome;

    // Construtor vazio, pois posso adcionar os atributos dele com o Método SET.
    public Marca() {
    }

    public Marca(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Infomações da Marca - " +
                " | Identificação: " + id +
                " | Nome: " + nome ;
    }
}
