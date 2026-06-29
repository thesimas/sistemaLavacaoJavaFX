package br.edu.ifsc.fln.sistemalavacaojavafx.controller;


import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Cliente;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.PessoaFisica;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.PessoaJuridica;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private DatePicker dpDataCadastro;

    @FXML
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;

    @FXML
    private TextField tfClienteCelular;

    @FXML
    private TextField tfClienteCpfCnpj;

    @FXML
    private TextField tfClienteEmail;

    @FXML
    private TextField tfClienteNascimentoInscricaoEstadual;

    @FXML
    private TextField tfClienteNome;

    @FXML
    private TextField tfClientePontos;

    @FXML
    private Label lbCpfCnpj;

    @FXML
    private Label lbNascimentoInscricao;

    @FXML
    private ToggleGroup tgGrupoBotao;

    @FXML
    private HBox hbBotoes;

    @FXML
    private Label tfTitulo;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setCliente(Cliente cliente) {
        if (cliente != null) {
            this.cliente = cliente;
            this.tfClienteNome.setText(cliente.getNome());
            this.tfClienteCelular.setText(cliente.getCelular());
            this.tfClienteEmail.setText(cliente.getEmail());
            this.dpDataCadastro.setValue(cliente.getDataCadastro());
            if (cliente.getPontuacao() != null) {
                this.tfClientePontos.setText(String.valueOf(cliente.getPontuacao().getQuantidade()));
            } else {
                this.tfClientePontos.setText("0");
            }
            if(cliente instanceof PessoaFisica){
                this.rbPessoaFisica.setSelected(true);
                this.hbBotoes.setDisable(true);
                this.lbCpfCnpj.setText("CPF:");
                this.lbNascimentoInscricao.setText("Data de Nascimento:");
                this.tfClienteCpfCnpj.setText(((PessoaFisica)cliente).getCpf());
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.tfClienteNascimentoInscricaoEstadual.setText(((PessoaFisica) cliente).getDataNascimento().format(formatador));
            }else {
                this.rbPessoaJuridica.setSelected(true);
                this.hbBotoes.setDisable(true);
                this.lbCpfCnpj.setText("CNPJ:");
                this.lbNascimentoInscricao.setText("Inscricao Estadual:");
                this.tfClienteCpfCnpj.setText(((PessoaJuridica)cliente).getCnpj());
                this.tfClienteNascimentoInscricaoEstadual.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }
        }
    }

    public Cliente getCliente() {
        return cliente;
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
    void handleRbPessoaFisica(ActionEvent event) {
        if(event.getSource() == rbPessoaFisica){
            this.lbCpfCnpj.setText("CPF:");
            this.lbNascimentoInscricao.setText("Data de Nascimento:");
            this.tfClienteNascimentoInscricaoEstadual.setPromptText("Ex: 10/10/2000");
        }
    }

    @FXML
    void handleRbPessoaJuridica(ActionEvent event) {
        if(event.getSource() == rbPessoaJuridica){
            this.lbCpfCnpj.setText("CNPJ:");
            this.lbNascimentoInscricao.setText("Inscricao Estadual:");
            this.tfClienteNascimentoInscricaoEstadual.setPromptText("");
        }
    }

    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            if (rbPessoaFisica.isSelected()) {
                if (cliente == null) {
                    this.cliente = new PessoaFisica();
                }
                this.cliente.setNome(tfClienteNome.getText());
                this.cliente.setCelular(tfClienteCelular.getText());
                this.cliente.setEmail(tfClienteEmail.getText());
                this.cliente.setDataCadastro(dpDataCadastro.getValue());
                ((PessoaFisica) cliente).setCpf(tfClienteCpfCnpj.getText());

                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                ((PessoaFisica) cliente).setDataNascimento(LocalDate.parse(tfClienteNascimentoInscricaoEstadual.getText(), formatador));
            } else {
                if (cliente == null) {
                    this.cliente = new PessoaJuridica();
                }
                this.cliente.setNome(tfClienteNome.getText());
                this.cliente.setCelular(tfClienteCelular.getText());
                this.cliente.setEmail(tfClienteEmail.getText());
                this.cliente.setDataCadastro(dpDataCadastro.getValue());
                ((PessoaJuridica) cliente).setCnpj(tfClienteCpfCnpj.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfClienteNascimentoInscricaoEstadual.getText());
            }
            this.cliente.getPontuacao().setQuantidade(Integer.parseInt(tfClientePontos.getText()));
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfClienteNome.getText() == null || this.tfClienteNome.getText().length() == 0) {
            errorMessage += "O nome deve ser informada\n";
        }
        if (this.tfClienteCelular == null) {
            errorMessage += "O celular deve ser informado\n";
        }
        if (this.tfClienteEmail == null) {
            errorMessage += "O email deve ser informado\n";
        }
        if (this.tfClientePontos == null) {
            errorMessage += "A pontuação deve ser informada\n";
        }
        if(rbPessoaFisica.isSelected() ) {
            if(tfClienteCpfCnpj.getText() == null || tfClienteCpfCnpj.getText().length() == 0) {
                errorMessage += "O CPF deve ser informado\n";
            }
            if(tfClienteNascimentoInscricaoEstadual.getText() == null){
                errorMessage += "A data de nascimento deve ser informada\n";
            }
        }else {
            if(tfClienteCpfCnpj.getText() == null || tfClienteCpfCnpj.getText().length() == 0) {
                errorMessage += "O CNPJ deve ser informado\n";
            }
            if(tfClienteNascimentoInscricaoEstadual.getText() == null){
                errorMessage += "A Inscrição estadual deve ser informada\n";
            }
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
