package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private ComboBox<Cor> cbCor;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private ComboBox<Modelo> cbModelo;

    @FXML
    private TextArea taObservacoes;

    @FXML
    private TextField tfPlaca;


    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MarcaDAO marcaDAO = new MarcaDAO();
        CorDAO corDAO = new CorDAO();
        ModeloDAO modeloDAO = new ModeloDAO();
        ClienteDAO clienteDAO = new ClienteDAO();

        cbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));
        cbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                if(cliente == null) {
                    return "";
                }
                return cliente.getNome();
            }
            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
        cbCor.setItems(FXCollections.observableArrayList(corDAO.listar()));
        cbCor.setConverter(new StringConverter<Cor>() {
            @Override
            public String toString(Cor cor) {
                if(cor == null){
                    return "";
                }
                return cor.getNome();
            }

            @Override
            public Cor fromString(String string) {
                return null;
            }
        });
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
        cbMarca.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                ModeloDAO modelDAO = new ModeloDAO();
                List<Modelo> modelosDaMarca = modelDAO.buscarModeloPorMarca(newValue);

                ObservableList<Modelo> modelos = FXCollections.observableArrayList(modelosDaMarca);
                cbModelo.setItems(modelos);
            }else {
                cbModelo.setItems(FXCollections.observableArrayList());
            }
        });
        cbModelo.setItems(FXCollections.observableArrayList(modeloDAO.listar()));
        cbModelo.setConverter(new  StringConverter<Modelo>() {
            @Override
            public String toString(Modelo modelo) {
                if(modelo == null){
                    return "";
                }
                return modelo.getDescricao();
            }
            @Override
            public Modelo fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    void eventoMarca(ActionEvent event) {
        if(cbMarca.getSelectionModel().getSelectedItem() == null){
            cbModelo.setDisable(true);
        }
        cbModelo.setDisable(false);
    }

    public void setVeiculo(Veiculo veiculo) {
        if(veiculo != null){
            this.veiculo = veiculo;
            this.tfPlaca.setText(veiculo.getPlaca());
            this.cbCor.getSelectionModel().select(veiculo.getCor());
            this.cbCliente.getSelectionModel().select(veiculo.getCliente());
            this.taObservacoes.setText(veiculo.getObservacoes());
            if(veiculo.getModelo() != null){
                this.cbModelo.getSelectionModel().select(veiculo.getModelo());
                this.cbMarca.getSelectionModel().select(veiculo.getModelo().getMarca());
            }
        }
    }

    public Veiculo getVeiculo() {
        return veiculo;
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
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());
            veiculo.setModelo(cbModelo.getValue());
            veiculo.setCor(cbCor.getValue());
            veiculo.setObservacoes(taObservacoes.getText());
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfPlaca.getText() == null || this.tfPlaca.getText().length() == 0) {
            errorMessage += "A Placa deve ser informada\n";
        }
        if (this.cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "O cliente deve ser selecionado\n";
        }
        if (this.cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "O modelo deve ser selecionado\n";
        }
        if (this.cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "A marca deve ser selecionada\n";
        }
        if (this.cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "A cor deve ser selecionada\n";
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
