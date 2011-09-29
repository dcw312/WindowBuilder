package us.northdeck.ui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import us.northdeck.ui.ApplicationController.SchemaNotFoundException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MRBRUiApplication {

	private JFrame frmMrbrEditor;
	private SchemaFlattenerPanel schemaFlatternerPanel = new SchemaFlattenerPanel();
	private ApplicationController ac = new ApplicationController();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MRBRUiApplication window = new MRBRUiApplication();
					window.frmMrbrEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MRBRUiApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMrbrEditor = new JFrame();
		frmMrbrEditor.setTitle("MRBR Editor");
		frmMrbrEditor.setBounds(100, 100, 762, 348);
		frmMrbrEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmMrbrEditor.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmFlattenSchema = new JMenuItem("Flatten Schema");
		mnTools.add(mntmFlattenSchema);
		
		JTextArea consoleTextArea = new JTextArea();
		frmMrbrEditor.getContentPane().add(consoleTextArea, BorderLayout.SOUTH);
		
		setDefaultFrame();
		ac.setConsoleTextArea(consoleTextArea);
	}

	private void setDefaultFrame() {
		frmMrbrEditor.getContentPane().add(schemaFlatternerPanel);
		String defaultFile = "C:\\cvs\\commonXsd\\Best_Buy_Mobile\\Activations\\CAP\\repository\\US\\content\\validations\\POSTPAID_PLANS_REQUEST.XSD";
		schemaFlatternerPanel.setFileName(new File(defaultFile));
		MouseListener l = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				FileChooserDialog fcd = new FileChooserDialog();
				int returnVal = fcd.fileChooser.showOpenDialog(schemaFlatternerPanel.getButton());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					schemaFlatternerPanel.setFileName(fcd.fileChooser.getSelectedFile());
					
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}};
		
			
			
			
		schemaFlatternerPanel.getButton().addMouseListener(l);
		
		schemaFlatternerPanel.getBtnFlatten().addMouseListener(
		new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ac.handleFlattenButton(schemaFlatternerPanel);
				} catch (SchemaNotFoundException e) {
					String message = "The schema was not found";
					String title = "File not found";
					int messageType = JOptionPane.INFORMATION_MESSAGE;
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(schemaFlatternerPanel, message, title, messageType);
					
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		
		
//		schemaFlatternerPanel.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				FileChooserDialog fcd = new FileChooserDialog();
//				int returnVal = fcd.fileChooser.showOpenDialog(schemaFlatternerPanel.getButton());
//				if (returnVal == JFileChooser.APPROVE_OPTION) {
//					schemaFlatternerPanel.setFileName(fcd.fileChooser.getSelectedFile());
//				}
//				
//			}
//		});
		
		
		
	}

}
