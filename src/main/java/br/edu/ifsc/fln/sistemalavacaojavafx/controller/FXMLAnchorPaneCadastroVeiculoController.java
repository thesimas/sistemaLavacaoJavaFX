/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
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
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

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
    private Label lbModeloMarca;

    @FXML
    private Label lbMotorPotencia;

    @FXML
    private Label lbMotorTipoCombustivel;

    @FXML
    private Label lbVeiculoCliente;

    @FXML
    private Label lbVeiculoCor;

    @FXML
    private Label lbVeiculoID;

    @FXML
    private Label lbVeiculoObservacoes;

    @FXML
    private Label lbVeiculoPlaca;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculoCor;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculoMarca;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculoModelo;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculoPlaca;

    @FXML
    private TableView<Veiculo> tableViewVeiculos;

    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewVeiculos();
        
        tableViewVeiculos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewVeiculos(newValue));
    }

    public void carregarTableViewVeiculos() {
        tableColumnVeiculoCor.setCellValueFactory(cellData -> {
            Cor cor = cellData.getValue().getCor();
            return new SimpleStringProperty(cor.getNome());
        });

        tableColumnVeiculoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tableColumnVeiculoMarca.setCellValueFactory(cellData-> {
            Marca marca = cellData.getValue().getModelo().getMarca();
            return new SimpleStringProperty(marca.getNome());
        });

        tableColumnVeiculoModelo.setCellValueFactory(cellData -> {
            Modelo modelo = cellData.getValue().getModelo();
            return new SimpleStringProperty(modelo.getDescricao());
        });

        try {
            listaVeiculos = veiculoDAO.listar();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }

        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableViewVeiculos.setItems(observableListVeiculos);
    }
    
    public void selecionarItemTableViewVeiculos(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoID.setText(String.valueOf(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            lbVeiculoCor.setText(veiculo.getCor().getNome());
            lbVeiculoCliente.setText(veiculo.getCliente().getNome());
            lbVeiculoObservacoes.setText(veiculo.getObservacoes());
            lbModeloCategoria.setText(veiculo.getModelo().getDescricao());
            lbModeloDescricao.setText(veiculo.getModelo().getDescricao());
            lbModeloMarca.setText(veiculo.getModelo().getMarca().getNome());
            lbMotorPotencia.setText(String.valueOf(veiculo.getModelo().getMotor().getPotencia()));
            lbMotorTipoCombustivel.setText(String.valueOf(veiculo.getModelo().getMotor().getTipoCombustivel()));
        } else {
            lbVeiculoID.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoCliente.setText("");
            lbVeiculoObservacoes.setText("");
            lbModeloDescricao.setText("");
            lbModeloCategoria.setText("");
            lbModeloMarca.setText("");
            lbMotorPotencia.setText("");
            lbMotorTipoCombustivel.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (btConfirmarClicked) {
            try {
                veiculoDAO.inserir(veiculo);
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
            carregarTableViewVeiculos();
        }
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                try {
                    veiculoDAO.alterar(veiculo);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewVeiculos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um Veiculo na tabela ao lado!");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir esse Veículo?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                try {
                    veiculoDAO.remover(veiculo);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewVeiculos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um Veiculo na tabela ao lado!");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("/view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veiculo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o objeto Veiculo para o controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}
