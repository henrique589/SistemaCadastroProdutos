package com.henrique.cadastroprodutos.exception;

public class ValorNegativoException extends RegraDeNegocioException {

    public ValorNegativoException(String campo) {
        super("O campo '" + campo + "' não pode ser negativo.");
    }
}
