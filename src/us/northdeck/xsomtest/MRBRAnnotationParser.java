package us.northdeck.xsomtest;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.sun.xml.xsom.parser.AnnotationContext;
import com.sun.xml.xsom.parser.AnnotationParser;

public class MRBRAnnotationParser extends AnnotationParser {
	DecisionTableHandler dth;

	@Override
	public ContentHandler getContentHandler(AnnotationContext context,
			String parentElementName, ErrorHandler errorHandler,
			EntityResolver entityResolver) {
		dth = new DecisionTableHandler();
		return dth;
	}

	@Override
	public Object getResult(Object existing) {
		// TODO Auto-generated method stub
		
		return dth;
	}

}
