package Validator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
	private String reportPath;
	private DataGraph dataGraph; 
	private ShapesGraph shapesGraph;
	private ValidationReport report;
	private List<Options> options;
	
	
	public Validator(String dataPath, String shapesPath, List<Options> options) {
		this.reportPath = shapesPath.replace(".ttl", "_report.ttl"); 
		dataGraph = new DataGraph(dataPath);
		shapesGraph = new ShapesGraph(shapesPath);
		this.options = options;
	}
	
	public ValidationReport validate() {
		report = ShaclValidator.get().validate(shapesGraph.getShapesGraph(), dataGraph.getDataGraph());
		return report; 
	}
	
	public void toFile() {
		try {
			OutputStream out = new FileOutputStream(reportPath);
			RDFDataMgr.write(out, report.getModel(), Lang.TTL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		StringWriter stringWriter = new StringWriter((int)report.getModel().size()+1);
		RDFDataMgr.write(stringWriter, report.getModel(), Lang.TTL);
		
		return stringWriter.toString();
	}
}
