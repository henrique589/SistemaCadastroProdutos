package controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.entidades.ProdutoFisico;

public class ProdutoFisicoControle {
	// Método de cadastro de produto físico.
	public static void cadastraProdutoFisico(int id, String nome, double preco, double peso,
			double dimensoes) {
		ProdutoFisico produtoFisico = new ProdutoFisico(id, nome, preco, peso, dimensoes);
		JOptionPane.showMessageDialog(null, "Produto Cadastrado" + "\n" + produtoFisico);
		File file = new File("c:\\temp\\bancoDeDados.txt");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
				bw.write(produtoFisico.getId() + ", " + produtoFisico.getNome() + ", " + String.format("%.2f", produtoFisico.getPreco()) + 
						", " + produtoFisico.getPeso() + ", " + produtoFisico.getDimensoes());
				bw.newLine();
			}
		catch(IOException e1) {
			System.out.println("Error: " + e1.getMessage());
		}
	}
}
