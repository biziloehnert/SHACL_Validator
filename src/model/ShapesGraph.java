package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.parser.Constraint;
import org.apache.jena.shacl.parser.NodeShape;
import org.apache.jena.shacl.parser.PropertyShape;
import org.apache.jena.shacl.parser.Shape;
import org.apache.jena.util.iterator.ExtendedIterator;

public class ShapesGraph {
	private Graph shapesGraph;
	Map<Node, Shape> shapeMap;
	private Shapes shapes;
	
	private Map<String, ShapeTriple> shapeTriples;
	
	private List<ShapeConstraint> shapeConstraints;
	private List<ShapeConstraint> magicShapeConstraints;
	
	private Stack<ShapeName> adornedShapes;
	
	public ShapesGraph(String pathname) {
		shapes = Shapes.parseAll(pathname);
		shapesGraph = shapes.getGraph();
		shapeMap = shapes.getShapeMap();
		shapeTriples = new HashMap<>();
		
		shapeConstraints = new ArrayList<>();
		magicShapeConstraints = new ArrayList<>();
		
		//parse shape graph
		readTriples();
		createExpressions();
		createConstraints();
	}
	
	public Graph getShapesGraph() {
		if(shapesGraph == null)
			shapesGraph = shapes.getGraph();
		
		return shapesGraph;
	}

	public ShapeName getNewShapeName(boolean magic) {
		if(magic)
			return new ShapeName("S" + (magicShapeConstraints.size()+1));
		return new ShapeName("S" + (shapeConstraints.size()+1));
	}
	
	public ShapeName getShapeName(ShapeExpression shapeExpression) {
		return shapeConstraints.get(shapeConstraints.indexOf(shapeExpression)).getShapeName();
	}

	public List<ShapeConstraint> getShapeConstraints(){
		return this.shapeConstraints;
	}
	
	public List<ShapeConstraint> getMagicShapeConstraints(List<ShapeName> targets) {
		adornedShapes = new Stack<>();
		magicShapeConstraints = buildQuerySeeds(targets);
		
		while(!adornedShapes.isEmpty()) {
			ShapeName s_a = adornedShapes.pop();
			for (ShapeConstraint r : getClone()) {
				if(r.getShapeName().equals(s_a)) {
					r.adorn();
					magicShapeConstraints.addAll(r.generate());
					magicShapeConstraints.addAll(r.modify());
				}
			}
			
			for (ShapeConstraint d : getDangerousShapeConstraints()) {
				if(d.containsExpression(s_a)) {
					d.swap(s_a);
					d.adorn(); 
					magicShapeConstraints.addAll(d.generate());
				}
			}
		}
		
		return magicShapeConstraints;
	}
	
	public List<ShapeConstraint> getClone(){
		List<ShapeConstraint> clone = new ArrayList<>();
		for (ShapeConstraint c : shapeConstraints)
			clone.add(new ShapeConstraint(c));
		return clone;
	}
	
	public List<ShapeName> getAllShapeNames(){
		List<ShapeName> shapeNames = new ArrayList<>();
		
		for (ShapeConstraint shapeConstraint : shapeConstraints) {
			shapeNames.add(shapeConstraint.getShapeName());
			shapeNames.addAll(shapeConstraint.getShapeExpression().getShapes());
		}
		
		return shapeNames;
	}
	
	public void readTriples() {
		
		ExtendedIterator<Triple> it = shapesGraph.find(Node.ANY, Node.ANY, Node.ANY);
		
		while(it.hasNext()){
			Triple t = it.next();
			
			ShapeTriple triple = shapeTriples.get(t.getSubject().toString());
			if(triple == null)
				triple = new ShapeTriple(t.getSubject());
			triple.addPredicateObject(t.getPredicate(), t.getObject());
			
			if(shapeMap.get(t.getSubject()) instanceof NodeShape) {
				triple.setNodeShape(true);
				if(triple.getSubject().isBlank())
					triple.setShapeName(getNewShapeName(false));
				else
					triple.setShapeName(new ShapeName(triple.getSubject().getLocalName().toString()));
			}
			
			shapeTriples.put(t.getSubject().toString(), triple);
		}
	}
	
