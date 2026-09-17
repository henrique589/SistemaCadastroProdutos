package com.henrique.cadastroprodutos.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DIGITAL")
public class ProdutoDigital extends Produto {

    /** Tamanho do arquivo em megabytes. */
    @Column(name = "tamanho_arquivo_mb")
    private Double tamanhoArquivoMb;

    protected ProdutoDigital() {
    }

    public ProdutoDigital(String nome, BigDecimal preco, Double tamanhoArquivoMb) {
        super(nome, preco);
        setTamanhoArquivoMb(tamanhoArquivoMb);
    }

    @Override
    public TipoProduto getTipo() {
        return TipoProduto.DIGITAL;
    }

    public Double getTamanhoArquivoMb() {
        return tamanhoArquivoMb;
    }

    public void setTamanhoArquivoMb(Double tamanhoArquivoMb) {
        this.tamanhoArquivoMb = naoNegativo("tamanhoArquivoMb", tamanhoArquivoMb);
    }
}
