package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controle.ProdutoDigitalControle;
import controle.ProdutoFisicoControle;

public class TelaCadastro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfId;
	private JTextField tfNome;
	private JTextField tfPreco;
	private JTextField tfPeso;
	private JTextField tfDimensoes;
	private JTextField tfTamanhoArquivo;

	public TelaCadastro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 250, 675, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tela Cadastro");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(257, 11, 109, 14);
		contentPane.add(lblNewLabel);
		
		JRadioButton rdProdutoFisico = new JRadioButton("Produto Físico");
		rdProdutoFisico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfPeso.setEditable(true);
				tfDimensoes.setEditable(true);
				tfTamanhoArquivo.setEditable(false);
				tfTamanhoArquivo.setText("");
			}
		});
		buttonGroup.add(rdProdutoFisico);
		rdProdutoFisico.setFont(new Font("Times New Roman", Font.BOLD, 16));
		rdProdutoFisico.setBounds(132, 32, 121, 23);
		contentPane.add(rdProdutoFisico);
		
		JRadioButton rdProdutoDigital = new JRadioButton("Produto Digital");
		rdProdutoDigital.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfTamanhoArquivo.setEditable(true);
				tfPeso.setEditable(false);
				tfPeso.setText("");
				tfDimensoes.setEditable(false);
				tfDimensoes.setText("");
			}
		});
		buttonGroup.add(rdProdutoDigital);
		rdProdutoDigital.setFont(new Font("Times New Roman", Font.BOLD, 16));
		rdProdutoDigital.setBounds(378, 33, 140, 23);
		contentPane.add(rdProdutoDigital);
		
		JLabel lblNewLabel_1 = new JLabel("ID:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1.setBounds(203, 89, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		tfId = new JTextField();
		tfId.setBounds(259, 87, 169, 20);
		contentPane.add(tfId);
		tfId.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nome:");
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1_1.setBounds(203, 126, 46, 14);
		contentPane.add(lblNewLabel_1_1);
		
		tfNome = new JTextField();
		tfNome.setColumns(10);
		tfNome.setBounds(260, 124, 168, 20);
		contentPane.add(tfNome);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Preço:");
		lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1_1_1.setBounds(203, 165, 46, 14);
		contentPane.add(lblNewLabel_1_1_1);
		
		tfPreco = new JTextField();
		tfPreco.setColumns(10);
		tfPreco.setBounds(260, 163, 168, 20);
		contentPane.add(tfPreco);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 206, 639, 2);
		contentPane.add(separator);
		
		// Botão de finalizar cadastro.
		JButton btnNewButton = new JButton("Finalizar Cadastro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Leitura dos campos.
				int id = Integer.parseInt(tfId.getText());
				String nome = tfNome.getText();
				double preco = Double.parseDouble(tfPreco.getText());
				if(rdProdutoDigital.isSelected()) {
					double tamanhoArquivo = Double.parseDouble(tfTamanhoArquivo.getText());
					// Cadastro do produto digital.
					ProdutoDigitalControle.cadastraProdutoDigital(id, nome, preco, tamanhoArquivo);
				}else {
					double peso = Double.parseDouble(tfPeso.getText());
					double dimensoes = Double.parseDouble(tfDimensoes.getText());
					// Cadastro do produto físico.
					ProdutoFisicoControle.cadastraProdutoFisico(id, nome, preco, peso, dimensoes);
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton.setBounds(236, 335, 180, 23);
		contentPane.add(btnNewButton);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(322, 206, 1, 122);
		contentPane.add(separator_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Peso:");
		lblNewLabel_1_2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1_2.setBounds(10, 243, 46, 14);
		contentPane.add(lblNewLabel_1_2);
		
		tfPeso = new JTextField();
		tfPeso.setColumns(10);
		tfPeso.setBounds(99, 241, 169, 20);
		contentPane.add(tfPeso);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Dimensões:");
		lblNewLabel_1_1_2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1_1_2.setBounds(10, 280, 92, 14);
		contentPane.add(lblNewLabel_1_1_2);
		
		tfDimensoes = new JTextField();
		tfDimensoes.setColumns(10);
		tfDimensoes.setBounds(100, 278, 168, 20);
		contentPane.add(tfDimensoes);
		
		JLabel lblNewLabel_1_3 = new JLabel("Tamanho do Arquivo:");
		lblNewLabel_1_3.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel_1_3.setBounds(335, 243, 156, 14);
		contentPane.add(lblNewLabel_1_3);
		
		tfTamanhoArquivo = new JTextField();
		tfTamanhoArquivo.setColumns(10);
		tfTamanhoArquivo.setBounds(489, 241, 101, 20);
		contentPane.add(tfTamanhoArquivo);
	}
}
