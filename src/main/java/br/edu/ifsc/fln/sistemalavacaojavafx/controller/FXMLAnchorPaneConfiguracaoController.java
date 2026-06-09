package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ConfiguracaoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Configuracao;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ECategoria;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FXMLAnchorPaneConfiguracaoController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private AnchorPane AnchorPaneConfiguracoes;

    @FXML
    private Spinner<Integer> spPontos;

    @FXML
    private Spinner<Double> spPorcentagemGrande;

    @FXML
    private Spinner<Double> spPorcentagemMedio;

    @FXML
    private Spinner<Double> spPorcentagemMoto;

    @FXML
    private Spinner<Double> spPorcentagemPequeno;

    private final ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
    private Configuracao configuracaoAtual = new Configuracao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //                                                                  Valor Mínimo, Valor Máximo, Valor Inicial, Passo (Step)
        spPontos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 0, 1));
        spPorcentagemPequeno.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 2.0, 1.0, 0.05));
        spPorcentagemMedio.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 2.0, 1.0, 0.05));
        spPorcentagemGrande.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 2.0, 1.0, 0.05));
        spPorcentagemMoto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 2.0, 1.0, 0.05));

        try {
            configuracaoAtual = configuracaoDAO.buscar(1);
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }

        HashMap mapaPorcentagens = configuracaoAtual.getPorcentagens();

        spPontos.getValueFactory().setValue(configuracaoAtual.getPontos());
        spPorcentagemPequeno.getValueFactory().setValue((Double) mapaPorcentagens.get(ECategoria.PEQUENO));
        spPorcentagemMedio.getValueFactory().setValue((Double) mapaPorcentagens.get(ECategoria.MEDIO));
        spPorcentagemGrande.getValueFactory().setValue((Double) mapaPorcentagens.get(ECategoria.GRANDE));
        spPorcentagemMoto.getValueFactory().setValue((Double) mapaPorcentagens.get(ECategoria.MOTO));

        informativo();

    }


    @FXML
    public void handleBtConfirmar() {

        HashMap mapaPorcentagensAlterada = new HashMap();
        mapaPorcentagensAlterada.put(ECategoria.PEQUENO, Double.parseDouble(String.valueOf(spPorcentagemPequeno.getValue())));
        mapaPorcentagensAlterada.put(ECategoria.MEDIO, Double.parseDouble(String.valueOf(spPorcentagemMedio.getValue())));
        mapaPorcentagensAlterada.put(ECategoria.GRANDE, Double.parseDouble(String.valueOf(spPorcentagemGrande.getValue())));
        mapaPorcentagensAlterada.put(ECategoria.MOTO, Double.parseDouble(String.valueOf(spPorcentagemMoto.getValue())));
        configuracaoAtual.setPorcentagens(mapaPorcentagensAlterada);
        configuracaoAtual.setPontos(Integer.parseInt(spPontos.getValue().toString()));

        try {
            configuracaoDAO.alterar(configuracaoAtual);
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
        Servico.setPontos(configuracaoAtual.getPontos());


        AnchorPane painelPrincipal = (AnchorPane) AnchorPaneConfiguracoes.getParent();
        painelPrincipal.getChildren().clear();
    }

    @FXML
    public void handleBtCancelar() {
        AnchorPane painelPrincipal = (AnchorPane) AnchorPaneConfiguracoes.getParent();
        painelPrincipal.getChildren().clear();
    }

    public void informativo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instruções de Configuração");
        alert.setHeaderText("Como funcionam os multiplicadores de valor?");

        String instrucoes = "Os valores definidos abaixo funcionam como fatores multiplicadores sobre o preço base de cada serviço:\n\n"
                + "• Valor 1.0: Mantém o valor original do serviço (100%).\n"
                + "• Valores maiores que 1.0: Aplicam um acréscimo. (Ex: 1.20 aumenta o valor em 20%).\n"
                + "• Valores menores que 1.0: Aplicam um desconto. (Ex: 0.90 concede 10% de desconto).\n"
                + "Ajuste as setas ou digite o valor decimal desejado para cada categoria.";

        alert.setContentText(instrucoes);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CANCEL) {
            AnchorPane painelPrincipal = (AnchorPane) AnchorPaneConfiguracoes.getParent();
            painelPrincipal.getChildren().clear();
        }
    }

}
