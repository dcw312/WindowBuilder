package us.northdeck.ui;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.sun.xml.xsom.XSSchemaSet;
import us.northdeck.xsomtest.XsomTest;


public class ApplicationController {
	private XSSchemaSet sset;
	private String[] nsList;
	private Console c;
	
	ApplicationController(){
		c = new SystemConsole();
		
		
	}
	
	class SystemConsole implements Console {
		@Override
		public void println(String s) {
			System.out.println(s);			
		}		
	}
	
	void setConsole (Console c) {
		this.c = c;
	}
	
	boolean loadSchemaSet(File f) {
		boolean result = false;
		try {
			sset = XsomTest.getSchemaSet(f);
			result = true;
		} catch (Exception e) {
			c.println(e.toString());
		}
		nsList = XsomTest.getNamespaceArray(sset);
		return result;
	}

	

	public String[] getNsList() {
		
		return nsList;
	}

	class SchemaNotFoundException extends Exception{
		private static final long serialVersionUID = -991408886252026578L;}
	
	protected void handleFlattenButton(SchemaFlattenerPanel schemaFlatternerPanel) throws SchemaNotFoundException, ParserConfigurationException, TransformerException, IOException {
		if (sset == null) {
			startSchemaLoad(schemaFlatternerPanel);
		} else {
			String ns = (String) schemaFlatternerPanel.nsBox.getSelectedItem();
			File inFile = new File(schemaFlatternerPanel.getFileName());
			if (!inFile.exists()) throw new SchemaNotFoundException();
			File outFile = new File (inFile.getAbsolutePath() + ".1");
			XsomTest.writeFlatSchemaFile(sset, ns, outFile);
		}
		
	}
	
	public void startSchemaLoad(SchemaFlattenerPanel schemaFlatternerPanel) {
		File f = new File(schemaFlatternerPanel.getFileName());
		loadSchemaSet(f);
		List<String> nsList1 = Arrays.asList(nsList);
		for (String s : nsList1) {
			schemaFlatternerPanel.nsBox.addItem(s);
		}
		
		//schemaFlatternerPanel.nsVector.addAll(nsList1);
		
		
	}

//	class TextAreaConsole implements Console {
//		JTextArea consoleTextArea;
//		TextAreaConsole (JTextArea consoleTextArea) {
//			this.consoleTextArea = consoleTextArea;
//		}
//		@Override
//		public void println(String s) {
//			consoleTextArea.a
//			
//		}
//	}
	
	public void setConsoleTextArea(JTextArea consoleTextArea) {
		
		
	}

	
	
	
	
	
	
}
