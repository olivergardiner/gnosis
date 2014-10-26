package uk.org.whitecottage.ea.gnosis.repository;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Parent;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Predecessor;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Process;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessFlow;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessInstance;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONBoolean;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;

public class ProcessFlows extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.framework");

	public ProcessFlows(String URI, String repositoryRoot, String context) {
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
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			JSONMap businessProcesses = new JSONMap();
			JSONArray processDomains = new JSONArray("processDomains");
			businessProcesses.put(processDomains);

			for (ProcessDomain domain: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessDomain()) {
				processDomains.add(renderProcessDomain(domain));
			}

			JSONArray processFlows = new JSONArray("processFlows");
			businessProcesses.put(processFlows);
			
			for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
				processFlows.add(renderProcessFlow(flow));
			}

			result = businessProcesses.toJSON();

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

	protected JSONMap renderProcessDomain(ProcessDomain domain) {
		JSONMap domainJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", domain.getDomainId());
		domainJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", domain.getName());
		domainJSON.put(nameJSON);
				
		JSONString valueChainJSON = new JSONString("valueChain", domain.getName());
		domainJSON.put(valueChainJSON);
				
		JSONString descriptionJSON = new JSONString("description", domain.getDescription());
		domainJSON.put(descriptionJSON);
		
		JSONArray processesJSON = new JSONArray("processes");
		domainJSON.put(processesJSON);
								
		for (Process process: domain.getProcess()) {
			processesJSON.add(renderProcess(process));
		}
		
		return domainJSON;
	}
	
	protected JSONMap renderProcess(Process process) {
		JSONMap processJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("id", process.getProcessId());
		processJSON.put(idJSON);
				
		JSONString nameJSON = new JSONString("name", process.getName());
		processJSON.put(nameJSON);
				
		JSONString descriptionJSON = new JSONString("description", process.getDescription());
		processJSON.put(descriptionJSON);
								
		return processJSON;
	}

	protected JSONMap renderProcessFlow(ProcessFlow processFlow) {
		JSONMap processJSON = new JSONMap();
		
		JSONString nameJSON = new JSONString("name", processFlow.getName());
		processJSON.put(nameJSON);
				
		JSONString idJSON = new JSONString("id", processFlow.getFlowId());
		processJSON.put(idJSON);
				
		JSONArray instancesJSON = new JSONArray("instances");
		processJSON.put(instancesJSON);
		
		for (ProcessInstance instance: processFlow.getProcessInstance()) {
			instancesJSON.add(renderProcessInstance(instance));
		}
		
		return processJSON;
	}
	
	protected JSONMap renderProcessInstance(ProcessInstance instance) {
		JSONMap instanceJSON = new JSONMap();
		
		JSONString idJSON = new JSONString("process", instance.getProcessId());
		instanceJSON.put(idJSON);
		
		if (instance.getDuration() != null) {
			JSONString durationJSON = new JSONString("duration", instance.getDuration());
			instanceJSON.put(durationJSON);
		}

		JSONArray parentsJSON = new JSONArray("parents");
		instanceJSON.put(parentsJSON);
		
		for (Parent parent: instance.getParent()) {
			JSONString parentJSON = new JSONString(parent.getProcess());
			parentsJSON.add(parentJSON);
		}
		
		JSONArray predecessorsJSON = new JSONArray("predecessors");
		instanceJSON.put(predecessorsJSON);
		
		for (Predecessor predecessor: instance.getPredecessor()) {
			JSONMap predecessorJSON = new JSONMap();
			predecessorsJSON.add(predecessorJSON);

			JSONString processJSON = new JSONString("predecessor", predecessor.getProcess());
			predecessorJSON.put(processJSON);
			
			if (predecessor.isContiguous() != null && !predecessor.isContiguous()) {
				predecessorJSON.put(new JSONBoolean("contiguous", false));
			} else {
				predecessorJSON.put(new JSONBoolean("contiguous", true));
			}
		}
		
		return instanceJSON;
	}

	public void updateProcessInstance(String flowId, String instanceId, String duration, String mode) {
		log.info("Updating process instance: " + flowId + ", " + instanceId + ", " + duration + ", " + mode);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
				if (flow.getFlowId().equals(flowId)) {
					if (mode.equals("add")) {
						ProcessInstance instance = new ProcessInstance();
						instance.setProcessId(instanceId);
						instance.setDuration(duration);
						flow.getProcessInstance().add(instance);
					} else {
						for (ProcessInstance instance: flow.getProcessInstance()) {
							if (instance.getProcessId().equals(instanceId)) {
								instance.setDuration(duration);
							}
						}
					}
				}
			}
			
	    	// Create the DOM document
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	    	frameworkMarshaller.marshal(framework, doc);
	    	
			storeDomResource(repository, "framework.xml", doc);
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
	}

	public void moveProcessInstance(String flowId, String instanceId, int position) {
		log.info("Moving process instance: " + flowId + ", " + instanceId + ", " + position);
		
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			for (ProcessFlow flow: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow()) {
				if (flow.getFlowId().equals(flowId)) {
					for (ProcessInstance instance: flow.getProcessInstance()) {
						if (instance.getProcessId().equals(instanceId)) {
							flow.getProcessInstance().remove(instance);
							flow.getProcessInstance().add(position, instance);
							break;
						}
					}
				}
			}

			// Create the DOM document
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	    	frameworkMarshaller.marshal(framework, doc);
	    	
			storeDomResource(repository, "framework.xml", doc);
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
	}

	public void deleteItem(String flowId, String instanceId, String dependencyId, String type) {
		log.info("Deleting item: " + flowId + ", " + instanceId + ", " + dependencyId + ", " + type);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			List<ProcessFlow> flows =  framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow();
			for (ProcessFlow flow: flows) {
				if (flow.getFlowId().equals(flowId)) {
					if (type.equals("flow")) {
						flows.remove(flow);
					} else {
						for (ProcessInstance instance: flow.getProcessInstance()) {
							if (instance.getProcessId().equals(instanceId)) {
								if (type.equals("instance")) {
									log.info("removing instance");
									flow.getProcessInstance().remove(instance);
								} else {
									if (type.equals("parent")) {
										for (Parent parent: instance.getParent()) {
											if (parent.getProcess().equals(dependencyId)) {
												instance.getParent().remove(parent);
												break;
											}
										}
									} else if (type.equals("predecessor")) {
										for (Predecessor predecessor: instance.getPredecessor()) {
											if (predecessor.getProcess().equals(dependencyId)) {
												instance.getPredecessor().remove(predecessor);
												break;
											}
										}
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
			
	    	// Create the DOM document
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	    	frameworkMarshaller.marshal(framework, doc);
	    	
			storeDomResource(repository, "framework.xml", doc);
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
	}
}
