package com.henrique.cadastroprodutos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.henrique.cadastroprodutos.exception.CampoObrigatorioException;
import com.henrique.cadastroprodutos.exception.ValorNegativoException;

/**
 * Garante que as invariantes do domínio são aplicadas em todos os caminhos de
 * construção — o bug do sistema original era exatamente o construtor pular os setters.
 */
class ProdutoTest {

    @Nested
    @DisplayName("Produto físico")
    class Fisico {

        @Test
        void criaProdutoValido() {
            ProdutoFisico p = new ProdutoFisico("  Teclado  ", new BigDecimal("349.90"), 0.85, 44.0);

            assertThat(p.getNome()).isEqualTo("Teclado");
            assertThat(p.getPreco()).isEqualByComparingTo("349.90");
            assertThat(p.getPesoKg()).isEqualTo(0.85);
            assertThat(p.getDimensoesCm()).isEqualTo(44.0);
            assertThat(p.getTipo()).isEqualTo(TipoProduto.FISICO);
        }

        @Test
        void rejeitaPesoNegativoNoConstrutor() {
            assertThatThrownBy(() -> new ProdutoFisico("Teclado", BigDecimal.TEN, -1.0, 10.0))
                    .isInstanceOf(ValorNegativoException.class)
                    .hasMessageContaining("pesoKg");
        }

        @Test
        void rejeitaDimensoesAusentes() {
            assertThatThrownBy(() -> new ProdutoFisico("Teclado", BigDecimal.TEN, 1.0, null))
                    .isInstanceOf(CampoObrigatorioException.class)
                    .hasMessageContaining("dimensoesCm");
        }
    }

    @Nested
    @DisplayName("Produto digital")
    class Digital {

        @Test
        void criaProdutoValido() {
            ProdutoDigital p = new ProdutoDigital("E-book", new BigDecimal("29.90"), 12.5);

            assertThat(p.getTamanhoArquivoMb()).isEqualTo(12.5);
            assertThat(p.getTipo()).isEqualTo(TipoProduto.DIGITAL);
        }

        @Test
        void rejeitaTamanhoNegativo() {
            assertThatThrownBy(() -> new ProdutoDigital("E-book", BigDecimal.TEN, -0.1))
                    .isInstanceOf(ValorNegativoException.class);
        }
    }

    @Nested
    @DisplayName("Regras comuns")
    class Comuns {

        @Test
        void nomeEmBrancoEhRejeitado() {
            assertThatThrownBy(() -> new ProdutoDigital("   ", BigDecimal.TEN, 1.0))
                    .isInstanceOf(CampoObrigatorioException.class)
                    .hasMessageContaining("nome");
        }

        @Test
        void precoNegativoEhRejeitado() {
            assertThatThrownBy(() -> new ProdutoDigital("E-book", new BigDecimal("-0.01"), 1.0))
                    .isInstanceOf(ValorNegativoException.class)
                    .hasMessageContaining("preco");
        }

        @Test
        void precoZeroEhPermitido() {
            ProdutoDigital gratuito = new ProdutoDigital("Manual", BigDecimal.ZERO, 1.0);
            assertThat(gratuito.getPreco()).isEqualByComparingTo("0");
        }

        @Test
        void setterTambemValida() {
            ProdutoDigital p = new ProdutoDigital("E-book", BigDecimal.TEN, 1.0);
            assertThatThrownBy(() -> p.setPreco(new BigDecimal("-5")))
                    .isInstanceOf(ValorNegativoException.class);
        }
    }
}
