package br.edu.ifsc.fln.sistemalavacaojavafx.model.report;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;

public class Relatorio {

    /* Associação por dependência se encontra aqui, a classe não tem atributo nenhum, mas apenas métodos
    * Ela usa o objeto da classe que está associada como argurmento dos seus métodos
    * Portanto é uma relação fraca, que só irá existir quando houver a chamada do metodo*/

    public String imprimir(Cliente cliente){
        StringBuilder dados = new StringBuilder();
        dados.append("----Informações do Cliente----\n");
        dados.append(cliente.getDados());
        // Dessa forma o objeto passado no parametro irá chamar o getDados e
        // puxara os dados corretamente daquele objeto, garantindo o polimorfismo.

        int x = 1;
        dados.append("--------Lista de Veiculos-------\n");
        for(Veiculo veiculo : cliente.getListaDeVeiculos()){
            dados.append(x + "° Veículo: " + veiculo.getModelo().getDescricao() + "\n");
            x ++;
        }
        dados.append("Quantidade de Pontos: " + cliente.getPontuacao().getQuantidade() + "\n");

        return dados.toString();
    }

    public String imprimir(Veiculo veiculo){
        StringBuilder dados = new StringBuilder();
        dados.append("----Informações do Veículo----\n");
        dados.append(veiculo.getDados());

        return  dados.toString();
    }
}
