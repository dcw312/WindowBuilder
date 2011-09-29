package us.northdeck.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

import us.northdeck.processor.MRBRProcessor.NodeListIterator;
import us.northdeck.processor.MRBRRules.rule;
import us.northdeck.processor.MRBRRules.setReq;
import us.northdeck.processor.MRBRRules.setType;
import us.northdeck.processor.MRBRRules.table;

public class MRBRProcessor {

	Document schema;
	


	void process() throws Exception {
		File fXmlFile = new File("c:\\file.xml");
		
		try {
		    schema = loadSchema(fXmlFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("unrecoverable problem loading schema");
		}
		table rulesTable = loadRules();
		applyRules(rulesTable);
	}
	
	class NodeListIterator implements Iterator<Node> {
		NodeList nl;
		int position = 0;
		int length = 0;
		String elFilterName = "";
		boolean filtered = false;

		NodeListIterator(NodeList nl) {
			this.nl = nl;
			length = nl.getLength();
		}
		
		public void setNodeFilter(String elName) {
			this.filtered = true;
			this.elFilterName = elName;
			
			
			
		}
		
		@Override
		public boolean hasNext() {
			if (length > 0) {
				return true;
			} else {
				return false;	
			}
		}

		@Override
		public Node next() throws NoSuchElementException {
			if (!this.hasNext()) throw new NoSuchElementException();
			Node n = nl.item(position);
			position++;
			return n;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			position = length + 1;
		}
		
	}
	
	class NoMatchingElementsException extends Exception {
		private static final long serialVersionUID = -7303991037189447757L;}
	
	ArrayList<Element> getElList (String ns, String type) throws NoMatchingElementsException {
		NodeList nl;
		//NamedNodeMap attrs;
		Node typeAttr;
		ArrayList<Element> elList = new ArrayList<Element>();
		nl = schema.getElementsByTagName("xs:complexType");
		NodeListIterator nli = this.new NodeListIterator(nl);
		while (nli.hasNext()) {
			Node n = nli.next();
			 if ((typeAttr = n.getAttributes().getNamedItem("type")) != null) {
				 if(typeAttr.getNodeValue() == ns + type) {
					 elList.add((Element) n);
				 }
			 }	
		}
		if (elList.size() == 0) throw new NoMatchingElementsException ();
		return elList;
	}
	
	private void applyRules(table t) throws NoMatchingElementsException {
		String file = t.file;
		String type = t.type;
		ArrayList<Element> elList = getElList(file, type);
		for (rule r : t.rules) {
			applyRule(r, elList);
		}		
	}

	//load flattened schema
	Document loadSchema(File fXmlFile) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		return doc;
	}
	
	//load rules xml
	table loadRules() {
		return MRBRRules.newTable();
	}
	
	
	
	private void applyRule(rule r, ArrayList<Element> elList) {
		for ( setType st : r.setTypes) {
			//TODO make work for simpletypes
			for (Element typeDef : elList) {
				Element el = (Element) typeDef.getParentNode();
				el.removeChild(typeDef);
				el.setAttribute("type", st.type);
			}
		}
		for (setReq sr : r.setReqs) {
			
			for (Element typeDef : elList) {
				NodeListIterator cni = new NodeListIterator(typeDef.getChildNodes());
				while (cni.hasNext()) {
					Node n = cni.next();
					if(isSubjectElement(sr.path, n)) {
						Element e = (Element) n;
						String value = "0";
						switch (sr.to) {
						case setReq.REQ:
							value = "1";
							break;
						}
						e.setAttribute("minOccurs", value);
					}
				}
			}
		}
		
		
	}


	//for each table find elements of type
	
	//if Carrier - Orderflow - WhereUsed
	//  for each setType, find element change type
	//  for each switch, put in appinfo
	//  for each setReq, change minOccurs
	
	private boolean isSubjectElement(String path, Node n) {
		if (n.getNodeName() == path && n.getNodeType() == Node.ELEMENT_NODE ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
