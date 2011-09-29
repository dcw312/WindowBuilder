package us.northdeck.xsomtest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MRBRSchema  {
	private Document document;
	private String ns = "http://www.w3.org/2001/XMLSchema";
	
	MRBRSchema (Document document) {
		this.document = document;
	}
	
	public Element createElement(String name) {
		Element el = document.createElementNS(ns, name);
		el.setPrefix("xs");
		return el;
	}
}
