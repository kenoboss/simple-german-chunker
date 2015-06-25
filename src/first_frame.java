import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;


public class first_frame extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					first_frame frame = new first_frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public first_frame() {
		setTitle("Simple German Chunker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 550);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmReset = new JMenuItem("Reset");
		mnFile.add(mntmReset);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmWelcome = new JMenuItem("Welcome");
		mnHelp.add(mntmWelcome);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAboutSimpleGerman = new JMenuItem("About");
		mnHelp.add(mntmAboutSimpleGerman);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSimpleGermanChunker = new JLabel("Simple German Chunker");
		lblSimpleGermanChunker.setBounds(132, 11, 214, 35);
		lblSimpleGermanChunker.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contentPane.add(lblSimpleGermanChunker);
		
		JButton btnLoadFile = new JButton("Load File");
		btnLoadFile.setBounds(27, 90, 89, 23);
		contentPane.add(btnLoadFile);
		
		JButton btnRunChunker = new JButton("Run");
		btnRunChunker.setBounds(27, 148, 89, 23);
		contentPane.add(btnRunChunker);
		
		JLabel lblResults = new JLabel("Results");
		lblResults.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblResults.setBounds(272, 82, 59, 35);
		contentPane.add(lblResults);
		
		JLabel lblNewLabel = new JLabel("This program is the result of a cl project by students from the university in Trier");
		lblNewLabel.setBounds(10, 436, 423, 14);
		contentPane.add(lblNewLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(177, 124, 250, 270);
		contentPane.add(textArea);
	}
}
