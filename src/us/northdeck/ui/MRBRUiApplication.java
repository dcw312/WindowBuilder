package us.northdeck.ui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MRBRUiApplication {

	private JFrame frmMrbrEditor;
	private SchemaFlattenerPanel schemaFlatternerPanel = new SchemaFlattenerPanel();

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
		
		JTextArea textArea = new JTextArea();
		frmMrbrEditor.getContentPane().add(textArea, BorderLayout.SOUTH);
		
		setDefaultFrame();
		
	}

	private void setDefaultFrame() {
		frmMrbrEditor.getContentPane().add(schemaFlatternerPanel);
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
