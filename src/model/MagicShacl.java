package model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.vocabulary.SHACL;
import org.apache.jena.vocabulary.RDF;

public class MagicShacl {
	private Model model;
	private List<String> targets;
	private Model magicModel;
	private List<ShapeConstraint> magicShapeConstraints;
	
	public MagicShacl(String fileOrURL, List<ShapeName> targets) {
		model = RDFDataMgr.loadModel(fileOrURL);
		ShapesGraph shapesGraph = new ShapesGraph(fileOrURL);
		
		magicShapeConstraints = shapesGraph.getMagicShapeConstraints(targets);
		System.out.println(shapesGraph.toString());
		
		magicModel = ModelFactory.createDefaultModel();
		Resource r = model.createResource("TestShape");		
		model.add(r, RDF.type, "testShape");
		
		Property p = model.getProperty(SHACL.NS+"property");
		model.add(r, p, "shaclpropo");
		// create the resource
		//Resource r = model.createResource("xxx");
		

		// add the property
		/*r.addProperty(RDFS.label, model.createLiteral("chat", "en"))
		 .addProperty(RDFS.label, model.createLiteral("chat", "fr"))
		 .addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true));*/

		Statement stmt = model.createLiteralStatement(r, p, "obj");
		model.add(stmt);		
	}
	
	public String toString(String format) {
		String s = "";
		switch(format.toUpperCase()) {
			case "ABSTRACT": 
				for (ShapeConstraint shapeConstraint : magicShapeConstraints)
					s += shapeConstraint.toString();
				break;
			default: 
				StringWriter out = new StringWriter();
				model.write(out, "TURTLE");
				s = out.toString();
		}
		
		return s;
	}
}
