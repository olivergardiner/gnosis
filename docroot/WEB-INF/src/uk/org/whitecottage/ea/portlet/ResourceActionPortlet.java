package uk.org.whitecottage.ea.portlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public abstract class ResourceActionPortlet extends Portlet {

	protected transient Map<String, Method> processResourceActionHandlingMethodsMap = new HashMap<String, Method>();
	protected transient Map<String, Method> processResourceRequestHandlingMethodsMap = new HashMap<String, Method>();

	//@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ResourceActionPortlet.class);

	public ResourceActionPortlet() {
		super();
	}

	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		cacheResourceActionMethods();
	}

	@Override
	public void init() throws PortletException {
		super.init();
	}

	private void cacheResourceActionMethods() {
		// cache all annotated and visible public methods
		for (Method method : this.getClass().getMethods()) {
			Annotation[] annotations = method.getAnnotations();
			if (annotations != null) {
				for (Annotation annotation : annotations) {
					Class<? extends Annotation> annotationType = annotation.annotationType();
					if (ProcessResourceAction.class.equals(annotationType)) {
						String name = ((ProcessResourceAction) annotation).name();
						if (name != null && name.length() > 0) {
							processResourceActionHandlingMethodsMap.put(name, method);
						}
					} else if (ProcessResourceRequest.class.equals(annotationType)) {
						String name = ((ProcessResourceRequest) annotation).name();
						if (name != null && name.length() > 0) {
							processResourceRequestHandlingMethodsMap.put(name, method);
						}
					}
				}
			}
		}
	}

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		String resourceId = request.getResourceID();
		
		if (resourceId.equals("action")) {
			//If you have liferay then you need the servlet request to get paramters
			String action = getParameter(request, "action");
			//If your portal doesn't strip out the request parameters then you can get them direct, as below
			//String action = request.getParameter("action");
			
			if (action == null) {
				throw new PortletException("no processResourceAction method specified");
			}
			
			try {
				// check if action method is cached
				Method actionMethod = processResourceActionHandlingMethodsMap.get(action);
				if (actionMethod != null) {
					actionMethod.invoke(this, request, response);
					return;
				}
			} catch (Exception e) {
				throw new PortletException(e);
			}
	
			// if no action processing method was found throw exception
			throw new PortletException("processResourceAction method not implemented");
		} else {
			log.info("Resource requested: " + resourceId);
			
			try {
				// check if resource method is cached
				Method resourceMethod = processResourceRequestHandlingMethodsMap.get(resourceId);
				if (resourceMethod != null) {
					resourceMethod.invoke(this, request, response);
					return;
				}
			} catch (Exception e) {
				throw new PortletException(e);
			}

			// if no resource processing method was found throw exception
			throw new PortletException("processResourceRequest method not implemented");
		}
	}
	
	protected void logParameters(PortletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			String values[] = request.getParameterValues(name);
			String value = "";
			String separator = "";
			for (int i = 0; i < values.length; i++) {
				value += separator + values[i];
				separator = ", ";
			}
			log.info("Parameter: " + name + " = " + value);
		}
	}
}
