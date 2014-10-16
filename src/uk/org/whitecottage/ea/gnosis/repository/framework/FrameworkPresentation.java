package uk.org.whitecottage.ea.gnosis.repository.framework;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.gnosis.repository.XmldbProcessor;

public class FrameworkPresentation extends XmldbProcessor {
	
	// Default page size is 720x540
	protected static final double PAGE_WIDTH = 720.0;
	protected static final double PAGE_HEIGHT = 540.0;
	protected static final double TITLE_HEIGHT = 80.0;
	protected static final double LAYER_WIDTH = 680.0;
	protected static final double LAYER_HEIGHT = 90.0;
	protected static final double CONTEXT_LAYER_HEIGHT = 30.0;
	protected static final double CAPABILITY_HEIGHT = 10.0;
	protected static final double DOMAIN_HEIGHT = 50.0;
	protected static final double MARGIN = 2.0;
	protected static final double TITLE_HEIGHT1 = 14.0;
	protected static final double TITLE_HEIGHT2 = 8.0;
	protected static final double TITLE_HEIGHT3 = 4.0;
	protected static final Color CLR_LAYER = new Color(0xdddddd);
	protected static final Color CLR_CAPABILITY = new Color(0xccccee);

	protected static final Color CLR_DIVERSE = new Color(0xffff99);
	protected static final Color CLR_REPLICATED = new Color(0x47dda7);
	protected static final Color CLR_COORDINATED = new Color(0xff9900);
	protected static final Color CLR_UNIFIED = new Color(0x6666ff);
	protected static final Color CLR_UNKNOWN = new Color(0xdddddd);
		
	protected static final double ECOSYSTEM_HEIGHT = 15.0;
	
	protected static final int MAX_DOMAINS = 8;
	protected static final double POINT_TO_CM = 0.0352777778;
	
	protected static final int MAX_LAYOUTS = 20;
	protected static final int PROCESS_DURATION = 5;
	protected static final double PROCESS_DOMAIN_WIDTH = 70;
	protected static final double PROCESS_X0 = 25;	
	protected static final double PROCESS_HEIGHT = 15;
	protected static final double PROCESS_FULL_WIDTH = 595;

	protected int resolution = 15;

	protected Framework framework;
	
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.repository.framework");

	public FrameworkPresentation(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		Collection repository = null;
		XMLResource applicationsResource = null;
		XMLResource frameworkResource = null;

		try {
		    JAXBContext frameworkContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(frameworkContext, context + "/WEB-INF/xsd/framework.xsd");
		    frameworkMarshaller = frameworkContext.createMarshaller();

		    repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
		    framework = (uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}

	public void render(XMLSlideShow presentation) {
		renderOverview(presentation);
		renderLevel3(presentation);
	}

	protected void renderOverview(XMLSlideShow presentation) {
    	XSLFSlide slide = presentation.createSlide();
    	XSLFAutoShape shape;
    	
    	drawTitle(slide, "Logical Architecture Framework");
    	
    	double y0 = 80.0;
    	double y = y0;
    	double x = (PAGE_WIDTH - LAYER_WIDTH)/2;
   	
    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, CONTEXT_LAYER_HEIGHT, "Market Context", 12.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	y += CONTEXT_LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Business Operating Model", 12.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Business Applications", 12.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getBusinessApplications().getTechnologyDomain(), x, y, false);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Common Services", 12.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getCommonServices().getTechnologyDomain(), x, y, false);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Infrastructure", 12.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getInfrastructure().getTechnologyDomain(), x, y, false);
    	y += LAYER_HEIGHT;
	}
	
	protected void renderLevel3(XMLSlideShow presentation) {
    	XSLFSlide slide = presentation.createSlide();
    	XSLFAutoShape shape;
    	
    	drawTitle(slide, "Logical Architecture Framework: Level 3");
    	
    	double y0 = 80.0;
    	double y = y0;
    	double x = (PAGE_WIDTH - LAYER_WIDTH)/2;
   	
    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, CONTEXT_LAYER_HEIGHT, "Market Context", 8.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	y += CONTEXT_LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Business Operating Model", 8.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Business Applications", 8.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getBusinessApplications().getTechnologyDomain(), x, y, true);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Common Services", 8.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getCommonServices().getTechnologyDomain(), x, y, true);
    	y += LAYER_HEIGHT + MARGIN;

    	shape = POIHelper.textShape(slide, x, y, LAYER_WIDTH, LAYER_HEIGHT, "Infrastructure", 8.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setVerticalAlignment(VerticalAlignment.TOP);
    	shape.setTopInset(0.0);
    	shape.setFillColor(CLR_LAYER);
    	drawTechnologyDomains(slide, framework.getInfrastructure().getTechnologyDomain(), x, y, true);
    	y += LAYER_HEIGHT;
	}
	