	public void createExpressions() {
		for(ShapeTriple triple : shapeTriples.values().stream().filter(v -> v.isNodeShape()).collect(Collectors.toList())) {
			for (PropertyShape propertyShape : shapeMap.get(triple.getSubject()).getPropertyShapes())
				triple.addShapeExpression(new ShapeExpression(propertyShape));
			
			for (Constraint constraint : shapeMap.get(triple.getSubject()).getConstraints()) {
				List<Node> objects = triple.getObject(constraint.toString());
				if (objects != null) {
					ShapeExpression exp = new ShapeExpression(shapeTriples.get(objects.get(0).toString()).getShapeName());
					exp.parseConstraint(constraint);
					triple.addShapeExpression(exp);
				}
			}		
		}
	}
	
	public void createConstraints() {
		for(ShapeTriple triple : shapeTriples.values().stream().filter(v -> v.isNodeShape()).collect(Collectors.toList())) {
			if(triple.getShapeExpressions().size() == 1)
				shapeConstraints.add(new ShapeConstraint(this, triple.getShapeName(), triple.getShapeExpressions().get(0)));
			else {
				ShapeExpression exp = new ShapeExpression(LogicOperator.AND);
				for (ShapeExpression shapeExpression : triple.getShapeExpressions()) {
					ShapeName s = getNewShapeName(false);
					shapeConstraints.add(new ShapeConstraint(this, s, shapeExpression));
					exp.addShapeName(s);
				}
				shapeConstraints.add(new ShapeConstraint(this, triple.getShapeName(), exp));
			}
		}
	}
	
	public void addShapeConstraint(ShapeConstraint shapeConstraint) {
		this.magicShapeConstraints.add(shapeConstraint);
	}
	
	public List<ShapeConstraint> buildQuerySeeds(List<ShapeName> targets){
		List<ShapeConstraint> magicShapes = new ArrayList<>();
		for (ShapeName target : targets) {
			adornedShapes.push(target);
			
			ShapeName s = new ShapeName("T", false);
			if (target.hasTerm()) {
				s.setShapename(target.getTerm());
			}
			magicShapes.add(new ShapeConstraint(this, target.getMagicShape(), new ShapeExpression(s)));
		}
		
		return magicShapes;
	}
	
	public void addAdornedShape(ShapeName shapeName) {
		this.adornedShapes.add(shapeName);
	}
	
	public List<ShapeConstraint> getDangerousShapeConstraints(){
		List<ShapeConstraint> dangerousShapeConstraints = new ArrayList<>();
		List<ShapeName> dangerousShapeName = new ArrayList<>();
		ShapeDependencyGraph dg = new ShapeDependencyGraph(this);
		
		//add all shape names occuring in an odd cycle to the list 
		dangerousShapeName.addAll(getAllShapeNames().stream().filter(s -> dg.occursInOddCycle(s)).collect(Collectors.toList()));
		
		//add all shape names occuring in a shape constraint with a dangerous shape name to the list
		for (ShapeConstraint shapeConstraint : shapeConstraints.stream().filter(c -> dangerousShapeName.contains(c.getShapeName())).collect(Collectors.toList())) {
			dangerousShapeName.addAll(shapeConstraint.getShapeExpression().getShapes());
		}
		
		//add all dangerous shape constraints (--> having a dangerous shape name) to the list 
		dangerousShapeConstraints.addAll(shapeConstraints.stream().filter(c -> dangerousShapeName.contains(c.getShapeName())).collect(Collectors.toList()));
		
		return dangerousShapeConstraints;
	}
	
	public String toString() {
		String s = "";
		
		for (ShapeConstraint shapeConstraint : shapeConstraints) {
			s += shapeConstraint.toString();
		}
		
		return s;
	}
}
