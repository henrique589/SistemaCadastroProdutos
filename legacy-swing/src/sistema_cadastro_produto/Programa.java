package sistema_cadastro_produto;

import java.util.Locale;

import view.TelaCadastro;

public class Programa {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		TelaCadastro telaCadastro = new TelaCadastro();
		telaCadastro.setVisible(true);

	}

}
