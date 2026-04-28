package br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions;

public class ExceptionLavacao extends Exception {

    public ExceptionLavacao(String message) {
        super(message);
    }

    public ExceptionLavacao(String message, Throwable cause) {
        super(message, cause);
    }
}
