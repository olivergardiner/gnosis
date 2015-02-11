package uk.org.whitecottage.ea.gnosis.repository.applications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Applications;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.ManagingOrganisation;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Migration;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.Stage;
import uk.org.whitecottage.ea.gnosis.jaxb.applications.UsedBy;
import uk.org.whitecottage.ea.gnosis.jaxb.classification.Classification;
import uk.org.whitecottage.ea.gnosis.jaxb.classification.Term;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Capability;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Company;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Division;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.Group;
import uk.org.whitecottage.ea.gnosis.jaxb.framework.TechnologyDomain;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ApplicationsSpreadsheet extends XmldbProcessor {
	protected Applications applications;
	protected Framework framework;
	protected List<String> capabilityFilter = null;

	protected Classification criticality;
	protected Classification sensitivity;
	protected Classification lifecycle;

	protected Unmarshaller applicationsUnmarshaller = null;
	protected Marshaller applicationsMarshaller = null;
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;
	protected Unmarshaller classificationUnmarshaller = null;
	protected Marshaller classificationMarshaller = null;
	
	protected static final int APPLICATION_NAME_WIDTH = 40;
	protected XSSFCellStyle headerStyle;
	protected XSSFCellStyle headerCentreStyle;
	protected XSSFCellStyle dateFieldStyle;
	protected int rowsProcessed;
	protected static final String columns[] = {
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
		"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ",
		"BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ",
		"CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV","CW","CX","CY","CZ"
	};

	private static final Log log = LogFactoryUtil.getLog(ApplicationsSpreadsheet.class);

	public ApplicationsSpreadsheet(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		Collection repository = null;
		XMLResource applicationsResource = null;
		XMLResource frameworkResource = null;

		Collection classifications = null;
		XMLResource criticalityResource = null;
		XMLResource sensitivityResource = null;
		XMLResource lifecycleResource = null;
		
		try {
		    JAXBContext applicationsContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.applications");
		    applicationsUnmarshaller = createUnmarshaller(applicationsContext, context + "/WEB-INF/xsd/applications.xsd");
		    applicationsMarshaller = applicationsContext.createMarshaller();

		    JAXBContext frameworkContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(frameworkContext, context + "/WEB-INF/xsd/framework.xsd");

		    JAXBContext classificationContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.classification");
		    classificationUnmarshaller = createUnmarshaller(classificationContext, context + "/WEB-INF/xsd/classification.xsd");

		    repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
		    frameworkResource = getResource(repository, "framework.xml");
		    framework = (uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());

		    classifications = repository.getChildCollection("classifications");
		    criticalityResource = (XMLResource) classifications.getResource("criticality.xml");
			criticality = (Classification) classificationUnmarshaller.unmarshal(criticalityResource.getContentAsDOM());
		    sensitivityResource = (XMLResource) classifications.getResource("sensitivity.xml");
			sensitivity = (Classification) classificationUnmarshaller.unmarshal(sensitivityResource.getContentAsDOM());
		    lifecycleResource = (XMLResource) classifications.getResource("application-lifecycle.xml");
			lifecycle = (Classification) classificationUnmarshaller.unmarshal(lifecycleResource.getContentAsDOM());

			for (Application app: applications.getApplication()) {
	    		Collections.sort(app.getStage(), new StageComparator());
	    	}
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
	
	public void update(XSSFWorkbook workbook) {
		updateGeneral(workbook);

		updateDomains(workbook);

		updateDivisions(workbook);

		updateLifecycle(workbook);

		updateMigrations(workbook);

		updateInvestments(workbook);
		
		store();
	}
	
	protected void store() {
		Collection repository = null;
		XMLResource updateResource = null;

	    try {
			// Create the DOM document
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.newDocument();
	    	applicationsMarshaller.marshal(applications, doc);
	    	
			repository = getCollection("");

		    // Convert the DOM document into an XMLResource
	    	updateResource = (XMLResource) repository.createResource("applications.xml", "XMLResource");
	    	updateResource.setContentAsDOM(doc);
	    	
	    	// Store the XMLResource
	    	repository.storeResource(updateResource);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void render(XSSFWorkbook workbook) {
		initialise(workbook);

		XSSFSheet sheet;
		
		while (workbook.getNumberOfSheets() > 0) {
			workbook.removeSheetAt(0);
		}
		
		sheet = workbook.createSheet("References");
		createReferences(sheet);
		workbook.setSheetHidden(0, XSSFWorkbook.SHEET_STATE_VERY_HIDDEN);

		sheet = workbook.createSheet("General");
		createGeneral(sheet);
		
		sheet = workbook.createSheet("Domains");
		createDomains(sheet);
		
		sheet = workbook.createSheet("Divisions");
		createDivisions(sheet);
				
		sheet = workbook.createSheet("Lifecycle");
		createLifecycle(sheet);
		
		sheet = workbook.createSheet("Migrations");
		createMigrations(sheet);
		
		sheet = workbook.createSheet("Investments");
		createInvestments(sheet);
	}

	protected void createReferences(XSSFSheet sheet) {
    	List<Application> apps = applications.getApplication();
    	
		int c = 0;
    	int r = 0;
    	for (Application app: apps) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(c);
    		cell.setCellValue(app.getAppId());
    	}
    	createName(sheet, "applicationId", c, r);
    	c++;

    	String HML[] = {"high", "medium", "low"};
    	for (r=0; r<3; r++) {
    		XSSFRow row = getRow(sheet, r);
    		XSSFCell cell = row.createCell(c);
    		cell.setCellValue(HML[r]);
    	}
    	createName(sheet, "HML", c, r);
    	c++;

    	String YesNo[] = {"Yes", "No",};
    	for (r=0; r<2; r++) {
    		XSSFRow row = getRow(sheet, r);
    		XSSFCell cell = row.createCell(c);
    		cell.setCellValue(YesNo[r]);
    	}
    	createName(sheet, "YesNo", c, r);
    	c++;

    	createClassification(sheet, criticality, "criticality", c);
    	c += 2;

    	createClassification(sheet, sensitivity, "sensitivity", c);
    	c += 2;

    	createClassification(sheet, lifecycle, "lifecycle", c);
    	c += 2;
    	
    	r = 0;
		for (TechnologyDomain domain: framework.getBusinessApplications().getTechnologyDomain()) {
			for (Capability capability: domain.getCapability()) {
	    		XSSFRow row = getRow(sheet, r);
				XSSFCell cell = row.createCell(c);
				cell.setCellValue(capability.getCapabilityId());
				cell = row.createCell(c + 1);
				cell.setCellValue(domain.getName() + " > " + capability.getName());
				r++;
			}
		}
		for (TechnologyDomain domain: framework.getCommonServices().getTechnologyDomain()) {
			for (Capability capability: domain.getCapability()) {
	    		XSSFRow row = getRow(sheet, r);
				XSSFCell cell = row.createCell(c);
				cell.setCellValue(capability.getCapabilityId());
				cell = row.createCell(c + 1);
				cell.setCellValue(domain.getName() + " > " + capability.getName());
				r++;
			}
		}
    	createName(sheet, "domainId", c, r);
    	createName(sheet, "domainName", c + 1, r);
		c += 2;
	}
	
	protected void createGeneral(XSSFSheet sheet) {
    	String names[] = {"Application Name", "Description", "Stage", "Managed by", "Contract ref", "Tower", "Owner", "Criticality", "Sensitivity", "Virtualisation", "Source", "Notes"};		
		int widths[] = {APPLICATION_NAME_WIDTH, 60, 20, 30, 30, 30, 30, 20, 20, 20, 30, 60};
		createHeader(sheet, addArray(new ArrayList<String>(), names), addArray(new ArrayList<Integer>(), widths));
    	
    	int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell;
    		cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    		cell = row.createCell(1);
    		cell.setCellValue(app.getDescription());
    		cell = row.createCell(2);
    		cell.setCellValue(getCurrentStage(app));
    		cell = row.createCell(3);
    		if (app.getManagingOrganisation() != null) {
	    		cell.setCellValue(app.getManagingOrganisation().getContent());
	    		cell = row.createCell(4);
	    		cell.setCellValue(app.getManagingOrganisation().getContractReference());
    		}
    		cell = row.createCell(5);
    		cell.setCellValue(app.getProcurementTower());
    		cell = row.createCell(6);
    		cell.setCellValue(app.getOwner());
    		cell = row.createCell(7);
    		cell.setCellValue(translateClassificationId(criticality, app.getCriticality()));
    		cell = row.createCell(8);
    		cell.setCellValue(translateClassificationId(sensitivity, app.getSensitivity()));
    		cell = row.createCell(9);
    		cell.setCellValue(app.getVirtualisation());
    		cell = row.createCell(10);
    		cell.setCellValue(app.getSource());
    		cell = row.createCell(11);
    		cell.setCellValue(app.getNotes());
    	}
    	
    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
    	DataValidationConstraint dvcCriticality = dvHelper.createFormulaListConstraint("criticalityName");
    	DataValidation dvCriticality = dvHelper.createValidation(dvcCriticality, new CellRangeAddressList(1, r, 7, 7));
    	sheet.addValidationData(dvCriticality);
    	DataValidationConstraint dvcSensitivity = dvHelper.createFormulaListConstraint("sensitivityName");
    	DataValidation dvSensitivity = dvHelper.createValidation(dvcSensitivity, new CellRangeAddressList(1, r, 8, 8));
    	sheet.addValidationData(dvSensitivity);
    	DataValidationConstraint dvcHML = dvHelper.createFormulaListConstraint("HML");
    	DataValidation dvVirtualisation = dvHelper.createValidation(dvcHML, new CellRangeAddressList(1, r, 9, 9));
    	sheet.addValidationData(dvVirtualisation);
    	
    	sheet.createFreezePane(0, 1);
	}

	protected void updateGeneral(XSSFWorkbook workbook) {
		XSSFSheet generalSheet = workbook.getSheet("General");

		for (Row row: generalSheet) {
			int r = row.getRowNum();
			if (r != 0) {
				Cell cell;
				Application app = getApplication(workbook, r);
				app.setName(getCellValue(row, 0));
				app.setDescription(getCellValue(row, 1));
				cell = row.getCell(3);
				if (cell != null) {
					ManagingOrganisation org = new ManagingOrganisation();
					org.setContent(cell.getStringCellValue());
					cell = row.getCell(4);
					if (cell != null) {
						org.setContractReference(cell.getStringCellValue());
					}
					app.setManagingOrganisation(org);
				}
				app.setProcurementTower(getCellValue(row, 5));
				app.setOwner(getCellValue(row, 6));
				String value = translateClassificationName(criticality, getCellValue(row, 7));
				if (value != null && !value.isEmpty()) {
					app.setCriticality(value);
				}
				value = translateClassificationName(sensitivity, getCellValue(row, 8));
				if (value != null && !value.isEmpty()) {
					app.setSensitivity(value);
				}
				value = getCellValue(row, 9);
				if (!value.isEmpty()) {
					app.setVirtualisation(value);
				}
				app.setSource(getCellValue(row, 10));
				app.setNotes(getCellValue(row, 11));
			}
		}
	}
	
	protected String getCurrentStage(Application app) {
		Calendar date = new GregorianCalendar();
		
		Stage current = null;
		for (Stage stage: app.getStage()) {
			if (stage.getDate() == null) {
				current = stage;
			} else {
				GregorianCalendar stageDate = stage.getDate().toGregorianCalendar();
				if (stageDate.after(date)) {
					return translateClassificationId(lifecycle, current.getLifecycle());
				} else {
					current = stage;
				}
			}
		}
		
		return translateClassificationId(lifecycle, current.getLifecycle());
	}

	protected void createDomains(XSSFSheet sheet) {
    	String names[] = {"Application Name", "Domain", "Domain", "Domain", "Domain", "Domain", "Domain", "Domain", "Domain", "Domain", "Domain"};		
		int widths[] = {APPLICATION_NAME_WIDTH, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
		createHeader(sheet, addArray(new ArrayList<String>(), names), addArray(new ArrayList<Integer>(), widths));

		int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    		int c = 1;
    		for (uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification classification: app.getClassification()) {
    			cell = row.createCell(c++);
		    	cell.setCellValue(translateDomainId(sheet.getWorkbook(), classification.getCapability()));    			
    		}
    	}

    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
    	DataValidationConstraint dvcDomains = dvHelper.createFormulaListConstraint("domainName");
    	DataValidation dvDomains = dvHelper.createValidation(dvcDomains, new CellRangeAddressList(1, r - 1, 1, 10));
    	sheet.addValidationData(dvDomains);

    	sheet.createFreezePane(0, 1);
	}

	protected void updateDomains(XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.getSheet("Domains");

		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				Application app = getApplication(workbook, r);
				app.getClassification().clear();
				for (int c=1; c<=10; c++) {
					Cell cell = row.getCell(c);
					if (cell != null && !cell.getStringCellValue().isEmpty()) {
						String domain = translateDomainName(workbook, cell.getStringCellValue());
						uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification appDomain = new uk.org.whitecottage.ea.gnosis.jaxb.applications.Classification();
						appDomain.setCapability(domain);
						app.getClassification().add(appDomain);
					}
				}
			}
		}
	}
	
	protected String translateDomainId(XSSFWorkbook workbook, String domainId) {
		XSSFSheet references = workbook.getSheet("References");
		XSSFName domainIdName = workbook.getName("domainId");
		String domainIdColumn = domainIdName.getRefersToFormula();
		int i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(i + 1);
		i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(0, i);
		
		for (i = 0; i < columns.length; i++) {
			if (domainIdColumn.equals(columns[i])) {
				break;
			}
		}
		
		if (i < columns.length) {
			for (Row row: references) {
				Cell cell = row.getCell(i);
				if (cell != null && cell.getStringCellValue().equals(domainId)) {
					return row.getCell(i + 1).getStringCellValue();
				}
			}
			
			return null;
		} else {
			return null;
		}
	}
	
	protected String translateDomainName(XSSFWorkbook workbook, String label) {
		XSSFSheet references = workbook.getSheet("References");
		XSSFName domainIdName = workbook.getName("domainId");
		String domainIdColumn = domainIdName.getRefersToFormula();
		int i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(i + 1);
		i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(0, i);
		
		for (i = 0; i < columns.length; i++) {
			if (domainIdColumn.equals(columns[i])) {
				break;
			}
		}
		
		if (i < columns.length) {
			for (Row row: references) {
				Cell cell = row.getCell(i + 1);
				if (cell != null && cell.getStringCellValue().equals(label)) {
					return row.getCell(i).getStringCellValue();
				}
			}
			
			return null;
		} else {
			return null;
		}
	}
	
	protected void createDivisions(XSSFSheet sheet) {
    	List<String> names = new ArrayList<String>();
    	names.add("Application Name");
    	List<Integer> widths = new ArrayList<Integer>();
    	widths.add(Integer.valueOf(APPLICATION_NAME_WIDTH));
		List<String> divisions = new ArrayList<String>();
		
		names.add("Pan-BBC");
		widths.add(Integer.valueOf(20));
   	
		for (Group group: framework.getBusinessOperatingModel().getOrganisation().getGroup()) {
			for (Company company: group.getCompany()) {
				for (Division division: company.getDivision()) {
					divisions.add(division.getDivisionId());
					names.add(division.getName());
					widths.add(Integer.valueOf(20));
				}
			}
		}
		
		createHeader(sheet, names, widths);
    	    	
    	int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    		cell = row.createCell(1);
    		cell.setCellFormula("IF(COUNTIF(C" + r + ":" + columns[divisions.size() + 1] + r + ",\"Yes\")=0,\"Yes\",\"\")");
    		List<UsedBy> usedBy = app.getUsedBy();
			for (UsedBy u: usedBy) {
        		cell = row.createCell(divisions.indexOf(u.getDivision()) + 2);
        		cell.setCellValue("Yes");
    		}
    	}
    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
    	DataValidationConstraint dvcYesNo = dvHelper.createFormulaListConstraint("YesNo");
    	DataValidation dvYesNo = dvHelper.createValidation(dvcYesNo, new CellRangeAddressList(1, r, 2, divisions.size() + 1));
    	sheet.addValidationData(dvYesNo);
    	
    	sheet.createFreezePane(0, 1);
	}
	
	protected void updateDivisions(XSSFWorkbook workbook) {
		List<String> divisions = new ArrayList<String>();
		
		for (Group group: framework.getBusinessOperatingModel().getOrganisation().getGroup()) {
			for (Company company: group.getCompany()) {
				for (Division division: company.getDivision()) {
					divisions.add(division.getDivisionId());
				}
			}
		}
		
		XSSFSheet sheet = workbook.getSheet("Divisions");

		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				Application app = getApplication(workbook, r);
				app.getUsedBy().clear();
				int c = 2;
				for (String divisionId: divisions) {
					Cell cell = row.getCell(c++);
					if (cell != null) {
						if (cell.getStringCellValue().equals("Yes")) {
							UsedBy u = new UsedBy();
							u.setDivision(divisionId);
							app.getUsedBy().add(u);
						}
					}
				}
			}
		}
		
		rowsProcessed = sheet.getLastRowNum();
	}
	
	protected void createLifecycle(XSSFSheet sheet) {
    	List<String> names = new ArrayList<String>();
    	names.add("Application Name");
    	List<Integer> widths = new ArrayList<Integer>();
    	widths.add(Integer.valueOf(APPLICATION_NAME_WIDTH));
    	
    	for (int i = 0; i < 7; i++) {
    		names.add("Lifecycle stage");
    		widths.add(Integer.valueOf(20));
    		names.add("Date");
    		widths.add(Integer.valueOf(20));
    	}

    	createHeader(sheet, names, widths);
    	
    	int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    		
        	int c = 1;
        	for (Stage stage: app.getStage()) {
        		cell = row.createCell(c++);
        		cell.setCellValue(translateClassificationId(lifecycle, stage.getLifecycle()));
        		if (stage.getDate() != null) {
	        		cell = row.createCell(c);
	        		cell.setCellValue(stage.getDate().toGregorianCalendar().getTime());
	        		cell.setCellStyle(dateFieldStyle);
        		}
        		c++;
        	}
    	}

    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
    	DataValidationConstraint dvcLifecycle = dvHelper.createFormulaListConstraint("lifecycleName");
    	for (int i = 0; i < 7; i++) {
        	DataValidation dvLifecycle = dvHelper.createValidation(dvcLifecycle, new CellRangeAddressList(1, r, i * 2 + 1, i * 2 + 1));
        	sheet.addValidationData(dvLifecycle);
    	}
    	
    	sheet.createFreezePane(0, 1);
	}

	protected void updateLifecycle(XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.getSheet("Lifecycle");

		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				Application app = getApplication(workbook, r);
				app.getStage().clear();
				for (int c=0; c<7; c++) {
					String lifecycle = getCellValue(row, c * 2 + 1);
					if (!lifecycle.isEmpty()) {
						Stage s = new Stage();
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						Date date = getDateCellValue(row, c * 2 + 2);
						if (date != null) {
							cal.setTime(date);
							s.setDate(getXMLDate(cal));
						}
						s.setLifecycle(translateLifecycle(workbook, lifecycle));
						app.getStage().add(s);
					}
				}
				
				if (app.getStage().size() == 0) {
					Stage s = new Stage();
					s.setLifecycle("unknown");
					app.getStage().add(s);
				}
			}
		}
	}
	
	protected String translateLifecycle(XSSFWorkbook workbook, String label) {
		XSSFSheet references = workbook.getSheet("References");
		XSSFName domainIdName = workbook.getName("lifecycleId");
		String domainIdColumn = domainIdName.getRefersToFormula();
		int i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(i + 1);
		i = domainIdColumn.indexOf("$");
		domainIdColumn = domainIdColumn.substring(0, i);
		
		for (i = 0; i < columns.length; i++) {
			if (domainIdColumn.equals(columns[i])) {
				break;
			}
		}
		
		if (i < columns.length) {
			for (Row row: references) {
				Cell cell = row.getCell(i + 1);
				if (cell != null && cell.getStringCellValue().equals(label)) {
					return row.getCell(i).getStringCellValue();
				}
			}
			
			return null;
		} else {
			return null;
		}
	}
	
	protected void createMigrations(XSSFSheet sheet) {
    	List<String> names = new ArrayList<String>();
    	names.add("Application Name");
    	List<Integer> widths = new ArrayList<Integer>();
    	widths.add(Integer.valueOf(APPLICATION_NAME_WIDTH));
    	
    	for (int i = 0; i < 4; i++) {
    		names.add("Target");
    		widths.add(Integer.valueOf(APPLICATION_NAME_WIDTH));
    		names.add("Date");
    		widths.add(Integer.valueOf(20));
    	}

    	createHeader(sheet, names, widths);
 
    	int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    		
        	int c = 1;
        	for (Migration migration: app.getMigration()) {
        		cell = row.createCell(c++);
        		cell.setCellValue(getApplication(migration.getTo()).getName());
        		if (migration.getDate() != null) {
	        		cell = row.createCell(c);
	        		cell.setCellValue(migration.getDate().toGregorianCalendar().getTime());
	        		cell.setCellStyle(dateFieldStyle);
        		}
        		c++;
        	}
    	}

    	//createName(sheet, "appName", 1, r);
    	XSSFName xName = sheet.getWorkbook().createName();
    	String c = columns[0];
    	xName.setNameName("appName");
    	xName.setRefersToFormula("Migrations!$" + c + "$2:$" + c + "$" + r);

    	XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
    	DataValidationConstraint dvcApplication = dvHelper.createFormulaListConstraint("appName");
    	for (int i = 0; i < 7; i++) {
        	DataValidation dvApplication = dvHelper.createValidation(dvcApplication, new CellRangeAddressList(1, r, i * 2 + 1, i * 2 + 1));
        	sheet.addValidationData(dvApplication);
    	}
    	
    	sheet.createFreezePane(0, 1);
	}

	protected void updateMigrations(XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.getSheet("Migrations");

		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				Application app = getApplication(workbook, r);
				app.getMigration().clear();
				for (int c=0; c<4; c++) {
					String target = getCellValue(row, c * 2 + 1);
					if (!target.isEmpty()) {
						Migration m = new Migration();
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTimeZone(TimeZone.getTimeZone("Europe/London"));
						Date date = getDateCellValue(row, c * 2 + 2);
						if (date != null) {
							cal.setTime(date);
							m.setDate(getXMLDate(cal));
						}
						m.setTo(findTarget(sheet, target));
						app.getMigration().add(m);
					}
				}
			}
		}
	}
	
	protected String findTarget(XSSFSheet sheet, String name) {
		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				if (getCellValue(row, 0).equals(name)) {
					return getApplicationId(sheet.getWorkbook(), r);
				}
			}
		}
		
		return "";
	}
	
	protected void createInvestments(XSSFSheet sheet) {
    	List<String> names = new ArrayList<String>();
    	names.add("Application Name");
    	List<Integer> widths = new ArrayList<Integer>();
    	widths.add(Integer.valueOf(APPLICATION_NAME_WIDTH));

    	createHeader(sheet, names, widths);
    	
    	int r = 1;
    	for (Application app: applications.getApplication()) {
    		XSSFRow row = sheet.createRow(r++);
    		XSSFCell cell = row.createCell(0);
    		cell.setCellValue(app.getName());
    	}
    	
    	sheet.createFreezePane(0, 1);
	}
	
	protected void updateInvestments(XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.getSheet("Investments");

		for (Row row: sheet) {
			int r = row.getRowNum();
			if (r != 0) {
				@SuppressWarnings("unused")
				Application app = getApplication(workbook, r);
				// TODO: Needs to be completed...
			}
		}
	}
	
	protected Application getApplication(XSSFWorkbook workbook, int row) {
		return getApplication(getApplicationId(workbook, row));
	}
	
	protected Application getApplication(String appId) {

		for (Application app: applications.getApplication()) {
			if (app.getAppId().equals(appId)) {
				return app;
			}
		}
		
		return null;
	}
	
	protected String getApplicationId(XSSFWorkbook workbook, int row) {
		XSSFSheet referencesSheet = workbook.getSheet("References");
		
		return referencesSheet.getRow(row - 1).getCell(0).getStringCellValue();
	}

	public void setDomainFilter(String filter) {
		if (filter == null || filter.isEmpty()) {
			capabilityFilter = null;
			return;
		}
		
		capabilityFilter = Arrays.asList(filter.split(","));
  	}
	
	protected boolean inCapabilityFilter(String capabilityId) {
		if (capabilityFilter == null) {
			return true;
		}
		
		return capabilityFilter.contains(capabilityId);
	}
	
	public void setDivisionFilter(String divisionFilter, boolean isStrict) {
		if (divisionFilter == null || divisionFilter.isEmpty()) {
			return;
		}
		
		List<String> divisions = Arrays.asList(divisionFilter.split(","));
    	List<Application> apps = applications.getApplication();
    	List<Application> removeList = new ArrayList<Application>();

    	Iterator<Application> a = apps.iterator();
    	while (a.hasNext()) {
    		Application app = a.next();
    		boolean include = false;
    		Iterator<UsedBy> u = app.getUsedBy().iterator();
    		if (!u.hasNext() && !isStrict) {
    			include = true;
    		}
    		while (u.hasNext()) {
	    		if (divisions.contains(u.next().getDivision())) {
	    			include = true;
	    		}
    		}
    		if (!include) {
    			removeList.add(app);
    		}
    	}
    	
    	a = removeList.iterator();
    	while (a.hasNext()) {
			apps.remove(a.next());
    	}
	}

	protected void createClassification(XSSFSheet sheet, Classification classification, String classificationName, int column) {
    	int r = 0;
    	for (Term term: classification.getTerm()) {
    		XSSFRow row = getRow(sheet, r++);
    		XSSFCell cell = row.createCell(column);
    		cell.setCellValue(term.getTermId());
    		cell = row.createCell(column + 1);
    		cell.setCellValue(term.getName());
    	}
    	createName(sheet, classificationName + "Id", column, r);
    	createName(sheet, classificationName + "Name", column + 1, r);
	}
	
	protected XSSFName createName(XSSFSheet sheet, String name, int column, int lastRow) {
    	XSSFName xName = sheet.getWorkbook().createName();
    	String c = columns[column];
    	xName.setNameName(name);
    	xName.setRefersToFormula("References!$" + c + "$1:$" + c + "$" + lastRow);
    	
    	return xName;
	}

	protected XSSFRow getRow(XSSFSheet sheet, int r) {
		XSSFRow row = sheet.getRow(r);
		if (row == null) {
			row = sheet.createRow(r);
		}
		return row;
	}
	
	protected String getCellValue(Row row, int c) {
		Cell cell = row.getCell(c);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			}
		}

		return "";
	}
	
	protected Date getDateCellValue(Row row, int c) {
		Cell cell = row.getCell(c);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getDateCellValue();
			}
		}

		return null;
	}
	
	protected XMLGregorianCalendar getXMLDate(GregorianCalendar cal) {
		XMLGregorianCalendar xgc = null;
				
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			xgc = df.newXMLGregorianCalendar();
			xgc.setDay(cal.get(Calendar.DAY_OF_MONTH));
			xgc.setMonth(cal.get(Calendar.MONTH) + 1);
			xgc.setYear(cal.get(Calendar.YEAR));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		return xgc;
	}
	
	protected void createHeader(XSSFSheet sheet, List<String> columnNames, List<Integer> columnWidths) {
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		
		for (int i=0; i<columnNames.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnNames.get(i));
			cell.setCellStyle(headerStyle);
		}

		for (int i=0; i<columnWidths.size(); i++) {
			sheet.setColumnWidth(i, columnWidths.get(i).intValue() * 256);
		}
		
		sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, columnNames.size() - 1));
	}
	
	public void initialise(XSSFWorkbook workbook) {
		headerStyle = workbook.createCellStyle();		
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		
		headerCentreStyle = workbook.createCellStyle();
		headerCentreStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCentreStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerCentreStyle.setFont(font);
		headerCentreStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerCentreStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerCentreStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerCentreStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerCentreStyle.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFDataFormat dataFormat = workbook.createDataFormat();
		dateFieldStyle = workbook.createCellStyle();
		dateFieldStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd;@"));
	}

	protected List<String> addArray(List<String> list, String array[]) {
		for (String string : array) {
			list.add(string);
		}
		
		return list;
	}

	protected List<Integer> addArray(List<Integer> list, int array[]) {
		for (int i : array) {
			list.add(Integer.valueOf(i));
		}
		
		return list;
	}

	protected String translateClassificationId(Classification classification, String id) {
		List<Term> terms = classification.getTerm();
		for (Term term: terms) {
			if (term.getTermId().equals(id)) {
				return term.getName();
			}
		}
		
		return null;
	}

	protected String translateClassificationName(Classification classification, String name) {
		List<Term> terms = classification.getTerm();
		for (Term term: terms) {
			if (term.getName().equals(name)) {
				return term.getTermId();
			}
		}
		
		return null;
	}

	public int getRowsProcessed() {
		return rowsProcessed;
	}
}
