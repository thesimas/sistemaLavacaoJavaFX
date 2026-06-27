package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ConfiguracaoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Configuracao;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
        Configuracao configuracaoAtual = new Configuracao();
        try {
            configuracaoAtual = configuracaoDAO.buscar(1);
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }
        if (configuracaoAtual != null) {
            Servico.setPontos(configuracaoAtual.getPontos());
        }
    }

    @FXML
    public void handleMenuItemCadastroCor() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroCor.fxml"));

        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroMarca() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroMarca.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroServico() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroServico.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroModelo() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroModelo.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroVeiculo() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroVeiculo.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroCliente() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroCliente.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemProcessoOrdemDeServico() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneProcessoOrdemDeSevico.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemGraficosVendasPorMes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLAnchorPaneGraficoVendasPorMes.fxml"));
        Parent root = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Gráfico de Vendas por Mês");

        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        dialogStage.showAndWait();
    }

    @FXML
    public void handleMenuItemRelatorios() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLAnchorPaneRelatorios.fxml"));
        Parent root = (Parent) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Relatórios do Sistema");

        // Bloqueia a tela principal enquanto as configurações estiverem aberta
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        dialogStage.showAndWait();
    }

    @FXML
    public void handleMenuItemConfigDefinir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLAnchorPaneConfiguracao.fxml"));
        Parent root = (Parent) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configurações do Sistema");

        // Bloqueia a tela principal enquanto as configurações estiverem aberta
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        dialogStage.showAndWait();
    }
}
