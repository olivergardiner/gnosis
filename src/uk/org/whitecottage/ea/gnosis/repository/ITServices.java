package uk.org.whitecottage.ea.gnosis.repository;

import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.BusinessProcesses;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Process;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.RecycleBin;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Dependency;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Services;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Tower;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class ITServices extends XmldbProcessor {
	protected Unmarshaller servicesUnmarshaller = null;
	protected Marshaller servicesMarshaller = null;
	
	protected Services services;

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.repository");

	public ITServices(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext servicesContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.services");
		    servicesUnmarshaller = createUnmarshaller(servicesContext, context + "/WEB-INF/xsd/services.xsd");
		    servicesMarshaller = servicesContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getJSON() {
		return getJSON(true);
	}
	
	public String getJSON(boolean withRecycleBin) {
		String result = null;

		Collection repository = null;
		XMLResource servicesResource = null;
		try {   
			repository = getCollection("");
		    servicesResource = getResource(repository, "services.xml");
			services = (Services) servicesUnmarshaller.unmarshal(servicesResource.getContentAsDOM());
			
			//BusinessProcesses businessProcesses = framework.getBusinessOperatingModel().getBusinessProcesses();
			
			JSTree tree = new JSTree();

			for (Tower tower: services.getTower()) {
				tree.add(renderTower(tower));
			}
			
			/*if (withRecycleBin) {
				tree.add(renderTrash(services.getRecycleBin()));
			}*/
			
			/*for (PrimaryActivity activity: framework.getValueChain().getPrimaryActivity()) {
				tree.add(renderActivity(businessProcesses, activity.getValueChainId(), activity.getValue()));
			}
			
			for (SupportActivity activity: framework.getValueChain().getSupportActivity()) {
				tree.add(renderActivity(businessProcesses, activity.getValueChainId(), activity.getValue()));
			}
			
			if (withRecycleBin) {
				tree.add(renderTrash(framework.getRecycleBin()));
			}*/
			
			result = tree.toJSON();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(servicesResource != null) {
		        try { ((EXistResource) servicesResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}

	protected JSTreeNode renderTower(Tower tower) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", tower.getId());
		data.put(idJSON);

		JSONString descriptionJSON = new JSONString("description", tower.getDescription());
		data.put(descriptionJSON);

		JSTreeNode towerJSON = new JSTreeNode(tower.getName(), "tower", data);
		
		for (Dependency dependency: tower.getDependency()) {
			towerJSON.getChildren().add(renderDependency(dependency));
		}
		
		return towerJSON;
	}
	
	protected JSTreeNode renderDependency(Dependency dependency) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("target", dependency.getId());
		data.put(idJSON);
				
		/*JSONString descriptionJSON = new JSONString("description", process.getDescription());
		data.put(descriptionJSON);*/
		String target = findTargetName(dependency);
								
		JSTreeNode dependencyJSON = new JSTreeNode(target, "process", data);
		
		return dependencyJSON;
	}
	
	protected String findTargetName(Dependency dependency) {
		return "";
	}
		
	protected JSTreeNode renderActivity(BusinessProcesses businessProcesses, String activityId, String name) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", activityId);
		data.put(idJSON);

		JSTreeNode activityJSON = new JSTreeNode(name, "activity", data);
		
		for (ProcessDomain domain: businessProcesses.getProcessDomain()) {
			if (domain.getValueChain().equals(activityId)) {
				activityJSON.getChildren().add(renderProcessDomain(domain));
			}
		}
		
		return activityJSON;
	}
	
	protected JSTreeNode renderProcessDomain(ProcessDomain domain) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", domain.getDomainId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", domain.getDescription());
		data.put(descriptionJSON);
								
		JSTreeNode domainJSON = new JSTreeNode(domain.getName(), "domain", data);
		
		for (Process process: domain.getProcess()) {
			domainJSON.getChildren().add(renderProcess(process));
		}
		
		return domainJSON;
	}
	
	protected JSTreeNode renderProcess(Process process) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", process.getProcessId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", process.getDescription());
		data.put(descriptionJSON);
								
		JSTreeNode processJSON = new JSTreeNode(process.getName(), "process", data);
		
		return processJSON;
	}
		
	protected JSTreeNode renderTrash(RecycleBin recycleBin) {
		JSTreeNode trash = new JSTreeNode("Recycle bin", "trash");
		JSONString trashId = new JSONString("id", "trash");
		trash.put(trashId);
		
		JSONArray children = trash.getChildren();
		
		if (recycleBin != null) {
			for (Object o: recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity()) {
				if (o instanceof ProcessDomain) {
					children.add(renderProcessDomain((ProcessDomain) o));
				} else if (o instanceof Process) {
					children.add(renderProcess((Process) o));
				}
			}
		}
		
		return trash;
	}
	
	public void updateProcessDomain(String domainId, String name, String description) {
		log.info("Updating domain: " + domainId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//processDomain[@domain-id='" + domainId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			domainNode.setAttribute("name", name);
			((Element) domainNode.getElementsByTagName("description").item(0)).setTextContent(description);

			storeDomResource(repository, "framework.xml", framework);
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
	
	public void updateProcess(String processId, String name, String description) {
		log.info("Updating process: " + processId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//process[@process-id='" + processId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element processNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			processNode.setAttribute("name", name);
			((Element) processNode.getElementsByTagName("description").item(0)).setTextContent(description);

			storeDomResource(repository, "framework.xml", framework);
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
	
	public void createProcessDomain(String valueChain, String domainId, String name, String description) {
		log.info("Creating domain: " + domainId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();
			
		   	String query = "/framework/businessOperatingModel/businessProcesses";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element processesNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
		   	query = "/framework/businessOperatingModel/businessProcesses/processFlow[1]";
			xpath = XPathFactory.newInstance().newXPath();
			Element beforeNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			Element domainNode = processesNode.getOwnerDocument().createElement("processDomain");
			Element descriptionNode = processesNode.getOwnerDocument().createElement("description");
			descriptionNode.setTextContent(description);
			domainNode.setAttribute("domain-id", domainId);
			domainNode.setAttribute("name", name);
			domainNode.setAttribute("value-chain", valueChain);
			domainNode.appendChild(descriptionNode);
			
			if (beforeNode != null) {
				processesNode.insertBefore(domainNode, beforeNode);
			} else {
				processesNode.appendChild(domainNode);
			}

			storeDomResource(repository, "framework.xml", framework);
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
	
	public void createProcess(String domainId, String processId, String name, String description) {
		log.info("Creating capability: " + processId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//processDomain[@domain-id='" + domainId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			Element processNode = domainNode.getOwnerDocument().createElement("process");
			Element descriptionNode = domainNode.getOwnerDocument().createElement("description");
			descriptionNode.setTextContent(description);
			processNode.setAttribute("process-id", processId);
			processNode.setAttribute("name", name);
			processNode.appendChild(descriptionNode);
			
			domainNode.appendChild(processNode);

			storeDomResource(repository, "framework.xml", framework);
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
	
	public void moveProcessDomain(String domainId, String parentId, String before) {
		log.info("Moving technology domain: " + domainId + " " + parentId + " " + before);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "/framework";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element frameworkNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			query = "//businessProcesses";
			Element processesNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			query = "//processDomain[@domain-id='" + domainId + "']";
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			query = "//processDomain[@domain-id='" + before + "']";
			Element beforeNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			if (beforeNode == null) {
			   	query = "/framework/businessOperatingModel/businessProcesses/processFlow[0]";
				xpath = XPathFactory.newInstance().newXPath();
				beforeNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			}

			Element oldParentNode = (Element) domainNode.getParentNode();

			if (!parentId.equals("trash")) {
				
				domainNode = (Element) oldParentNode.removeChild(domainNode);
				domainNode.setAttribute("value-chain", parentId);
				//processesNode.insertBefore(domainNode, processesNode.getElementsByTagName("processDomain").item(position));

				if (beforeNode != null) {
					processesNode.insertBefore(domainNode, beforeNode);
				} else {
					processesNode.appendChild(domainNode);
				}
			} else {
				Element trash = (Element) frameworkNode.getElementsByTagName("recycleBin").item(0);
				if (trash == null) {
					trash = frameworkNode.getOwnerDocument().createElement("recycleBin");
					frameworkNode.appendChild(trash);
				}
				domainNode = (Element) oldParentNode.removeChild(domainNode);
				trash.appendChild(domainNode);
			}
			
			storeDomResource(repository, "framework.xml", framework);
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
	
	public void moveProcess(String processId, String parentId, String before) {
		log.info("Moving process: " + processId + ", " + parentId + ", " + before);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "/framework";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element frameworkNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

		   	query = "//processDomain[@domain-id='" + parentId + "']";
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

		   	query = "//process[@process-id='" + processId + "']";
			Element processNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			query = "//process[@process-id='" + before + "']";
			Element beforeNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			Element oldParentNode = (Element) processNode.getParentNode();

			if (!parentId.equals("trash")) {
				processNode = (Element) oldParentNode.removeChild(processNode);

				if (beforeNode != null) {
					domainNode.insertBefore(processNode, beforeNode);
				} else {
					domainNode.appendChild(processNode);
				}
			} else {
				Element trash = (Element) frameworkNode.getElementsByTagName("recycleBin").item(0);
				if (trash == null) {
					trash = frameworkNode.getOwnerDocument().createElement("recycleBin");
					frameworkNode.appendChild(trash);
				}
				processNode = (Element) oldParentNode.removeChild(processNode);
				trash.appendChild(processNode);
			}
			
			storeDomResource(repository, "framework.xml", framework);
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
	
	public void emptyRecycleBin() {
		log.info("Emptying recycle bin");

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "/framework/recycleBin";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element trash = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			NodeList children = trash.getChildNodes();
			
			int l = children.getLength();
			for (int i=0; i<l; i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					String elementName = ((Element) child).getNodeName();
					switch (elementName) {
					case "processDomain":
					case "process":
						trash.removeChild(child);
						break;

					default:
					}
				}
			}
			
			storeDomResource(repository, "framework.xml", framework);
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
