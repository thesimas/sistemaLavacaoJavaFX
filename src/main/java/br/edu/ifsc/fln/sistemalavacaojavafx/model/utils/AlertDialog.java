package br.edu.ifsc.fln.sistemalavacaojavafx.model.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertDialog {
    public static boolean confirmarExclusao(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dialogo de Confirmação");
        alert.setHeaderText("Confirmar a exclusão!");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public static void exceptionMessage(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Mensagem de Exceção");
        alert.setHeaderText("Mensagem: " + ex.getMessage());
        try {
            alert.setContentText("Detalhes da exceção: \n" + ex.getCause().toString());
        } finally {
            alert.show();
        }
    }
}
