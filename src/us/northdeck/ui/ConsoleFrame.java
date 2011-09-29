package us.northdeck.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;

public class ConsoleFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1237168597151055797L;
	private JPanel contentPane;
	protected JTextArea consoleTextArea = new JTextArea();

	

	/**
	 * Create the frame.
	 */
	public ConsoleFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.setTitle("Console");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		
		panel.add(consoleTextArea);
		
	}

}
