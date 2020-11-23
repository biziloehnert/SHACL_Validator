package Validator;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.jena.graph.GetTriple;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.shacl.sys.ValidationGraph;
import org.apache.jena.shacl.validation.ReportEntry;

import Model.DataGraph;
import Model.Options;
import Model.ShapesGraph;

public class main {

	public static void main(String[] args) {
		if (args.length < 1) {
            System.out.println("A filename is expected as argument! ");
            return;
        }
		
		Validator validator = new Validator(args[0], args[0], new ArrayList<>());
		validator.validate();
		
		
		/*DataGraph dataGraph = new DataGraph(args[0]);
		System.out.println(dataGraph.toString());
		
		ShapesGraph shapesGraph = new ShapesGraph(args[0]);
	    System.out.println(shapesGraph.toString());
		
	    
	    ValidationReport report = ShaclValidator.get().validate(shapesGraph.getShapesGraph(), dataGraph.getDataGraph()); 
	    ShLib.printReport(report); 
	    
	    ValidationReport report1 = ValidationReport.fromGraph(dataGraph.getDataGraph());
	    Collection<ReportEntry> entry1 = report1.getEntries();
	    
	    
	    System.out.println();
	    RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);*/
		 
	}
}