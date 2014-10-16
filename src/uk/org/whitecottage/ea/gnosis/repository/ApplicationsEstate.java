package uk.org.whitecottage.ea.gnosis.repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Applications;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Stage;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONObject;
import uk.org.whitecottage.ea.gnosis.json.JSONString;

public class ApplicationsEstate extends XmldbProcessor {

	protected Unmarshaller applicationsUnmarshaller = null;
	protected Marshaller applicationsMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.repository");

	public ApplicationsEstate(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext applicationsContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.applications");
		    applicationsUnmarshaller = createUnmarshaller(applicationsContext, context + "/WEB-INF/xsd/applications.xsd");
		    applicationsMarshaller = applicationsContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getApplicationsListJSON() {
		String result = null;

		Collection repository = null;
		XMLResource applicationsResource = null;
		try {
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
			
			JSONObject applicationsList = renderApplicationsList(applications);
			result = applicationsList.toJSON();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}

	public String getApplicationJSON(String applicationId) {
		String result = null;
		
		if (applicationId == null) {
			return "";
		}

		Collection repository = null;
		XMLResource applicationsResource = null;
		try {
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
			
			for (Application application: applications.getApplication()) {
				if (application.getAppId().equals(applicationId)) {
					JSONObject applicationJSON = renderApplication(application);
					result = applicationJSON.toJSON();
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}

	protected JSONMap renderApplication(Application application) {
		JSONMap data = new JSONMap();
		
		JSONString idJSON = new JSONString("applicationId", application.getAppId());
		data.put(idJSON);
		
		JSONString nameJSON = new JSONString("name", application.getName());
		data.put(nameJSON);
		
		JSONString descriptionJSON = new JSONString("description", application.getDescription());
		data.put(descriptionJSON);
		
		JSONArray capabilities = new JSONArray("capabilities");
		for (Classification classification: application.getClassification()) {
			JSONString classificationJSON = new JSONString(classification.getCapability());
			capabilities.add(classificationJSON);
		}
		data.put(capabilities);
		
		JSONArray lifecycle = new JSONArray("lifecycle");
		for (Stage stage: application.getStage()) {
			JSONMap stageJSON = new JSONMap();
			
			JSONString lifecycleJSON = new JSONString("stage", stage.getLifecycle());
			stageJSON.put(lifecycleJSON);
			
			XMLGregorianCalendar date = stage.getDate();
			if (date != null) {
				JSONString dateJSON = new JSONString("date", date.toXMLFormat());
				stageJSON.put(dateJSON);
			}
			
			lifecycle.add(stageJSON);
		}
		data.put(lifecycle);
		
		return data;
	}
	
	protected JSONMap renderApplicationsList(Applications applications) {
		JSONMap data = new JSONMap();
		
		JSONArray table = new JSONArray("data");
		
		for (Application application: applications.getApplication()) {
			table.add(renderApplication(application));
		}
		
		data.put(table);
		
		return data;
	}

	public void addApplication(String applicationId, String name, String description) {
		log.info("Id: " + applicationId);
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
			
			Application application = new Application();
			application.setAppId(applicationId);
			application.setName(name);
			if (description != null) {
				application.setDescription(description);
			} else {
				application.setDescription("");
			}

			applications.getApplication().add(application);
			
	    	// Create the DOM document
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	    	applicationsMarshaller.marshal(applications, doc);
	    	
			storeDomResource(repository, "applications.xml", doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}
	
	public void removeApplication(String applicationId) {
		log.info("Id: " + applicationId);
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Node applications = applicationsResource.getContentAsDOM();

		   	String query = "//application[@app-id='" + applicationId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element applicationNode = (Element) xpath.evaluate(query, applications, XPathConstants.NODE);
			if (applicationNode != null) {
				query = "/applications";
				Element parentNode = (Element) xpath.evaluate(query, applications, XPathConstants.NODE);
				parentNode.removeChild(applicationNode);
				
				storeDomResource(repository, "applications.xml", applications);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}
	
	public void updateApplicationBasic(String applicationId, String name, String description) {
		log.info("Id: " + applicationId);
		log.info("Name: " + name);
		log.info("Description: " + description);
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");

			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			application.setName(name);
		   			application.setDescription(description);
		   			
		   			update = true;
		   		}
		   	}
			
			if (update) {
		    	// Create the DOM document
		    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        dbf.setNamespaceAware(true);
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        Document doc = db.newDocument();
		    	applicationsMarshaller.marshal(applications, doc);
		    	
				storeDomResource(repository, "applications.xml", doc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}

	public void addCapability(String applicationId, String capability) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			Classification classification = new Classification();
		   			classification.setCapability(capability);
		   			
		   			application.getClassification().add(classification);
		   			update = true;
		   		}
		   	}
			
			if (update) {
		    	// Create the DOM document
		    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        dbf.setNamespaceAware(true);
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        Document doc = db.newDocument();
		    	applicationsMarshaller.marshal(applications, doc);
		    	
				storeDomResource(repository, "applications.xml", doc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}

	public void removeCapability(String applicationId, String capability) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Node applications = applicationsResource.getContentAsDOM();

		   	String query = "//application[@app-id='" + applicationId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element applicationNode = (Element) xpath.evaluate(query, applications, XPathConstants.NODE);
			if (applicationNode != null) {
				query = "classification[@capability='" + capability + "']";
				Node classificationNode = (Node) xpath.evaluate(query, applicationNode, XPathConstants.NODE);
				if (classificationNode != null) {
					applicationNode.removeChild(classificationNode);
		
					storeDomResource(repository, "applications.xml", applications);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}

	public void updateLifecycle(String applicationId, String stage, String mode, String date) {
		log.info("Id: " + applicationId);
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    Date stageDate = null;
		    
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    if (!date.equals("")) {
			    stageDate = format.parse(date);
		    }

		    Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

		    boolean update = false;
		    for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			if (date.equals("")) {
			   			for (Stage stageElement: application.getStage()) {
			   				if (stageElement.getDate() == null) {
				   				stageElement.setLifecycle(stage);
				   				update = true;
			   				}
			   			}
			   			
			   			if (!update) {
			   				mode = "new";
			   			}
		   			} else {
			   			for (Stage stageElement: application.getStage()) {
			   				if (stageElement.getLifecycle().equals(stage)) {
								GregorianCalendar cal = new GregorianCalendar();
								cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
								cal.setTime(stageDate);
								stageElement.setDate(getXMLDate(cal));
				   				update = true;
			   				}
			   			}
		   			}
		   			
		   			if (mode.equals("new") && !update) {
		   				Stage stageElement = new Stage();
		   				stageElement.setLifecycle(stage);
		   				if (stageDate != null) {
							GregorianCalendar cal = new GregorianCalendar();
							cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
							cal.setTime(stageDate);
							stageElement.setDate(getXMLDate(cal));
		   				}
		   				
		   				application.getStage().add(stageElement);
		   				update = true;
		   			}
		   			
		   			break;
		   		}
		   	}
			
			if (update) {
		    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        dbf.setNamespaceAware(true);
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        Document doc = db.newDocument();
		    	applicationsMarshaller.marshal(applications, doc);
		    	
				storeDomResource(repository, "applications.xml", doc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}

	protected XMLGregorianCalendar getXMLDate(GregorianCalendar cal) {
		XMLGregorianCalendar xgc = null;
				
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			xgc = df.newXMLGregorianCalendar();
			xgc.setDay(cal.get(Calendar.DAY_OF_MONTH));
			xgc.setMonth(cal.get(Calendar.MONTH) + 1);
			xgc.setYear(cal.get(Calendar.YEAR));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		return xgc;
	}
	
	public void removeLifecycle(String applicationId, String stage) {
		log.info("Id: " + applicationId);
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Node applications = applicationsResource.getContentAsDOM();

		   	String query = "//application[@app-id='" + applicationId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element applicationNode = (Element) xpath.evaluate(query, applications, XPathConstants.NODE);
			if (applicationNode != null) {
				query = "./stage[@lifecycle='" + stage + "']";
				log.info("Query: " + query);
				Element stageNode = (Element) xpath.evaluate(query, applicationNode, XPathConstants.NODE);
				if (stageNode != null) {
					applicationNode.removeChild(stageNode);
				} else {
					log.info("Stage not found");
				}
				
				storeDomResource(repository, "applications.xml", applications);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}
}
