/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.MarcaDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Marca;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
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

public class FXMLAnchorPaneCadastroMarcaController implements Initializable {

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbMarcaNome;

    @FXML
    private Label lbMarcaId;

    @FXML
    private TableColumn<Marca, String> tableColumnMarcaNome;

    @FXML
    private TableView<Marca> tableViewMarcas;
    
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;
    private final MarcaDAO marcaDAO = new MarcaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewMarca();
        
        tableViewMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewMarcas(newValue));
    }     
    
    public void carregarTableViewMarca() {
        tableColumnMarcaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        try {
            listaMarcas = marcaDAO.listar();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }

        observableListMarcas = FXCollections.observableArrayList(listaMarcas);
        tableViewMarcas.setItems(observableListMarcas);
    }
    
    public void selecionarItemTableViewMarcas(Marca marca) {
        if (marca != null) {
            lbMarcaId.setText(String.valueOf(marca.getId()));
            lbMarcaNome.setText(marca.getNome());
        } else {
            lbMarcaId.setText("");
            lbMarcaNome.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Marca marca = new Marca();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
        if (btConfirmarClicked) {
            try {
                marcaDAO.inserir(marca);
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
            carregarTableViewMarca();
        }

    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroMarcaDialog(marca);
            if (btConfirmarClicked) {
                try {
                    marcaDAO.alterar(marca);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de uma Marca na tabela ao lado!");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Marca marca = tableViewMarcas.getSelectionModel().getSelectedItem();
        if (marca != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir esse marca ?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                    marcaDAO.remover(marca);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de uma Marca na tabela ao lado!");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroMarcaDialog(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroMarcaController.class.getResource("/view/FXMLAnchorPaneCadastroMarcaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Marca");
        Scene scene = new Scene(page);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        dialogStage.setScene(scene);

        dialogStage.setResizable(false);
        dialogStage.sizeToScene();

        //enviando o objeto Marca para o controller
        FXMLAnchorPaneCadastroMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}
