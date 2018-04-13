package uk.org.whitecottage.gnosis.uml.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FixXMI {
    private static Logger logger = Logger.getLogger(FixXMI.class.getCanonicalName());

	public static void main(String[] args) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse("model.uml");

			XMICleaner cleaner = new XMICleaner(doc);
			cleaner.removeDefaultValues();
			cleaner.makeTypeReferencesLocal();
			
			DOMSource source = new DOMSource(doc);
		    FileWriter writer = new FileWriter(new File("model-new.uml"));
		    StreamResult result = new StreamResult(writer);

		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.transform(source, result);
		    
		} catch (ParserConfigurationException e) {
			logger.severe(e.getMessage());
		} catch (SAXException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} catch (TransformerException e) {
			logger.severe(e.getMessage());
		}
	}
}
