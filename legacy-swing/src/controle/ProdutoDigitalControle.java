package controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.entidades.ProdutoDigital;

public class ProdutoDigitalControle {
	// Método de cadastro de produto digital.
	public static void cadastraProdutoDigital(int id, String nome, double preco, double tamanhoArquivo) {
		ProdutoDigital produtoDigital = new ProdutoDigital(id, nome, preco, tamanhoArquivo);
		JOptionPane.showMessageDialog(null, "Produto Cadastrado" + "\n" + produtoDigital);
		File file = new File("c:\\temp\\bancoDeDados.txt");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
				bw.write(produtoDigital.getId() + ", " + produtoDigital.getNome() + ", " + String.format("%.2f", produtoDigital.getPreco()) + 
						", " + produtoDigital.getTamanhoDoArquivo());
				bw.newLine();
			}
		catch(IOException e1) {
			System.out.println("Error: " + e1.getMessage());
		}
	}
}
