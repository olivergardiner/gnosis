package uk.org.whitecottage.ea.gnosis.repository.framework;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Parent;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Predecessor;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Process;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessDomain;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessFlow;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.ProcessInstance;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class ProcessFlowPresentation extends XmldbProcessor {
	
	// Default page size is 720x540
	protected static final double PAGE_WIDTH = 720.0;
	protected static final double PAGE_HEIGHT = 540.0;
	protected static final double TITLE_HEIGHT = 80.0;
	protected static final double MARGIN = 2.0;
	protected static final double TITLE_HEIGHT1 = 14.0;
	protected static final double TITLE_HEIGHT2 = 8.0;
	protected static final double TITLE_HEIGHT3 = 4.0;

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

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.repository.framework");

	public ProcessFlowPresentation(String URI, String repositoryRoot, String context) {
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
		renderProcesses(presentation);
	}

	protected void renderProcesses(XMLSlideShow presentation) {
    	XSLFSlide slide;
    	
    	/*
    	 * Rendering rules:
    	 * 		Child must start later than all parents
    	 * 		Parent must finish later than all children
    	 * 		Process must start after all its predecessors have finished
    	 * 		Non-contiguous processes must be separated by a gap
    	 */
    	
    	if (framework.getBusinessOperatingModel().getBusinessProcesses() != null) {
    		List<ProcessFlow> processFlows = framework.getBusinessOperatingModel().getBusinessProcesses().getProcessFlow();
	    	for (ProcessFlow flow: processFlows) {
	        	slide = presentation.createSlide();
	        	
	        	drawTitle(slide, "Process Flows: " + flow.getName());
	        	
	        	List<ProcessElement> flowProcesses = new ArrayList<ProcessElement>();
	        	
	        	for (ProcessInstance instance: flow.getProcessInstance()) {
	        		//for (String processId: instance.getProcessId()) {
	        		Process process = findProcess(instance.getProcessId());
	            	flowProcesses.add(new ProcessElement(instance, process));
	        	}
	        	

	        	log.info("Laying out " + flow.getName() );

	        	//System.out.println("Started layout");
	        	layoutProcesses(flowProcesses);
	        	//System.out.println("Finished layout");
	        	
	        	double y0 = 80.0;
	        	double y = y0;
	        	ProcessDomain currentDomain = null;
				for (ProcessElement element: flowProcesses) {
					ProcessDomain domain = findProcessDomain(element.getProcess().getProcessId());
					if (currentDomain == null) {
						currentDomain = domain;
					}
					if (domain != currentDomain) {
						drawProcessDomain(slide, currentDomain, y0, y - y0 - MARGIN);
						currentDomain = domain;
			    		y0 = y;
					}
					drawProcess(slide, element, y);
					y += PROCESS_HEIGHT + MARGIN;
				}
				drawProcessDomain(slide, currentDomain, y0, y - y0 - MARGIN);
	    	}
		}
	}
	
	protected Process findProcess(String processId) {
		for (ProcessDomain domain: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessDomain()) {
			for (Process process: domain.getProcess()) {
				if (process.getProcessId().equals(processId)) {
					return process;
				}
			}
		}
		
		return null;
	}
	
	protected ProcessDomain findProcessDomain(String processId) {
		for (ProcessDomain domain: framework.getBusinessOperatingModel().getBusinessProcesses().getProcessDomain()) {
			for (Process process: domain.getProcess()) {
				if (process.getProcessId().equals(processId)) {
					return domain;
				}
			}
		}
		
		return null;
	}
	
	protected void drawProcess(XSLFSlide slide, ProcessElement process, double y) {
		double x0 = PROCESS_X0 + PROCESS_DOMAIN_WIDTH + MARGIN;
		double unitWidth = PROCESS_FULL_WIDTH / resolution;
		double x = x0 + unitWidth * process.getStart();
		double width = unitWidth * (process.getEnd() - process.getStart());		

		XSLFAutoShape shape = POIHelper.textShape(slide, x, y, width, PROCESS_HEIGHT, process.getName(), 6.0);
    	shape.setFillColor(new Color(0xcc88cc));	    	
	}
	
	protected void drawProcessDomain(XSLFSlide slide, ProcessDomain domain, double y0, double height) {
		double width = PROCESS_DOMAIN_WIDTH;
		double y = y0;
		double x = PROCESS_X0;

		XSLFAutoShape shape = POIHelper.textShape(slide, x, y, width, height, domain.getName(), 6.0);
    	shape.setFillColor(new Color(0xeeaaee));	    	
	}
	
	protected void layoutProcesses(List<ProcessElement> flowProcesses) {
    	boolean changed = true;
    	int maxEnd = 0;
    	int layoutCount = 0;
    	while (changed && layoutCount < MAX_LAYOUTS) {
    		changed = false;
        	for (ProcessElement process: flowProcesses) {
        		if (process.getParent().size() == 0 && process.getPredecessor().size() == 0) {
        			process.setStart(0);
        			process.setEnd(maxEnd);
        		}	
    			if (alignChildProcesses(flowProcesses, process)) {
    	        	changed = true;
    			}
    			if (alignPredecessors(flowProcesses, process)) {
    				changed = true;
    			}
    			
    			if (process.getEnd() > maxEnd) {
    				maxEnd = process.getEnd();
    			}
        	}
        	layoutCount++;
    	}
    	
    	log.info("maxEnd: " + maxEnd);
    	
    	resolution = maxEnd;
	}
	
	protected boolean alignPredecessors(List<ProcessElement> areaProcesses, ProcessElement followingProcess) {
		boolean changed = false;

		for (ProcessElement predecessor: areaProcesses) {
			for (Predecessor p: followingProcess.getPredecessor()) {
				if (predecessor.hasProcessId(p.getProcess())) {
					boolean contiguous = true;
					if (p.isContiguous() != null) {
						contiguous = p.isContiguous().booleanValue();
					}
					int offset = (contiguous) ? 0 : 2;
					if (predecessor.getEnd() + offset > followingProcess.getStart()) {
						changed = true;
						followingProcess.setStart(predecessor.getEnd() + offset);
					}
					if (followingProcess.getEnd() - followingProcess.getStart() < processDuration(followingProcess)) {
						changed = true;
						followingProcess.setEnd(followingProcess.getStart() + processDuration(followingProcess));
					}
				}
			}
		}

		return changed;
	}
	
	protected boolean alignChildProcesses(List<ProcessElement> areaProcesses, ProcessElement parentProcess) {
		boolean changed = false;
		for (ProcessElement childProcess: areaProcesses) {
			for (Parent p: childProcess.getParent()) {
				if (parentProcess.hasProcessId(p.getProcess())) {
					if (childProcess.getStart() <= parentProcess.getStart()) {
						changed = true;
						childProcess.setStart(parentProcess.getStart() + 1);
					}
					if (childProcess.getEnd() - childProcess.getStart() < processDuration(childProcess, parentProcess)) {
						changed = true;
						childProcess.setEnd(childProcess.getStart() + processDuration(childProcess, parentProcess));
					}
					if (childProcess.getEnd() >= parentProcess.getEnd()) {
						changed = true;
						parentProcess.setEnd(childProcess.getEnd() + 1);
					}
				}
			}
		}
		
		return changed;
	}
	
	protected int processDuration(ProcessElement process) {
		int duration = PROCESS_DURATION;
		if (process.getDuration() != null) {
			switch (process.getDuration()) {
			case "shorter":
				duration--;
			case "short":
				duration--;
				break;
			case "longer":
				duration++;
			case "long":
				duration++;
				break;
			default:
				break;
			}
		}
		
		return duration;
	}
	
	protected int processDuration(ProcessElement process, ProcessElement parentProcess) {
		int duration = processDuration(process);
		if (process.getDuration() != null && process.getDuration().equals("always")) {
				duration = parentProcess.getEnd() - process.getStart() - 1;
				if (duration < PROCESS_DURATION) {
					duration = PROCESS_DURATION;
				}
		}
		
		return duration;
	}
	
	protected void drawTitle(XSLFSlide slide, String title) {
		XSLFAutoShape shape = POIHelper.textShape(slide, 0, 0, PAGE_WIDTH, TITLE_HEIGHT, title, 24.0);
		shape.setFillColor(null);
		shape.setLineColor(null);
	}
}
