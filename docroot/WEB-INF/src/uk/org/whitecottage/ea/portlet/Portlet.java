package uk.org.whitecottage.ea.portlet;

import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.util.PortalUtil;

public class Portlet extends GenericPortlet {
	protected String dataDir;
	
	public Portlet() {
		// For Liferay we can use the following directory
		dataDir = System.getProperty("liferay.home") + "/data/";
	}
	
	protected String getParameter(PortletRequest request, String parameter) {
		// For Liferay we need to get to the original servlet request in order to see the parameters
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(request);
		HttpServletRequest originalServletRequest = PortalUtil.getOriginalServletRequest(servletRequest);
		
		return originalServletRequest.getParameter(parameter);
	}
	
	protected void logParameters(PortletRequest request, Log log) {
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(request);
		HttpServletRequest originalServletRequest = PortalUtil.getOriginalServletRequest(servletRequest);
		
		log.info("Parameter map:");
		
		for (String parameter: originalServletRequest.getParameterMap().keySet()) {
			log.info("Parameter: " + parameter + " -> " + originalServletRequest.getParameter(parameter));
		}
	}
	
	protected Map<String, String[]> getParameterMap(PortletRequest request) {
		HttpServletRequest servletRequest = PortalUtil.getHttpServletRequest(request);
		HttpServletRequest originalServletRequest = PortalUtil.getOriginalServletRequest(servletRequest);
		
		return originalServletRequest.getParameterMap();
	}
}
