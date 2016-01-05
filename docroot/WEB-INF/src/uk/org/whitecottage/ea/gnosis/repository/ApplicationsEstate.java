package uk.org.whitecottage.ea.gnosis.repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Ecosystem;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Investment;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Migration;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Stage;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Tag;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONInteger;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONObject;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ApplicationsEstate extends XmldbProcessor {

	protected Unmarshaller applicationsUnmarshaller = null;
	protected Marshaller applicationsMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ApplicationsEstate.class);

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
			//log.info("JSON data: " + result);

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
		
		JSONArray ecosystems = new JSONArray("ecosystems");
		for (Ecosystem ecosystem: application.getEcosystem()) {
			JSONMap ecosystemJSON = new JSONMap();

			JSONString ecosystemIdJSON = new JSONString("ecosystemId", ecosystem.getEcosystem());
			ecosystemJSON.put(ecosystemIdJSON);
			
			JSONArray capabilitiesJSON = new JSONArray("capabilities");
			for (Capability capability: ecosystem.getCapability()) {
				JSONString capabilityJSON = new JSONString(capability.getCapability());
				capabilitiesJSON.add(capabilityJSON);
			}
			
			ecosystemJSON.put(capabilitiesJSON);
			
			ecosystems.add(ecosystemJSON);
		}
		data.put(ecosystems);
		
		JSONArray tags = new JSONArray("tags");
		for (Tag tag: application.getTag()) {
			JSONMap tagJSON = new JSONMap();
			
			JSONString taxonomyIdJSON = new JSONString("taxonomyId", tag.getTaxonomyId());
			tagJSON.put(taxonomyIdJSON);

			JSONString termIdJSON = new JSONString("termId", tag.getTermId());
			tagJSON.put(termIdJSON);
			
			tags.add(tagJSON);
		}
		data.put(tags);
				
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
		
		int index = 0;
		JSONArray investments = new JSONArray("milestones");
		for (Investment investment: application.getInvestment()) {
			JSONMap investmentJSON = new JSONMap();
			
			JSONInteger indexJSON = new JSONInteger("index", index++);
			investmentJSON.put(indexJSON);
			
			JSONString decriptionJSON = new JSONString("description", investment.getValue());
			investmentJSON.put(decriptionJSON);
			
			XMLGregorianCalendar date = investment.getDate();
			if (date != null) {
				JSONString dateJSON = new JSONString("date", date.toXMLFormat());
				investmentJSON.put(dateJSON);
			}
			
			investments.add(investmentJSON);
		}
		data.put(investments);
		
		index = 0;
		JSONArray migrations = new JSONArray("migrations");
		for (Migration migration: application.getMigration()) {
			JSONMap migrationJSON = new JSONMap();
			
			JSONInteger indexJSON = new JSONInteger("index", index++);
			migrationJSON.put(indexJSON);
			
			JSONString decriptionJSON = new JSONString("target", migration.getTo());
			migrationJSON.put(decriptionJSON);
			
			XMLGregorianCalendar date = migration.getDate();
			if (date != null) {
				JSONString dateJSON = new JSONString("date", date.toXMLFormat());
				migrationJSON.put(dateJSON);
			}
			
			migrations.add(migrationJSON);
		}
		data.put(migrations);
		
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
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			List<Classification> classifications = application.getClassification();
		   			for (Classification classification: application.getClassification()) {
			   			if (classification.getCapability().equals(capability)) {
			   				classifications.remove(classification);
			   				update = true;
			   				break;
			   			}
		   			}
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

	public void addTag(String applicationId, String taxonomyId, String termId) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			Tag tag = new Tag();
		   			tag.setTaxonomyId(taxonomyId);
		   			tag.setTermId(termId);
		   			
		   			application.getTag().add(tag);
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

	public void removeTag(String applicationId, String taxonomyId, String termId) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			List<Tag> tags = application.getTag();
		   			for (Tag tag: application.getTag()) {
			   			if (tag.getTaxonomyId().equals(taxonomyId) && tag.getTermId().equals(termId)) {
			   				tags.remove(tag);
			   				update = true;
			   				break;
			   			}
		   			}
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

	public void addEcosystem(String applicationId, String ecosystemId, String capabilityId) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
   					Capability capability = new Capability();
   					capability.setCapability(capabilityId);

   					boolean found = false;
		   			for (Ecosystem ecosystem: application.getEcosystem()) {
		   				if (ecosystem.getEcosystem().equals(ecosystemId)) {
		   					found = true;
		   					ecosystem.getCapability().add(capability);
		   				}
		   			}
		   			
		   			if (!found) {
		   				Ecosystem ecosystem = new Ecosystem();
		   				ecosystem.setEcosystem(ecosystemId);
	   					ecosystem.getCapability().add(capability);
	   					application.getEcosystem().add(ecosystem);
		   			}
		   			
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

	public void removeEcosystem(String applicationId, String ecosystemId, String capabilityId) {
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

			boolean update = false;
			
			for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			for (Ecosystem ecosystem: application.getEcosystem()) {
		   				if (ecosystem.getEcosystem().equals(ecosystemId)) {
				   			for (Capability capability: ecosystem.getCapability()) {
					   			if (capability.getCapability().equals(capabilityId)) {
					   				ecosystem.getCapability().remove(capability);
					   				update = true;
					   				break;
					   			}
				   			}
		   				}
		   				if (update) {
		   					if (ecosystem.getCapability().isEmpty()) {
		   						application.getEcosystem().remove(ecosystem);
		   					}
		   					break;
		   				}
		   			}
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

	public void updateInvestment(String applicationId, String index, String mode, String date, String description, String capital, String runrate) {
		log.info("Id: " + applicationId);

		try {
			Integer.parseInt(capital);
		} catch (Exception e) {
			capital = "0";
		}
		
		try {
			Integer.parseInt(runrate);
		} catch (Exception e) {
			runrate = "0";
		}
		
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    Date investmentDate = null;
		    
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    if (!date.equals("")) {
			    investmentDate = format.parse(date);
		    }

		    Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

		    boolean update = false;
		    for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			
		   			if (mode.equals("edit")) {
		   				Investment investment = application.getInvestment().get(Integer.parseInt(index));
		   				investment.setValue(description);
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(investmentDate);
		   				investment.setDate(getXMLDate(cal));
		   				investment.setCapital(new BigInteger(capital));
		   				investment.setRunrate(new BigInteger(runrate));
		   				
		   				update = true;
		   			}
		   			
		   			if (mode.equals("new") && !update) {
		   				Investment investment = new Investment();
		   				investment.setValue(description);
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(investmentDate);
		   				investment.setDate(getXMLDate(cal));
		   				investment.setCapital(new BigInteger(capital));
		   				investment.setRunrate(new BigInteger(runrate));
		   				
		   				application.getInvestment().add(investment);

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

	public void updateMigration(String applicationId, String index, String mode, String date, String to) {
		log.info("Id: " + applicationId);

		Collection repository = null;
		XMLResource applicationsResource = null;
		try {   
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
		    Date migrationDate = null;
		    
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    if (!date.equals("")) {
			    migrationDate = format.parse(date);
		    }

		    Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());

		    boolean update = false;
		    for (Application application: applications.getApplication()) {
		   		if (application.getAppId().equals(applicationId)) {
		   			
		   			if (mode.equals("edit")) {
		   				Migration migration = application.getMigration().get(Integer.parseInt(index));
		   				migration.setTo(to);;
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(migrationDate);
		   				migration.setDate(getXMLDate(cal));
		   				
		   				update = true;
		   			}
		   			
		   			if (mode.equals("new") && !update) {
		   				Migration migration = new Migration();
		   				migration.setTo(to);;
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(migrationDate);
		   				migration.setDate(getXMLDate(cal));
		   				
		   				application.getMigration().add(migration);

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
	
	public void removeLifecycle(String applicationId, String stage, String milestone, String to) {
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
				if (stage != null) {
					query = "./stage[@lifecycle='" + stage + "']";
				} else if (milestone != null) {
					int index = Integer.parseInt(milestone) + 1;
					query = "./investment[" + index + "]";
				} else if (to != null) {
					int index = Integer.parseInt(to) + 1;
					query = "./migration[" + index + "]";
				}
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
