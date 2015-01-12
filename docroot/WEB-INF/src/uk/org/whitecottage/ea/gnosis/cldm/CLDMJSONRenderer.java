package uk.org.whitecottage.ea.gnosis.cldm;

import java.util.Collection;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;

import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;


public class CLDMJSONRenderer extends CLDMProcessor {

	public CLDMJSONRenderer(Model root) {
		super(root);
	}
		
	public String generateJSON() {
		Package ldm = getLDM();
		
		JSTree tree = new JSTree();

		JSONMap root = renderPackage(ldm, "");
		tree.add(root);
		
		return tree.toJSON();
	}
	
	protected JSTreeNode buildNode(String text, String type, String path) {
		JSONMap data = new JSONMap("data");
		
		JSONString pathJSON = new JSONString("path", path);
		data.put(pathJSON);
				
		JSTreeNode node = new JSTreeNode(text, type, data);
		
		return node;
	}
	
	protected JSONMap renderPackage(Package pkg, String path) {
		
		String newPath = path + ((path.length() == 0) ? "" : "/") + pkg.getName();
		
		JSTreeNode packageJSON = buildNode(pkg.getName(), "package", path);
		JSONArray children = packageJSON.getChildren();
		
		for (Object o: EcoreUtil.getObjectsByType(pkg.getPackagedElements(), UMLPackage.Literals.PACKAGE)) {
			children.add(renderPackage((Package) o, newPath));
		}
		
		renderClasses(children, pkg, profile.getOwnedStereotype("Entity"), newPath);

		renderClasses(children, pkg, profile.getOwnedStereotype("ReferenceEntity"), newPath);

		renderClasses(children, pkg, profile.getOwnedStereotype("ComplexEntity"), newPath);

		return packageJSON;
	}
	
	protected void renderClasses(JSONArray children, Package pkg, Stereotype s, String path) {
		Collection<Class> classes = filterClasses(s, EcoreUtil.getObjectsByType(pkg.getPackagedElements(),UMLPackage.Literals.CLASS));

		for (Class c: classes) {
			children.add(renderClass(c, s.getName(), path));
		}
	}
	
	protected JSONMap renderClass(Class c, String type, String path) {
		JSONMap classJSON = buildNode(c.getName(), type, path);
		
		return classJSON;
	}
}