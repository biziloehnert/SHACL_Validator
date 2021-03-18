package Validator;

import java.util.ArrayList;


public class main {

	public static void main(String[] args) {
		if (args.length < 1) {
            System.out.println("A filename (datagraph) is expected as argument! ");
            return;
        } 
		if (args.length < 2) {
            System.out.println("A filename (shapegraph) is expected as argument! ");
            return;
        } 
		
		long startTime = System.currentTimeMillis();
		
		Validator validator = new Validator(args[0], args[1], new ArrayList<>());
		validator.validate();
		
		long endTime = System.currentTimeMillis();
		long sec = (endTime - startTime)/1000;
		long millis = (endTime - startTime)%1000;
		System.out.println(sec + "s " + millis + "ms");
		
		validator.toFile();
		
		
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