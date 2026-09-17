package com.henrique.cadastroprodutos.exception;

/**
 * Base para violações de regras de negócio do domínio.
 * É mapeada para HTTP 400 pelo {@code ApiExceptionHandler}.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
