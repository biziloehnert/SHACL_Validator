package Model;

import org.apache.jena.graph.Graph;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.parser.Shape;

public class ShapesGraph {
	private Graph shapesGraph;
	private Shapes shapes;
	
	public ShapesGraph(String pathname) {
		shapes = Shapes.parseAll(pathname);
	}
	
	public Graph getShapesGraph() {
		if(shapesGraph == null)
			shapesGraph = shapes.getGraph();
		
		return shapesGraph;
	}
	
	public String toString() {
		String s = "";
		
		for (Shape shape : shapes)
			s += shape.toString() + "\n";
		
		return s;
	}
}
