package uk.org.whitecottage.gnosis.uml.tools;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

public class Parse {
    private static Logger logger = Logger.getLogger(Parse.class.getCanonicalName());
	
	public static void main(String[] args) {
		UMLPackage.eINSTANCE.eClass();
		
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, new XMIResourceFactoryImpl());

        ResourceSet resourceSet = new ResourceSetImpl();

        Resource resource = resourceSet.getResource(URI.createFileURI("model.uml"), true);
		try {
			resource.load(Collections.emptyMap());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
        Model root = (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);

		Profile profile = root.getAppliedProfile("DataModelling", true);
		logger.info(root.getName());
		logger.info(profile.getName());
	}

}
