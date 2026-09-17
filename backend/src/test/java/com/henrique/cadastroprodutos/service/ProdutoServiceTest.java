package com.henrique.cadastroprodutos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henrique.cadastroprodutos.domain.Produto;
import com.henrique.cadastroprodutos.domain.ProdutoDigital;
import com.henrique.cadastroprodutos.domain.ProdutoFisico;
import com.henrique.cadastroprodutos.domain.TipoProduto;
import com.henrique.cadastroprodutos.dto.ProdutoRequest;
import com.henrique.cadastroprodutos.dto.ProdutoResponse;
import com.henrique.cadastroprodutos.exception.RecursoNaoEncontradoException;
import com.henrique.cadastroprodutos.exception.RegraDeNegocioException;
import com.henrique.cadastroprodutos.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    ProdutoRepository repository;

    @InjectMocks
    ProdutoService service;

    @Test
    void criarProdutoFisicoPersisteEntidadeCorreta() {
        when(repository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
        var request = new ProdutoRequest(TipoProduto.FISICO, "Mouse", new BigDecimal("99.90"),
                0.1, 12.0, null);

        ProdutoResponse resposta = service.criar(request);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue()).isInstanceOf(ProdutoFisico.class);
        assertThat(resposta.tipo()).isEqualTo(TipoProduto.FISICO);
        assertThat(resposta.pesoKg()).isEqualTo(0.1);
        assertThat(resposta.tamanhoArquivoMb()).isNull();
    }

    @Test
    void criarProdutoDigitalIgnoraCamposDeFisico() {
        when(repository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));
        var request = new ProdutoRequest(TipoProduto.DIGITAL, "Curso", BigDecimal.TEN,
                5.0, 5.0, 800.0);

        ProdutoResponse resposta = service.criar(request);

        assertThat(resposta.tipo()).isEqualTo(TipoProduto.DIGITAL);
        assertThat(resposta.pesoKg()).isNull();
        assertThat(resposta.tamanhoArquivoMb()).isEqualTo(800.0);
    }

    @Test
    void buscarPorIdInexistenteLanca404() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(42L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("42");
    }

    @Test
    void atualizarAlteraCamposDoProdutoExistente() {
        var existente = new ProdutoDigital("Antigo", BigDecimal.ONE, 1.0);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        var request = new ProdutoRequest(TipoProduto.DIGITAL, "Novo", new BigDecimal("2.50"),
                null, null, 3.0);

        ProdutoResponse resposta = service.atualizar(1L, request);

        assertThat(resposta.nome()).isEqualTo("Novo");
        assertThat(resposta.preco()).isEqualByComparingTo("2.50");
        assertThat(resposta.tamanhoArquivoMb()).isEqualTo(3.0);
        assertThat(existente.getNome()).isEqualTo("Novo");
    }

    @Test
    void atualizarNaoPermiteTrocarTipo() {
        var existente = new ProdutoDigital("E-book", BigDecimal.ONE, 1.0);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        var request = new ProdutoRequest(TipoProduto.FISICO, "E-book", BigDecimal.ONE,
                1.0, 1.0, null);

        assertThatThrownBy(() -> service.atualizar(1L, request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("tipo");
    }

    @Test
    void excluirInexistenteNaoChamaDelete() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(7L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).delete(any(Produto.class));
    }
}
