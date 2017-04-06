package uk.org.whitecottage.gnosis.backend.data;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.vaadin.data.Item;

public class ApplicationTaxonomyContainer extends TaxonomyContainer {

	private static final long serialVersionUID = 1L;

	public ApplicationTaxonomyContainer() {
		super();
	}

	public ApplicationTaxonomyContainer(FindIterable<Document> taxonomy) {
		super(taxonomy);
	}

	@Override
	protected void buildNode(Item item, Document document) {
		// Builds the container node from the Bson Document
		// Node id, name and description are handled by the super class
		// This method specialises the properties by adding statements like:
		// item.getItemProperty("PropertyName").setValue(document.get("bsonDocumentPropertyName"));

	}

	@Override
	protected void toBson(Item item, Document document) {
		// converts the container node back into a Bson Document
		// Node id, name and description are handled by the super class
		// This method specialises the properties by adding statements like:
		// document.append("bsonDocumentPropertyName", item.getItemProperty("PropertyName").getValue());
		
	}
}
