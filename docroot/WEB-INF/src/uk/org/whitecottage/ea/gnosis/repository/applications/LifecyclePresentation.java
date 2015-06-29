package uk.org.whitecottage.ea.gnosis.repository.applications;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xslf.usermodel.LineDecoration;
import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShapeType;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Applications;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Investment;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Migration;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Stage;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Activity;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.CapabilityInstance;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Ecosystem;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Milestone;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.gnosis.repository.roadmap.Timeline;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class LifecyclePresentation extends XmldbProcessor {
	
	// Default page size is 720x540
	protected double X0 = 69;
	protected double Y0 = 90;
	protected double DOMAIN_WIDTH = 60;
	protected double DOMAIN_HEIGHT = 50;
	protected double APP_WIDTH = 65;
	protected double APP_HEIGHT = 16;
	protected double STAGE_WIDTH = 97;
	protected double INV_WIDTH = 40;
	protected double INV_HEIGHT = 16;
	protected double MILESTONE_WIDTH = 80;
	protected double MILESTONE_HEIGHT = 16;
	protected double MILESTONE = 10;
	protected double CORNER = 10;
	protected double H_SPACING = 5;
	protected double V_SPACING = 5;
	protected double MARKER = 5;
	protected double COST_HEIGHT = 10;
	protected double CONNECTOR = 3;
	protected double LINE = 2;
	protected double TUBE_LINE = 6;
	protected double TEXT_OFFSET = 0;
	protected double TEXT_WIDTH = 80;
	protected double TITLE_WIDTH = 700;
	protected double TITLE_HEIGHT = 40;
	protected double TITLE_POSITION = 40;
	protected double ARROW = 5;
	protected int SUMMARY_PAGINATION = 7;
	protected int PAGINATION = 16;

	protected static final int CLR_CANDIDATE = 0xc020c0;
	protected static final int CLR_EMERGING = 0x20c0c0;
	protected static final int CLR_MAINSTREAM = 0x20c020;
	protected static final int CLR_HERITAGE = 0x808080;
	protected static final int CLR_SUNSET = 0xc0c020;
	protected static final int CLR_RETIRE = 0xc02020;
	protected static final int CLR_REMOVE = 0xc12020;
	protected static final int CLR_UNKNOWN = 0x000000;
	protected static final int CLR_DOMAIN = 0xe0e0e0;
	
	protected static final int[] STAGE_CLRS = {
		CLR_CANDIDATE, CLR_EMERGING, CLR_MAINSTREAM, CLR_HERITAGE, CLR_SUNSET, CLR_RETIRE, CLR_REMOVE, CLR_UNKNOWN
	};
	
	protected static final String[] STAGE_NAMES = {
		"Candidate", "Emerging", "Mainstream", "Heritage", "Sunset", "Retire"
	};
	
	protected static final String[] STAGE_VALUES = {
		"candidate", "emerging", "mainstream", "heritage", "sunset", "retire", "remove", "unknown"
	};

	protected Applications applications;
	protected Framework framework;
	protected List<String> capabilityFilter = null;
	
	protected Unmarshaller applicationsUnmarshaller = null;
	protected Marshaller applicationsMarshaller = null;
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(LifecyclePresentation.class);

	protected class EcosystemComparator implements Comparator<Application> {

		@Override
		public int compare(Application app1, Application app2) {
			if (app1.getEcosystem().isEmpty()) {
				if (app2.getEcosystem().isEmpty()) {
					return 0;
				} else {
					return -1;
				}
			} else if (app2.getEcosystem().isEmpty()) {
				return 1;
			}
			
			uk.org.whitecottage.ea.gnosis.jaxb.applications.Capability eco1 = app1.getEcosystem().get(0).getCapability().get(0);
			uk.org.whitecottage.ea.gnosis.jaxb.applications.Capability eco2 = app2.getEcosystem().get(0).getCapability().get(0);

			return compareEcosystem(eco1.getCapability(), eco2.getCapability());
		}
		
		protected int compareEcosystem(String eco1, String eco2) {
			int i1 = ecosystemIndex(eco1);
			int i2 = ecosystemIndex(eco2);
			
			//log.info("Eco1: " + eco1 + "  index: " + i1);
			
			if (i1 < i2) {
				return -1;
			} else if (i1 == i2) {
				return 0;
			} else {
				return 1;
			}
		}
		
		protected int ecosystemIndex(String capability) {
			int index = 0;
			
			for (Activity a: framework.getValueChain().getPrimaryActivities().getActivity()) {
				for (Ecosystem e: a.getEcosystem()) {
					for (CapabilityInstance c: e.getCapabilityInstance()) {
						if (c.getCapabilityId().equals(capability)) {
							return index;
						}
						index++;
					}
				}
			}
			
			for (Activity a: framework.getValueChain().getSupportActivities().getActivity()) {
				for (Ecosystem e: a.getEcosystem()) {
					for (CapabilityInstance c: e.getCapabilityInstance()) {
						if (c.getCapabilityId().equals(capability)) {
							return index;
						}
						index++;
					}
				}
			}
			
			return -1;
		}
	}
	
	public LifecyclePresentation(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		Collection repository = null;
		XMLResource applicationsResource = null;
		XMLResource frameworkResource = null;

		try {
		    JAXBContext applicationsContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.applications");
		    applicationsUnmarshaller = createUnmarshaller(applicationsContext, context + "/WEB-INF/xsd/applications.xsd");
		    applicationsMarshaller = applicationsContext.createMarshaller();

		    JAXBContext frameworkContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(frameworkContext, context + "/WEB-INF/xsd/framework.xsd");
		    frameworkMarshaller = frameworkContext.createMarshaller();

		    repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
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

	public void setCapabilityFilter(String filter) {
		if (filter == null || filter.isEmpty()) {
			capabilityFilter = null;
			return;
		}
		
		capabilityFilter = Arrays.asList(filter.split(","));
  	}
	
	public void render(XMLSlideShow presentation) {
		renderSummary(presentation);
		renderRoadmap(presentation);
		
		for (Activity activity: framework.getValueChain().getPrimaryActivities().getActivity()) {
			renderTubemap(presentation, activity, 7);
		}

		for (Activity activity: framework.getValueChain().getPrimaryActivities().getActivity()) {
			renderTubemap(presentation, activity, 6);
		}

		//renderTubemap(presentation, null, 2);
	}
	
	protected boolean inCapabilityFilter(String capabilityId) {
		if (capabilityFilter == null) {
			return true;
		}
		
		return capabilityFilter.contains(capabilityId);
	}
	
	protected boolean inCapabilityFilter(Application app) {
		if (capabilityFilter == null) {
			return true;
		}
		
		for (Classification classification: app.getClassification()) {
			if (inCapabilityFilter(classification.getCapability())) {
				return true;
			}
		}
		
		return false;
	}
	
	protected void renderSummary(XMLSlideShow presentation) {
    	XSLFSlide slide;
    	XSLFAutoShape shape;
    	List<TechnologyDomain> domains = new ArrayList<TechnologyDomain>();
    	
    	domains.addAll(framework.getBusinessApplications().getTechnologyDomain());
    	domains.addAll(framework.getCommonServices().getTechnologyDomain());
    	domains.addAll(framework.getInfrastructure().getTechnologyDomain());

    	slide = presentation.createSlide();
    	summaryHeader(slide);
    	
    	double y = Y0 + APP_HEIGHT;
		int count = 0;

		for (TechnologyDomain domain: domains) {
			List<Capability> capabilities = new ArrayList<Capability>();
			for (Capability c: domain.getCapability()) {
				if (inCapabilityFilter(c.getCapabilityId())) {
					capabilities.add(c);
				}
			}
			
			if (capabilities.size() > 0) {
				if (capabilities.size() + count > SUMMARY_PAGINATION) {
	    			slide = presentation.createSlide();
	    	    	summaryHeader(slide);
	    	    	
	    		   	y = Y0 + APP_HEIGHT;
	    			count = 0;
				}
				
				double domainHeight = capabilities.size() * DOMAIN_HEIGHT;
	    		shape = textShape(slide, X0 - DOMAIN_WIDTH, y , DOMAIN_WIDTH, domainHeight, domain.getName(), 6.0);
	    		shape.setFillColor(new Color(CLR_DOMAIN));
				
				int offset = 0;
				for (Capability capability: capabilities) {
					if (inCapabilityFilter(capability.getCapabilityId())) {
			    		shape = textShape(slide, X0, y + offset, DOMAIN_WIDTH, DOMAIN_HEIGHT, capability.getName(), 6.0);
			    		shape.setFillColor(new Color(CLR_DOMAIN));
			    		
			    		for (int k=0;k < STAGE_NAMES.length; k++) {
			    			shape = textShape(slide, X0 + DOMAIN_WIDTH + k * STAGE_WIDTH, y + offset, STAGE_WIDTH, DOMAIN_HEIGHT,
			    					getAppListByStage(STAGE_VALUES[k], capability, applications), 7.0);
			    			shape.setFillColor(new Color(STAGE_CLRS[k] + 0x2f2f2f));
			    		}
			    		
			    		offset += DOMAIN_HEIGHT;
					}
				}
				
				y += domainHeight;
				count += capabilities.size();
			}
		}
    }

    protected void summaryHeader(XSLFSlide slide) {
    	XSLFAutoShape shape;
    	
		for (int i=0;i < STAGE_NAMES.length; i++) {
			shape = textShape(slide, X0 + DOMAIN_WIDTH + i * STAGE_WIDTH, Y0, STAGE_WIDTH, APP_HEIGHT, STAGE_NAMES[i], 8.0);
			shape.setFillColor(new Color(STAGE_CLRS[i]));
			shape.getTextParagraphs().get(0).getTextRuns().get(0).setFontSize(8.0);
		}
    }
    
    protected String getAppListByStage(String stage, Capability capability, Applications applications) {
    	List<Application> apps = applications.getApplication();
    	String list = "";
    	
    	Iterator<Application> a = apps.iterator();
    	while (a.hasNext()) {
    		Application app = a.next();
    		if (providesCapability(app, capability)) {
	    		Stage appStage = getCurrentStage(app);
	    		if (appStage != null) {
		    		if (appStage.getLifecycle().equals(stage)) {
		    			if (list.length() > 0) {
		    				list += ", ";
		    			}
		    			list += app.getName();
		    		}
	    		}
    		}
    	}
    	
    	return list;
    }

    protected Stage getCurrentStage(Application app) {
    	
    	Stage currentStage = new Stage();
    	currentStage.setLifecycle("unknown");
    	GregorianCalendar date = new GregorianCalendar();
		List<Stage> stages = app.getStage();
		Collections.sort(stages, new StageComparator());
		Iterator<Stage> s = stages.iterator();
		while (s.hasNext()) {
			Stage stage = s.next();
			if (stage.getDate() == null) {
				currentStage = stage;
			} else {
				GregorianCalendar stageDate = stage.getDate().toGregorianCalendar();
				if (stageDate.after(date)) {
					return currentStage;
				} else {
					currentStage = stage;
				}
			}
		}
    	
    	return currentStage;
    }

    protected void renderRoadmap(XMLSlideShow presentation) {
    	List<TechnologyDomain> domains = new ArrayList<TechnologyDomain>();
    	
    	domains.addAll(framework.getBusinessApplications().getTechnologyDomain());
    	domains.addAll(framework.getCommonServices().getTechnologyDomain());
    	domains.addAll(framework.getInfrastructure().getTechnologyDomain());

    	for (TechnologyDomain domain: domains) {
			List<ApplicationGroup> groups = groupAppsByDomain(applications, domain, 2);
			Iterator<ApplicationGroup> j = groups.iterator();
			List<ApplicationGroup> groupPage = new ArrayList<ApplicationGroup>();
			int count = 0;
			
			while (j.hasNext()) {
				ApplicationGroup group = j.next();
				if (count != 0 && count + group.size() > PAGINATION) {
					createSlide2(presentation.createSlide(), groupPage, domain);
	    			groupPage = new ArrayList<ApplicationGroup>();
	    			count = 0;
	    		}

				if (group.size() > PAGINATION) {
					List<ApplicationGroup> splitGroups = group.split(PAGINATION);
					Iterator<ApplicationGroup> k = splitGroups.iterator();
					while (k.hasNext()) {
						ApplicationGroup sgroup = k.next();
						if (sgroup.size() + count > PAGINATION) {
							createSlide2(presentation.createSlide(), groupPage, domain);
			    			groupPage = new ArrayList<ApplicationGroup>();
			    			count = 0;
			    		}
		    			groupPage.add(sgroup);
		    			count += sgroup.size();
						
					}
	    		} else {
	    			groupPage.add(group);
	    			count += group.size();
	    		}
			}
			
    		if (count > 0) {
				createSlide2(presentation.createSlide(), groupPage, domain);
    		}
		}
    }

    protected void createSlide2(XSLFSlide slide, List<ApplicationGroup> groups, TechnologyDomain domain) {
		XSLFAutoShape shape;
   		XSLFConnectorShape connector;
    	Timeline tl = new Timeline(X0 + APP_WIDTH + H_SPACING, Y0);
    	tl.setRange(3);
    	tl.setCalendar(true);
    	
    	tl.addAll(getMilestones(domain));

    	header(slide, tl, domain.getName() + " roadmap");
   	
    	double y = Y0 + 3 * tl.getHeight();
    	
    	double ag = 0;
     	
    	Iterator<ApplicationGroup> i = groups.iterator();
    	while (i.hasNext()) {
    		ApplicationGroup group = i.next();
    		if (group.size() > 0) {
    			double gs = group.size() * (APP_HEIGHT + V_SPACING) - V_SPACING;
    			
    			shape = textShape(slide, X0 - DOMAIN_WIDTH, y, DOMAIN_WIDTH, gs, " " + group.getName(), 6.0);
    			shape.setLineColor(null);
	    		shape.setFillColor(new Color(CLR_DOMAIN));
    		}
    		
    		Iterator<Application> a = group.iterator();
    		while (a.hasNext()) {
    			
	    		Application app = a.next();
	    		
	    		if (!app.getAppId().isEmpty()) {
	    			Stage current = getCurrentStage(app);
	    			shape = textShape(slide, X0 + H_SPACING, y , APP_WIDTH, APP_HEIGHT, " " + app.getName(), 6.0);
	    			shape.setShapeType(XSLFShapeType.ROUND_RECT);
		    		if (current == null) {
		    			shape.setLineColor(new Color(0xaaaaaa));
		    		} else {
		    			shape.setLineColor(new Color(setColour(current.getLifecycle())));
		    		}
	    		}
	    		
	    		List<Migration> migrations = app.getMigration();
	    		Iterator<Migration> k = migrations.iterator();
	    		while (k.hasNext()) {
	    			Migration migration = k.next();
	    			if (tl.contains(migration.getDate())) {
    				String to = migration.getTo();
	    				int target = findApp2(group, to);
	    				if (target >= 0) {
	    					double yt = Y0 + 3 * tl.getHeight() + ((target + ag) % PAGINATION) * (APP_HEIGHT + V_SPACING);
	    					//double xm = X0 + APP_WIDTH + H_SPACING + tl.position(migration.getDate()) + tl.getOffset();
	    					double xm = X0 + APP_WIDTH + H_SPACING + tl.truePosition(migration.getDate());
	    					
		    		    	if (yt != y) {
		    		       		connector = slide.createConnector();
		    		       		if (yt < y) {
		    		       			connector.setAnchor(new Rectangle2D.Double(xm, yt + (APP_HEIGHT + MARKER) / 2, 0, y - yt - MARKER / 2));
		    		       			connector.setFlipVertical(true);
		    		       		} else {
		    		       			connector.setAnchor(new Rectangle2D.Double(xm, y + APP_HEIGHT / 2, 0, yt - y - MARKER / 2));
		    		       		}
		    					connector.setLineColor(Color.black);
		    					connector.setLineWidth(LINE);
		    					connector.setLineHeadDecoration(LineDecoration.OVAL);
		    					connector.setLineTailDecoration(LineDecoration.TRIANGLE);
		    		    	}
	    				}
	    			}
	    		}
	
	    		List<Stage> stages = app.getStage();
	    		Collections.sort(stages, new StageComparator());
	    		int lineColour = 0xffffff;
	    		double x = X0 + APP_WIDTH + H_SPACING;
	    		double w0 = 0;
				double mOffset = 0;
	    		for (Stage stage: stages) {
	    			int leadLineColour = lineColour;
					if (tl.contains(stage.getDate())) {
						lineColour = setColour(stage.getLifecycle());
		    			if (stage.getDate() != null) {
		    				//double w = tl.position(stage.getDate()) - w0;
		    				double w = tl.truePosition(stage.getDate()) - w0;
		    				double pOffset = 0;
		    				//if (w0 == 0) {
		    				//	pOffset = tl.getOffset();
		    				//}
		    				
		    				if (w > mOffset) {
		    		       		connector = slide.createConnector();
		    					connector.setAnchor(new Rectangle2D.Double(x + mOffset, y + APP_HEIGHT / 2, w + pOffset - mOffset, 0));
		    					connector.setLineColor(new Color(leadLineColour));
		    					connector.setLineWidth(LINE);
		    				}
		
		    				mOffset = MARKER / 2;
		    				w0 += w;
	    					x += w + pOffset;
							marker(slide, x, y + APP_HEIGHT / 2, lineColour);
		    			}
	    			}
	    		}
	    		
	    		if (lineColour != CLR_REMOVE && stages.size() > 0) {
		       		connector = slide.createConnector();
					connector.setAnchor(new Rectangle2D.Double(x + mOffset, y + APP_HEIGHT / 2, tl.getWidth() + X0 + APP_WIDTH + H_SPACING - x - mOffset, 0));
					connector.setLineColor(new Color(lineColour));
					connector.setLineWidth(2.0);
	    		}
	    		
	    		List<Investment> investments = app.getInvestment();
	    		Collections.sort(investments, new InvestmentComparator());
	    		for (Investment inv: investments) {
	    			BigInteger capital = inv.getCapital();
	    			BigInteger runrate = inv.getRunrate();
					if (tl.contains(inv.getDate())) {
		    			if (inv.getDate() != null) {
	    					//x = X0 + APP_WIDTH + H_SPACING + tl.position(inv.getDate());
	    					x = X0 + APP_WIDTH + H_SPACING + tl.truePosition(inv.getDate());
							investment(slide, x, y + APP_HEIGHT / 2, inv.getValue());							
							costs(slide, x + INV_WIDTH, y + APP_HEIGHT / 2, capital, runrate);
		    			} else {
							costs(slide, x, y + APP_HEIGHT / 2, capital, runrate);
		    			}
	    			}
	    		}
				
	   			y += APP_HEIGHT + V_SPACING;
	    	}
    		
    		ag += group.size();
    	}
    }
    
    protected void renderTubemap(XMLSlideShow presentation, Activity activity, int mask) {
    	XSLFSlide slide = null;
    	XSLFAutoShape shape;
    	Timeline tl = new Timeline(X0 + APP_WIDTH + H_SPACING, Y0);
    	tl.setRange(3);
    	tl.setCalendar(true);
    	
    	//tl.addAll(getMilestones(domain));

    	double y = 0;
    	boolean createSlide = true;
    	boolean useFutureFilter = (mask & 1) != 0;
    	boolean useCapabilityFilter = (mask & 2) != 0;
    	boolean useActivityFilter = (mask & 4) != 0;
    	
		List<Application> appsList = applications.getApplication();
		Collections.sort(appsList, new EcosystemComparator());
		
		for (Application app: appsList) {
			boolean include = (!useFutureFilter || isFuture(app));
			include = include && (!useCapabilityFilter || inCapabilityFilter(app));
			include = include && (!useActivityFilter || inValueChain(app, activity));
			
			if (include) {
				if (createSlide) {
	   				y = Y0 + 3 * tl.getHeight();
	   				slide = presentation.createSlide();
	   		    	header(slide, tl, "Application Tube Map: " + activity.getName());
	   		    	createSlide = false;
				}
	    		List<Stage> stages = app.getStage();
	    		Collections.sort(stages, new StageComparator());
	    		int lineColour = 0xffffff;
	    		double x = X0 + APP_WIDTH + H_SPACING;
	    		double w0 = 0;
	    		for (Stage stage: stages) {
	    			int leadLineColour = lineColour;
					lineColour = setColour(stage.getLifecycle());
					if (tl.contains(stage.getDate())) {
		    			if (stage.getDate() != null) {
		    				//double w = tl.position(stage.getDate()) - w0;
		    				double w = tl.truePosition(stage.getDate()) - w0;
		    				double pOffset = 0;
		    				//if (w0 == 0) {
		    				//	pOffset = tl.getOffset();
		    				//}
		    				
		        			shape = textShape(slide, x, y , w + pOffset, APP_HEIGHT, "", 6.0);
		        			shape.setShapeType(XSLFShapeType.RECT);
		        			shape.setFillColor(new Color(leadLineColour));
		        			shape.setLineColor(null);
		
		    				w0 += w;
	    					x += w + pOffset;
		    			}
	    			}
	    		}
	    		
	    		if (lineColour != CLR_REMOVE) {
        			shape = textShape(slide, x, y , tl.getWidth() + X0 + APP_WIDTH + H_SPACING - x, APP_HEIGHT, "", 6.0);
        			shape.setShapeType(XSLFShapeType.RECT);
        			shape.setFillColor(new Color(lineColour));
        			shape.setLineColor(null);
	    		}

	    		List<Investment> investments = app.getInvestment();
	    		Collections.sort(investments, new InvestmentComparator());
	    		for (Investment inv: investments) {
					if (tl.contains(inv.getDate())) {
		    			if (inv.getDate() != null) {
	    					//x = X0 + APP_WIDTH + H_SPACING + tl.position(inv.getDate());
	    					x = X0 + APP_WIDTH + H_SPACING + tl.truePosition(inv.getDate());
							milestone(slide, x , y + APP_HEIGHT / 2, inv.getValue());
		    			}
	    			}
	    		}
	    		
    			Stage current = getCurrentStage(app);
    			shape = textShape(slide, X0 - DOMAIN_WIDTH + H_SPACING, y , APP_WIDTH + DOMAIN_WIDTH, APP_HEIGHT, " " + app.getName(), 6.0);
    			shape.setShapeType(XSLFShapeType.ROUND_RECT);
	    		if (current == null) {
	    			shape.setLineColor(new Color(0xaaaaaa));
	    		} else {
	    			shape.setLineColor(new Color(setColour(current.getLifecycle())));
	    		}
			
	    		y += APP_HEIGHT + V_SPACING;
	   			if (y > 450.0) {
	   				createSlide = true;
	   			}
			}
		}
    }
    
    protected void header(XSLFSlide slide, Timeline tl, String domainName) {
    	XSLFAutoShape shape;
    	
    	double tx = (720 - TITLE_WIDTH) / 2;
    	shape = textShape(slide, tx, TITLE_POSITION, TITLE_WIDTH, TITLE_HEIGHT, domainName, 28.0);
    	shape.setLineColor(null);

    	tl.render(slide);
    }
    
    protected static List<Milestone> getMilestones(TechnologyDomain domain) {
    	List<Milestone> milestones = new ArrayList<Milestone>();
    	for (uk.org.whitecottage.ea.gnosis.jaxb.framework.Milestone m: domain.getMilestone()) {
    		Milestone milestone = new Milestone();
    		milestone.setValue(m.getValue());
    		milestone.setDate(m.getDate());
    		milestones.add(milestone);
    	}
     	
    	for (Capability c: domain.getCapability()) {
    		milestones.addAll(getMilestones(c));
    	}

    	return milestones;
    }
    
    protected static List<Milestone> getMilestones(Capability capability) {
    	List<Milestone> milestones = new ArrayList<Milestone>();
    	for (Milestone m: capability.getMilestone()) {
    		Milestone milestone = new Milestone();
    		milestone.setValue(m.getValue());
    		milestone.setDate(m.getDate());
    		milestones.add(milestone);
    	}
    	return milestones;
    }
    
    protected static int findApp(List<Application> applications, String id) {
    	for (int i=0;i<applications.size();i++) {
    		if (applications.get(i).getAppId().equals(id)) {
    			return i;
    		}
    	}
    	
    	return -1;
    }
    
    protected static int findApp2(ApplicationGroup group, String id) {
    	for (int i=0;i<group.getApplications().size();i++) {
        	if (group.getApplications().get(i) == null) {
        	} else if (group.getApplications().get(i).getAppId().equals(id)) {
    			return i;
    		}
    	}
    	
    	return -1;
    }

    protected XSLFAutoShape textShape(XSLFSlide slide, double x, double y, double width, double height, String label, double size) {
    	XSLFAutoShape shape;
    	XSLFTextParagraph labelPara;
    	XSLFTextRun labelText;

    	shape = slide.createAutoShape();
    	shape.setShapeType(XSLFShapeType.RECT);
    	shape.setAnchor(new Rectangle2D.Double(x, y , width, height));
    	shape.setLineColor(Color.black);
    	labelPara = shape.addNewTextParagraph();
    	labelPara.setTextAlign(TextAlign.CENTER);
    	labelText = labelPara.addNewTextRun();
    	labelText.setText(label);
    	labelText.setFontSize(size);
    	shape.setVerticalAlignment(VerticalAlignment.MIDDLE);

    	return shape;
    }

    protected void arrow(XSLFSlide slide, double x, double y, boolean up) {
    	XSLFAutoShape shape;

    	double xo = ARROW / 2;
    	double height = ARROW * 14 / 10;
    	double yo = height / 2;
    	
    	shape = slide.createAutoShape();
    	shape.setShapeType(XSLFShapeType.TRIANGLE);
    	shape.setLineColor(Color.black);
    	shape.setAnchor(new Rectangle2D.Double(x - xo, y - yo, ARROW, height));
    	
    	if (!up) {
        	shape.setFlipVertical(true);
    	}
    	
		shape.setLineWidth(LINE);
    }
    
    protected void marker(XSLFSlide slide, double x, double y, int colour) {
    	XSLFAutoShape shape;

    	double marker = MARKER;

    	shape = slide.createAutoShape();
    	shape.setShapeType(XSLFShapeType.ELLIPSE);
    	shape.setLineColor(new Color(colour));
		shape.setLineWidth(LINE);

    	if (colour == CLR_REMOVE) {
			marker = MARKER * 2;
			shape.setFillColor(Color.white);
    	} else {
			shape.setFillColor(new Color(colour));
    	}
    	
       	shape.setAnchor(new Rectangle2D.Double(x - marker / 2, y - marker / 2, marker, marker));
		
       	if (colour == CLR_REMOVE) {
       		XSLFConnectorShape connector;
			double o = marker / 2.82842;
       		
       		connector = slide.createConnector();
			connector.setAnchor(new Rectangle2D.Double(x - o, y - o, 2 * o, 2 * o));
			connector.setLineColor(new Color(colour));
			connector.setLineWidth(LINE);
       	}
    }

    protected void investment(XSLFSlide slide, double x, double y, String text) {
    	XSLFAutoShape shape;
   	
		shape = textShape(slide, x, y - INV_HEIGHT / 2 , INV_WIDTH, INV_HEIGHT, text, 3.0);
    	shape.setShapeType(XSLFShapeType.ROUND_RECT);
		shape.setFillColor(new Color(0xffffff));
    }

    protected void milestone(XSLFSlide slide, double x, double y, String text) {
    	XSLFAutoShape shape;
   	
		shape = textShape(slide, x, y - MILESTONE / 2 , MILESTONE, MILESTONE, "", 3.0);
    	shape.setShapeType(XSLFShapeType.RECT);
    	shape.setRotation(45.0);
		shape.setFillColor(new Color(0x000000));
	   	
		shape = textShape(slide, x + 20, y - (10 + MILESTONE_HEIGHT), MILESTONE_WIDTH, MILESTONE_HEIGHT, text, 4.0);
    	shape.setShapeType(XSLFShapeType.RECT);
		shape.setFillColor(new Color(0xffff66));

   		XSLFConnectorShape connector;
   		
   		connector = slide.createConnector();
		connector.setAnchor(new Rectangle2D.Double(x +  MILESTONE/2, y - (10 + MILESTONE_HEIGHT/2), 0, 10 + MILESTONE_HEIGHT/2));
		connector.setLineColor(new Color(0x000000));
		connector.setLineWidth(LINE);
   		
   		connector = slide.createConnector();
		connector.setAnchor(new Rectangle2D.Double(x +  MILESTONE/2, y - (10 + MILESTONE_HEIGHT/2), 20 - MILESTONE/2, 0));
		connector.setLineColor(new Color(0x000000));
		connector.setLineWidth(LINE);
    }

    protected void costs(XSLFSlide slide, double x, double y, BigInteger capital, BigInteger runrate) {
    	XSLFAutoShape shape;
    	XSLFTextParagraph labelPara;
    	XSLFTextRun labelText;
    	String label;

		if (capital != null) {
			label = "£" + capital + "k";
		} else {
			label = "£???k";
		}

    	shape = slide.createAutoShape();
    	shape.setAnchor(new Rectangle2D.Double(x, y, TEXT_WIDTH, COST_HEIGHT));
    	labelPara = shape.addNewTextParagraph();
    	labelPara.setTextAlign(TextAlign.LEFT);
    	labelText = labelPara.addNewTextRun();
    	labelText.setText(label);
    	labelText.setFontSize(6.0);
    	labelText.setFontColor(new Color(CLR_HERITAGE));
    	shape.setVerticalAlignment(VerticalAlignment.MIDDLE);
		
		if (runrate != null) {
			label = "£" + runrate + "k p.a.";
		} else {
			label = "£???k p.a.";
		}
		
    	shape = slide.createAutoShape();
    	shape.setAnchor(new Rectangle2D.Double(x + TEXT_OFFSET, y - COST_HEIGHT, TEXT_WIDTH, COST_HEIGHT));
    	labelPara = shape.addNewTextParagraph();
    	labelPara.setTextAlign(TextAlign.LEFT);
    	labelText = labelPara.addNewTextRun();
    	labelText.setText(label);
    	labelText.setFontSize(6.0);
    	labelText.setFontColor(new Color(CLR_HERITAGE));
    	shape.setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    protected int setColour(String s) {

    	if (s.equals("candidate")) {
    		return CLR_CANDIDATE;
    	} 
    	
    	if (s.equals("emerging")) {
    		return CLR_EMERGING;
    	} 
    	
    	if (s.equals("mainstream")) {
    		return CLR_MAINSTREAM;
    	} 
    	
    	if (s.equals("heritage")) {
    		return CLR_HERITAGE;
    	} 
    	
    	if (s.equals("sunset")) {
    		return CLR_SUNSET;
    	} 
    	
    	if (s.equals("retire")) {
    		return CLR_RETIRE;
    	} 
    	
    	if (s.equals("remove")) {
    		return CLR_REMOVE;
    	} 
    	
    	return CLR_UNKNOWN;
    }
    
    protected List<ApplicationGroup> groupAppsByDomain(Applications applications, TechnologyDomain domain, int level) {
    	List<ApplicationGroup> groups = new ArrayList<ApplicationGroup>();
    	
    	List<Capability> capabilities = domain.getCapability();
    	
    	for (Capability capability: capabilities) {
    		ApplicationGroup g = new ApplicationGroup();
    		g.setId(capability.getCapabilityId());
    		g.setName(capability.getName());
    		
			for (Application app: applications.getApplication()) {
				if (providesCapability(app, capability) && inCapabilityFilter(capability.getCapabilityId())) {
					g.add(app);
				}
			}
			
			groups.add(g);
		}
    	
     	return groups;
    }
    
    protected boolean isFuture(Application app) {
    	Stage stage = getCurrentStage(app);
    	if (stage.getLifecycle().equals(STAGE_VALUES[0]) || stage.getLifecycle().equals(STAGE_VALUES[1]) || stage.getLifecycle().equals(STAGE_VALUES[2]) || stage.getLifecycle().equals("unknown")) {
    		return true;
    	} else {    	
    		return false;
    	}
    }
        
    protected boolean providesCapability(Application app, Capability capability) {
    	Iterator<Classification> c = app.getClassification().iterator();
    	while (c.hasNext()) {
	    	if (c.next().getCapability().equals(capability.getCapabilityId())) {
	    		return true;
	    	}
    	}

    	return false;
    }

    protected boolean inEcosystem(Application app, Ecosystem ecosystem) {
    	for (uk.org.whitecottage.ea.gnosis.jaxb.applications.Ecosystem e: app.getEcosystem()) {
    		if (e.getEcosystem().equals(ecosystem.getEcosystemId())) {
    			return true;
    		}
    	}

    	return false;
    }

	protected boolean inValueChain(Application app, Activity activity) {
		for (Ecosystem ecosystem: activity.getEcosystem()) {
			if (inEcosystem(app, ecosystem)) {
				return true;
			}
		}
		
		return false;
	}
}
