module br.edu.ifsc.fln.sistemalavacaojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jshell;


    opens br.edu.ifsc.fln.sistemalavacaojavafx to javafx.fxml;
    exports br.edu.ifsc.fln.sistemalavacaojavafx;

    opens br.edu.ifsc.fln.sistemalavacaojavafx.controller to javafx.fxml;
    opens br.edu.ifsc.fln.sistemalavacaojavafx.model.domain to javafx.base;
}