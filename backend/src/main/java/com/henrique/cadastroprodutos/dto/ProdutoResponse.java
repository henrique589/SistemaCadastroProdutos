package com.henrique.cadastroprodutos.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.henrique.cadastroprodutos.domain.Produto;
import com.henrique.cadastroprodutos.domain.ProdutoDigital;
import com.henrique.cadastroprodutos.domain.ProdutoFisico;
import com.henrique.cadastroprodutos.domain.TipoProduto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoResponse(
        Long id,
        TipoProduto tipo,
        String nome,
        BigDecimal preco,
        Double pesoKg,
        Double dimensoesCm,
        Double tamanhoArquivoMb,
        Instant criadoEm,
        Instant atualizadoEm) {

    public static ProdutoResponse de(Produto produto) {
        Double pesoKg = null;
        Double dimensoesCm = null;
        Double tamanhoArquivoMb = null;

        if (produto instanceof ProdutoFisico fisico) {
            pesoKg = fisico.getPesoKg();
            dimensoesCm = fisico.getDimensoesCm();
        } else if (produto instanceof ProdutoDigital digital) {
            tamanhoArquivoMb = digital.getTamanhoArquivoMb();
        }

        return new ProdutoResponse(
                produto.getId(),
                produto.getTipo(),
                produto.getNome(),
                produto.getPreco(),
                pesoKg,
                dimensoesCm,
                tamanhoArquivoMb,
                produto.getCriadoEm(),
                produto.getAtualizadoEm());
    }
}
