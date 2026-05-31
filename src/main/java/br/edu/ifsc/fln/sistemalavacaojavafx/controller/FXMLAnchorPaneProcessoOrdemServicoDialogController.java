package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FXMLAnchorPaneProcessoOrdemServicoDialogController {

    @FXML
    private Button btAdicionarObservacao;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<?> cbPlaca;

    @FXML
    private ComboBox<?> cbServico;

    @FXML
    private ComboBox<?> cbStatus;

    @FXML
    private DatePicker dpData;

    @FXML
    private Button handleBtRemoverServico;

    @FXML
    private Label lbSubtotal;

    @FXML
    private Label lbValorTotal;

    @FXML
    private TableColumn<?, ?> tableColumnOSCategoria;

    @FXML
    private TableColumn<?, ?> tableColumnOSObservacao;

    @FXML
    private TableColumn<?, ?> tableColumnOSServico;

    @FXML
    private TableColumn<?, ?> tableColumnOSValor;

    @FXML
    private TableView<?> tableViewOrdemServicoServicos;

    @FXML
    private TextField tfCategoria;

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfDesconto;

    @FXML
    private TextField tfMarca;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfObservacoesServico;

    @FXML
    private Label tfTitulo;

    @FXML
    void handleBtAdcionar(ActionEvent event) {

    }

    @FXML
    void handleBtCancelar(ActionEvent event) {

    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {

    }

}
