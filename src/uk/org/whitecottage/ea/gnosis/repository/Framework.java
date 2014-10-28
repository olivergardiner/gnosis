package uk.org.whitecottage.ea.gnosis.repository;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.BusinessOperatingModel;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.PrimaryActivity;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.SupportActivity;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class Framework extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.framework");

	public Framework(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext frameworkContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(frameworkContext, context + "/WEB-INF/xsd/framework.xsd");
		    frameworkMarshaller = frameworkContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getJSON() {
		String result = null;

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
		    uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework framework = (uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			JSONMap frameworkJSON = new JSONMap();
			
			frameworkJSON.put(renderOperatingModel(framework.getBusinessOperatingModel()));

			frameworkJSON.put(renderTechnologyDomainList(framework.getBusinessApplications().getTechnologyDomain(), "businessApplications"));

			frameworkJSON.put(renderTechnologyDomainList(framework.getCommonServices().getTechnologyDomain(), "commonServices"));

			frameworkJSON.put(renderTechnologyDomainList(framework.getInfrastructure().getTechnologyDomain(), "infrastructure"));

			frameworkJSON.put(renderValueChain(framework.getValueChain()));
						
			result = frameworkJSON.toJSON();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(frameworkResource != null) {
		        try { ((EXistResource) frameworkResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}
	
	protected JSONMap renderOperatingModel(BusinessOperatingModel operatingModel) {
		JSONMap operatingModelJSON = new JSONMap("operatingModel");
		
		JSONMap organisationJSON = new JSONMap("organisation");
		operatingModelJSON.put(organisationJSON);
		
		JSONMap processFlowsJSON = new JSONMap("processFlows");
		operatingModelJSON.put(processFlowsJSON);
		
		JSONMap informationJSON = new JSONMap("information");
		operatingModelJSON.put(informationJSON);
		
		return operatingModelJSON;
	}

	protected JSONArray renderTechnologyDomainList(List<TechnologyDomain> technologyDomains, String layer) {
		
		JSONArray layerJSON = new JSONArray(layer);
		
		for (TechnologyDomain domain: technologyDomains) {
			layerJSON.add(renderTechnologyDomain(domain));
		}
		
		return layerJSON;
	}
	
	protected JSONMap renderTechnologyDomain(TechnologyDomain domain) {
		JSONMap domainJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", domain.getDomainId());
		domainJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", domain.getName());
		domainJSON.put(nameJSON);
				
		JSONString descriptionJSON = new JSONString("description", domain.getDescription());
		domainJSON.put(descriptionJSON);
				
		if (domain.getValueChain() != null) {
			JSONString valueChainJSON = new JSONString("valueChain", domain.getValueChain());
			domainJSON.put(valueChainJSON);
		}
		
		JSONArray capabilitiesJSON = new JSONArray("capabilities");
		domainJSON.put(capabilitiesJSON);
		
		for (Capability capability: domain.getCapability()) {
			capabilitiesJSON.add(renderCapability(capability));
		}
		
		return domainJSON;
	}
	
	protected JSONMap renderCapability(Capability capability) {
		JSONMap capabilityJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", capability.getCapabilityId());
		capabilityJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", capability.getName());
		capabilityJSON.put(nameJSON);
				
		JSONString descriptionJSON = new JSONString("description", capability.getDescription());
		capabilityJSON.put(descriptionJSON);
				
		return capabilityJSON;
	}

	protected JSONMap renderValueChain(ValueChain valueChain) {
		JSONMap valueChainJSON = new JSONMap("valueChain");
		
		JSONArray primaryActivitiesJSON = new JSONArray("primaryActivities");
		valueChainJSON.put(primaryActivitiesJSON);
		for (PrimaryActivity activity: valueChain.getPrimaryActivity()) {
			primaryActivitiesJSON.add(renderPrimaryActivity(activity));
		}
		
		JSONArray supportActivitiesJSON = new JSONArray("supportActivities");
		valueChainJSON.put(supportActivitiesJSON);
		for (SupportActivity activity: valueChain.getSupportActivity()) {
			supportActivitiesJSON.add(renderSupportActivity(activity));
		}
		
		return valueChainJSON;
	}
	
	protected JSONMap renderPrimaryActivity(PrimaryActivity activity) {
		JSONMap activityJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", activity.getValueChainId());
		activityJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", activity.getValue());
		activityJSON.put(nameJSON);
		
		return activityJSON;
	}
	
	protected JSONMap renderSupportActivity(SupportActivity activity) {
		JSONMap activityJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", activity.getValueChainId());
		activityJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", activity.getValue());
		activityJSON.put(nameJSON);
		
		return activityJSON;
	}
}
