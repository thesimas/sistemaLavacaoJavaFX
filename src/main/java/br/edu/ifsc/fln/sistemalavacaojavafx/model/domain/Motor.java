package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

public class Motor {
    private int potencia;
    private ETipoCombustivel tipoCombustivel;
    // Associação unidirecional, onde o Motor conhece o ENUM tipo de combustivel.

    public Motor(int potencia, ETipoCombustivel tipoCombustivel) {
        this.potencia = potencia;
        this.tipoCombustivel = tipoCombustivel;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    @Override
    public String toString() {
        return "Informações do Motor - " +
                " | Potência: " + potencia +
                " | Tipo de Combustivel: " + tipoCombustivel ;
    }
}
