package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

import java.util.HashMap;

public class Configuracao {
    private int id;
    private int pontos;
    private HashMap<ECategoria, Double> porcentagens;

    public Configuracao() {
    }

    public Configuracao(int id, int pontos, HashMap<ECategoria, Double> porcentagens) {
        this.id = id;
        this.pontos = pontos;
        this.porcentagens = porcentagens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public HashMap<ECategoria, Double> getPorcentagens() {
        return porcentagens;
    }

    public void setPorcentagens(HashMap<ECategoria, Double> porcentagens) {
        this.porcentagens = porcentagens;
    }
}
