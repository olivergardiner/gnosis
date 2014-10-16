package uk.org.whitecottage.ea.gnosis.repository;

import java.util.logging.Logger;

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

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.PrimaryActivity;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.RecycleBin;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.SupportActivity;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;

public class ValueChain extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.framework");

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
	
	protected JSTreeNode renderPrimaryActivities(Framework framework) {
		
		JSTreeNode root = new JSTreeNode("Primary Activities", "primary");
		
		for (PrimaryActivity activity: framework.getValueChain().getPrimaryActivity()) {
			root.getChildren().add(renderPrimaryActivity(activity));
		}
		
		return root;
	}
	
	protected JSTreeNode renderSupportActivities(Framework framework) {
		
		JSTreeNode root = new JSTreeNode("Support Activities", "support");
		
		for (SupportActivity activity: framework.getValueChain().getSupportActivity()) {
			root.getChildren().add(renderSupportActivity(activity));
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
				if (o instanceof PrimaryActivity) {
					children.add(renderPrimaryActivity((PrimaryActivity) o));
				} else if (o instanceof SupportActivity) {
					children.add(renderSupportActivity((SupportActivity) o));
				}
			}
		}
		
		return trash;
	}
	
	protected JSTreeNode renderPrimaryActivity(PrimaryActivity activity) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", activity.getValueChainId());
		data.put(idJSON);
				
		JSTreeNode node = new JSTreeNode(activity.getValue(), "primary-activity", data);
		
		return node;
	}
	
	protected JSTreeNode renderSupportActivity(SupportActivity activity) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", activity.getValueChainId());
		data.put(idJSON);
				
		JSTreeNode node = new JSTreeNode(activity.getValue(), "support-activity", data);
		
		return node;
	}
	
	public void updateValueChain(String valueChainId, String name, String type) {
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
			if (type.equals("primary-activity")) {
				boolean found = false;
				for (PrimaryActivity primaryActivity: valueChain.getPrimaryActivity()) {
					
					if (primaryActivity.getValueChainId().equals(valueChainId)) {
						found = true;
						primaryActivity.setValue(name);
					}
				}
				
				if (!found) {
					PrimaryActivity primaryActivity = new PrimaryActivity();
					primaryActivity.setValueChainId(valueChainId);
					primaryActivity.setValue(name);
					
					valueChain.getPrimaryActivity().add(primaryActivity);
				}
			} else {
				boolean found = false;
				for (SupportActivity supportActivity: valueChain.getSupportActivity()) {
					
					if (supportActivity.getValueChainId().equals(valueChainId)) {
						found = true;
						supportActivity.setValue(name);
					}
				}
				
				if (!found) {
					SupportActivity supportActivity = new SupportActivity();
					supportActivity.setValueChainId(valueChainId);
					supportActivity.setValue(name);
					
					valueChain.getSupportActivity().add(supportActivity);
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
	
	public void createValueChain(String valueChainId, String name, String type, int position) {

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
			
			if (type.equals("primary-activity")) {
				PrimaryActivity primaryActivity = new PrimaryActivity();
				primaryActivity.setValueChainId(valueChainId);
				primaryActivity.setValue(name);
				
				valueChain.getPrimaryActivity().add(position, primaryActivity);
			} else {
				SupportActivity supportActivity = new SupportActivity();
				supportActivity.setValueChainId(valueChainId);
				supportActivity.setValue(name);
				
				valueChain.getSupportActivity().add(position, supportActivity);
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
	
	public void moveValueChain(String valueChainId, String from, String to, int position) {
		log.info("Moving value chain activity: " + valueChainId + ", " + from + ", " + to + ", " + position );

		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			Framework framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
			
			uk.org.whitecottage.ea.gnosis.jaxb.framework.ValueChain valueChain = framework.getValueChain();
			RecycleBin recycleBin = framework.getRecycleBin();
			
			if (from.equals("trash")) {
				for (Object o: recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity()) {
					if (o instanceof PrimaryActivity) {
						PrimaryActivity primaryActivity = (PrimaryActivity) o;
						if (primaryActivity.getValueChainId().equals(valueChainId)) {
							recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity().remove(primaryActivity);
							valueChain.getPrimaryActivity().add(position, primaryActivity);
							break;
						}
					} else if (o instanceof SupportActivity) {
						SupportActivity supportActivity = (SupportActivity) o;
						if (supportActivity.getValueChainId().equals(valueChainId)) {
							recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity().remove(supportActivity);
							valueChain.getSupportActivity().add(position, supportActivity);
							break;
						}
					}
				}				
			} else if (from.equals("primary")) {
				for (PrimaryActivity primaryActivity: valueChain.getPrimaryActivity()) {
					if (primaryActivity.getValueChainId().equals(valueChainId)) {
						valueChain.getPrimaryActivity().remove(primaryActivity);
						
						if (to.equals("primary")) {
							valueChain.getPrimaryActivity().add(position, primaryActivity);
						} else if (to.equals("trash")) {
							recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity().add(position, primaryActivity);
						}
						break;
					}
				}				
			} else if (from.equals("support")) {
				for (SupportActivity supportActivity: valueChain.getSupportActivity()) {
					if (supportActivity.getValueChainId().equals(valueChainId)) {
						valueChain.getSupportActivity().remove(supportActivity);

						if (to.equals("support")) {
							valueChain.getSupportActivity().add(position, supportActivity);
						} else if (to.equals("trash")) {
							recycleBin.getTechnologyDomainOrCapabilityOrPrimaryActivity().add(position, supportActivity);
						}
						break;
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
			
			int l = children.getLength();
			for (int i=0; i<l; i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					String elementName = ((Element) child).getNodeName();
					switch (elementName) {
					case "primaryActivity":
					case "supportActivity":
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
