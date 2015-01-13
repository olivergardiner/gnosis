package uk.org.whitecottage.ea.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

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
}
