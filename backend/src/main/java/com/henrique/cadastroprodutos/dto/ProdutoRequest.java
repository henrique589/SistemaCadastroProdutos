package com.henrique.cadastroprodutos.dto;

import java.math.BigDecimal;

import com.henrique.cadastroprodutos.domain.TipoProduto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Corpo de criação/atualização de produto. Os campos específicos de cada tipo
 * são validados no {@code ProdutoService}, pois dependem do valor de {@code tipo}.
 */
@Schema(description = "Dados para criar ou atualizar um produto")
public record ProdutoRequest(

        @NotNull
        @Schema(example = "FISICO")
        TipoProduto tipo,

        @NotBlank
        @Size(max = 120)
        @Schema(example = "Teclado mecânico")
        String nome,

        @NotNull
        @DecimalMin(value = "0.0")
        @Schema(example = "349.90")
        BigDecimal preco,

        @PositiveOrZero
        @Schema(description = "Somente para FISICO. Peso em kg", example = "0.85")
        Double pesoKg,

        @PositiveOrZero
        @Schema(description = "Somente para FISICO. Maior dimensão em cm", example = "44.0")
        Double dimensoesCm,

        @PositiveOrZero
        @Schema(description = "Somente para DIGITAL. Tamanho do arquivo em MB", example = "1250.0")
        Double tamanhoArquivoMb) {
}
