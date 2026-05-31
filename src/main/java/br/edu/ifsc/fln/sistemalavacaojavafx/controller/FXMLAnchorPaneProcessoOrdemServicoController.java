package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Cliente;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ItemOS;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Modelo;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.OrdemServico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private TableColumn<Cliente, String> tableColumnOSCliente;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    @FXML
    void handleBtAlterar(ActionEvent event) {

    }

    @FXML
    void handleBtExcluir(ActionEvent event) {

    }

    @FXML
    void handleBtInserir(ActionEvent event) {

    }

    private boolean showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico ordemServico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Ordem de Servico");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o objeto Ordem de Servico para o controller
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(ordemServico);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}
