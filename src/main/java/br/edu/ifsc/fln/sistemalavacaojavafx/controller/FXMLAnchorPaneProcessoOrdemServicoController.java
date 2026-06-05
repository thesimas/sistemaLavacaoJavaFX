package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.ExceptionLavacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneProcessoOrdemServicoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbData;

    @FXML
    private Label lbOrdemServicoDesconto;

    @FXML
    private Label lbOrdemServicoNumero;

    @FXML
    private Label lbOrdemServicoStatus;

    @FXML
    private Label lbOrdemServicoValorTotal;

    @FXML
    private Label lbNomeCliente;

    @FXML
    private Label lbVeiculoModeloDescricao;

    @FXML
    private Label lbVeiculoPlaca;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnOSCliente;

    @FXML
    private TableColumn<ItemOS, String> tableColumnOSItemOsServico;

    @FXML
    private TableColumn<ItemOS, String> tableColumnOSItemOsServicoValor;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnOSnumero;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnStatus;

    @FXML
    private TableView<ItemOS> tableViewOrdemDeServicoItemOs;

    @FXML
    private TableView<OrdemServico> tableViewOrdensDeServicos;

    private List<OrdemServico> ordensDeServicos;
    private ObservableList<OrdemServico> observableListOrdensDeServicos;
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewOrdensDeServico();
        tableViewOrdensDeServicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewOrdensDeServico(newValue)
        );
    }

    public void carregarTableViewOrdensDeServico() {
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableColumnOSCliente.setCellValueFactory(celldata -> {
            OrdemServico ordemServico = celldata.getValue();
            return new SimpleStringProperty(ordemServico.getVeiculo().getCliente().getNome());
        });

        tableColumnOSnumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        tableColumnOSItemOsServicoValor.setCellValueFactory(celldata -> {
            ItemOS itemOS = celldata.getValue();
            String valor = "R$: " + itemOS.getValorServico();
            return new SimpleStringProperty(valor);
        });

        tableColumnOSItemOsServico.setCellValueFactory(celldata -> {
            ItemOS itemOS = celldata.getValue();
            return new SimpleStringProperty(itemOS.getServico().getDescricao());
        });

        ordensDeServicos = ordemServicoDAO.listar();
        observableListOrdensDeServicos = FXCollections.observableArrayList(ordensDeServicos);
        tableViewOrdensDeServicos.setItems(observableListOrdensDeServicos);
    }

    public void selecionarItemTableViewOrdensDeServico(OrdemServico ordemServico) {
        if(ordemServico != null) {
            if(ordemServico.getItensOS().isEmpty()){
                ordemServicoDAO.carregarItemOS(ordemServico);
            }

            lbNomeCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
            lbVeiculoModeloDescricao.setText(ordemServico.getVeiculo().getModelo().getDescricao());
            lbVeiculoPlaca.setText(ordemServico.getVeiculo().getPlaca());

            lbOrdemServicoNumero.setText(String.valueOf(ordemServico.getNumero()));
            lbOrdemServicoStatus.setText(String.valueOf(ordemServico.getStatus()));
            lbOrdemServicoDesconto.setText(String.valueOf(ordemServico.getDesconto()));
            lbData.setText(String.valueOf(ordemServico.getDataAgendamento()));
            try {
                lbOrdemServicoValorTotal.setText(String.valueOf(ordemServico.getTotal()));
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
            // Preenchendo a tabela secundaria, quando o usuario clicar em uma ordem de serviço.
            ObservableList<ItemOS>  observableListItemOS = FXCollections.observableArrayList(ordemServico.getItensOS());
            tableViewOrdemDeServicoItemOs.setItems(observableListItemOS);

        }else {
            lbOrdemServicoNumero.setText("");
            lbOrdemServicoStatus.setText("");
            lbOrdemServicoDesconto.setText("");
            lbOrdemServicoValorTotal.setText("");
            lbData.setText("");
            tableViewOrdemDeServicoItemOs.setItems(null);
        }
    }

    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException {
        OrdemServico ordemServicoSelecionada = tableViewOrdensDeServicos.getSelectionModel().getSelectedItem();
        if(ordemServicoSelecionada != null) {
            OrdemServico ordemServicoAtualizada = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServicoSelecionada);
            if (ordemServicoAtualizada != null) {
                ordemServicoDAO.alterar(ordemServicoAtualizada);
                carregarTableViewOrdensDeServico();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Esta operação requer a seleção de uma Ordem de Serviço na tabela ao lado!");
                alert.show();
            }
        }
    }

    @FXML
    void handleBtExcluir(ActionEvent event) throws IOException {
        OrdemServico ordemServico = tableViewOrdensDeServicos.getSelectionModel().getSelectedItem();
        if(ordemServico != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Deseja realmente excluir esse cliente?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                ordemServicoDAO.excluir(ordemServico);
                carregarTableViewOrdensDeServico();
                System.out.println("Ordem de Serviço excluida com sucesso");
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Está operação requer a seleção de uma Ordem de Serviço na tabela ao lado!");
            alert.show();
        }
    }

    @FXML
    void handleBtInserir(ActionEvent event) throws IOException {
        OrdemServico ordemServico = new OrdemServico();
        OrdemServico ordemServicoNova = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServico);
        if (ordemServicoNova != null) {
            ordemServicoDAO.inserir(ordemServicoNova);
            carregarTableViewOrdensDeServico();
        }
    }

    private OrdemServico showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico ordemServico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneProcessoOrdemDeServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Ordem de Servico");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o objeto Ordem de Servico para o controller
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemDeServico(ordemServico);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        if(controller.isBtConfirmarClicked()){
            return controller.getOrdemDeServico();
        }

        return null;
    }
}
