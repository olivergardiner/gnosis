package uk.org.whitecottage.ea.gnosis.repository;

import java.util.List;
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

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.RecycleBin;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONBoolean;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class TechnologyDomains extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.framework");

	public TechnologyDomains(String URI, String repositoryRoot, String context) {
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

	public String getJSON(String layer) {
		return getJSON(layer, true);
	}
	
	public String getJSON(String layer, boolean withRecycleBin) {
		String result = null;

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			JSTree tree = new JSTree();
			
			switch (layer) {
			case "BusinessApplications":
				tree.add(renderTechnologyDomainList(framework.getBusinessApplications().getTechnologyDomain(), "Business Applications", true));
				break;
			case "CommonServices":
				tree.add(renderTechnologyDomainList(framework.getCommonServices().getTechnologyDomain(), "Common Services", false));
				break;
			case "Infrastructure":
				tree.add(renderTechnologyDomainList(framework.getInfrastructure().getTechnologyDomain(), "Infrastructure", false));
				break;
			default:
				tree.add(renderTechnologyDomainList(framework.getBusinessApplications().getTechnologyDomain(), "Business Applications", true));
				tree.add(renderTechnologyDomainList(framework.getCommonServices().getTechnologyDomain(), "Common Services", false));
				tree.add(renderTechnologyDomainList(framework.getInfrastructure().getTechnologyDomain(), "Infrastructure", false));
				break;
			}

			if (withRecycleBin) {
				tree.add(renderTrash(framework.getRecycleBin()));
			}
			
			result = tree.toJSON();

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
	
	protected JSTreeNode renderTechnologyDomainList(List<TechnologyDomain> technologyDomains, String layer, boolean isLOB) {
		
		JSTreeNode root = new JSTreeNode(layer, "root");
		
		for (TechnologyDomain domain: technologyDomains) {
			root.getChildren().add(renderTechnologyDomain(domain, isLOB));
		}
		
		return root;
	}
	
	protected JSTreeNode renderTrash(RecycleBin recycleBin) {
		JSTreeNode trash = new JSTreeNode("Recycle bin", "trash");
		JSONString trashId = new JSONString("id", "trash");
		trash.put(trashId);
		
		JSONArray children = trash.getChildren();
		
		if (recycleBin != null) {
			for (Object o: recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity()) {
				if (o instanceof TechnologyDomain) {
					children.add(renderTechnologyDomain((TechnologyDomain) o, false));
				} else if (o instanceof Capability) {
					children.add(renderCapability((Capability) o));
				}
			}
		}
		
		return trash;
	}
	
	protected JSTreeNode renderTechnologyDomain(TechnologyDomain domain, boolean isLOB) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", domain.getDomainId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", domain.getDescription());
		data.put(descriptionJSON);
				
		JSONBoolean isLOBJSON = new JSONBoolean("isLOB", isLOB);
		data.put(isLOBJSON);
				
		if (domain.getValueChain() != null) {
			JSONString valueChainJSON = new JSONString("valueChain", domain.getValueChain());
			data.put(valueChainJSON);
		}
		
		JSTreeNode node = new JSTreeNode(domain.getName(), "technology-domain", data);
		JSONArray children = node.getChildren();
		
		for (Capability capability: domain.getCapability()) {
			children.add(renderCapability(capability));
		}
		
		return node;
	}
	
	protected JSTreeNode renderCapability(Capability capability) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", capability.getCapabilityId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", capability.getDescription());
		data.put(descriptionJSON);
				
		JSTreeNode node = new JSTreeNode(capability.getName(), "capability", data);
		
		return node;
	}
	
	public void updateTechnologyDomain(String domainId, String name, String description, String valueChain) {
		log.info("Updating domain: " + domainId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//technologyDomain[@domain-id='" + domainId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			domainNode.setAttribute("name", name);
			if (valueChain != null) {
				domainNode.setAttribute("value-chain", valueChain);
			}
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
	
	public void updateCapability(String capabilityId, String name, String description) {
		log.info("Updating capability: " + capabilityId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//capability[@capability-id='" + capabilityId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element capabilityNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			capabilityNode.setAttribute("name", name);
			((Element) capabilityNode.getElementsByTagName("description").item(0)).setTextContent(description);

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
	
	public void createTechnologyDomain(String layer, String domainId, String name, String description, int position) {
		log.info("Creating domain: " + domainId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();
			
			String layerElement = layer.substring(0, 1).toLowerCase() + layer.substring(1);

		   	String query = "/framework/" + layerElement;
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element layerNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			Element domainNode = layerNode.getOwnerDocument().createElement("technologyDomain");
			Element descriptionNode = layerNode.getOwnerDocument().createElement("description");
			descriptionNode.setTextContent(description);
			domainNode.setAttribute("domain-id", domainId);
			domainNode.setAttribute("name", name);
			domainNode.appendChild(descriptionNode);
			
			if (position == -1) {
				layerNode.appendChild(domainNode);

			} else {
				layerNode.insertBefore(domainNode, domainNode.getElementsByTagName("technologyDomain").item(position));
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
	
	public void createCapability(String domainId, String capabilityId, String name, String description, int position) {
		log.info("Creating capability: " + capabilityId + ", " + name + ", " + description);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "//technologyDomain[@domain-id='" + domainId + "']";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			Element capabilityNode = domainNode.getOwnerDocument().createElement("capability");
			Element descriptionNode = domainNode.getOwnerDocument().createElement("description");
			descriptionNode.setTextContent(description);
			capabilityNode.setAttribute("capability-id", capabilityId);
			capabilityNode.setAttribute("name", name);
			capabilityNode.appendChild(descriptionNode);
			
			if (position == -1) {
				domainNode.appendChild(capabilityNode);
			} else {
				domainNode.insertBefore(capabilityNode, domainNode.getElementsByTagName("capability").item(position));
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
	
	public void moveTechnologyDomain(String layer, String domainId, String parentId, int position) {
		log.info("Moving technology domain");

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

			String layerElement = layer.substring(0, 1).toLowerCase() + layer.substring(1);

		   	String query = "/framework";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element frameworkNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

			query = "/framework/" + layerElement;
			Element layerNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			query = "//technologyDomain[@domain-id='" + domainId + "']";
			Element domainNode = (Element) xpath.evaluate(query, layerNode, XPathConstants.NODE);

			Element oldParentNode = (Element) domainNode.getParentNode();

			if (!parentId.equals("trash")) {
				
				domainNode = (Element) oldParentNode.removeChild(domainNode);
				//layerNode.insertBefore(domainNode, layerNode.getElementsByTagName("technologyDomain").item(position));

				Node insertBefore = layerNode.getElementsByTagName("technologyDomain").item(position);
				if (insertBefore != null) {
					layerNode.insertBefore(domainNode, insertBefore);
				} else {
					layerNode.appendChild(domainNode);
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
	
	public void moveCapability(String capabilityId, String parentId, int position) {
		log.info("Moving capability: " + capabilityId + ", " + parentId + ", " + position);

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Node framework = frameworkResource.getContentAsDOM();

		   	String query = "/framework";
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element frameworkNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

		   	query = "//technologyDomain[@domain-id='" + parentId + "']";
			Element domainNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);

		   	query = "//capability[@capability-id='" + capabilityId + "']";
			Element capabilityNode = (Element) xpath.evaluate(query, framework, XPathConstants.NODE);
			
			Element oldParentNode = (Element) capabilityNode.getParentNode();

			if (!parentId.equals("trash")) {
				capabilityNode = (Element) oldParentNode.removeChild(capabilityNode);

				Node insertBefore = domainNode.getElementsByTagName("capability").item(position);
				if (insertBefore != null) {
					domainNode.insertBefore(capabilityNode, insertBefore);
				} else {
					domainNode.appendChild(capabilityNode);
				}
			} else {
				Element trash = (Element) frameworkNode.getElementsByTagName("recycleBin").item(0);
				if (trash == null) {
					trash = frameworkNode.getOwnerDocument().createElement("recycleBin");
					frameworkNode.appendChild(trash);
				}
				capabilityNode = (Element) oldParentNode.removeChild(capabilityNode);
				trash.appendChild(capabilityNode);
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
					case "technologyDomain":
					case "capability":
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
