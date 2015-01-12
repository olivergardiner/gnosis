package uk.org.whitecottage.ea.gnosis.repository;

import java.util.logging.Logger;

import uk.org.whitecottage.ea.gnosis.jaxb.services.Dependency;
import uk.org.whitecottage.ea.gnosis.jaxb.services.RecycleBin;
import uk.org.whitecottage.ea.gnosis.jaxb.services.ServiceComponent;
import uk.org.whitecottage.ea.gnosis.jaxb.services.ServiceElement;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Services;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Tower;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;

public class ITServices extends ITServicesProcessor {
	
	protected Services services;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.repository");

	public ITServices(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot, context);
	}

	public String getJSON() {
		return getJSON(true);
	}
	
	public String getJSON(boolean withRecycleBin) {
		services = loadServices();

		JSTree tree = new JSTree();
		
		JSTreeNode root = new JSTreeNode("Service Towers", "root");
		tree.add(root);

		for (Tower tower: services.getTower()) {
			root.getChildren().add(renderTower(tower));
		}
		
		if (withRecycleBin) {
			tree.add(renderTrash(services.getRecycleBin()));
		}

		return tree.toJSON();
	}

	protected JSTreeNode renderTower(Tower tower) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", tower.getId());
		data.put(idJSON);

		JSONString descriptionJSON = new JSONString("description", tower.getDescription());
		data.put(descriptionJSON);

		JSTreeNode towerJSON = new JSTreeNode(tower.getName(), "tower", data);
		
		for (ServiceComponent component: tower.getServiceComponent()) {
			towerJSON.getChildren().add(renderServiceComponent(component));
		}
		
		for (Dependency dependency: tower.getDependency()) {
			towerJSON.getChildren().add(renderDependency(dependency));
		}
		
		return towerJSON;
	}
	
	protected JSTreeNode renderServiceComponent(ServiceComponent component) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", component.getId());
		data.put(idJSON);

		JSONString descriptionJSON = new JSONString("description", component.getDescription());
		data.put(descriptionJSON);

		JSTreeNode componentJSON = new JSTreeNode(component.getName(), component.getType(), data);
		
		for (ServiceComponent subComponent: component.getServiceComponent()) {
			componentJSON.getChildren().add(renderServiceComponent(subComponent));
		}
		
		for (ServiceElement serviceElement: component.getServiceElement()) {
			componentJSON.getChildren().add(renderServiceElement(serviceElement));
		}
		
		for (Dependency dependency: component.getDependency()) {
			componentJSON.getChildren().add(renderDependency(dependency));
		}
		
		return componentJSON;
	}
	
	protected JSTreeNode renderServiceElement(ServiceElement serviceElement) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", serviceElement.getId());
		data.put(idJSON);

		JSONString descriptionJSON = new JSONString("description", serviceElement.getDescription());
		data.put(descriptionJSON);

		JSTreeNode elementJSON = new JSTreeNode(serviceElement.getName(), "service-element", data);
				
		for (Dependency dependency: serviceElement.getDependency()) {
			elementJSON.getChildren().add(renderDependency(dependency));
		}
		
		return elementJSON;
	}
	
	protected JSTreeNode renderDependency(Dependency dependency) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("target", dependency.getId());
		data.put(idJSON);
				
		JSONString reasonJSON = new JSONString("reason", dependency.getDependencyReason());
		data.put(reasonJSON);
		
		String target = findTargetName(dependency);
								
		JSTreeNode dependencyJSON = new JSTreeNode(target, "dependency", data);
		
		return dependencyJSON;
	}
	
	protected String findTargetName(Dependency dependency) {
		for (Tower tower: services.getTower()) {
			if (tower.getId().equals(dependency.getId())) {
				return tower.getName();
			}
			
			String name = findTargetName(tower, dependency);
			if (name != null) {
				return name;
			}
		}
		
		return "Unknown";
	}
				
	protected String findTargetName(Tower tower, Dependency dependency) {
		for (ServiceComponent component: tower.getServiceComponent()) {
			if (component.getId().equals(dependency.getId())) {
				return component.getName();
			}
			
			String name = findTargetName(component, dependency);
			if (name != null) {
				return name;
			}
		}
		
		return null;
	}
				
	protected String findTargetName(ServiceComponent component, Dependency dependency) {
		for (ServiceComponent subComponent: component.getServiceComponent()) {
			if (subComponent.getId().equals(dependency.getId())) {
				return subComponent.getName();
			}
			
			String name = findTargetName(subComponent, dependency);
			if (name != null) {
				return name;
			}
		}
		
		for (ServiceElement serviceElement: component.getServiceElement()) {
			if (serviceElement.getId().equals(dependency.getId())) {
				return serviceElement.getName();
			}
		}
		
		return null;
	}
				
	protected JSTreeNode renderTrash(RecycleBin recycleBin) {
		JSTreeNode trash = new JSTreeNode("Recycle bin", "trash");
		JSONString trashId = new JSONString("id", "trash");
		trash.put(trashId);
		
		JSONArray children = trash.getChildren();
		
		if (recycleBin != null) {
			for (Object o: recycleBin.getTowerOrServiceComponentOrServiceElement()) {
				if (o instanceof Tower) {
					children.add(renderTower((Tower) o));
				} else if (o instanceof ServiceComponent) {
					children.add(renderServiceComponent((ServiceComponent) o));
				} else if (o instanceof ServiceElement) {
					children.add(renderServiceElement((ServiceElement) o));
				} else if (o instanceof Dependency) {
					children.add(renderDependency((Dependency) o));
				}
			}
		}
		
		return trash;
	}

	public void updateComponent(String id, String name, String description) {
		Services services = loadServices();

		
		saveServices(services);
	}

	public void emptyRecycleBin() {
		Services services = loadServices();

		
		saveServices(services);
	}
}
