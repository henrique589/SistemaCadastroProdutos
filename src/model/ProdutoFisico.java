package model;

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

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(Double dimensoes) {
		this.dimensoes = dimensoes;
	}

	@Override
	public String toString() {
		return "ProdutoFisico [id = " + getId() + ", nome = " + getNome() + ", preço = R$ " + getPreco() + ", peso=" + peso + "Kg, dimensoes=" + dimensoes + "]";
	}
}