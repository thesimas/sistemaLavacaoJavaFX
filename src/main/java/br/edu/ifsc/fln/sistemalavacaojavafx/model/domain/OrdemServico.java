package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private long numero;
    private double total;
    private LocalDate dataAgendamento;
    private double desconto;
    private EStatus status;
    /*Associação por Composição
    Se a OrdemServico deixar de existir, os itensOS não fazem sentido sozinhos.
    Por isso a lista e instanciada dentro do construtor da OS.*/
    private List<ItemOS> itensOrdemServico;
    /*Associação por Agregação
    A OS "tem um" Veiculo, mas o Veiculo existe independentemente da OS.
    Ele vem de fora (passado no construtor).*/
    private Veiculo veiculo;

    public OrdemServico(long numero, LocalDate dataAgendamento, double desconto, EStatus status, Veiculo veiculo) {
        this.numero = numero;
        this.total = 0; // Valor da OS se iniciando com zero ao criar o objeto.
        this.dataAgendamento = dataAgendamento;
        this.desconto = desconto;
        this.status = status;
        this.itensOrdemServico = new ArrayList<>(); // Associação por composição. Inicializando o ItemOS dentro do construtor de OS.
        this.veiculo = veiculo; // associação unidirecional por meio de agregação.
    }

    public OrdemServico() {
        this.itensOrdemServico = new ArrayList<>();
    }

    public double calcularServico() throws  ExceptionLavacao {
        // CONCEITO: Tratamento de Exceções Personalizadas
        // Valido a regra de negócio antes de executar. Se a lista estiver vazia,
        // lanço uma exceção específica (ExceptionLavacao) para avisar a camada superior.
        if(itensOrdemServico.isEmpty()){
            throw new ExceptionLavacao("Não há serviços na lista para serem calculados");
        }

        double totalServico = 0;
        for (ItemOS itemOS : itensOrdemServico) {
            totalServico += itemOS.getValorServico(); // Pecorrendo o array de itens e pegando o valor.
        }

        if(this.desconto > 0){ // Considerando se tem desconto ou não.
            double valorDesconto = totalServico * (this.desconto / 100);
            totalServico -= valorDesconto;
        }

        this.total = totalServico; // Atualizando o atributo total da classe OrdemServico.

        return totalServico;
    }
    // Sobrecarga de construtor addItemOS;
    public void addItemOS(String observacoes, Servico servico) throws  ExceptionLavacao{
        if(status == EStatus.FECHADA){
            throw new ExceptionLavacao("Não é possivel adcionar itens na OS, pois ela está fechada!");
        } else if (status == EStatus.CANCELADA) {
            throw new ExceptionLavacao("Não é possivel adcionar itens na OS, pois ela está cancelada!");
        }else {
            //Quando eu fizer a lógica de calcular o valor do serviço pela categoria, terei que consultar
            // o banco de dados da tabela configuracoes_sistema, pois ela guarda as porcentagens de cada categoria.

            double valor = servico.calculaValorPelaCategoria(veiculo.getModelo().getCategoria());
            int pontosCliente = servico.getPontos();

            // Associando os pontos a pontuação do cliente.
            this.veiculo.getCliente().getPontuacao().adicionar(pontosCliente);

            ItemOS itemOrdem = new ItemOS(valor, observacoes, this, servico);
            itensOrdemServico.add(itemOrdem);
            itemOrdem.setOrdemServico(this);
        }
    }
    // Utiliza-se esse construtor para alterar o valor do serviço, se não é o valor já definido ao instanciar o Servico.
    public void addItemOS(double valoServicoAlterado, String observacoes, Servico servico) throws  ExceptionLavacao{
        if(status == EStatus.FECHADA || status == EStatus.CANCELADA) {
            throw new ExceptionLavacao("Não é possivel remover itens na OS fechada/cancelada!");
        }else {
            ItemOS itemOrdem = new ItemOS(valoServicoAlterado, observacoes, this, servico);
            itensOrdemServico.add(itemOrdem);
            itemOrdem.setOrdemServico(this);

            // Associando os pontos a pontuação do cliente.
            this.veiculo.getCliente().getPontuacao().adicionar(Servico.getPontos());
        }
    }

    public void removeItemOS(ItemOS itemOS) throws  ExceptionLavacao{
        if(status == EStatus.FECHADA || status == EStatus.CANCELADA) {
            throw new ExceptionLavacao("Não é possivel remover itens na OS fechada/cancelada!");
        }
        try{
            itemOS.setOrdemServico(null);
            int pontosItem = itemOS.getServico().getPontos();
            // Removendo a pontuação do cliente.
            this.veiculo.getCliente().getPontuacao().subtrair(pontosItem);
            itensOrdemServico.remove(itemOS);

        }catch(ExceptionLavacao ex){
            ex.printStackTrace();
        }
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public double getTotal() throws  ExceptionLavacao {
        try {
            this.total = calcularServico();
            return  total;
        }catch(ExceptionLavacao e){
            throw new ExceptionLavacao("Mensagem : " + e.getMessage());
        }
    }

    public double getTotalServicoSemDesconto() throws  ExceptionLavacao {
        double totalServico = 0;
        for(ItemOS itemOrdem : itensOrdemServico){
            totalServico += itemOrdem.getValorServico();
        }
        return totalServico;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public List<ItemOS> getItensOS() {
        return itensOrdemServico;
    }

    // Sem setItemOS por causa da composição.
}
