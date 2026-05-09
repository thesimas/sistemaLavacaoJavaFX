package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

import java.util.HashMap;

public class Servico {
    private int id;
    private String descricao;
    private double valor;
    private static int pontos;
    private ECategoria categoria = ECategoria.PADRAO; // Dessa forma, todos os serviços nascem como padrão.

    public Servico() {
    }

    public Servico(int id, String descricao, double valor, ECategoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
    }

    public double CalcularValorPelaCategoria(ECategoria categoria, Configuracao configuracao) {

        if(categoria == ECategoria.PADRAO) {
            return this.valor;
        }

        HashMap<ECategoria, Double> porcentagens = configuracao.getPorcentagens();

        double precoFinal = this.valor * porcentagens.get(categoria);

        return precoFinal;
    }

    // Lógica do valor do serviço referente a categoria do veiculo.
    public double calculaValorPelaCategoria(ECategoria categoria) {
        // Criando essa variavel temporaria para não alterar o valor do serviço, no atributo da classe.
        double precoFinal = this.valor;

        switch (categoria) {
            case MOTO:
                precoFinal = this.valor * 0.90;
                break;
            case PEQUENO:
                precoFinal = this.valor * 0.95;
                break;
            case MEDIO:
                precoFinal = this.valor * 1.10;
                break;
            case GRANDE:
                precoFinal = this.valor * 1.20;
                break;
            default:
                precoFinal = this.valor; // Valor para veiculo padrão.
        }
        return precoFinal;
    }

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

//    // Mudança na logica do pontos, vai ser baseado no valor pago do serviço.
//    // Antes era um valor padrão.
//    public static int getPontos(double valor) {
//        if(valor <= 50){
//            pontos = 20;
//        } else if (valor <= 100) {
//            pontos = 30;
//        } else if (valor <= 150) {
//            pontos = 40;
//        }else {
//            pontos = 50;
//        }
//        return pontos;
//    }

    public static int getPontos() {
        return pontos;
    }

    public static void setPontos(int pontos) {
        Servico.pontos = pontos;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

}
