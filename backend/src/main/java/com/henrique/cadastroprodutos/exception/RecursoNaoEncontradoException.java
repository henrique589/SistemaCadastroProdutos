package com.henrique.cadastroprodutos.exception;

/** Mapeada para HTTP 404. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado.");
    }
}
