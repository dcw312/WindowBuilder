package us.northdeck.SupplementalFileMaker;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import javax.xml.xpath.XPathConstants;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.*;
import org.xml.sax.SAXException;

public class SupplementalFileMaker {
	
	String xmlNs = "http://www.w3.org/2001/XMLSchema";
	private XPath xpath;
	private TreeWalker tw;

//	•	Make a copy of flattened schema
//	•	Start at the root element of flattened CAP schema
//	•	Go to Body
//	o	advance pointer in sample xml
//	•	Combine all compositor’s into all
//	•	Go to all
//	•	 Insert wildcard
//	•	Delete all required elements
//	•	If sample element is present, mark element required
//	•	delete element if no samples are present

	Document getDom(File f) throws ParserConfigurationException, SAXException, IOException {
		Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
		return d;
	}
	
	DocumentTraversal getTraversal(Document d) {
		DocumentTraversal traversal = (DocumentTraversal) d;
		return traversal;
	}
	
	void createTreeWalker (Document d) {
		int types = NodeFilter.SHOW_ELEMENT;
		tw = getTraversal(d).createTreeWalker(d, types, null, false);
	}
	
	Element getRootElement(Document d, DocumentTraversal dt) {
		int types = NodeFilter.SHOW_ELEMENT;
		TreeWalker tw = dt.createTreeWalker(d, types, null, false);
		Element e = (Element) tw.firstChild().getFirstChild();
		return e;
	}
	
	
	
	Element getFirstChildElement(Node d, String expression) throws XPathExpressionException {
		//String expression = "/xs:element[1]";
		return (Element) xpath.evaluate(expression, d, XPathConstants.NODE);
	}
	
	Element insertAllElement(Document d, Element parent ) throws XPathExpressionException {
		Element newChild = d.createElementNS(this.xmlNs, "all");
		String expression = "/[xs:sequence|xs:choice][1]";
		Element firstXsElement = this.getFirstChildElement(parent, expression );
		d.insertBefore(newChild, firstXsElement);
		return newChild;
	}
	
	Element getNextSibling (Document d, Element e) {
		Element sib = (Element) tw.firstChild();
		return sib;
	}
	
	void moveSiblingsChildren(Document d, Element firstSibling) {
		//nextSibling = firstSibling.getn
	}
	

	/**
	 * @return
	 */
	private XPath getXPathObject() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		return xpath;
	}
	
}
