package br.edu.ifsc.fln.sistemalavacaojavafx.controller;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ClienteDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.ServicoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.ExceptionLavacao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneProcessoOrdemServicoDialogController implements Initializable {


    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Veiculo> cbPlaca;

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private ComboBox<EStatus> cbStatus;

    @FXML
    private DatePicker dpData;

    @FXML
    private Label lbSubtotal;

    @FXML
    private Label lbValorTotal;


    @FXML
    private Spinner<Double> spDesconto;

    @FXML
    private Spinner<Double> spValorAlterado;

    @FXML
    private TableColumn<ItemOS, String> tableColumnOSCategoria;

    @FXML
    private TableColumn<ItemOS, String> tableColumnOSObservacao;

    @FXML
    private TableColumn<ItemOS, String> tableColumnOSServico;

    @FXML
    private TableColumn<ItemOS, Double> tableColumnOSValor;

    @FXML
    private TableView<ItemOS> tableViewOrdemServicoServicos;

    @FXML
    private TextField tfCategoria;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private TextField tfMarca;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfNumeroOs;

    @FXML
    private TextField tfObservacoesServico;

    @FXML
    private Label tfTitulo;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private OrdemServico ordemServico;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ServicoDAO servicoDAO = new ServicoDAO();
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
                                                                                            // Minimo, maximo, valorInicial, step
        spValorAlterado.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 1.01));
        spDesconto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0));

        cbStatus.setItems(FXCollections.observableArrayList(EStatus.values()));

        cbServico.setItems(FXCollections.observableArrayList(servicoDAO.listar()));
        cbServico.setConverter(new StringConverter<Servico>() {
            @Override
            public String toString(Servico servico) {
                if (servico == null) {
                    return null;
                }
                return servico.getDescricao();
            }
            @Override
            public Servico fromString(String s) {
                return null;
            }
        });

        cbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));
        cbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) {
                    return null;
                }
                return cliente.getNome();
            }
            @Override
            public Cliente fromString(String s) {
                return null;
            }
        });
        cbPlaca.setItems(FXCollections.observableArrayList(veiculoDAO.listar()));
        // Ouvinte que irá listar os veiculos de acordo com o cliente selecionado.
        cbCliente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Limpando os dados do veiculo sempre que o cliente for alterado na seleção do comboBox.
            cbPlaca.getSelectionModel().clearSelection();
            tfCategoria.setText("");
            tfModelo.setText("");
            tfMarca.setText("");
            if (newValue != null) {
                cbPlaca.setDisable(false);
                List<Veiculo> veiculosCliente = veiculoDAO.buscarVeiculoCliente(newValue.getId());
                ObservableList<Veiculo> veiculos = FXCollections.observableArrayList(veiculosCliente);
                cbPlaca.setItems(veiculos);
            }else {
                cbPlaca.setItems(FXCollections.observableArrayList());
            }
        });
        cbPlaca.setConverter(new StringConverter<Veiculo>() {
            @Override
            public String toString(Veiculo veiculo) {
                if (veiculo == null) {
                    return null;
                }
                return veiculo.getPlaca();
            }

            @Override
            public Veiculo fromString(String s) {
                return null;
            }
        });

        tableColumnOSServico.setCellValueFactory(cellData -> {
            ItemOS itemLinha = cellData.getValue();
            return new SimpleObjectProperty<>(itemLinha.getServico().getDescricao());
        });

        tableColumnOSCategoria.setCellValueFactory(cellData -> {
            ItemOS itemLinha = cellData.getValue();
            return new SimpleStringProperty(itemLinha.getServico().getCategoria().name());
        });

        tableColumnOSValor.setCellValueFactory(cellData -> {
            ItemOS itemLinha = cellData.getValue();
            return new SimpleObjectProperty<>(itemLinha.getValorServico());
        });

        tableColumnOSObservacao.setCellValueFactory(cellData -> {
            ItemOS itemLinha = cellData.getValue();
            return new SimpleStringProperty(itemLinha.getObservacoes());
        });

        cbPlaca.setDisable(true);
        tfMarca.setDisable(true);
        tfCategoria.setDisable(true);
        tfModelo.setDisable(true);
    }

    public void setOrdemDeServico(OrdemServico ordemServico) {
        if(ordemServico != null){
            this.ordemServico = ordemServico;
            this.tfNumeroOs.setText(String.valueOf(ordemServico.getNumero()));
            if(ordemServico.getVeiculo() != null){
                this.tfModelo.setText(ordemServico.getVeiculo().getModelo().getDescricao());
                this.tfMarca.setText(ordemServico.getVeiculo().getModelo().getMarca().getNome());
                this.tfCategoria.setText(ordemServico.getVeiculo().getModelo().getCategoria().name());
                this.cbCliente.setValue(ordemServico.getVeiculo().getCliente());
            }
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if(ordemServico.getDataAgendamento() != null){
                this.dpData.setValue(LocalDate.parse(ordemServico.getDataAgendamento().format(formatador)));
            }else {
                this.dpData.setValue(LocalDate.now());
            }
            if(!ordemServico.getItensOS().isEmpty()){
                try {
                    this.lbValorTotal.setText(String.valueOf(ordemServico.getTotal()));
                } catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
                try {
                    this.lbSubtotal.setText(String.valueOf(ordemServico.getTotalServicoSemDesconto()));
                } catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public OrdemServico getOrdemDeServico() {
        return ordemServico;
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
            ordemServico.setNumero(Long.parseLong(tfNumeroOs.getText()));
            ordemServico.setStatus(cbStatus.getSelectionModel().getSelectedItem());
            ordemServico.setVeiculo(cbPlaca.getSelectionModel().getSelectedItem());
            ordemServico.setDataAgendamento(dpData.getValue());
            ordemServico.setTotal(Long.parseLong(lbSubtotal.getText()));

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    void eventoPlaca(ActionEvent event) {
        if(cbPlaca.getSelectionModel().getSelectedItem() == null){
            cbPlaca.setDisable(true);
        }else {
            tfMarca.setText(cbPlaca.getSelectionModel().getSelectedItem().getModelo().getMarca().getNome());
            tfModelo.setText(cbPlaca.getSelectionModel().getSelectedItem().getModelo().getDescricao());
            tfCategoria.setText(cbPlaca.getSelectionModel().getSelectedItem().getModelo().getCategoria().name());
        }
    }

    @FXML
    void handleBtAdicionarServico(ActionEvent event) {
        if(cbPlaca.getSelectionModel().getSelectedItem() != null){
            // Associando o cliente ao veiculo para evitar erros da pontuação.
            cbPlaca.getSelectionModel().getSelectedItem().setCliente(this.cbCliente.getValue());
            this.ordemServico.setVeiculo(cbPlaca.getSelectionModel().getSelectedItem());

            Servico servicoSelecionado = cbServico.getSelectionModel().getSelectedItem();
            String observacao = tfObservacoesServico.getText();
            Double valorAlterado = spValorAlterado.getValue();
            if(servicoSelecionado != null){
                if(valorAlterado > 0 ){
                    try {
                        this.ordemServico.addItemOS(valorAlterado, observacao, servicoSelecionado);
                    } catch (ExceptionLavacao e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    try {
                        this.ordemServico.addItemOS(observacao, servicoSelecionado);
                    } catch (ExceptionLavacao e) {
                        throw new RuntimeException(e);
                    }
                }
                ObservableList<ItemOS> itemOS = FXCollections.observableArrayList(this.ordemServico.getItensOS());
                tableViewOrdemServicoServicos.setItems(itemOS);
                try {
                    lbSubtotal.setText(String.valueOf(ordemServico.getTotalServicoSemDesconto()));
                } catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
                try {
                    lbValorTotal.setText(String.valueOf(ordemServico.getTotal()));
                } catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
                // Limpar os campos assim que o usuario adcionar um serviço.
                cbServico.getSelectionModel().clearSelection();
                cbServico.setPromptText("Selecione o Servico");
                tfObservacoesServico.setText("");
                spValorAlterado.getValueFactory().setValue(null);

            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erro");
                alert.setContentText("Selecione um Servico.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Para adicionar um serviço, o veiculo devera ser selecionado.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleBtRemoverServico(ActionEvent event) {
        if(tableViewOrdemServicoServicos.getSelectionModel().getSelectedItem() != null){
            try {
                this.ordemServico.removeItemOS(tableViewOrdemServicoServicos.getSelectionModel().getSelectedIndex());
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
        }
    }


    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (cbCliente.getSelectionModel() == null) {
            errorMessage += "O cliente deve ser selecionado!\n";
        }
        if (cbPlaca.getSelectionModel() == null) {
            errorMessage += "O veiculo deve ser selecionada!\n";
        }
        if (cbStatus.getSelectionModel() == null) {
            errorMessage += "O status deve ser selecionado!\n";
        }
        if (ordemServico.getItensOS().isEmpty()) {
            errorMessage += "A Ordem de Serviço deve ter ao menos um serviço adcionado!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        }
        else {
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
