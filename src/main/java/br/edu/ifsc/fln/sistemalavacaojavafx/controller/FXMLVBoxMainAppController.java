/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static java.lang.Integer.parseInt;

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLVBoxMainAppController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String sql = "SELECT pontos FROM parametros_sistema";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            ResultSet resultado = stmt.getResultSet();
            if(resultado.next()){
                Servico.setPontos(resultado.getInt("pontos"));
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void handleMenuItemCadastroProduto() throws IOException {
        //TODO not implemented yet
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
    public void handleMenuItemConfigPontos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("view/FXMLAnchorPane"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemConfigValores() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("view/FXMLAnchorPane"));
        anchorPane.getChildren().setAll(a);
    }


    
}
