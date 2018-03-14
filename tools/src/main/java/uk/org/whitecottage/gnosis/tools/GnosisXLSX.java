package uk.org.whitecottage.gnosis.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.org.whitecottage.gnosis.Activity;
import uk.org.whitecottage.gnosis.Application;
import uk.org.whitecottage.gnosis.ApplicationArchitecture;
import uk.org.whitecottage.gnosis.ApplicationDomain;
import uk.org.whitecottage.gnosis.GnosisFactory;
import uk.org.whitecottage.gnosis.LogicalApplication;
import uk.org.whitecottage.gnosis.LogicalApplicationArchitecture;
import uk.org.whitecottage.gnosis.Model;
import uk.org.whitecottage.gnosis.ProcessTaxonomy;

public class GnosisXLSX {
	protected XSSFWorkbook xlsx;
	GnosisFactory factory = GnosisFactory.eINSTANCE;

	public GnosisXLSX(XSSFWorkbook xlsx) {
		this.xlsx = xlsx;		
	}

	public void buildTaxonomy(Model model) {
		ProcessTaxonomy processTaxonomy = model.getProcessarchitecture().getProcesstaxonomy();
		processTaxonomy.getActivity().clear();
		
		Sheet process = xlsx.getSheet("ProcessTaxonomy");
		
		for (Row row: process) {
			String[] index = getCellValue(row, 1).split("\\.");
			if (row.getRowNum() > 0 && index.length == 1) {
				Activity activity = factory.createActivity();
				activity.setName(getCellValue(row, 2));
				activity.setDescription(getCellValue(row, 3));
			
				parseProcess(activity, row, 2);

				processTaxonomy.getActivity().add(activity);
			}
		}
	}
	
	protected void parseProcess(Activity parentActivity, Row row, int level) {

		row = nextRow(row);
		while (row != null) {
			String[] index = getCellValue(row, 1).split("\\.");
			if (index.length == level && !index[1].equals("0")) {
				Activity activity = factory.createActivity();
				activity.setName(getCellValue(row, 2));
				activity.setDescription(getCellValue(row, 3));
				
				parseProcess(activity, row, level + 1);
				
				parentActivity.getActivity().add(activity);
			} else if (index.length < level || index[1].equals("0")) {
				break;
			}
			row = nextRow(row);
		}
	}
	
	public void buildApplications(Model model) {
		LogicalApplicationArchitecture logicalApplicationArchitecture = model.getLogicalapplicationarchitecture();
		logicalApplicationArchitecture.getApplicationdomain().clear();
		ApplicationArchitecture applicationArchitecture = model.getApplicationarchitecture();
		applicationArchitecture.getApplication().clear();

		Map<String, ApplicationDomain> domainMap = new HashMap<String, ApplicationDomain>();
		Map<String, ApplicationDomain> ecosystemMap = new HashMap<String, ApplicationDomain>();
		Map<String, LogicalApplication> logicalAppMap = new HashMap<String, LogicalApplication>();
		
		Sheet applications = xlsx.getSheet("Applications");
		
		for (Row row: applications) {
			if (row.getRowNum() > 0) {
				String domainId = getCellValue(row, 0);
				ApplicationDomain domain = domainMap.get(domainId);
				if (domain == null) {
					domain = factory.createApplicationDomain();
					domain.setName(getCellValue(row, 1));
					domain.setDescription(getCellValue(row, 2));
					
					logicalApplicationArchitecture.getApplicationdomain().add(domain);
					domainMap.put(domainId, domain);
				}
				
				String ecosystemId = getCellValue(row, 3);
				ApplicationDomain ecosystem = ecosystemMap.get(ecosystemId);
				if (ecosystem == null) {
					ecosystem = factory.createApplicationDomain();
					ecosystem.setName(getCellValue(row, 4));
					ecosystem.setDescription(getCellValue(row, 5));
					
					domain.getApplicationdomain().add(ecosystem);
					ecosystemMap.put(ecosystemId, ecosystem);
				}
				
				String logicalAppId = getCellValue(row, 6);
				LogicalApplication logicalApp = logicalAppMap.get(logicalAppId);
				if (logicalApp == null) {
					logicalApp = factory.createLogicalApplication();
					logicalApp.setName(getCellValue(row, 7));
					logicalApp.setDescription(getCellValue(row, 8));
					
					ecosystem.getLogicalapplication().add(logicalApp);
					logicalAppMap.put(logicalAppId, logicalApp);
				}
				
				//String applicationId = getCellValue(row, 9);
				Application application = factory.createApplication();
				application.setName(getCellValue(row, 10));
				application.setDescription(getCellValue(row, 11));
				application.getRealises().add(logicalApp);
				
				applicationArchitecture.getApplication().add(application);
			}			
		}
	}
		
	protected Row nextRow(Row row) {
		return row.getSheet().getRow(row.getRowNum() + 1);
	}
	
	protected String getCellValue(Sheet sheet, int r, int c) {
		return getCellValue(sheet.getRow(r), c);
	}

	protected String getCellValue(Row row, int c) {
		if (row != null) {
			return getCellValue(row.getCell(c));
		}
		
		return "";
	}

	protected String getCellValue(Cell cell) {
		if (cell != null) {
			if (cell.getCellTypeEnum() == CellType.STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				Double d = cell.getNumericCellValue();
				return Integer.toString(d.intValue());
			}
		}
		
		return "";
	}
}
