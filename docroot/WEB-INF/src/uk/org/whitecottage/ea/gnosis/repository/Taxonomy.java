package uk.org.whitecottage.ea.gnosis.repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Term;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTree;
import uk.org.whitecottage.ea.gnosis.json.jstree.JSTreeNode;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class Taxonomy extends XmldbProcessor {
	protected Unmarshaller taxonomyUnmarshaller = null;
	protected Marshaller taxonomyMarshaller = null;

	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(Taxonomy.class);

	public Taxonomy(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext taxonomyContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.taxonomy");
		    taxonomyUnmarshaller = createUnmarshaller(taxonomyContext, context + "/WEB-INF/xsd/taxonomy.xsd");
		    taxonomyMarshaller = taxonomyContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getJSON() {
		String result = null;

		Collection repository = null;
		XMLResource taxonomyResource = null;
		try {   
			repository = getCollection("");
		    taxonomyResource = getResource(repository, "taxonomies.xml");
		    uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Taxonomies taxonomies = (uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Taxonomies) taxonomyUnmarshaller.unmarshal(taxonomyResource.getContentAsDOM());
			
			JSTree tree = new JSTree();
			
			for (uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Taxonomy taxonomy: taxonomies.getTaxonomy()) {
				JSONMap data = new JSONMap("data");
				
				JSONString idJSON = new JSONString("id", taxonomy.getTaxonomyId());
				data.put(idJSON);
						
				JSONString descriptionJSON = new JSONString("description", taxonomy.getDescription());
				data.put(descriptionJSON);

				JSTreeNode taxonomyJSON = new JSTreeNode(taxonomy.getName(), "taxonomy", data);
					
				for (Term term: taxonomy.getTerm()) {
					taxonomyJSON.getChildren().add(getTermJSON(term));
				}
				
				tree.add(taxonomyJSON);
			}
			
			result = tree.toJSON();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(taxonomyResource != null) {
		        try { ((EXistResource) taxonomyResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}

	protected JSTreeNode getTermJSON(Term term) {
		JSONMap data = new JSONMap("data");
		
		JSONString idJSON = new JSONString("id", term.getTermId());
		data.put(idJSON);
				
		JSONString descriptionJSON = new JSONString("description", term.getDescription());
		data.put(descriptionJSON);

		JSTreeNode termJSON = new JSTreeNode(term.getName(), "term", data);
			
		for (Term childTerm: term.getTerm()) {
			termJSON.getChildren().add(getTermJSON(childTerm));
		}
		
		return termJSON;
	}
}
