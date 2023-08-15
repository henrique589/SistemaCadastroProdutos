package model.entidades;

import model.entidades.exception.EmptyException;
import model.entidades.exception.NegativeException;

public abstract class Produto {
	private Integer id;
	private String nome;
	private Double preco;
	
	public Produto() {
	}

	public Produto(Integer id, String nome, Double preco) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) throws NegativeException{
		if(id < 0) {
			throw new NegativeException("O id não pode ser negativo.");
		}
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) throws EmptyException{
		if(nome.isEmpty()) {
			throw new EmptyException("O nome deve ser preenchido.");
		}
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) throws NegativeException{
		if(preco < 0) {
			throw new NegativeException("O preço não pode ser negativo.");
		}
		this.preco = preco;
	}
}