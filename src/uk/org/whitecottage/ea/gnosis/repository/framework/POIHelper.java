package uk.org.whitecottage.ea.gnosis.repository.framework;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.apache.poi.xslf.usermodel.LineDecoration;
import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShapeType;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

public class POIHelper {

	public POIHelper() {
		// TODO Auto-generated constructor stub
	}

    public static XSLFAutoShape textShape(XSLFSlide slide, double x, double y, double width, double height, String label, double size) {
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

    public static void drawLine(XSLFSlide slide, double x1, double y1, double x2, double y2) {
    	double xt1;
    	double xt2;
    	double yt1;
    	double yt2;
    	boolean fliph = false;
    	boolean flipv = false;
 
    	XSLFConnectorShape connector = slide.createConnector();
		connector.setLineColor(Color.BLACK);
		//connector.setLineWidth(LINE);
		connector.setLineTailDecoration(LineDecoration.TRIANGLE);
		
    	if (x1 < x2) {
    		xt1 = x1;
    		xt2 = x2;
    	} else {
    		xt1 = x2;
    		xt2 = x1;
    		fliph = true;
    	}
    	
    	if (y1 < y2) {
    		yt1 = y1;
    		yt2 = y2;
    	} else {
    		yt1 = y2;
    		yt2 = y1;
    		flipv = true;
    	}
		
    	connector.setAnchor(new Rectangle2D.Double(xt1, yt1, xt2 - xt1, yt2 - yt1));
    	connector.setFlipHorizontal(fliph);
    	connector.setFlipVertical(flipv);
    }
}
