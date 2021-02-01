package validator;

import java.util.ArrayList;
import java.util.List;

import model.MagicShacl;
import model.ShapeName;

public class main {

	public static void main(String[] args) {
		if (args.length < 1) {
            System.out.println("A filename is expected as argument! ");
            return;
        }
		 
		List<ShapeName> targets = new ArrayList<>();
		ShapeName t = new ShapeName("NotExampleShape");
		targets.add(t);
		
		MagicShacl magic = new MagicShacl(args[0],targets);
		System.out.println(magic.toString("abstract"));
		
		//Validator validator = new Validator(args[0], args[0], new ArrayList<>());
		//validator.validate();
		
		
		
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