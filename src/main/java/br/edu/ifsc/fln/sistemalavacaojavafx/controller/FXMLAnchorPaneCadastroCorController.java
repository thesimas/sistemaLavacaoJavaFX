package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.CorDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Cor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
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

public class FXMLAnchorPaneCadastroCorController implements Initializable {

    @FXML
    private Button btnAlterar;

    @FXML
    private Button btExcluir;
    
    @FXML
    private Button btInserir;

    @FXML
    private Label lbCorNome;

    @FXML
    private Label lbCorId;

    @FXML
    private TableColumn<Cor, String> tableColumnCorNome;

    @FXML
    private TableView<Cor> tableViewCores;
    
    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores;
    private final CorDAO corDAO = new CorDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewCor();
        
        tableViewCores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCores(newValue));
    }     
    
    public void carregarTableViewCor() {
        tableColumnCorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        listaCores = corDAO.listar();
        
        observableListCores = FXCollections.observableArrayList(listaCores);
        tableViewCores.setItems(observableListCores);
    }
    
    public void selecionarItemTableViewCores(Cor cor) {
        if (cor != null) {
            lbCorId.setText(String.valueOf(cor.getId()));
            lbCorNome.setText(cor.getNome());
        } else {
            lbCorId.setText("");
            lbCorNome.setText("");
        }
    }
    
    @FXML
    public void handleBtInserir() throws IOException {
        Cor cor = new Cor();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
        if (btConfirmarClicked) {
            corDAO.inserir(cor);
            carregarTableViewCor();
        }

    }
    
    @FXML 
    public void handleBtAlterar() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
            if (btConfirmarClicked) {
                corDAO.alterar(cor);
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Cor cor = tableViewCores.getSelectionModel().getSelectedItem();
        if (cor != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir essa Cor?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK) {
                corDAO.remover(cor);
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroCorDialog(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCorController.class.getResource("/view/FXMLAnchorPaneCadastroCorDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cor para o controller
        FXMLAnchorPaneCadastroCorDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
    
}
