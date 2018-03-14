package uk.org.whitecottage.gnosis.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import uk.org.whitecottage.gnosis.GnosisPackage;
import uk.org.whitecottage.gnosis.Model;

public class XLSX2Gnosis {
	private static final Logger logger = LogManager.getLogger(XLSX2Gnosis.class.getCanonicalName());

	public static void main(String[] args) {
		XSSFWorkbook xlsx = null;

		try {
			xlsx = new XSSFWorkbook(new FileInputStream(new File("gnosis.xlsx")));
		} catch (FileNotFoundException e) {
			logger.error("Unable to open file: " + e.getMessage());
			return;
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		GnosisXLSX gnosis = new GnosisXLSX(xlsx);

		GnosisPackage.eINSTANCE.eClass();
		
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("gnosis", new XMIResourceFactoryImpl());

        ResourceSet resourceSet = new ResourceSetImpl();

        Resource resource = resourceSet.getResource(URI.createFileURI("sample.gnosis"), true);
		try {
			resource.load(Collections.emptyMap());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		Model model = (Model) resource.getContents().get(0);
		
		model.getIntegrationarchitecture().getIntegration().clear();
		
		gnosis.buildTaxonomy(model);
		gnosis.buildApplications(model);
		
		try {
			resource.save(Collections.emptyMap());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
