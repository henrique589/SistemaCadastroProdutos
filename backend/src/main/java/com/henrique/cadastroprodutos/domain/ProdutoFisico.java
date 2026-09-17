package com.henrique.cadastroprodutos.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FISICO")
public class ProdutoFisico extends Produto {

    /** Peso em quilogramas. */
    @Column(name = "peso_kg")
    private Double pesoKg;

    /** Maior dimensão em centímetros (compatível com o campo único do sistema original). */
    @Column(name = "dimensoes_cm")
    private Double dimensoesCm;

    protected ProdutoFisico() {
    }

    public ProdutoFisico(String nome, BigDecimal preco, Double pesoKg, Double dimensoesCm) {
        super(nome, preco);
        setPesoKg(pesoKg);
        setDimensoesCm(dimensoesCm);
    }

    @Override
    public TipoProduto getTipo() {
        return TipoProduto.FISICO;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = naoNegativo("pesoKg", pesoKg);
    }

    public Double getDimensoesCm() {
        return dimensoesCm;
    }

    public void setDimensoesCm(Double dimensoesCm) {
        this.dimensoesCm = naoNegativo("dimensoesCm", dimensoesCm);
    }
}
