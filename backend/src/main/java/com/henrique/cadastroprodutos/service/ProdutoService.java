package com.henrique.cadastroprodutos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.henrique.cadastroprodutos.domain.Produto;
import com.henrique.cadastroprodutos.domain.ProdutoDigital;
import com.henrique.cadastroprodutos.domain.ProdutoFisico;
import com.henrique.cadastroprodutos.domain.TipoProduto;
import com.henrique.cadastroprodutos.dto.ProdutoRequest;
import com.henrique.cadastroprodutos.dto.ProdutoResponse;
import com.henrique.cadastroprodutos.exception.RecursoNaoEncontradoException;
import com.henrique.cadastroprodutos.exception.RegraDeNegocioException;
import com.henrique.cadastroprodutos.repository.ProdutoRepository;
import com.henrique.cadastroprodutos.repository.ProdutoSpecifications;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Page<ProdutoResponse> listar(TipoProduto tipo, String nome, Pageable pageable) {
        Specification<Produto> filtro = Specification
                .where(ProdutoSpecifications.comTipo(tipo))
                .and(ProdutoSpecifications.comNomeContendo(nome));
        return repository.findAll(filtro, pageable).map(ProdutoResponse::de);
    }

    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.de(obter(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = switch (request.tipo()) {
            case FISICO -> new ProdutoFisico(request.nome(), request.preco(),
                    request.pesoKg(), request.dimensoesCm());
            case DIGITAL -> new ProdutoDigital(request.nome(), request.preco(),
                    request.tamanhoArquivoMb());
        };
        return ProdutoResponse.de(repository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = obter(id);
        if (produto.getTipo() != request.tipo()) {
            throw new RegraDeNegocioException(
                    "Não é permitido alterar o tipo de um produto (" + produto.getTipo()
                            + " para " + request.tipo() + ").");
        }

        produto.setNome(request.nome());
        produto.setPreco(request.preco());

        if (produto instanceof ProdutoFisico fisico) {
            fisico.setPesoKg(request.pesoKg());
            fisico.setDimensoesCm(request.dimensoesCm());
        } else if (produto instanceof ProdutoDigital digital) {
            digital.setTamanhoArquivoMb(request.tamanhoArquivoMb());
        }
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
    }

    private Produto obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }
}
