package model.entidades;

import model.exceptions.NegativeException;

public class ProdutoFisico extends Produto{
	private Double peso;
	private Double dimensoes;
	
	public ProdutoFisico() {
		super();
	}

	public ProdutoFisico(Integer id, String nome, Double preco, Double peso, Double dimensoes) {
		super(id, nome, preco);
		this.peso = peso;
		this.dimensoes = dimensoes;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) throws NegativeException{
		if(peso < 0) {
			throw new NegativeException("O peso não pode ser negativo.");
		}
		this.peso = peso;
	}

	public Double getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(Double dimensoes) throws NegativeException {
		if(dimensoes < 0) {
			throw new NegativeException("As dimensões não podem ser negativas.");
		}
		this.dimensoes = dimensoes;
	}

	@Override
	public String toString() {
		return "ProdutoFisico [id = " + getId() + ", nome = " + getNome() + ", preço = R$ " + getPreco() + ", peso=" + peso + "Kg, dimensoes=" + dimensoes + "]";
	}
}