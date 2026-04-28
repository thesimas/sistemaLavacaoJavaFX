package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;
// Classe associativa gerada atraves de uma multiciplidade de muitos para muitos.
// Onde Ordem de serviço compoe ItemOS.

public class ItemOS {
    private double valorServico;
    private String observacoes;
    private OrdemServico ordemServico;
    private Servico servico;

    public ItemOS(double valorServico, String observacoes, OrdemServico ordemServico, Servico servico) {
        this.valorServico = valorServico;
        this.observacoes = observacoes;
        this.ordemServico = ordemServico;
        this.servico = servico;
    }

    public double getValorServico() {
        return valorServico;
    }

    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
