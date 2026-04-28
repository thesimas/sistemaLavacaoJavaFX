/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ModeloDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Modelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbModeloCategoria;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbModeloMarca;

    @FXML
    private Label lbMotorPotencia;

    @FXML
    private Label lbMotorTipoCombustivel;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloDescricao;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloMarca;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloCategoria;

    @FXML
    private TableView<Modelo> tableViewMarcas;

    private List<Modelo> listaMarcas;
    private ObservableList<Modelo> observableListMarcas;
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        carregarTableViewMarca();
        
        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarcas(newValue));
    }     
    
    public void carregarTableViewMarca() {
        tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        listaMarcas = modeloDAO.listar();
        
        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        tableViewMarcas.setItems(observableListMarcas);
    }
    
    public void selecionarItemTableViewMarcas(Modelo modelo) {
        if (modelo != null) {
            lbMarcaId.setText(String.valueOf(modelo.getId()));
            lbMarcaNome.setText(modelo.getNome());
        } else {
            lbMarcaId.setText("");
            lbMarcaNome.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(modelo);
        if (btConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewMarca();
        }

    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(modelo);
            if (btConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Modelo modelo = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableViewMarca();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroMarcaDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneCadastroMarcaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto Modelo para o controller
        FXMLAnchorPaneCadastroMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(modelo);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}
