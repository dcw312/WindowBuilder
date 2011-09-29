package us.northdeck.ui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JProgressBar;
import javax.swing.JComboBox;

public class SchemaFlattenerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7363190848001148551L;
	private JTextField schemaFileName;
	JButton button = new JButton("...");
	private JButton btnFlatten;
	protected Vector<String> nsVector = new Vector<String>();
	protected JComboBox nsBox = new JComboBox(nsVector);


	/**
	 * Create the panel.
	 */
	public SchemaFlattenerPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("58px"),
				ColumnSpec.decode("564px:grow"),
				ColumnSpec.decode("121px"),},
			new RowSpec[] {
				RowSpec.decode("30px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSchema = new JLabel("Schema");
		add(lblSchema, "1, 1, fill, fill");
		
		schemaFileName = new JTextField();
		add(schemaFileName, "2, 1, fill, fill");
		schemaFileName.setColumns(10);
		
		
		add(button, "3, 1, fill, fill");
		
		nsBox = new JComboBox();
		add(nsBox, "2, 3, fill, default");
		
		btnFlatten = new JButton("Flatten");
		add(btnFlatten, "2, 5");

	}
	
	public JButton getButton() {
		return button;
	}
	
	void setFileName(File f) {
		schemaFileName.setText(f.getAbsolutePath());
	}
	
	String getFileName() {
		return schemaFileName.getText();
	}
	
	JButton getBtnFlatten () {
		return btnFlatten;
	}

}
