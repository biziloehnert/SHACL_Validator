package Validator;

import java.io.StringWriter;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.ValidationReport;
import Model.DataGraph;
import Model.Options;
import Model.ShapesGraph;

public class Validator {
	private DataGraph dataGraph; 
	private ShapesGraph shapesGraph;
	private ValidationReport report;
	private List<Options> options;
	
	public Validator(String dataPath, String shapesPath, List<Options> options) {
		dataGraph = new DataGraph(dataPath);
		shapesGraph = new ShapesGraph(shapesPath);
		this.options = options;
		
		List<String> facts = dataGraph.toASPFacts();
	}
	
	public ValidationReport validate() {
		if(!options.contains(Options.dlv))
			report = ShaclValidator.get().validate(shapesGraph.getShapesGraph(), dataGraph.getDataGraph());
		
		return report; 
	}
	
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		RDFDataMgr.write(stringWriter, report.getModel(), Lang.TTL);
		return stringWriter.toString();
	}
}
