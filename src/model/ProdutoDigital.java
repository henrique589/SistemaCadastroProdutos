package model;

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

	public void setTamanhoDoArquivo(Double tamanhoDoArquivo) {
		this.tamanhoDoArquivo = tamanhoDoArquivo;
	}

	@Override
	public String toString() {
		return "ProdutoDigital [id = " + getId() + ", nome = " + getNome() + ", preço = R$ " + getPreco() + ", tamanhoDoArquivo = " + tamanhoDoArquivo + "]";
	}
}