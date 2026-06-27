package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLAnchorPaneGraficoServicosMaisContratadosController implements Initializable {
    @FXML
    private Button btFechar;

    @FXML
    private PieChart pieChartServicos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
        Map<String, Integer> dados = new LinkedHashMap<>();
        try {
            dados =  ordemServicoDAO.listarServicosMaisContratados();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for(Map.Entry<String, Integer> dado : dados.entrySet()){
            String legenda =  dado.getKey() + " - " + dado.getValue();
            pieChartData.add(new PieChart.Data(legenda, dado.getValue()));
        }
        pieChartServicos.setData(pieChartData);
        pieChartServicos.setTitle("Servicos Mais de Contratados");
    }

    @FXML
    public void handleBtFechar() {
        // Pega a janela atual a partir do botão e fecha
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }
}
