package uk.org.whitecottage.xmldb;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.exist.xmldb.XQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public abstract class XmldbProcessor {
	protected String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
	protected String driver = "org.exist.xmldb.DatabaseImpl";
	protected String repositoryRoot = "/db/gnosis";
	protected String username = "gnosis";
	protected String password = "gnosis";

	protected Database database;
	protected SchemaFactory schemaFactory;

	public XmldbProcessor(String URI, String repositoryRoot) {
		this.URI = URI;
		this.repositoryRoot = repositoryRoot;
		
    	schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			database = (Database) Class.forName(driver).newInstance();
		    database.setProperty("create-database", "true");
		    DatabaseManager.registerDatabase(database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public void setRepositoryRoot(String repositoryRoot) {
		this.repositoryRoot = repositoryRoot;
	}

	protected Unmarshaller createUnmarshaller(JAXBContext context, String schemaName) throws SAXException, JAXBException {
	    Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaName));

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(schema);
		
		return unmarshaller;
	}
	
	protected Collection getCollection(String collectionName) throws XMLDBException {
		Collection collection = DatabaseManager.getCollection(URI + repositoryRoot + collectionName, username, password);
		collection.setProperty(OutputKeys.INDENT, "no");
				
		return collection;
	}

	protected XMLResource getResource(Collection collection, String resourceName) throws XMLDBException {
	    XMLResource resource = (XMLResource) collection.getResource(resourceName);
	    
		return resource;
	}
	
	protected ResourceSet queryCollection(Collection collection, String query) throws XMLDBException {
		XQueryService xqs = (XQueryService)collection.getService("XQueryService", "1.0");
		CompiledExpression compiledQuery = xqs.compile(query);
		
		return xqs.execute(compiledQuery);
	}
	
	protected void storeJaxbResource(Collection collection, String resourceName, Object jaxbElement, Marshaller marshaller) throws XMLDBException, JAXBException, ParserConfigurationException {
    	storeDomResource(collection, resourceName, createDomFromJaxb(jaxbElement, marshaller));
	}
	
	protected Node createDomFromJaxb(Object jaxbElement, Marshaller marshaller) throws ParserConfigurationException, JAXBException {
	   	// Create the DOM document
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
    	marshaller.marshal(jaxbElement, doc);
		return doc;
	}

	protected void storeDomResource(Collection collection, String resourceName, Node node) throws XMLDBException {
    	// Convert the DOM document into an XMLResource
    	XMLResource updateResource = (XMLResource) collection.createResource(resourceName, "XMLResource");
    	updateResource.setContentAsDOM(node);
    	
    	// Store the XMLResource
    	collection.storeResource(updateResource);
	}
}