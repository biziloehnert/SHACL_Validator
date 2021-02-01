package validator;

import java.io.StringWriter;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.ValidationReport;

import model.DataGraph;
import model.Options;
import model.ShapesGraph;

public class Validator {
	private DataGraph dataGraph; 
	private ShapesGraph shapesGraph;
	private ValidationReport report;
	private List<Options> options;
	
	public Validator(String dataPath, String shapesPath, List<Options> options) {
		dataGraph = new DataGraph(dataPath);
		shapesGraph = new ShapesGraph(shapesPath);
		this.options = options;
		
		//for (String s : shapesGraph.toAbstractSyntax())
			//System.out.println(s);
		
		/*List<String> facts = dataGraph.toASPFacts();
		
		for (String string : facts) {
			System.out.println(string);
		}*/		
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
