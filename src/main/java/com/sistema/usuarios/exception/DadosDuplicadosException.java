package com.sistema.usuarios.exception;

// Esta exceção será usada sempre que houver duplicidade de dados únicos
public class DadosDuplicadosException extends RuntimeException {
    public DadosDuplicadosException(String message) {
        super(message);
    }
}