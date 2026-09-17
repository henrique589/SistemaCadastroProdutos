package com.henrique.cadastroprodutos.domain;

import java.math.BigDecimal;
import java.time.Instant;

import com.henrique.cadastroprodutos.exception.CampoObrigatorioException;
import com.henrique.cadastroprodutos.exception.ValorNegativoException;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Raiz da hierarquia de produtos. As regras de negócio (nome obrigatório, valores
 * não negativos) vivem aqui, nos setters, para que nenhum caminho de código
 * consiga criar um produto em estado inválido.
 */
@Entity
@Table(name = "produto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 10)
public abstract class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected Produto() {
        // exigido pelo JPA
    }

    protected Produto(String nome, BigDecimal preco) {
        setNome(nome);
        setPreco(preco);
    }

    public abstract TipoProduto getTipo();

    @PrePersist
    void aoCriar() {
        criadoEm = Instant.now();
        atualizadoEm = criadoEm;
    }

    @PreUpdate
    void aoAtualizar() {
        atualizadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new CampoObrigatorioException("nome");
        }
        this.nome = nome.trim();
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null) {
            throw new CampoObrigatorioException("preco");
        }
        if (preco.signum() < 0) {
            throw new ValorNegativoException("preco");
        }
        this.preco = preco;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    /** Valida um valor numérico opcional que, quando informado, não pode ser negativo. */
    protected static Double naoNegativo(String campo, Double valor) {
        if (valor == null) {
            throw new CampoObrigatorioException(campo);
        }
        if (valor < 0) {
            throw new ValorNegativoException(campo);
        }
        return valor;
    }
}
