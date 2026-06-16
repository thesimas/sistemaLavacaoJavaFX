package br.edu.ifsc.fln.sistemalavacaojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Servico;
import java.io.IOException;
import java.util.Objects;

public class JavaFXApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApp.class.getResource("/view/FXMLVBoxMainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 630);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/IFSC_logo_vertical.png")));
        stage.setTitle("Sistema Norteador (Lavação) - IFSC Campus Florianópolis");
        scene.getStylesheets().add(getClass().getResource("/css/Style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
