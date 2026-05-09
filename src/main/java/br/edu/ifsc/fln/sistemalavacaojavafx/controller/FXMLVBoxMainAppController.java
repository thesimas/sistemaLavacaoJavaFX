package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ConfiguracaoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Configuracao;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();
        Configuracao configuracaoAtual = new Configuracao();
        configuracaoAtual = configuracaoDAO.buscar(1);
        if (configuracaoAtual != null) {
            Servico.setPontos(configuracaoAtual.getPontos());
        }
    }
    
    @FXML
    public void handleMenuItemCadastroCor() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastroMarca() throws IOException {
                AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroMarca.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroServico() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroModelo() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroModelo.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroVeiculo() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroVeiculo.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCadastroCliente() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneCadastroCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemProcessoEstoque() throws IOException {
        //TODO not implemented yet
    }

    @FXML
    public void handleMenuItemProcessoVenda() throws IOException {
        //TODO not implemented yet
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
        anchorPane.getChildren().setAll(a);
    }
}
