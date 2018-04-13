package uk.org.whitecottage.gnosis.uml.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMICleaner {
    private static Logger logger = Logger.getLogger(XMICleaner.class.getCanonicalName());
	protected Document document;
	protected XPathFactory xPathfactory;
	protected XPath xpath;
	
	public XMICleaner(Document document) {
		this.document = document;
		xPathfactory = XPathFactory.newInstance();
		xpath = xPathfactory.newXPath();
	}
	
	public void removeDefaultValues() {
		try {
			XPathExpression expr = xpath.compile("//defaultValue");
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				logger.info(node.getNodeName());
				node.getParentNode().removeChild(node);
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		
	}
	
	public void makeTypeReferencesLocal() {
		try {
			XPathExpression expr = xpath.compile("//type[@href]");
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nl.getLength(); i++) {
				Element type = (Element) (nl.item(i));
				String ref = type.getAttribute("href");
				logger.info(ref);
				if (!ref.startsWith("http:")) {
					String local = ref.split("#")[1];
					logger.log(Level.INFO, "Converting {0} to {1}", new String[] {ref, local});
					
					type.setAttribute("xmi:idref", local);
					type.removeAttribute("href");
				}
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
