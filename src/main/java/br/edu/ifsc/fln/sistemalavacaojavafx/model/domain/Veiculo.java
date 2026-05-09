package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;

// Aqui a classe assina um contrato com IDados, obrigando-se a implementar getDados().
public class Veiculo implements IDados{
    private int id;
    private String placa;
    private String observacoes;
    private Modelo modelo; // Associação Unidirecional
    private Cor cor; // Associação Unidirecional
    private Cliente cliente; // Associação Bidirecional;

    public Veiculo(int id, String placa, String observacoes, Modelo modelo, Cor cor, Cliente cliente) {
        this.id = id;
        this.placa = placa;
        this.observacoes = observacoes;
        this.modelo = modelo;
        this.cor = cor;
        this.cliente = cliente;
    }

    public Veiculo() {
    }

    @Override
    public String getDados(){
        StringBuilder dados = new StringBuilder();
        dados.append("Proprietário.....: ").append(this.getCliente().getNome() + "\n");
        dados.append("Placa............: ").append(placa).append("\n");
        dados.append("Modelo...........: ").append(modelo.getDescricao()).append("\n");
        dados.append("Marca............: ").append(modelo.getMarca().getNome()).append("\n");
        dados.append("Categoria........: ").append(modelo.getCategoria().getDescricao()).append("\n");
        dados.append("Potência do Motor: ").append(modelo.getMotor().getPotencia()).append("\n");
        dados.append("Tipo de Gasolina.: ").append(modelo.getMotor().getTipoCombustivel()).append("\n");
        dados.append("\n");
        return dados.toString();
    }

    @Override
    public String getDados(String observacao){
        StringBuilder dados = new StringBuilder(getDados());
        dados.append("Obeservação: ").append(observacao);
        return dados.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Informações do Veículo:" +
                "\nIdentificação: " + id +
                "\nPlaca........: " + placa +
                "\nObservações..: " + observacoes +
                "\ndomain.Modelo.......: " + modelo.getDescricao() +
                "\ndomain.Cor..........: " + cor.getNome() +
                "\nProprietário.: " + cliente.getNome();
    }
}
