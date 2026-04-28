package br.edu.ifsc.fln.sistemalavacaojavafx.model.report;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ImpressaoOS {
    public static String imprimirOS(OrdemServico ordem) throws ExceptionLavacao {

        StringBuilder dados = new StringBuilder();
        dados.append("\n============== Ordem De Serviço ===================\n");
        // Primeira linha:
        dados.append("Número: " +ordem.getNumero());dados.append("\t\t"+ "Dia: " + ordem.getDataAgendamento());
        dados.append("\t\t " + "Status: " +ordem.getStatus() + "\n");
        // Segunda linha:
        dados.append("Cliente: " + ordem.getVeiculo().getCliente().getNome() + "\n");
        // Terceira linha:
        dados.append("Veículo: " + ordem.getVeiculo().getPlaca() + "\t\t\t\t" + "Modelo: " + ordem.getVeiculo().getModelo().getDescricao() + "-" + ordem.getVeiculo().getModelo().getCategoria() + "\n");
        // Quarta linha:
        dados.append("Marca: " + ordem.getVeiculo().getModelo().getMarca().getNome() + "\t\t\t\t" + "Cor: \t" + ordem.getVeiculo().getCor().getNome() + "\n");
        // Separador de linha:
        dados.append("===================================================\n");
        // Quinta linha:
        dados.append("ITEM\tDESCRIÇÃO\t\t\t\t\t\t\t" + "VALOR\n");
        // Separador de linha:
        dados.append("===================================================\n");
        // Sexta linha:
        Double valorOS = 0.0;
        for(int x = 0; x < ordem.getItensOS().size(); x++){
            ItemOS itemOS = ordem.getItensOS().get(x);

            dados.append((x +1) + "°\t\t" + itemOS.getServico().getDescricao());
            dados.append("\t\t\t\t\t\t" + formatandoValores(itemOS.getValorServico()));
            valorOS += itemOS.getValorServico();
            dados.append("\n");
        }
        //Setima linha:
        dados.append("===================================================\n");

        dados.append("SUBTOTAL \t\t\t\t\t\t\t\t\t" + formatandoValores(valorOS) + "\n");
        // oitava linha:
        dados.append("DESCONTO (" + ordem.getDesconto() + "%) \t\t\t\t\t\t\t" + formatandoValores(valorOS - ordem.calcularServico()) );
        // nona linha:
        dados.append("\n===================================================");
        try {
            dados.append("\nTOTAL \t\t\t\t\t\t\t\t\t\t" + formatandoValores(ordem.calcularServico()) + "\n");
        }catch(ExceptionLavacao e){
            throw new ExceptionLavacao("Erro ao calcular o serviço: " + e.getMessage());
        }
        dados.append("===================================================\n");

        return dados.toString();
    }

    private static String formatandoValores(Double valor){
        DecimalFormat formato = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        String valorFormatado = formato.format(valor);
        return valorFormatado;
    }
}
