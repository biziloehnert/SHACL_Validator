package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.cycle.Cycles;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class ShapeDependencyGraph {
	private DefaultDirectedGraph<ShapeName, DefaultEdge> dependencyGraph;
	private ShapesGraph shapesGraph;
	
	public ShapeDependencyGraph(ShapesGraph shapesGraph) {
		this.shapesGraph = shapesGraph;
		dependencyGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
		
		for (ShapeName shapeName : shapesGraph.getAllShapeNames())
			dependencyGraph.addVertex(shapeName);
		
		for (ShapeConstraint shapeConstraint : shapesGraph.getShapeConstraints()) {
			for (ShapeName shapeName : shapeConstraint.getShapeExpression().getShapes()) {
				dependencyGraph.addEdge(shapeName, shapeConstraint.getShapeName());
			}
		}
	}
	
	public List<List<ShapeName>> getCycles() {
		DirectedSimpleCycles<ShapeName, DefaultEdge> simpleCycles = new JohnsonSimpleCycles<>(dependencyGraph);
		return simpleCycles.findSimpleCycles();
	}
	
	public List<List<ShapeName>> getOddCycles(){
		List<List<ShapeName>> oddCycles = new ArrayList<>();
		for (List<ShapeName> cycle : getCycles()) {
			int marked = 0;
			for (ShapeName shapeName : cycle) {
				for (ShapeConstraint shapeConstraint : shapesGraph.getShapeConstraints().stream()
						.filter(c -> c.getShapeExpression().contains(LogicOperator.NOT) || 
									c.getShapeExpression().contains(LogicOperator.ONLY))
						.collect(Collectors.toList())) {
					if ( shapeConstraint.getShapeExpression().getShapes().contains(shapeName))
						marked++;
				}
			}
			
			if (marked % 2 == 1)
				oddCycles.add(cycle);
		}
		
		return oddCycles;
	}
	
	public boolean occursInOddCycle(ShapeName shapeName) {
		List<List<ShapeName>> oddCycles = getOddCycles();
		for (List<ShapeName> oddCycle : oddCycles) {
			if(oddCycle.contains(shapeName))
				return true;
		}
			
		return false;
	}
}
