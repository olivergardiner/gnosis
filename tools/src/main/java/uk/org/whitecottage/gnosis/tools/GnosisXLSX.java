package uk.org.whitecottage.gnosis.tools;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.org.whitecottage.gnosis.Activity;
import uk.org.whitecottage.gnosis.GnosisFactory;
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