	protected void drawTechnologyDomains(XSLFSlide slide, List<TechnologyDomain> technologyDomains, double x0, double y0, boolean detail) {
		int rows = (technologyDomains.size() - 1)/MAX_DOMAINS + 1;
		double titleHeight = (detail) ? TITLE_HEIGHT2 : TITLE_HEIGHT1;
		double domainHeight = (LAYER_HEIGHT - MARGIN - titleHeight)/rows - MARGIN;

		int domainCount = technologyDomains.size();
		if (rows > 1) {
			domainCount = (technologyDomains.size() - 1) / rows + 1;
		}
		double domainWidth = (LAYER_WIDTH - MARGIN)/domainCount - MARGIN;
		
		double x = x0 + MARGIN;
		double y = y0 + MARGIN + titleHeight;
		int i = 0;
		for (TechnologyDomain domain: technologyDomains) {
			drawDomain(slide, domain, x, y, domainWidth, domainHeight, detail, CLR_UNKNOWN, "");
	    	
	    	if ((i % domainCount) == (domainCount - 1)) {
	    		x = x0 + MARGIN;
	    		y += domainHeight + MARGIN;
	    	} else {
	    		x += MARGIN + domainWidth;
	    	}
	    	
	    	i++;
		}
	}
	
	protected void drawDomain(XSLFSlide slide, TechnologyDomain domain, double x, double y, double width, double height, boolean detail, Color colour, String domainPrefix) {
		XSLFAutoShape shape = POIHelper.textShape(slide, x, y, width, height, domain.getName(), (detail) ? 4.0 : 6.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setLeftInset(2.84);
    	shape.setRightInset(2.84);
    	//shape.setFillColor(getStandardisationColour(domain.getStandardisation()));
    	shape.setFillColor(colour);
		if (detail) {
	    	shape.setTopInset(1.0);
			shape.setVerticalAlignment(VerticalAlignment.TOP);
			drawCapabilities(slide, domain.getCapability(), x, y, width, height, colour, domainPrefix);
		}		
	}
	
	protected void drawCapabilities(XSLFSlide slide, List<Capability> capabilities, double x0, double y0, double domainWidth, double domainHeight, Color domainColour, String domainPrefix) {
		int capabilityCount = capabilities.size();
		int maxRows = (int) Math.ceil((domainHeight - TITLE_HEIGHT3 - MARGIN) / (CAPABILITY_HEIGHT + MARGIN));
		int columns = (capabilityCount - 1)/maxRows + 1;
		int rows = (capabilityCount + columns - 1)/columns;

		double capabilityHeight = (domainHeight - TITLE_HEIGHT3 - MARGIN) / rows - MARGIN;
		//double capabilityHeight = CAPABILITY_HEIGHT;
		double capabilityWidth = (domainWidth - MARGIN)/columns - MARGIN;
		
		double x = x0 + MARGIN;
		double y = y0 + MARGIN + TITLE_HEIGHT3;
		int i = 0;
		for (Capability capability: capabilities) {
			XSLFAutoShape shape = POIHelper.textShape(slide, x, y, capabilityWidth, capabilityHeight, domainPrefix + capability.getName(), 4.0);
	    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
	    	shape.setLeftInset(1.84);
	    	shape.setRightInset(1.84);
	    	shape.setFillColor(domainColour.brighter());
	    	
	    	if ((i % columns) == (columns - 1)) {
	    		x = x0 + MARGIN;
	    		y += capabilityHeight + MARGIN;
	    		if ((capabilityCount - i) <= columns) {
	    			capabilityWidth = (domainWidth - MARGIN)/(capabilityCount - i - 1) - MARGIN;
	    		}
	    	} else {
	    		x += MARGIN + capabilityWidth;
	    	}
	    	
	    	i++;
		}
		
	}
	
	protected void drawDomain(XSLFSlide slide, ProcessDomain domain, double x, double y, double width, double height, boolean detail) {
		XSLFAutoShape shape = POIHelper.textShape(slide, x, y, width, height, domain.getName(), (detail) ? 4.0 : 6.0);
    	//shape.setShapeType(XSLFShapeType.ROUND_RECT);
    	shape.setLeftInset(2.84);
    	shape.setRightInset(2.84);
    	//shape.setFillColor(getStandardisationColour(domain.getStandardisation()));
		if (detail) {
	    	shape.setTopInset(1.0);
			shape.setVerticalAlignment(VerticalAlignment.TOP);
		}		
	}

	protected List<TechnologyDomain> buildValueChain(String valueChainId, String ecosystemId) {
		List<TechnologyDomain> domains = new ArrayList<TechnologyDomain>();

		for (TechnologyDomain domain: framework.getBusinessApplications().getTechnologyDomain()) {
			if (domain.getValueChain() != null && domain.getValueChain().equals(valueChainId)) {
				//if (domain.getDiversityQualifier().size() == 0 || isInEcosystem(domain, ecosystemId)) {
					domains.add(domain);
				//}
			}
		}
		
		/*for (TechnologyDomain domain: framework.getCommonServices().getTechnologyDomain()) {
			if (domain.getValueChain() != null && domain.getValueChain().equals(valueChainId)) {
				//if (domain.getDiversityQualifier().size() == 0 || isInEcosystem(domain, ecosystemId)) {
					domains.add(domain);
				//}
			}
		}
		
		for (TechnologyDomain domain: framework.getInfrastructure().getTechnologyDomain()) {
			if (domain.getValueChain() != null && domain.getValueChain().equals(valueChainId)) {
				//if (domain.getDiversityQualifier().size() == 0 || isInEcosystem(domain, ecosystemId)) {
					domains.add(domain);
				//}
			}
		}*/
		
		return domains;
	}
	
	protected void drawTitle(XSLFSlide slide, String title) {
		XSLFAutoShape shape = POIHelper.textShape(slide, 0, 0, PAGE_WIDTH, TITLE_HEIGHT, title, 24.0);
		shape.setFillColor(null);
		shape.setLineColor(null);
	}
}
