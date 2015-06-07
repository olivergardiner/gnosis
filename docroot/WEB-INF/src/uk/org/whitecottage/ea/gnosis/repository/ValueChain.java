package uk.org.whitecottage.ea.gnosis.repository;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.CapabilityInstance;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Ecosystem;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Activity;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.RecycleBin;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ValueChain extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ValueChain.class);

	public ValueChain(String URI, String repositoryRoot, String context) {
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
		return getJSON(true);
	}
	
	public String getJSON(boolean withRecycleBin) {
		String result = null;

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			JSTree tree = new JSTree();
			
			tree.add(renderPrimaryActivities(framework));
			
			tree.add(renderSupportActivities(framework));

			if (withRecycleBin) {
				tree.add(renderTrash(framework, framework.getRecycleBin()));
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
	
	protected JSTreeNode renderPrimaryActivities(Framework framework) {
		
		JSTreeNode root = new JSTreeNode("Primary Activities", "primary");
		
		for (Activity activity: framework.getValueChain().getPrimaryActivities().getActivity()) {
			root.getChildren().add(renderActivity(framework, activity));
		}
		
		return root;
	}
	
	protected JSTreeNode renderSupportActivities(Framework framework) {
		
		JSTreeNode root = new JSTreeNode("Support Activities", "support");
		
		for (Activity activity: framework.getValueChain().getSupportActivities().getActivity()) {
			root.getChildren().add(renderActivity(framework, activity));
		}
		
		return root;
	}
	
	protected JSTreeNode renderTrash(Framework framework, RecycleBin recycleBin) {
		JSTreeNode trash = new JSTreeNode("Recycle bin", "trash");
		JSONString trashId = new JSONString("id", "trash");
		trash.put(trashId);
		
		JSONArray children = trash.getChildren();
		
		if (recycleBin != null) {
			for (Object o: recycleBin.getTechnologyDomainOrCapabilityOrActivity()) {
				if (o instanceof Activity) {
					children.add(renderActivity(framework, (Activity) o));
				}
			}
		}
		
		return trash;
	}
	
	protected JSTreeNode renderActivity(Framework framework, Activity activity) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", activity.getActivityId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", activity.getDescription());
		data.put(descriptionJSON);
				
		JSTreeNode node = new JSTreeNode(activity.getName(), "activity", data);
		
		for (Ecosystem ecosystem: activity.getEcosystem()) {
			node.getChildren().add(renderEcosystem(framework, ecosystem));
		}
		
		return node;
	}
	
	protected JSTreeNode renderEcosystem(Framework framework, Ecosystem ecosystem) {
		JSONMap data = new JSONMap("data");
				
		JSONString idJSON = new JSONString("id", ecosystem.getEcosystemId());
		data.put(idJSON);
		
		JSONString descriptionJSON = new JSONString("description", ecosystem.getDescription());
		data.put(descriptionJSON);
		
		JSTreeNode node = new JSTreeNode(ecosystem.getName(), "ecosystem", data);

		for (CapabilityInstance capability: ecosystem.getCapabilityInstance()) {
			node.getChildren().add(renderCapabilityInstance(framework, capability));
		}
		
		return node;
	}

	protected JSTreeNode renderCapabilityInstance(Framework framework, CapabilityInstance capability) {
		//TODO: Change to return a JSTreeNode
		JSONMap data = new JSONMap("data");
				
		String capabilityId = capability.getCapabilityId();
		JSONString idJSON = new JSONString("id", capabilityId);
		data.put(idJSON);
		
		Capability capabilityRef = findCapability(framework, capabilityId);
		JSONString capabilityNameJSON = new JSONString("name", capabilityRef.getName());
		data.put(capabilityNameJSON);
				
		JSONString descriptionJSON = new JSONString("description", capability.getDescription());
		data.put(descriptionJSON);

		JSTreeNode node = new JSTreeNode(capability.getName(), "capability", data);

		return node;
	}
	
	public void updateValueChain(String type, String activityId, String ecosystemId, String capabilityId, String name, String description) {
		if (description == null) {
			description = "";
		}

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			switch (type) {
			case "activity":
				updateActivity(framework, activityId, name, description);
				break;
			case "ecosystem":
				updateEcosystem(framework, ecosystemId, name, description);
				break;
			case "capability":
				updateCapabilityInstance(framework, ecosystemId, capabilityId, name, description);
				break;
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

	protected void updateActivity(Framework framework, String activityId, String name, String description) {
		uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
		boolean found = false;
		for (Activity primaryActivity: valueChain.getPrimaryActivities().getActivity()) {		
			if (primaryActivity.getActivityId().equals(activityId)) {
				found = true;
				primaryActivity.setName(name);
				primaryActivity.setDescription(description);
				break;
			}
		}
		if (!found) {
			for (Activity supportActivity: valueChain.getSupportActivities().getActivity()) {		
				if (supportActivity.getActivityId().equals(activityId)) {
					supportActivity.setName(name);
					supportActivity.setDescription(description);
					break;
				}
			}
		}
	}

	protected void updateEcosystem(Framework framework, String ecosystemId, String name, String description) {
		Ecosystem ecosystem = findEcosystem(framework, ecosystemId);
		if (ecosystem != null) {
			ecosystem.setName(name);
			ecosystem.setDescription(description);
		}
	}
	
	protected void updateCapabilityInstance(Framework framework, String ecosystemId, String capabilityId, String name, String description) {
		CapabilityInstance capability = findCapabilityInstance(framework, ecosystemId, capabilityId);
		if (capability != null) {
			capability.setName(name);
			capability.setDescription(description);
		}
	}

	public void createActivity(String type, String activityId, String name, String description) {
		if (description == null) {
			description = "";
		}

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
			
			if (type.equals("primary")) {
				Activity primaryActivity = new Activity();
				primaryActivity.setActivityId(activityId);
				primaryActivity.setName(name);
				primaryActivity.setDescription(description);
				
				valueChain.getPrimaryActivities().getActivity().add(primaryActivity);
			} else {
				Activity supportActivity = new Activity();
				supportActivity.setActivityId(activityId);
				supportActivity.setName(name);
				supportActivity.setDescription(description);
				
				valueChain.getSupportActivities().getActivity().add(supportActivity);
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

	public void createEcosystem(String activityId, String ecosystemId, String name, String description) {
		if (description == null) {
			description = "";
		}
		
		log.info("Activity: " + activityId);
		log.info("Ecosystem: " + ecosystemId);
		log.info("Name: " + name);
		log.info("Description: " + description);
		
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			Ecosystem ecosystem = new Ecosystem();
			ecosystem.setEcosystemId(ecosystemId);;
			ecosystem.setName(name);
			ecosystem.setDescription(description);
			
			Activity activity = findActivity(framework, activityId);
			
			if (activity != null) {
				activity.getEcosystem().add(ecosystem);
			} else {
				log.info("Activity not found");
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

	public void createCapabilityInstance(String ecosystemId, String capabilityId, String name, String description) {
		if (description == null) {
			description = "";
		}

		log.info("Ecosystem: " + ecosystemId);
		log.info("Capability: " + capabilityId);
		log.info("Name: " + name);
		log.info("Description: " + description);
		
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			CapabilityInstance capability = new CapabilityInstance();
			capability.setCapabilityId(capabilityId);
			capability.setName(name);
			capability.setDescription(description);
			
			Ecosystem ecosystem = findEcosystem(framework, ecosystemId);
			ecosystem.getCapabilityInstance().add(capability);
			
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

	public void moveValueChain(String id, String from, String fromId, String to, String toId, int position) {
		log.info("Moving value chain element: " + id + ", " + from + ", " + to + ", " + position );

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
			RecycleBin recycleBin = framework.getRecycleBin();
			
			if (from.equals("trash")) {
				for (Object o: recycleBin.getTechnologyDomainOrCapabilityOrActivity()) {
					if (o instanceof Activity) {
						Activity activity = (Activity) o;
						if (activity.getActivityId().equals(id)) {
							recycleBin.getTechnologyDomainOrCapabilityOrActivity().remove(activity);
							if ("primary".equals(to)) {
								valueChain.getPrimaryActivities().getActivity().add(position, activity);
							break;
							} else {
								valueChain.getSupportActivities().getActivity().add(position, activity);
							}
						}
					} else if (o instanceof Ecosystem) {
						Ecosystem ecosystem = (Ecosystem) o;
						if (ecosystem.getEcosystemId().equals(id)) {
							recycleBin.getTechnologyDomainOrCapabilityOrActivity().remove(ecosystem);
							Activity primaryActivity = findActivity(framework, toId);
							primaryActivity.getEcosystem().add(position, ecosystem);
							break;
						}
					} else if (o instanceof CapabilityInstance) {
						CapabilityInstance capability = (CapabilityInstance) o;
						if (capability.getCapabilityId().equals(id)) {
							Ecosystem ecosystem = findEcosystem(framework, toId);
							if (ecosystem != null) {
								boolean capabilityExists = false;
								for (CapabilityInstance ecosystemCapability: ecosystem.getCapabilityInstance()) {
									if (ecosystemCapability.getCapabilityId().equals(id)) {
										capabilityExists = true;
										break;
									}
								}
								if (!capabilityExists) {
									recycleBin.getTechnologyDomainOrCapabilityOrActivity().remove(capability);
									ecosystem.getCapabilityInstance().add(position, capability);
									break;
								}
							}
						}
					}
				}				
			} else if (from.equals("primary")) {
				for (Activity primaryActivity: valueChain.getPrimaryActivities().getActivity()) {
					if (primaryActivity.getActivityId().equals(id)) {
						valueChain.getPrimaryActivities().getActivity().remove(primaryActivity);
						
						if ("primary".equals(to)) {
							valueChain.getPrimaryActivities().getActivity().add(position, primaryActivity);
						} else if ("support".equals(to)) {
							valueChain.getSupportActivities().getActivity().add(position, primaryActivity);
						} else if ("trash".equals(to)) {
							recycleBin.getTechnologyDomainOrCapabilityOrActivity().add(position, primaryActivity);
						}
						break;
					}
				}				
			} else if (from.equals("support")) {
				for (Activity supportActivity: valueChain.getSupportActivities().getActivity()) {
					if (supportActivity.getActivityId().equals(id)) {
						valueChain.getSupportActivities().getActivity().remove(supportActivity);
						
						if ("primary".equals(to)) {
							valueChain.getPrimaryActivities().getActivity().add(position, supportActivity);
						} else if ("support".equals(to)) {
							valueChain.getSupportActivities().getActivity().add(position, supportActivity);
						} else if ("trash".equals(to)) {
							recycleBin.getTechnologyDomainOrCapabilityOrActivity().add(position, supportActivity);
						}
						break;
					}
				}				
			} else if (from.equals("activity")) {
				Activity activity = findActivity(framework, fromId);
				if (activity != null) {
					for (Ecosystem ecosystem: activity.getEcosystem()) {
						if (ecosystem.getEcosystemId().equals(id)) {
							if (to.equals("trash")) {
								recycleBin.getTechnologyDomainOrCapabilityOrActivity().add(position, ecosystem);
								activity.getEcosystem().remove(ecosystem);
							} else if (to.equals("activity")) {
								Activity targetActivity = findActivity(framework, toId);
								if (targetActivity != null) {
									targetActivity.getEcosystem().add(position, ecosystem);
									activity.getEcosystem().remove(ecosystem);
								}
							}
							break;
						}
					}
				}
			} else if (from.equals("ecosystem")) {
				Ecosystem ecosystem = findEcosystem(framework, fromId);
				if (ecosystem != null) {
					for (CapabilityInstance capability: ecosystem.getCapabilityInstance()) {
						if (capability.getCapabilityId().equals(id)) {
							if ("trash".equals(to)) {
								recycleBin.getTechnologyDomainOrCapabilityOrActivity().add(position, capability);
								ecosystem.getCapabilityInstance().remove(capability);
							} else if ("ecosystem".equals(to)) {
								Ecosystem targetEcosystem = findEcosystem(framework, toId);
								if (targetEcosystem != null) {
									boolean capabilityExists = false;
									for (CapabilityInstance ecosystemCapability: targetEcosystem.getCapabilityInstance()) {
										if (ecosystemCapability.getCapabilityId().equals(id)) {
											capabilityExists = true;
											break;
										}
									}
									if (!capabilityExists) {
										ecosystem.getCapabilityInstance().remove(capability);
										targetEcosystem.getCapabilityInstance().add(position, capability);
									}
								}
							}
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
			List<Element> toDelete = new ArrayList<Element>();
			
			int l = children.getLength();
			for (int i=0; i<l; i++) {
				Node child = children.item(i);
				log.info("Checking element: " + child.getNodeName());
				if (child instanceof Element) {
					String elementName = ((Element) child).getNodeName();
					switch (elementName) {
					case "activity":
					case "ecosystem":
					case "capability":
						toDelete.add((Element) child);
						break;

					default:
					}
				}
			}
			
			for (Element child: toDelete) {
				trash.removeChild(child);
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

	protected Ecosystem findEcosystem(Framework framework, String ecosystemId) {
		uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
		Ecosystem foundEcosystem = null;
		for (Activity primaryActivity: valueChain.getPrimaryActivities().getActivity()) {
			for (Ecosystem ecosystem: primaryActivity.getEcosystem()) {
				
				if (ecosystem.getEcosystemId().equals(ecosystemId)) {
					foundEcosystem = ecosystem;
					break;
				}
			}
		}
			
		if (foundEcosystem == null) {
			for (Activity supportActivity: valueChain.getSupportActivities().getActivity()) {
				for (Ecosystem ecosystem: supportActivity.getEcosystem()) {
					if (ecosystem.getEcosystemId().equals(ecosystemId)) {
						foundEcosystem = ecosystem;
						break;
					}
				}
			}
		}

		return foundEcosystem;
	}

	protected Capability findCapability(Framework framework, String capabilityId) {
		Capability capability = findCapability(framework.getBusinessApplications().getTechnologyDomain(), capabilityId);

		if (capability == null) {
			capability = findCapability(framework.getCommonServices().getTechnologyDomain(), capabilityId);
		}

		if (capability == null) {
			capability = findCapability(framework.getInfrastructure().getTechnologyDomain(), capabilityId);
		}
		
		return capability;
	}
	
	protected Capability findCapability(List<TechnologyDomain> domains, String capabilityId) {
		for (TechnologyDomain domain: domains) {
			for (Capability capability: domain.getCapability()) {
				if (capability.getCapabilityId().equals(capabilityId)) {
					return capability;
				}
			}
		}
		
		return null;
	}

	protected CapabilityInstance findCapabilityInstance(Framework framework, String ecosystemId, String capabilityId) {
		Ecosystem ecosystem = findEcosystem(framework, ecosystemId);

		return getCapabilityInstance(ecosystem, capabilityId);
	}

	protected CapabilityInstance getCapabilityInstance(Ecosystem ecosystem, String capabilityId) {
		CapabilityInstance foundCapabilityInstance = null;
		if (ecosystem != null) {
			for (CapabilityInstance capabilityInstance: ecosystem.getCapabilityInstance()) {
				
				if (capabilityInstance.getCapabilityId().equals(capabilityId)) {
					foundCapabilityInstance = capabilityInstance;
					break;
				}
			}
		}
		
		return foundCapabilityInstance;
	}

	protected Activity findActivity(Framework framework, String activityId) {
		Activity foundActivity = getPrimaryActivity(framework, activityId);
		if (foundActivity == null) {
			foundActivity = getSupportActivity(framework, activityId);
		}

		return foundActivity;
	}

	protected Activity getPrimaryActivity(Framework framework, String activityId) {
		uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();

		Activity foundActivity = null;
		for (Activity primaryActivity: valueChain.getPrimaryActivities().getActivity()) {
			
			if (primaryActivity.getActivityId().equals(activityId)) {
				foundActivity = primaryActivity;
				break;
			}
		}

		return foundActivity;
	}

	protected Activity getSupportActivity(Framework framework, String activityId) {
		uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();

		Activity foundActivity = null;
		for (Activity supportActivity: valueChain.getSupportActivities().getActivity()) {
			
			if (supportActivity.getActivityId().equals(activityId)) {
				foundActivity = supportActivity;
				break;
			}
		}

		return foundActivity;
	}
}
