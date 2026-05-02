package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ECategoria;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private TextField tfDescricao;

    @FXML
    private Spinner<Integer> spPontos;

    @FXML
    private Spinner<Double> spValor;

    @FXML
    private ComboBox<ECategoria> cbCategoria;
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
        cbCategoria.getSelectionModel().select(ECategoria.PADRAO);
        spValor.setEditable(true);
        spPontos.setEditable(true);
    }       

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfDescricao.setText(servico.getDescricao());
        spValor.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, servico.getValor(), 0.01));
        spPontos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, servico.getPontos(), 1));
        cbCategoria.getSelectionModel().select(servico.getCategoria());
    }
    

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfDescricao.getText());
            servico.setValor(spValor.getValue());
            servico.setCategoria(cbCategoria.getValue());
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida\n";
        }
        if (this.spValor.getValue() == null || this.spValor.getValue() <= 0) {
            errorMessage += "Valor deve ser maior que zero\n";
        }
        if (this.cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "A categoria deve ser selecionada\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
