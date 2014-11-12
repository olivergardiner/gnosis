package uk.org.whitecottage.ea.gnosis.repository.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import uk.org.whitecottage.ea.gnosis.jaxb.services.ServiceComponent;
import uk.org.whitecottage.ea.gnosis.jaxb.services.ServiceElement;

public class ServiceComponentBuilder {
	protected String id = "";
	protected String name = "";
	protected int index;
	protected boolean isPenultimate = false;
	protected String type = "";
	protected String description = "";
	
	protected Map<String, ServiceComponentBuilder> children;
	
	public ServiceComponentBuilder(String id, String name) {
		this.id = id;
		this.name = name;
		
		children = new HashMap<String, ServiceComponentBuilder>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPenultimate() {
		return isPenultimate;
	}

	public void setPenultimate(boolean isPenultimate) {
		this.isPenultimate = isPenultimate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = StringEscapeUtils.escapeXml(description);
	}

	public void clear() {
		children.clear();
	}

	public boolean containsKey(Object arg0) {
		return children.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return children.containsValue(arg0);
	}

	public ServiceComponentBuilder get(Object arg0) {
		return children.get(arg0);
	}

	public Set<String> getKeys() {
		return children.keySet();
	}

	public ServiceComponentBuilder put(String arg0, ServiceComponentBuilder arg1) {
		return children.put(arg0, arg1);
	}

	public ServiceComponentBuilder remove(Object arg0) {
		return children.remove(arg0);
	}
	
	public int size() {
		return children.size();
	}

	public ServiceComponent toXML() {
    	ServiceComponent serviceComponent = new ServiceComponent();
    	   	
    	serviceComponent.setId(id);
    	serviceComponent.setName(name);
    	//serviceComponent.setIndex(Integer.toString(index));
    	serviceComponent.setDescription(description);
    	serviceComponent.setType(type);

    	if (isPenultimate) {
	    	for (ServiceComponentBuilder child: children.values()) {
	    		serviceComponent.getServiceElement().add(child.serviceElementToXML());
			}
    	} else {
	    	for (ServiceComponentBuilder child: children.values()) {
	    		serviceComponent.getServiceComponent().add(child.toXML());
			}
    	}
    	    	
    	return serviceComponent;
	}
	
	protected ServiceElement serviceElementToXML() {
    	ServiceElement serviceComponent = new ServiceElement();
    	   	
    	serviceComponent.setId(id);
    	serviceComponent.setName(name);
    	serviceComponent.setDescription(description);
    	
    	return serviceComponent;
	}
}
