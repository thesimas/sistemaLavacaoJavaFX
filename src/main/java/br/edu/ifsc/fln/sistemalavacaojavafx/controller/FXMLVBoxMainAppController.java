package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ConfiguracaoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Configuracao;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imagemFundo;

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
        //TODO not implemented yet
    }

    @FXML
    public void handleMenuItemRelatorioEstoqueProdutos() throws IOException {
        //TODO not implemented yet
    }

    @FXML
    public void handleMenuItemConfigDefinir() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneConfiguracao.fxml"));
        AnchorPane.setTopAnchor(a, 0.0);
        AnchorPane.setBottomAnchor(a, 0.0);
        AnchorPane.setLeftAnchor(a, 0.0);
        AnchorPane.setRightAnchor(a, 0.0);
        anchorPane.getChildren().setAll(a);
    }
}
