package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.OrdemServico;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLAnchorPaneGraficoFaturamentoMensalController implements Initializable {

    @FXML
    private Button btFechar;
    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private LineChart<String, Double> lineChartFaturamento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
        Map<String, Double> dados = new LinkedHashMap<>();

        try {
            dados = ordemServicoDAO.listarFaturamentoMensal();
        } catch (DAOException e) {
            AlertDialog.exceptionMessage(e);
        }

        XYChart.Series<String, Double> linhas = new XYChart.Series<>();
        linhas.setName("Receita Total R$: ");
        for(Map.Entry<String, Double> dado : dados.entrySet()){
            linhas.getData().add(new XYChart.Data(dado.getKey(), dado.getValue()));
        }

        lineChartFaturamento.getData().add(linhas);
        lineChartFaturamento.setTitle("Faturamento Mensal");
    }

    @FXML
    void handleBtFechar(ActionEvent event) {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }

}
