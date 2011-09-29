package us.northdeck.xsomtest;

import com.sun.xml.xsom.parser.AnnotationParser;
import com.sun.xml.xsom.parser.AnnotationParserFactory;

public class MRBRAnnotationParserFactory implements AnnotationParserFactory {

	@Override
	public AnnotationParser create() {
		// TODO Auto-generated method stub
		return new MRBRAnnotationParser();
	}

}
