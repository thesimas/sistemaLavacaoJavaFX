package br.edu.ifsc.fln.sistemalavacaojavafx.model.domain;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.*;

public class Pontuacao {

    // CONCEITO: Encapsulamento
    // Não criei o metodo setQuantidade() para proteger a integridade do saldo.
    // A única forma de alterar a pontuação é através das regras de negócio (adicionar/subtrair).
    private int quantidade;

    public Pontuacao() {
        this.quantidade = 0;
    }

    public int adicionar(int quantidade) throws ExceptionLavacao {
        if (quantidade <= 0) {
            throw new ExceptionLavacao("Não é possivel adcionar pontuação menor ou igual a 0");
        }

        this.quantidade += quantidade;
        return quantidade;
    }

    public int subtrair(int quantidade) throws ExceptionLavacao {
        if(quantidade > this.quantidade) {
            throw new ExceptionLavacao("Não é possivel subtrair uma pontuação maior que a atual!");
        } else if (quantidade < 0) {
            throw new ExceptionLavacao("Não é possivel subtrair a pontuação com um valor inferior a um!");
        }else  {
            // Senão irá efetuar a operação correta;
            return this.quantidade -= quantidade;
        }
    }
    public int saldo(){
        return this.quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Pontuação: " + this.quantidade;
    }
}
