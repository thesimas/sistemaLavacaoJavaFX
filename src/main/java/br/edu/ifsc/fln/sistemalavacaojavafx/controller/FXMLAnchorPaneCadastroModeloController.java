/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ModeloDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Marca;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Modelo;
import javafx.beans.property.SimpleStringProperty;
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
    private Label lbModeloMotorPotencia;

    @FXML
    private Label lbModeloMotorTipoCombustivel;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloCategoria;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloDescricao;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloMarca;

    @FXML
    private TableView<Modelo> tableViewModelos;

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;
    private final ModeloDAO modeloDAO = new ModeloDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewMarca();
        
        tableViewModelos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewModelos(newValue));
    }

    public void carregarTableViewMarca() {

        tableColumnModeloDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnModeloMarca.setCellValueFactory(cellData-> {
            Marca marca = cellData.getValue().getMarca();
            return new SimpleStringProperty(marca.getNome());
        });
        tableColumnModeloCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        
        listaModelos = modeloDAO.listar();
        
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableViewModelos.setItems(observableListModelos);
    }
    
    public void selecionarItemTableViewModelos(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(String.valueOf(modelo.getId()));
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloCategoria.setText(String.valueOf(modelo.getCategoria()));
            lbModeloMarca.setText(String.valueOf(modelo.getMarca().getNome()));
            lbModeloMotorPotencia.setText(String.valueOf(modelo.getMotor().getPotencia()));
            lbModeloMotorTipoCombustivel.setText(String.valueOf(modelo.getMotor().getTipoCombustivel()));
        } else {
            lbModeloId.setText("");
            lbModeloDescricao.setText("");
            lbModeloCategoria.setText("");
            lbModeloMarca.setText("");
            lbModeloMotorPotencia.setText("");
            lbModeloMotorTipoCombustivel.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo(0 , ETipoCombustivel.GASOLINA);
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (btConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewMarca();
        }
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
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
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realemente excluir esse marca ?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                modeloDAO.remover(modelo);
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o objeto Modelo para o controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}
