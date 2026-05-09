/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ClienteDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteDocumento;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteLegenda;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClientePontos;

    @FXML
    private Label lbCnpj;

    @FXML
    private Label lbInscricaoEstadual;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteDocumento;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteTipo;

    @FXML
    private TableView<Cliente> tableViewClientes;

    @FXML
    private TableColumn<Veiculo, String> tableColumnClienteVeiculoModelo;

    @FXML
    private TableColumn<Veiculo, String> tableColumnClienteVeiculoPlaca;

    @FXML
    private TableView<Veiculo> tableViewClientesVeiculos;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewClientes();
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));

    }

    public void carregarTableViewClientes() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableColumnClienteTipo.setCellValueFactory(celldata -> {
            Cliente cliente = celldata.getValue();
            if(cliente instanceof PessoaFisica){
                return new SimpleStringProperty(((PessoaFisica) cliente).getTipo());
            }else {
                return new SimpleStringProperty(((PessoaJuridica) cliente).getTipo());
            }
        });

        tableColumnClienteDocumento.setCellValueFactory(celldata -> {
            Cliente cliente = celldata.getValue();
            if (cliente instanceof PessoaJuridica){
                return new SimpleStringProperty(((PessoaJuridica)cliente).getCnpj());
            }else {
                return new SimpleStringProperty(((PessoaFisica)cliente).getCpf());
            }
        });

        tableColumnClienteVeiculoModelo.setCellValueFactory(celldata -> {
            Veiculo veiculo = celldata.getValue();
            return new SimpleStringProperty(veiculo.getModelo().getDescricao());
        });

        tableColumnClienteVeiculoPlaca.setCellValueFactory(celdata -> {
            Veiculo veiculo = celdata.getValue();
            return new SimpleStringProperty(veiculo.getPlaca());
        });

        listaClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId()));
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(String.valueOf(cliente.getDataCadastro()));

            if (cliente instanceof PessoaFisica) {
                lbCnpj.setText("CPF:");
                lbInscricaoEstadual.setText("Data de Nascimento:");
                lbClienteDocumento.setText(((PessoaFisica) cliente).getCpf());
                lbClienteLegenda.setText(String.valueOf(((PessoaFisica) cliente).getDataNascimento()));
            }else {
                lbCnpj.setText("CNPJ:");
                lbInscricaoEstadual.setText("Incrisção Estadual:");
                lbClienteDocumento.setText(((PessoaJuridica)cliente).getCnpj());
                lbClienteLegenda.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }

            lbClientePontos.setText(String.valueOf(cliente.getPontuacao().getQuantidade()));
        } else {
            lbClienteId.setText("");
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbClienteDocumento.setText("");
            lbClienteLegenda.setText("");
            lbClientePontos.setText("");
        }

        List<Veiculo> veiculosCliente = cliente.getListaDeVeiculos();
        if (veiculosCliente != null) {
            ObservableList<Veiculo> veiculos = FXCollections.observableArrayList(veiculosCliente);
            tableViewClientesVeiculos.setItems(veiculos);
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Cliente cliente = null;
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
        if (btConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTableViewClientes();
        }
    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewClientes();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir esse cliente?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                clienteDAO.remover(cliente);
                carregarTableViewClientes();
                System.out.println("Cliente excluido com sucesso");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("/view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        //enviando o objeto Cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}
