package model.entidades;

import model.entidades.exception.NegativeException;

public class ProdutoDigital extends Produto{
	private Double tamanhoDoArquivo;
	
	public ProdutoDigital(){
		super();
	}

	public ProdutoDigital(Integer id, String nome, Double preco, Double tamanhoDoArquivo) {
		super(id, nome, preco);
		this.tamanhoDoArquivo = tamanhoDoArquivo;
	}

	public Double getTamanhoDoArquivo() {
		return tamanhoDoArquivo;
	}

	public void setTamanhoDoArquivo(Double tamanhoDoArquivo) throws NegativeException{
		if(tamanhoDoArquivo < 0) {
			throw new NegativeException("O tamanho do arquivo não pode ser negativo.");
		}
		this.tamanhoDoArquivo = tamanhoDoArquivo;
	}

	@Override
	public String toString() {
		return "ProdutoDigital [id = " + getId() + ", nome = " + getNome() + ", preço = R$ " + getPreco() + ", tamanhoDoArquivo = " + tamanhoDoArquivo + "]";
	}
}