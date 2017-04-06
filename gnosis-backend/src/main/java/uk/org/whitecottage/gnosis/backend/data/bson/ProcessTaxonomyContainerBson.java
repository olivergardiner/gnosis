package uk.org.whitecottage.gnosis.backend.data.bson;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class ProcessTaxonomyContainerBson extends TaxonomyContainerBson {

	public ProcessTaxonomyContainerBson(FindIterable<Document> taxonomy) {
		super(taxonomy);
	}

	private static final long serialVersionUID = 1L;
}
