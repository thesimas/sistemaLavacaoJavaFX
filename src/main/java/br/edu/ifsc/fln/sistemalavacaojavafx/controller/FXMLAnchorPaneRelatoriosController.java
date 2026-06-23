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

    private GeradorRelatorio geradorRelatorio = new GeradorRelatorio();

    @FXML
    void handleBtClientes(ActionEvent event) {
        geradorRelatorio.imprimirRelatorio("/relatorios/listagemDosClientes.jasper", "Relatório dos Clientes");
    }

    @FXML
    void handleBtFechar(ActionEvent event) {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtServicos(ActionEvent event) {
        geradorRelatorio.imprimirRelatorio("/relatorios/listagemDosServicos.jasper", "Relatório dos Serviços");
    }

    @FXML
    void handleBtVeiculos(ActionEvent event) {
        geradorRelatorio.imprimirRelatorio("/relatorios/listagemDosVeiculos.jasper", "Relatório dos Veiculos");
    }
}
