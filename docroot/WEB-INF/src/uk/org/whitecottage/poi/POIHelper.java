package uk.org.whitecottage.poi;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xslf.usermodel.LineDecoration;
import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShapeType;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class POIHelper {
	protected static final String COLUMNS[] = {
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
		"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ",
		"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ",
		"CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV","CW","CX","CY","CZ"
	};

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

	public static int intFromDouble(double d) {
		Double dd = new Double(d);
		return dd.intValue();
	}
		
	public static String getCellStringValue(XSSFSheet sheet, int r, int c) {
		return getCellStringValue(sheet.getRow(r), c);
	}

	public static String getCellStringValue(Row row, int c) {
		if (row != null) {
			return getCellStringValue(row.getCell(c));
		}
		
		return "";
	}

	public static String getCellStringValue(Cell cell) {
		if (cell != null) {
			//System.out.println("Cell(" + cell.getRowIndex() + ", " + cell.getColumnIndex() + ")");
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
					return cell.getStringCellValue();
				} else {
					return "";
				}
			} 
		}
		
		return "";
	}
	
	public static double getCellNumericValue(XSSFSheet sheet, int r, int c) {
		return getCellNumericValue(sheet.getRow(r), c);
	}

	public static double getCellNumericValue(Row row, int c) {
		if (row != null) {
			return getCellNumericValue(row.getCell(c));
		}
		
		return 0.0;
	}

	public static double getCellNumericValue(Cell cell) {
		if (cell != null) {
			//System.out.println("Cell(" + cell.getRowIndex() + ", " + cell.getColumnIndex() + ")");
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
					return cell.getNumericCellValue();
				} else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
					String n = cell.getStringCellValue();
					try {
						Double d = new Double(n);
						return d.doubleValue();
					} catch (java.lang.NumberFormatException e) {
						return 0.0;
					}
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String n = cell.getStringCellValue();
				try {
					Double d = new Double(n);
					return d.doubleValue();
				} catch (java.lang.NumberFormatException e) {
					return 0.0;
				}
			}
		}
		
		return 0.0;
	}

	protected XSSFName createName(XSSFSheet sheet, String name, int column, int lastRow) {
    	XSSFName xName = sheet.getWorkbook().createName();
    	String c = COLUMNS[column];
    	xName.setNameName(name);
    	xName.setRefersToFormula("References!$" + c + "$1:$" + c + "$" + lastRow);
    	
    	return xName;
	}
}
