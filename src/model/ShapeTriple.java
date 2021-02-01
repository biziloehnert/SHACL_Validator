package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.graph.Node;

public class ShapeTriple {
	private Node subject;
	private Map<Node,List<Node>> predicateObject;
	private boolean isNodeShape;
	private ShapeName shapeName;
	private List<ShapeExpression> shapeExpressions;
	
	public ShapeTriple(Node subject){
		this.subject = subject;
		predicateObject = new HashMap<>();
		
		isNodeShape = false;
	}
	
	public void addPredicateObject(Node predicate, Node object) {
		if(predicateObject.containsKey(predicate))
			predicateObject.get(predicate).add(object);
		else {
			List<Node> objects = new ArrayList<>();
			objects.add(object);
			predicateObject.put(predicate, objects);
		}
	}
	
	public Node getSubject() {
		return this.subject;
	}
	
	public List<Node> getObject(String suffix){
		for (Node predicate : predicateObject.keySet()) {
			if(predicate.toString().toLowerCase().endsWith(suffix.toLowerCase()))
				return predicateObject.get(predicate);
		}
		return null;
	}
	
	public boolean isNodeShape() {
		return this.isNodeShape;
	}
	
	public void setNodeShape(boolean isNodeShape) {
		this.isNodeShape = isNodeShape;
	}
	
	public void setShapeName(ShapeName shapeName) {
		this.shapeName = shapeName;
	}
	
	public ShapeName getShapeName() {
		return this.shapeName;
	}
	
	public List<ShapeExpression> getShapeExpressions(){
		return this.shapeExpressions;
	}
	
	public void addShapeExpression(ShapeExpression shapeExpression) {
		if(this.shapeExpressions == null)
			this.shapeExpressions = new ArrayList<>();
		this.shapeExpressions.add(shapeExpression);
	}
	
	public String toString() {
		String s = (isNodeShape ? "Shape: " : "");
		if(!subject.isBlank())
			s += subject.getLocalName() + "\n";
		else 
			s += subject + "\n";
		
		for (Node predicate : predicateObject.keySet()) {
			s += "[" + predicate + " " + predicateObject.get(predicate) + "]\n"; 
		}
		
		return s;
	}
}
