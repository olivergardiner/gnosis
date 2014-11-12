package uk.org.whitecottage.ea.gnosis.repository.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import uk.org.whitecottage.ea.gnosis.jaxb.services.Services;
import uk.org.whitecottage.ea.gnosis.jaxb.services.Tower;
import uk.org.whitecottage.poi.POIHelper;

public class ServicesXLSXParser {
	protected XSSFWorkbook xlsx;
	protected String name;
	protected Services services;

	public static void main(String[] args) {
		ServicesXLSXParser fi = new ServicesXLSXParser();
		fi.init(args, false);
		fi.process();
	}

	protected void init(String[] args, boolean withXml) {
		if (args.length == 1) {
			name = args[0];
		} else {
			name = "aurora";
		}
		
		if (withXml) {
			Unmarshaller unmarshaller = null;
			try {
			    JAXBContext context = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.services");
			    unmarshaller = createUnmarshaller(context, "WEB-INF/services.xsd");
			} catch (JAXBException e) {
				System.err.println("Failed to create parser (JAXB problem)");
				e.printStackTrace();
			} catch (SAXException e) {
				System.err.println("Failed to create parser (SAX problem)");
				e.printStackTrace();
			}
			
			if (args.length < 1) {
				System.out.println("Please specify a file to process");
				return;
			}
			
			FileInputStream inputXml;
			try {
				inputXml = new FileInputStream(new File(name + ".xml"));
			} catch (FileNotFoundException e) {
				System.out.println("Unable to open file: " + name + ".xml");
				return;
			}
			
			try {
				services = (Services) unmarshaller.unmarshal(inputXml);
			} catch (JAXBException e) {
				System.err.println("Failed to parse XML file");
				e.printStackTrace();
			}
		}

		FileInputStream inputXlsx;
		try {
			inputXlsx = new FileInputStream(new File(name + ".xlsx"));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file: " + name + ".xlsx");
			return;
		}
		
		xlsx = null;
		try {
			xlsx = new XSSFWorkbook(inputXlsx);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			inputXlsx.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Unmarshaller createUnmarshaller(JAXBContext context, String schemaPath) throws SAXException, JAXBException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    File schemaFile = new File(schemaPath);
	    Schema schema = schemaFactory.newSchema(schemaFile);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(schema);
		
		return unmarshaller;
	}

	protected void process() {
    	Services newServices = new Services();
    	
    	boolean isTower = false;
    	for (XSSFSheet sheet: xlsx) {
    		if (sheet.getSheetName().equals("EndTower")) {
    			break;
    		}
    		
    		if (isTower) {
    			newServices.getTower().add(parseTower(sheet));
    		}
    		
    		if (sheet.getSheetName().equals("StartTower")) {
    			isTower = true;
    		}
    	}

		try {
		    JAXBContext context = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.services");
		    
		    Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

    		FileOutputStream output = new FileOutputStream(new File(name + ".xml"));

		    marshaller.marshal(newServices, output);
		    
		    output.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file: " + name + ".xml");
		} catch (JAXBException e) {
			System.out.println("Unable to create file: " + name + ".xml");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to close file: " + name + ".xml");
			e.printStackTrace();
		}
	}
	
	protected Tower parseTower(XSSFSheet sheet) {
		Tower tower = new Tower();
		
		tower.setId(UUID.randomUUID().toString());
		tower.setName(sheet.getSheetName());
		tower.setDescription("");
		
		//tower.setBundle(POIHelper.getCellStringValue(sheet, 2, 0));
		
		Map<String, ServiceComponentBuilder> serviceGroups = new HashMap<String, ServiceComponentBuilder>();
		
		System.out.println("Tower: " + tower.getName());

		int r = 2;
		while (sheet.getRow(r) != null) {
			if (!POIHelper.getCellStringValue(sheet, r, 0).isEmpty()) {
				int c = 1;
				Row row = sheet.getRow(r);
	
				Integer serviceGroupIndex = POIHelper.intFromDouble(POIHelper.getCellNumericValue(row, c++));
				String serviceGroupName = POIHelper.getCellStringValue(row, c++);
	
				if (serviceGroups.containsKey(serviceGroupName)) {
					parseLevel1(serviceGroups.get(serviceGroupName), row, c);
				} else {
					if (!serviceGroupName.isEmpty()) {
						ServiceComponentBuilder serviceGroup = new ServiceComponentBuilder(UUID.randomUUID().toString(), serviceGroupName);
						serviceGroup.setIndex(serviceGroupIndex.intValue());
						serviceGroup.setType("service-group");
						parseLevel1(serviceGroup, row, c);
						serviceGroups.put(serviceGroupName, serviceGroup);
					} else {
						System.out.println("Un-named service group @ row: " + row.getRowNum());
					}
				}
			}
			
			r++;
		}
		
		for (ServiceComponentBuilder serviceGroup: serviceGroups.values()) {
			tower.getServiceComponent().add(serviceGroup.toXML());
		}
		
		return tower;
	}
	
	protected void parseLevel1(ServiceComponentBuilder parent, Row row, int c) {
		Integer index = POIHelper.intFromDouble(POIHelper.getCellNumericValue(row, c++));
		String name = POIHelper.getCellStringValue(row, c++);
		String description = POIHelper.getCellStringValue(row, c++);
		
		ServiceComponentBuilder serviceComponent;
		if (parent.containsKey(name)) {
			serviceComponent = parent.get(name);
			if (serviceComponent.getDescription().isEmpty() && !description.isEmpty()) {
				serviceComponent.setDescription(formatText(description));
			}

			parseLevel2(serviceComponent, row, c);
		} else {
			if (!name.isEmpty()) {
				serviceComponent = new ServiceComponentBuilder(UUID.randomUUID().toString(), name);
				serviceComponent.setIndex(index.intValue());
				serviceComponent.setType("services-sub-group");
				serviceComponent.setDescription(formatText(description));
				parent.put(serviceComponent.getName(), serviceComponent);
			} else {
				System.out.println("Un-named level 1 service component @ row: " + row.getRowNum());
			}
		}
	}

	protected void parseLevel2(ServiceComponentBuilder parent, Row row, int c) {
		Integer index = POIHelper.intFromDouble(POIHelper.getCellNumericValue(row, c++));
		String name = POIHelper.getCellStringValue(row, c++);
		String description = POIHelper.getCellStringValue(row, c++);
		
		ServiceComponentBuilder serviceComponent;
		if (parent.containsKey(name)) {
			serviceComponent = parent.get(name);
			if (serviceComponent.getDescription().isEmpty() && !description.isEmpty()) {
				serviceComponent.setDescription(formatText(description));
			}
		} else {
			if (!name.isEmpty()) {
				serviceComponent = new ServiceComponentBuilder(UUID.randomUUID().toString(), name);
				serviceComponent.setIndex(index.intValue());
				serviceComponent.setPenultimate(true);
				serviceComponent.setType("service-sub-group");
				serviceComponent.setDescription(formatText(description));
				parent.put(serviceComponent.getName(), serviceComponent);
			} else {
				System.out.println("Un-named level 2 service component @ row: " + row.getRowNum());
				return;
			}
		}

		parseLevel3(serviceComponent, row, c);
	}

	protected void parseLevel3(ServiceComponentBuilder parent, Row row, int c) {
		Integer index = new Integer(parent.size() + 1);
		String name = POIHelper.getCellStringValue(row, c++);
		String description = POIHelper.getCellStringValue(row, c++);
		
		ServiceComponentBuilder serviceElement;
		if (parent.containsKey(name)) {
			// This should never happen...
			System.out.println("Non-unique level 3 service component declared");
			serviceElement = parent.get(name);
			if (serviceElement.getDescription().isEmpty() && !description.isEmpty()) {
				serviceElement.setDescription(formatText(description));
			}
		} else {
			if (!(name.isEmpty() && description.isEmpty())) {
				if (name.isEmpty()) {
					name = "Un-named level 3";
				}
				serviceElement = new ServiceComponentBuilder(UUID.randomUUID().toString(), name);
				serviceElement.setIndex(index.intValue());
				serviceElement.setType("service-element");
				serviceElement.setDescription(formatText(description));
				parent.put(serviceElement.getName(), serviceElement);
			} else {
				System.out.println("Blank level 3 service component @ row: " + row.getRowNum());
			}
		}
	}
	
	protected String formatText(String source) {
		String s = "";

		String lines[] = source.split("\n");		
		boolean isList = false;

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("\u2022")) {
				if (!isList) {
					isList = true;
					s += "<ul>";
				}
				s += "<li>" + sanitise(line) + "</li>";
			} else {
				if (isList) {
					isList = false;
					s += "</ul>";
				}
				s += "<p>" + sanitise(line) + "</p>";
			}
		}
		
		if (isList) {
			s += "</ul>";
		}
		
		return s;
	}
	
	protected String sanitise(String s) {		
		return s.replace("\u2022", "").replace("\u2026", "...").replace("\u201C", "\"").replace("\u201D", "\"").replace("\u2019", "'");
	}
}
