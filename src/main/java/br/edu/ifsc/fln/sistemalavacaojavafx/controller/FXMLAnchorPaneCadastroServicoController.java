package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ECategoria;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoCategoria;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoPontos;

    @FXML
    private Label lbServicoValor;

    @FXML
    private TableColumn<Servico, String> tableColumnServicoDescricao;

    @FXML
    private TableColumn<Servico, Double> tableColumnServicoValor;

    @FXML
    private TableView<Servico> tableViewServicos;

    private List<Servico> listaServicos;
    private ObservableList<Servico> observableListServicos;
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewServicos();
        tableViewServicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewServicos(newValue));
    }

    public void carregarTableViewServicos() {
        tableColumnServicoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tableColumnServicoValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        // Esse bloco de codigo irá formatar a coluna de valores da tabela serviços e adcionar o cifrão.
        tableColumnServicoValor.setCellFactory(tc -> new TableCell<Servico, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                }else {
                    setText(NumberFormat.getCurrencyInstance().format(valor));
                }
            }
        });

        try {
            listaServicos = servicoDAO.listar();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
        observableListServicos = FXCollections.observableArrayList(listaServicos);
        tableViewServicos.setItems(observableListServicos);
    }

    public void selecionarItemTableViewServicos(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(String.valueOf(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoCategoria.setText(String.valueOf(servico.getCategoria()));
            lbServicoValor.setText(NumberFormat.getCurrencyInstance().format(servico.getValor()));
            lbServicoPontos.setText(String.valueOf(servico.getPontos()));

        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoCategoria.setText("");
            lbServicoValor.setText("");
            lbServicoPontos.setText("");
        }
    }

    @FXML
    public void handleBtInserir() throws IOException {
        Servico servico = new Servico();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (btConfirmarClicked) {
            try {
                servicoDAO.inserir(servico);
            } catch (DAOException e) {
                AlertDialog.exceptionMessage(e);
            }
            carregarTableViewServicos();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (btConfirmarClicked) {
                try {
                    servicoDAO.alterar(servico);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewServicos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um Servico na tabela ao lado!");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir esse Serviço?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                    servicoDAO.remover(servico);
                } catch (DAOException e) {
                    AlertDialog.exceptionMessage(e);
                }
                carregarTableViewServicos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção de um Servico na tabela ao lado!");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoController.class.getResource("/view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Serviço");
        Scene scene = new Scene(page);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        dialogStage.setScene(scene);

        dialogStage.setResizable(false);
        dialogStage.sizeToScene();

        //enviando o obejto Servico para o controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}
