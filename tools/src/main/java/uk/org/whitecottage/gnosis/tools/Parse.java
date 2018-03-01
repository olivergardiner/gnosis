package uk.org.whitecottage.gnosis.tools;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import uk.org.whitecottage.gnosis.GnosisPackage;
import uk.org.whitecottage.gnosis.Model;

public class Parse {
    private static Logger logger = Logger.getLogger(Parse.class.getCanonicalName());

	public static void main(String[] args) {
		GnosisPackage.eINSTANCE.eClass();
		
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("gnosis", new XMIResourceFactoryImpl());

        ResourceSet resourceSet = new ResourceSetImpl();

        Resource resource = resourceSet.getResource(URI.createFileURI("sample.gnosis"), true);
		try {
			resource.load(Collections.emptyMap());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
		Model root = (Model) resource.getContents().get(0);
		logger.info(root.getLogicalapplicationarchitecture().getApplicationdomain().get(0).getName());
	}

}
