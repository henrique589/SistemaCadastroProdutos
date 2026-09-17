package com.henrique.cadastroprodutos.repository;

import org.springframework.data.jpa.domain.Specification;

import com.henrique.cadastroprodutos.domain.Produto;
import com.henrique.cadastroprodutos.domain.ProdutoDigital;
import com.henrique.cadastroprodutos.domain.ProdutoFisico;
import com.henrique.cadastroprodutos.domain.TipoProduto;

/** Filtros dinâmicos e combináveis da listagem de produtos. */
public final class ProdutoSpecifications {

    private ProdutoSpecifications() {
    }

    public static Specification<Produto> comTipo(TipoProduto tipo) {
        if (tipo == null) {
            return null;
        }
        Class<? extends Produto> classe = switch (tipo) {
            case FISICO -> ProdutoFisico.class;
            case DIGITAL -> ProdutoDigital.class;
        };
        return (root, query, cb) -> cb.equal(root.type(), classe);
    }

    public static Specification<Produto> comNomeContendo(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }
        String padrao = "%" + nome.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("nome")), padrao);
    }
}
