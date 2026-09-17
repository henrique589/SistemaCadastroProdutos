package com.henrique.cadastroprodutos.exception;

public class CampoObrigatorioException extends RegraDeNegocioException {

    public CampoObrigatorioException(String campo) {
        super("O campo '" + campo + "' deve ser preenchido.");
    }
}
