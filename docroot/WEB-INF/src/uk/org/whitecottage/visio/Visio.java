package uk.org.whitecottage.visio;

import java.io.File;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import uk.org.whitecottage.visio.jaxb.visio2003.AngleType;
import uk.org.whitecottage.visio.jaxb.visio2003.CellType;
import uk.org.whitecottage.visio.jaxb.visio2003.FillType;
import uk.org.whitecottage.visio.jaxb.visio2003.FlipXType;
import uk.org.whitecottage.visio.jaxb.visio2003.FlipYType;
import uk.org.whitecottage.visio.jaxb.visio2003.GeomType;
import uk.org.whitecottage.visio.jaxb.visio2003.HeightType;
import uk.org.whitecottage.visio.jaxb.visio2003.LineToType;
import uk.org.whitecottage.visio.jaxb.visio2003.LineType;
import uk.org.whitecottage.visio.jaxb.visio2003.LocPinXType;
import uk.org.whitecottage.visio.jaxb.visio2003.LocPinYType;
import uk.org.whitecottage.visio.jaxb.visio2003.MoveToType;
import uk.org.whitecottage.visio.jaxb.visio2003.NoFillType;
import uk.org.whitecottage.visio.jaxb.visio2003.NoLineType;
import uk.org.whitecottage.visio.jaxb.visio2003.NoShowType;
import uk.org.whitecottage.visio.jaxb.visio2003.NoSnapType;
import uk.org.whitecottage.visio.jaxb.visio2003.PageType;
import uk.org.whitecottage.visio.jaxb.visio2003.PinXType;
import uk.org.whitecottage.visio.jaxb.visio2003.PinYType;
import uk.org.whitecottage.visio.jaxb.visio2003.ResizeModeType;
import uk.org.whitecottage.visio.jaxb.visio2003.ShapeType;
import uk.org.whitecottage.visio.jaxb.visio2003.ShapesType;
import uk.org.whitecottage.visio.jaxb.visio2003.TextType;
import uk.org.whitecottage.visio.jaxb.visio2003.VisioDocumentType;
import uk.org.whitecottage.visio.jaxb.visio2003.WidthType;
import uk.org.whitecottage.visio.jaxb.visio2003.XFormType;
import uk.org.whitecottage.visio.jaxb.visio2003.XType;
import uk.org.whitecottage.visio.jaxb.visio2003.YType;

public class Visio {
	protected JAXBElement<VisioDocumentType> visio;
	protected Unmarshaller visioUnmarshaller;
	protected Marshaller visioMarshaller;

	public Visio() {
		this(null);
	}
	
