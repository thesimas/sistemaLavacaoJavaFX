package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;
import java.time.LocalDate;

//Herda atributos e métodos de cliente e é obrigado a implemetar os metodos abstratos.
public class PessoaJuridica extends Cliente{
    private String cnpj;
    private String inscricaoEstadual;

    public PessoaJuridica(int id, String nome, String celular, String email, LocalDate dataCadastro, String cnpj, String inscricaoEstadual) {
        super(id, nome, celular, email, dataCadastro);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String getDados(){
        StringBuilder dados = new StringBuilder();
        dados.append(super.getDados().toString());
        dados.append("CNPJ..............: ").append(cnpj).append("\n");
        dados.append("Inscrição Estadual: ").append(inscricaoEstadual);
        return dados.toString();
    }

    @Override
    public String getDados(String observacao){
        StringBuilder dados = new StringBuilder();
        dados.append(getDados().toString()).append("\n");
        dados.append("Oberservação........: ").append(observacao).append("\n");
        return dados.toString();
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nDados da Pessoa Fisica: \n" +
                " | CNPJ: "+ cnpj + '\'' +
                " | Inscrição Estadual: " + inscricaoEstadual ;
    }
}
