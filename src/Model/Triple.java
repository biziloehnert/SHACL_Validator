package Model;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class Triple {
	private Resource subject;
	private Property predicate;
	private RDFNode object;
	
	public Triple(Resource subject, Property predicate, RDFNode object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String toString() {
		String triple = subject.toString() + " " + predicate.toString() + " ";
		
		if (object instanceof Resource)
			triple += object.toString();
		else
			// object is a literal
		    triple += " \"" + object.toString() + "\"";

		return triple + " .";
	}
}
