package uk.org.whitecottage.gnosis.backend.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassificationMap {
	protected Map<Long, String> keyMap;
	protected Map<String, String> appMap;

	public ClassificationMap(Collection<LogicalApplicationBean> logicalApplications) {
		keyMap = new HashMap<Long, String>();
		appMap = new HashMap<String, String>();
		
		for (LogicalApplicationBean logicalApplication: logicalApplications) {
			String applicationId = logicalApplication.getApplicationId();
			keyMap.put(new Long(applicationId.hashCode()), applicationId);
			appMap.put(applicationId, logicalApplication.getApplicationName());
		}
	}
	
	public String getLogicalApplicationId(int hashCode) {
		return keyMap.get(new Long(hashCode));
	}
	
	public String getLogicalApplicationName(String id) {
		return appMap.get(id);
	}
	
	public void printMappings() {
		for (String id: appMap.keySet()) {
			System.out.println("Id: " + id);
			System.out.println("Name: " + appMap.get(id));
		}
	}
}
