package uk.org.whitecottage.ea.gnosis.repository.roadmap;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShapeType;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Milestone;

public class Timeline {
	protected double x;
	protected double y;
	protected GregorianCalendar date;
	protected int range;
	protected boolean calendar;
	protected boolean showMilestoneLeader = true;
	protected List<Milestone> milestones;
	
	protected double WIDTH = 570;
	protected double HEIGHT = 20;
	protected double MILESTONE = 10;
	protected double MILESTONE_TEXT = 200;
	protected double SCREEN_HEIGHT = 390;
	protected int QTR_LABEL = 1;
	
	private static final Log log = LogFactoryUtil.getLog(Timeline.class);

	public Timeline() {
		range = 3;
		date = new GregorianCalendar();
		calendar = true;
		milestones = new ArrayList<Milestone>();
	}
	
	public Timeline(double x, double y) {
		range = 3;
		date = new GregorianCalendar();
		calendar = true;
		milestones = new ArrayList<Milestone>();
		this.x = x;
		this.y = y;
	}
	
	public boolean add(Milestone arg0) {
		return milestones.add(arg0);
	}

	public boolean addAll(Collection<? extends Milestone> arg0) {
		return milestones.addAll(arg0);
	}

	public void clear() {
		milestones.clear();
	}

	public Milestone get(int arg0) {
		return milestones.get(arg0);
	}

	public Iterator<Milestone> iterator() {
		return milestones.iterator();
	}

	public void render(XSLFSlide slide) {
		XSLFAutoShape shape;
		XSLFConnectorShape connector;
   		
		renderMilestones(slide);

		shape = slide.createAutoShape();
		shape.setAnchor(new Rectangle2D.Double(x, y, WIDTH, HEIGHT));
		shape.setLineColor(Color.black);
		
		int period = range * 4;
		double qtr = WIDTH / period;
		
		int year = date.get(GregorianCalendar.YEAR);
		int quarter = date.get(GregorianCalendar.MONTH) / 3;
		if (!calendar) {
			quarter = (quarter + 3) % 4;
		}
		
		if ((4 - quarter) > QTR_LABEL && quarter != 0) {
			double yearWidth = (4 - quarter) * qtr;
			String label = Integer.toString(year);
			if (!calendar) {
				label += "/" + (year + 1) % 100;
			}
			shape = textBox(slide, x, y, yearWidth, HEIGHT, label, 8.0);
		}
		
		for (int i=0; i <= period;i++) {
			if (quarter == 0) {
				if (i > 0) {
					year++;
				}

	       		connector = slide.createConnector();
				connector.setAnchor(new Rectangle2D.Double(x + i * qtr, y, 0, 3 * HEIGHT / 2));
				connector.setLineColor(Color.black);

				int qtrsLeft = period - i;
				if (qtrsLeft > QTR_LABEL) {
					double yearWidth;
					
					if (qtrsLeft > 4) {
						yearWidth = WIDTH / range;
					} else {
						yearWidth = qtrsLeft * qtr;
					}
					
					String label = Integer.toString(year);
					if (!calendar) {
						label += "/" + (year + 1) % 100;
					}
					
					shape = textBox(slide, x + i * qtr, y, yearWidth, HEIGHT, label, 8.0);
				}
				
			} else {
	       		connector = slide.createConnector();
				connector.setAnchor(new Rectangle2D.Double(x + i * qtr, y + HEIGHT, 0, HEIGHT / 4));
				connector.setLineColor(Color.black);
			}

			if (i < period) {
				shape = textBox(slide, x + i * qtr, y + HEIGHT, qtr, HEIGHT, "Q" + (quarter + 1), 8.0);
			}
			
			quarter = (quarter + 1) % 4;
		}
	}

