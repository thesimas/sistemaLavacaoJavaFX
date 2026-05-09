package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.MarcaDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ECategoria;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Modelo;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Marca;
import javafx.collections.FXCollections;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<ECategoria> cbCategoria;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<ETipoCombustivel> cbTipoCombustivel;

    @FXML
    private Spinner<Integer> spPotencia;

    @FXML
    private TextField tfDescricao;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Modelo modelo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MarcaDAO marcaDAO = new MarcaDAO();
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
        cbTipoCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
        spPotencia.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0));
        cbMarca.setItems(FXCollections.observableArrayList(marcaDAO.listar()));
        cbMarca.setConverter(new StringConverter<Marca>() {
            @Override
            public String toString(Marca marca) {
                if (marca == null) {
                    return "";
                }
                return marca.getNome();
            }
            @Override
            public Marca fromString(String string) {
                return null;
            }
        });
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        this.tfDescricao.setText(modelo.getDescricao());
        this.cbCategoria.getSelectionModel().select(modelo.getCategoria());
        this.cbMarca.getSelectionModel().select(modelo.getMarca());
        this.cbTipoCombustivel.getSelectionModel().select(modelo.getMotor().getTipoCombustivel());
        this.spPotencia.getValueFactory().setValue(modelo.getMotor().getPotencia());
    }

    public Modelo getModelo() {
        return modelo;
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

    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if(validarEntradaDeDados()){
            modelo.setDescricao(tfDescricao.getText());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(spPotencia.getValue());
            modelo.getMotor().setTipoCombustivel(cbTipoCombustivel.getValue());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida\n";
        }
        if (this.spPotencia.getValue() == null || this.spPotencia.getValue() <= 0) {
            errorMessage += "Potência do veículo deve ser maior que zero\n";
        }
        if (this.cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "A categoria deve ser selecionada\n";
        }
        if (this.cbTipoCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "O tipo de combustivel deve ser selecionada\n";
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
