package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;
import java.time.LocalDate;

//Herda atributos e métodos de cliente e é obrigado a implemetar os metodos abstratos.
public class PessoaFisica extends Cliente{
    private String cpf;
    private LocalDate dataNascimento;


    public PessoaFisica() {
    }

    public PessoaFisica(int id, String nome, String celular, String email, LocalDate dataCadastro, String cpf, LocalDate dataNascimento) {
        super(id, nome, celular, email, dataCadastro);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public PessoaFisica(int id, String nome, String celular, String email, LocalDate dataCadastro) {
        super(id, nome, celular, email, dataCadastro);
    }

    @Override
    public String getTipo(){
        return "Pessoa Fisica";
    }

    @Override
    public String getDados(){
        StringBuilder dados = new StringBuilder();
        dados.append(super.getDados().toString());
        dados.append("CPF................: ").append(cpf).append("\n");
        dados.append("Data de Nascimento.: ").append(dataNascimento).append("\n");
        if(temDescontoAniversario()){
            dados.append("Oberservação.......: ");
            dados.append("Você está na semana do seu Aniversário e tem 20% de desconto na próxima lavação!\n");
        }
        return dados.toString();
    }

    @Override
    public String getDados(String observacao){
        StringBuilder dados = new StringBuilder();
        dados.append(getDados().toString());
        dados.append("Oberservação adcional: ").append(observacao).append("\n");

        return dados.toString();
    }

    public boolean temDescontoAniversario() {
        LocalDate dataAtual = LocalDate.now();
        int diaAtual = dataAtual.getDayOfMonth();
        int mesAtual = dataAtual.getMonthValue();
        int diaDoAniver = this.dataNascimento.getDayOfMonth();
        int mesDoAniver = this.dataNascimento.getMonthValue();

        if((mesAtual ==  mesDoAniver) && ((diaDoAniver + 7) > diaAtual)){
            // A condição irá verificar se o mes do aniversario é igual ao mês atual em que o sistema está sendo utilizado, se isso for verdadeiro irá comparar a outra condição;
            // Depois irá verificar se o dia do aniversario dele + 7(Equivale a semana do Aniver dele) for maior que o dia atual, isso é verdadeiro e retorna a condição como true;
            return true;
        }else {
            return false;
        }
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nDados da Pessoa Fisica:\n" +
                " | CPF: " + this.cpf +
                " | Data de Nascimento: " + this.dataNascimento;
    }
}