	public Visio(String schemaPath) {
		try {
		    JAXBContext visioContext = JAXBContext.newInstance("uk.org.whitecottage.visio.jaxb.visio2003:uk.org.whitecottage.visio.jaxb.visio2007:uk.org.whitecottage.visio.jaxb.visio2010");
		    visioUnmarshaller = createUnmarshaller(visioContext, schemaPath);
		    visioMarshaller = visioContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public Visio(File file, String schemaPath) {
		this(schemaPath);
		
		try {
		    read(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public VisioDocumentType getRoot() {
		return (VisioDocumentType) visio.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public void read(File file) throws JAXBException {
		visio = (JAXBElement<VisioDocumentType>) visioUnmarshaller.unmarshal(file);
		
		VisioDocumentType visioDocument = visio.getValue();
		visioDocument.setKey(null);
		visioDocument.setStart(null);
	}

	public void write(OutputStream output) {
		try {
		    visioMarshaller.marshal(visio, output);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void write(File file) {
		try {
		    visioMarshaller.marshal(visio, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public ShapeType createBox(double x, double y, double width, double height) {
		ShapeType shape = createShape(x, y, width, height);
		
		GeomType geom = null;
		
		for (Object o: shape.getTextOrXFormOrLine()) {
			Object value = ((JAXBElement<?>) o).getValue();
			if (value instanceof GeomType) {
				geom = (GeomType) value;
				break;
			}
		}
		
		if (geom != null) {			
			List<Object> contents = geom.getNoFillOrNoLineOrNoShow();
			contents.add(createMoveTo(1, "Width*0", 0, "Height*0", 0));
			contents.add(createLineTo(2, "Width*1", width, "Height*0", 0));
			contents.add(createLineTo(3, "Width*1", width, "Height*1", height));
			contents.add(createLineTo(4, "Width*0", 0, "Height*1", height));
			contents.add(createLineTo(5, "Geometry1.X1", 0, "Geometry1.Y1", 0));
		}
		
		return shape;
	}
	
	public MoveToType createMoveTo(int index, String fx, double x, String fy, double y) {
		MoveToType moveTo = new MoveToType();
		
		moveTo.setIX(BigInteger.valueOf(index));
		moveTo.setX(createX(fx, x));
		moveTo.setY(createY(fy, y));
		
		return moveTo;
	}
	
	public LineToType createLineTo(int index, String fx, double x, String fy, double y) {
		LineToType lineTo = new LineToType();
		
		lineTo.setIX(BigInteger.valueOf(index));
		lineTo.setX(createX(fx, x));
		lineTo.setY(createY(fy, y));
		
		return lineTo;
	}
	
	public XType createX(String f, double xValue) {
		XType x = new XType();
		
		x.setValue(String.valueOf(xValue/25.4));
		x.setF(f);
		
		return x;
	}
	
	public YType createY(String f, double yValue) {
		YType y = new YType();
		
		y.setValue(String.valueOf(yValue/25.4));
		y.setF(f);
		
		return y;
	}
	
	public ShapeType createShape(double x, double y, double width, double height) {
		ShapeType shape = new ShapeType();

		shape.setType("Shape");				
		shape.setLineStyle(BigInteger.valueOf(3));
		shape.setFillStyle(BigInteger.valueOf(3));
		shape.setTextStyle(BigInteger.valueOf(3));
		
		List<Object> contents = shape.getTextOrXFormOrLine();
		contents.add(createXForm(x, y, width, height));
		
		JAXBElement<GeomType> geom = createGeom();
		contents.add(geom);
		
		return shape;
	}
	
	public JAXBElement<XFormType> createXForm(double x, double y, double width, double height) {		
		XFormType xform = new XFormType();
		List<CellType> contents = xform.getPinXOrPinYOrWidth();
		
		x /= 25.4;
		y /= 25.4;
		width /= 25.4;
		height /= 25.4;
		
		PinXType pinX = new PinXType();
		PinYType pinY = new PinYType();
		pinX.setValue(Double.toString(x));
		pinY.setValue(Double.toString(y));
		contents.add(pinX);
		contents.add(pinY);
		
		WidthType xfWidth = new WidthType();
		HeightType xfHeight = new HeightType();
		xfWidth.setValue(Double.toString(width));
		xfHeight.setValue(Double.toString(height));
		contents.add(xfWidth);
		contents.add(xfHeight);
		
		LocPinXType locPinX = new LocPinXType();
		LocPinYType locPinY = new LocPinYType();
		locPinX.setF("Width*0.5");
		locPinX.setValue(Double.toString(width * 0.5));
		locPinY.setF("Height*0.5");
		locPinY.setValue(Double.toString(height * 0.5));
		contents.add(locPinX);
		contents.add(locPinY);
		
		AngleType angle = new AngleType();
		angle.setValue("0");
		FlipXType flipX = new FlipXType();
		flipX.setValue("0");
		FlipYType flipY = new FlipYType();
		flipY.setValue("0");
		ResizeModeType resizeMode = new ResizeModeType();
		resizeMode.setValue("0");
		contents.add(angle);
		contents.add(flipX);
		contents.add(flipY);
		contents.add(resizeMode);
		
		return new JAXBElement<XFormType>(new QName("http://schemas.microsoft.com/visio/2003/core", "XForm"), XFormType.class, xform);
	}
	
	public JAXBElement<LineType> createLine() {		
		LineType line = new LineType();
		
		return new JAXBElement<LineType>(new QName("http://schemas.microsoft.com/visio/2003/core", "Line"), LineType.class, line);
	}
	
	public JAXBElement<FillType> createFill() {
		FillType fill = new FillType();
		
		return new JAXBElement<FillType>(new QName("http://schemas.microsoft.com/visio/2003/core", "Fill"), FillType.class, fill);
	}
	
	public JAXBElement<TextType> createText(String text) {
		TextType textElement = new TextType();
		
		textElement.getContent().add(text);
		
		return new JAXBElement<TextType>(new QName("http://schemas.microsoft.com/visio/2003/core", "Text"), TextType.class, textElement);
	}
	
	public JAXBElement<GeomType> createGeom() {
		GeomType geom = new GeomType();
		List<Object> contents = geom.getNoFillOrNoLineOrNoShow();
		
		NoFillType noFill = new NoFillType();
		noFill.setValue("0");
		NoLineType noLine = new NoLineType();
		noLine.setValue("0");
		NoShowType noShow = new NoShowType();
		noShow.setValue("0");
		NoSnapType noSnap = new NoSnapType();
		noSnap.setValue("0");
		contents.add(noFill);
		contents.add(noLine);
		contents.add(noShow);
		contents.add(noSnap);
		
		geom.setIX(BigInteger.valueOf(0));

		return new JAXBElement<GeomType>(new QName("http://schemas.microsoft.com/visio/2003/core", "Geom"), GeomType.class, geom);
	}
	
	public void setShapeID(ShapeType shape, int id) {
		shape.setID(BigInteger.valueOf(id));
	}
	
	public ShapesType getShapes(ShapeType shape) {
		for (Object o: shape.getTextOrXFormOrLine()) {
			if (o instanceof ShapesType) {
				return (ShapesType) o;
			}
		}
		
		ShapesType shapes = new ShapesType();
		shape.getTextOrXFormOrLine().add(shapes);

		return shapes;
	}
	
	public ShapesType getShapes(PageType page) {
		for (Object o: page.getPageSheetOrShapesOrConnects()) {
			if (o instanceof ShapesType) {
				return (ShapesType) o;
			}
		}
		
		ShapesType shapes = new ShapesType();
		page.getPageSheetOrShapesOrConnects().add(shapes);

		return shapes;
	}
	
	public PageType getPage(int index) {
		return visio.getValue().getPages().getPage().get(index);
	}
	
	public ShapeType addBox(List<ShapeType> shapeList, double x, double y, double width, double height) {
		ShapeType shape = createBox(x, y, width, height);

		shapeList.add(shape);
		
		setShapeID(shape, shapeList.size());
		
		return shape;
	}
	
	public ShapeType addBox(ShapeType shape, double x, double y, double width, double height) {
		return addBox(getShapes(shape).getShape(), x, y, width, height);
	}
	
	public ShapeType addBox(PageType page, double x, double y, double width, double height) {
		return addBox(getShapes(page).getShape(), x, y, width, height);
	}
	
	protected Unmarshaller createUnmarshaller(JAXBContext context, String schemaPath) throws SAXException, JAXBException {
		Unmarshaller unmarshaller = context.createUnmarshaller();

		if (schemaPath != null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
		    Source schemas[] = {
			    	new StreamSource(new File(schemaPath + "/visio.xsd")),
			    	new StreamSource(new File(schemaPath + "/visio12.xsd")),
			    	new StreamSource(new File(schemaPath + "/visio14.xsd"))
		    };
		    
		    Schema schema = schemaFactory.newSchema(schemas);
	
			unmarshaller.setSchema(schema);
		}
		
		return unmarshaller;
	}	
}
