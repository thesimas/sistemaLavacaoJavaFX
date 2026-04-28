package br.edu.ifsc.fln.sistemalavacaojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;

import java.io.IOException;

public class JavaFXApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApp.class.getResource("/view/FXMLVBoxMainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Servico.setPontos(20);
        stage.setTitle("Sistema Lavação!");
        stage.setScene(scene);
        stage.show();
    }
}
