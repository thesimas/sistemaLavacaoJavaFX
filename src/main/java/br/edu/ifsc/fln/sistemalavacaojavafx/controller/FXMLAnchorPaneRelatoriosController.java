package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.report.GeradorRelatorio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLAnchorPaneRelatoriosController {

    @FXML
    private Button btFechar;

    @FXML
    private Button btImprimirClientes;

    @FXML
    private Button btImprimirServico;

    @FXML
    private Button btImprimirVeiculos;

    @FXML
    void handleBtClientes(ActionEvent event) {
        GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
        geradorRelatorio.imprimirRelatorioPadrao("/relatorios/listagemDosClientes.jasper", "Relatório dos Clientes");
    }

    @FXML
    void handleBtFechar(ActionEvent event) {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtServicos(ActionEvent event) {
        GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
        geradorRelatorio.imprimirRelatorioPadrao("/relatorios/listagemDosServicos.jasper", "Relatório dos Serviços");
    }

    @FXML
    void handleBtVeiculos(ActionEvent event) {
        GeradorRelatorio geradorRelatorio = new GeradorRelatorio();
        geradorRelatorio.imprimirRelatorioPadrao("/relatorios/listagemDosVeiculos.jasper", "Relatório dos Veiculos");
    }
}
