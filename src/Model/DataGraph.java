package Model;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class DataGraph {
	private Model model;
	private List<Triple> triples;
	
	public DataGraph(String pathname) {
		model = ModelFactory.createDefaultModel();
		model.read(pathname);
		
		triples = new ArrayList<>();
		
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stmt      = iter.nextStatement();  // get next statement
			Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object
			
		    triples.add(new Triple(subject, predicate, object));
		}
	}
	
	public String toString() {
		String dataGraph = "";
		
		for (Triple triple : triples)
			dataGraph += triple.toString() + "\n";
		
		return dataGraph;
	}
}
