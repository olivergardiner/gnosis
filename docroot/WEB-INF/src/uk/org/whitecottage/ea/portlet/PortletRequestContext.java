package uk.org.whitecottage.ea.portlet;

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ClientDataRequest;

import org.apache.commons.fileupload.RequestContext;

public class PortletRequestContext implements RequestContext {

	private ClientDataRequest request;

	public PortletRequestContext(ClientDataRequest request) {
		this.request = request;
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public String getContentType() {
		return request.getContentType();
	}

	@Deprecated
	public int getContentLength() {
		return request.getContentLength();
	}

	public InputStream getInputStream() throws IOException {
		return request.getPortletInputStream();
	}

	public String toString() {
		return "ContentLength=" + this.getContentLength() + ", ContentType="
				+ this.getContentType();
	}
}