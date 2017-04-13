package uk.org.whitecottage.gnosis.backend.data;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Item;

public class ClassificationMap {
	protected Map<Long, String> keyMap;
	protected Map<String, String> appMap;

	public ClassificationMap(ApplicationTaxonomyContainer applicationTaxonomy) {
		keyMap = new HashMap<>();
		appMap = new HashMap<>();
		
		for (Object id: applicationTaxonomy.getItemIds()) {
			if (!applicationTaxonomy.hasChildren(id)) {
				Item item = applicationTaxonomy.getItem(id);
				
				String applicationName = (String) item.getItemProperty("Name").getValue();
				String applicationId = (String) id;
				
				keyMap.put(Long.valueOf(applicationId.hashCode()), applicationId);
				appMap.put(applicationId, applicationName);
			}
		}
	}
	
	public String getLogicalApplicationId(int hashCode) {
		return keyMap.get(Long.valueOf(hashCode));
	}
	
	public String getLogicalApplicationName(String id) {
		return appMap.get(id);
	}
}
