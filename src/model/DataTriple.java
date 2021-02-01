package model;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class DataTriple {
	private Resource subject;
	private Property predicate;
	private RDFNode object;
	
	public DataTriple(Resource subject, Property predicate, RDFNode object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String toFact() {
		if(subject.getLocalName() == null || predicate.getLocalName() == null) 
			return null;
		
		if((object instanceof Resource) && (object.asResource().getLocalName() != null))
			return (predicate.getLocalName() + "(" + subject.getLocalName() + "," + object.asResource().getLocalName() + ").").toLowerCase();
		
		if ((object instanceof Literal) && (subject.getLocalName() != null))
			return (predicate.getLocalName() + "(" + subject.getLocalName() + "," + object.asLiteral().getValue() + ").").toLowerCase() ;
		
		return null;
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
