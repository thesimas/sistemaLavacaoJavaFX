package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

// Aqui definimos apenas o comportamento esperado (o contrato).
// Quem assinar esse contrato (implementar IDados) é obrigado a fornecer seus dados.

public interface IDados {
    public String getDados();
    public String getDados(String observacao);
}
