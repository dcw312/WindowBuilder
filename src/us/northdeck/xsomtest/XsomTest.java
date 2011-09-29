package us.northdeck.xsomtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSTerm;


/**
 * @author a121757
 *
 */
public class XsomTest {

	
//	
//	public static void process(XSComplexType ct  ) {
//		print( ct.getName());
//		 XSContentType cont = ct.getContentType();
//		process(cont);
//	}
//	
//	public static void process(XSContentType ct  ) {
//		XSParticle p = ct.asParticle();
//		print(p.getMinOccurs().toString() + ".." + p.getMinOccurs().toString());
//		XSTerm t = p.getTerm();
//		//process (t);
//		
//	}
//	
//	public static void process(XSModelGroup m  ) {
//		print(m.getCompositor().toString());
//		for(XSParticle p : m.getChildren()) {
//			print(p.getMinOccurs().toString() + ".." + p.getMinOccurs().toString());
//			//process(p.getTerm());
//		}
//	}
//	
//	public static void process (XSElementDecl el) {
//		
//	}
//	
//	public static void print (String s) {
//		System.out.println(s);
//	}
	
	/**
	 * Parses schemas into an XSOM model and uses the XSOM visitor implementation to recurse through a selected schema.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String[] schemas = {"C:\\cvs\\commonXsd\\Best_Buy_Mobile\\Activations\\CAP\\repository\\US\\content\\validations\\POSTPAID_PLANS_REQUEST.XSD"
				, "C:\\cvs\\mrbrSandbox\\Best_Buy_Mobile\\Activations\\CAPTools\\MRBR\\Supplement\\VEZ\\POSTPAID_PLANS_REQUEST_VEZ_ALL.xsd"
				, "C:\\cvs\\mrbrSandbox\\Best_Buy_Mobile\\Activations\\CAPTools\\MRBR\\CAP_COMMON_TT_DEMO.XSD"};
		
		String schema1 = schemas[0];
		
		XSSchemaSet sset = getSchemaSet(new File(schema1));
		
		String[] nsArray = getNamespaceArray(sset);
		
		nsArray.toString();
		
//		for (XSSchema s : sset.getSchemas()) {
//			String tns = s.getTargetNamespace();
//			tns.toString();
//		}
		
		String ns = "http://bestbuy.com/bbym/beast/cap/common"; //
		ns = "http://bestbuy.com/bbym/beast/cap/request/postpaid/plans";
		String outFileName = "C:\\Documents and Settings\\a121757\\Desktop\\sampleXsd_VEZ.xsd";
		File outFile = new File(outFileName);
		
		writeFlatSchemaFile(sset, ns, outFile);
	}

/**
 * @param sset
 * @param ns
 * @param outFile
 * @throws ParserConfigurationException
 * @throws TransformerException
 * @throws IOException
 */
public static void writeFlatSchemaFile(XSSchemaSet sset, String ns, File outFile)
		throws ParserConfigurationException, TransformerException, IOException {
	Document newSchemaDoc = visitElementDeclarations(sset, ns);
	writeDocumentXML(newSchemaDoc, outFile);
}

/**
 * Returns the namespaces for XSSchemaSet
 * @param sset
 * @return
 */
public static String[] getNamespaceArray(XSSchemaSet sset) {
	Collection<XSSchema> sCol = sset.getSchemas();
	ArrayList<String> nsList = new ArrayList<String>();
	for (XSSchema s : sCol) {
		//Map<String, XSNotation> nm = s.getNotations();
		//nm.toString();
		nsList.add(s.getTargetNamespace());
	}
	
	String[] nsArray = new String[nsList.size()]; 
	nsList.toArray(nsArray);
	return nsArray;
}

/**
 * Returns a schema set from a schema file
 * @param file A schema file
 * @return 
 * @throws Exception 
 */
public static XSSchemaSet getSchemaSet(File file) throws Exception {
	XSOMParser parser = new XSOMParser();
	parser.setAnnotationParser(new MRBRAnnotationParserFactory());
	if (!file.exists()) throw new Exception("no file");
	parser.parse(file);

	XSSchemaSet sset = parser.getResult();
	return sset;
}

	/**
	 * @param newSchemaDoc
	 * @param outFile 
	 * @throws IOException
	 * @throws TransformerException
	 */
	private static void writeDocumentXML(Document newSchemaDoc, File outFile)
			throws IOException, TransformerException {
		
		FileWriter fstream = new FileWriter(outFile.getAbsoluteFile());
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(toXML(newSchemaDoc));
		out.close();
	}
	
	public static String toXML(Document document) throws TransformerException, IOException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		Source source = new DOMSource(document);
		transformer.transform(source, result);
		writer.close();
		String xml = writer.toString();
		return xml;
	}

/**
 * @param sset
 * @param ns
 * @return 
 * @throws ParserConfigurationException
 * @throws TransformerException
 * @throws IOException
 */
private static Document visitElementDeclarations(XSSchemaSet sset, String ns)
		throws ParserConfigurationException, TransformerException, IOException {
	
	XSSchema plans = sset.getSchema(ns);

	
	
	Collection<XSElementDecl> elDecs = plans.getElementDecls().values();
	MRBRComponentVisitor v = new MRBRComponentVisitor();
	v.initDoc();
	for (XSElementDecl elDec : elDecs) {
		//Begin visitor recursion down into schema
		//see MRBRComponentVisitor for handling of each schema element
		elDec.visit(v);
	}
	return v.getDocument();
	
	
	
}

}
