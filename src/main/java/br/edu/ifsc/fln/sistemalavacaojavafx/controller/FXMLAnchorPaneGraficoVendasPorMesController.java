package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.OrdemServicoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLAnchorPaneGraficoVendasPorMesController implements Initializable {

    @FXML
    private Button btFechar;
    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private CategoryAxis categoryAxis;

    private ObservableList<String> observableListMeses = FXCollections.observableArrayList();
    OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //obtem um array com nomes dos meses
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        //converte o array em uma lista e adiciona na observableList
        observableListMeses.addAll(Arrays.asList(meses));
        //Associa os nomes do mês como categorias para o eixo horizontal
        categoryAxis.setCategories(observableListMeses);

        //prepara os dados para o eixo vertical
        Map<Integer, ArrayList> dados = ordemServicoDAO.listarQuantidadeVendasPorMes();
        for (Map.Entry<Integer, ArrayList> dadosItem : dados.entrySet()) {
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Quantidade de Ordem de Serviços Finalizadas");
            for (int i = 0; i < dadosItem.getValue().size(); i += 2) {
                String mes;
                Integer quantidade;
                mes = retornaNomeMes((int) dadosItem.getValue().get(i));
                quantidade = (Integer) dadosItem.getValue().get(i + 1);
                series.getData().add(new XYChart.Data<>(mes, quantidade));
            }
            barChart.getData().add(series);
        }
    }

    @FXML
    public void handleBtFechar() {
        // Pega a janela atual a partir do botão e fecha
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }

    private String retornaNomeMes(int mes) {
        switch (mes) {
            case 1:
                return "Jan";
            case 2:
                return "Fev";
            case 3:
                return "Mar";
            case 4:
                return "Abr";
            case 5:
                return "Mai";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Ago";
            case 9:
                return "Set";
            case 10:
                return "Out";
            case 11:
                return "Nov";
            case 12:
                return "Dez";
            default:
                return null;
        }
    }
}