	public void renderMilestones(XSLFSlide slide) {
		XSLFAutoShape shape;
    	XSLFTextParagraph labelPara;
    	XSLFTextRun labelText;
		XSLFConnectorShape connector;
		
		Collections.sort(milestones, new MilestoneComparator());
		
		int quarter0 = date.get(GregorianCalendar.MONTH) / 3;
		int day0 = quarter0 * 91;
		int year0 = date.get(GregorianCalendar.YEAR);
		
		Iterator<Milestone> m = milestones.iterator();
		boolean above = true;
		while (m.hasNext()) {
			Milestone milestone = m.next();
			if (contains(milestone.getDate())) {
				GregorianCalendar milestoneDate = milestone.getDate().toGregorianCalendar();
				int day = milestoneDate.get(GregorianCalendar.DAY_OF_YEAR);
				int year = milestoneDate.get(GregorianCalendar.YEAR);
				double xm = ((year - year0) * WIDTH + (day - day0) * WIDTH / 365) / range;
				double ym;
				double hm;
				
				if (above) {
					ym = y - HEIGHT;
					hm = SCREEN_HEIGHT + HEIGHT;
				} else {
					ym = y + 2 * HEIGHT;
					hm = SCREEN_HEIGHT - 2 * HEIGHT;
				}
				
				if (showMilestoneLeader) {
		       		connector = slide.createConnector();
					connector.setAnchor(new Rectangle2D.Double(x + xm, ym, 0, hm));
					connector.setLineColor(new Color(0x808080));
				}

				shape = slide.createAutoShape();
				shape.setAnchor(new Rectangle2D.Double(x + xm - MILESTONE / 2, ym, MILESTONE, MILESTONE));
				shape.setFillColor(new Color(0x80c0d0));
				shape.setRotation(45.0);
				
				shape = slide.createAutoShape();
				shape.setAnchor(new Rectangle2D.Double(x + xm - MILESTONE_TEXT - MILESTONE, ym, MILESTONE_TEXT, MILESTONE));
		    	labelPara = shape.addNewTextParagraph();
		    	labelPara.setTextAlign(TextAlign.RIGHT);
		    	labelText = labelPara.addNewTextRun();
		    	labelText.setText(milestone.getValue());
		    	labelText.setFontSize(8.0);
		    	shape.setVerticalAlignment(VerticalAlignment.MIDDLE);
				
				above = !above;
			}
		}
	}

	public boolean isShowMilestoneLeader() {
		return showMilestoneLeader;
	}

	public void setShowMilestoneLeader(boolean showMilestoneLeader) {
		this.showMilestoneLeader = showMilestoneLeader;
	}

	public double position(XMLGregorianCalendar d) {
		GregorianCalendar qDate = d.toGregorianCalendar();
		
		int year = date.get(GregorianCalendar.YEAR);
		int quarter = date.get(GregorianCalendar.MONTH) / 3;
		int y = qDate.get(GregorianCalendar.YEAR);
		int q = qDate.get(GregorianCalendar.MONTH) / 3;
		
		if (y < year || (y == year && q <= quarter)) {
			return 0;
		}
		
		int qtrs = (y - year) * 4 + q - quarter;
		
		return qtrs * WIDTH / (4 * range);
	}
	
	public double truePosition(XMLGregorianCalendar d) {
		GregorianCalendar qDate = d.toGregorianCalendar();
		
		int year = date.get(GregorianCalendar.YEAR);
		int quarter = date.get(GregorianCalendar.MONTH) / 3;
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(year, quarter * 3, 0);
		
		int offset = (int) ((qDate.getTimeInMillis() - startDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
		log.info("Offset: " + offset);
		
		if (offset < 0) {
			return 0;
		}
		
		return offset * WIDTH / (range * 365);
	}
	
	public boolean contains(XMLGregorianCalendar d) {
		if (d == null) {
			return true;
		}
		GregorianCalendar qDate = d.toGregorianCalendar();

		int year = date.get(GregorianCalendar.YEAR);
		int quarter = date.get(GregorianCalendar.MONTH) / 3;
		int y = qDate.get(GregorianCalendar.YEAR);
		int q = qDate.get(GregorianCalendar.MONTH) / 3;
		
		int qDiff = (y - year) * 4 + q - quarter;
		
		return !(qDiff < 0 || qDiff >= 4 * range);
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public boolean isCalendar() {
		return calendar;
	}

	public void setCalendar(boolean calendar) {
		this.calendar = calendar;
	}

	public double getWidth() {
		return WIDTH;
	}

	public void setWidth(double width) {
		WIDTH = width;
	}

	public double getHeight() {
		return HEIGHT;
	}

	public void setHeight(double height) {
		HEIGHT = height;
	}

	public double getOffset() {
		return WIDTH / (8 * range);
	}

	protected XSLFAutoShape textBox(XSLFSlide slide, double x, double y, double width, double height, String label, double size) {
    	XSLFAutoShape shape;
    	XSLFTextParagraph labelPara;
    	XSLFTextRun labelText;

    	shape = slide.createAutoShape();
    	shape.setShapeType(XSLFShapeType.RECT);
    	shape.setAnchor(new Rectangle2D.Double(x, y , width, height));
    	labelPara = shape.addNewTextParagraph();
    	labelPara.setTextAlign(TextAlign.CENTER);
    	labelText = labelPara.addNewTextRun();
    	labelText.setText(label);
    	labelText.setFontSize(size);
    	shape.setVerticalAlignment(VerticalAlignment.MIDDLE);

    	return shape;
    }
}
