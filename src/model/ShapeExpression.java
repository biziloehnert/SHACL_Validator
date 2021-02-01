package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.jena.graph.Node;
import org.apache.jena.shacl.engine.constraint.CardinalityConstraint;
import org.apache.jena.shacl.engine.constraint.ClassConstraint;
import org.apache.jena.shacl.engine.constraint.ConstraintOpN;
import org.apache.jena.shacl.engine.constraint.MaxCount;
import org.apache.jena.shacl.engine.constraint.ShAnd;
import org.apache.jena.shacl.engine.constraint.ShNot;
import org.apache.jena.shacl.engine.constraint.ShOr;
import org.apache.jena.shacl.parser.Constraint;
import org.apache.jena.shacl.parser.PropertyShape;
import org.apache.jena.shacl.parser.Shape;
import org.apache.jena.shacl.parser.ShapesParser;

public class ShapeExpression {
	private LogicOperator logicOperator;
	private Path path;
	private int n= 0;
	private List<ShapeName> shapeNames;
	
	public ShapeExpression(PropertyShape propertyShape) {
		this.shapeNames = new ArrayList<>();
		this.path = new Path(propertyShape.getPath());
		this.logicOperator = LogicOperator.SOME;
		
		for (Constraint c : propertyShape.getConstraints()) {
			parseConstraint(c);
		}	
	}
	
	public ShapeExpression(ShapeName shapeName) {
		this.shapeNames = new ArrayList<>();
		this.shapeNames.add(shapeName);
	}
	
	public ShapeExpression(LogicOperator logicOperator) {
		this.shapeNames = new ArrayList<>();
		this.logicOperator = logicOperator;
		
	}
	
	public ShapeExpression(ShapeExpression shapeExpression) {
		this.logicOperator = shapeExpression.logicOperator;
		this.path = shapeExpression.path;
		this.n= shapeExpression.n;
		this.shapeNames = new ArrayList<>(shapeExpression.shapeNames);
	}
	
	public List<ShapeName> getShapes() {
		return shapeNames.stream().filter(s -> s.isIDB()).collect(Collectors.toList());
	}
	
	public void parseConstraint(Constraint c) {
		if (c instanceof ClassConstraint) {
			this.logicOperator = LogicOperator.SOME;
			this.shapeNames.add(new ShapeName(c.toString(), false));
		} else if (c instanceof CardinalityConstraint) {
			this.logicOperator = (c instanceof MaxCount) ? LogicOperator.MAX : LogicOperator.MIN;
			this.n = Integer.decode(c.toString().replaceAll("\\D+",""));
		} else if (c instanceof ShNot) 
			this.logicOperator = LogicOperator.NOT;
		else if (c instanceof ShAnd) 
			this.logicOperator = LogicOperator.AND;
		else if (c instanceof ShOr) 
			this.logicOperator = LogicOperator.OR;
	}
	
	public boolean contains(LogicOperator logicOperator) {
		return this.logicOperator.equals(logicOperator);
	}
	
	public void addShapeName(ShapeName shapeName) {
		this.shapeNames.add(shapeName);
	}
	
	//replace shapeName1 by shapeName2
	public void replaceShapeName(ShapeName shapeName1, ShapeName shapeName2) {
		shapeNames.remove(shapeName1);
		shapeNames.add(shapeName2);
	}
	
	public String toString() {
		if (shapeNames.isEmpty())
			shapeNames.add(new ShapeName(LogicOperator.T.toString(), false));

		if(logicOperator == null)
			return shapeNames.get(0).toString();
		
		String s = "";
		switch(logicOperator) {
			case NOT: s += "NOT " + shapeNames.get(0); break;
			case SOME: case ONLY: s += path + " " + logicOperator + " " + shapeNames.get(0); break;
			case AND: case OR: 
				for (ShapeName shapeName : shapeNames)
					s += " " + logicOperator + " " + shapeName;
				s = s.replaceFirst(" "+logicOperator+" ", "");
				break;
			case MIN: case MAX: s += logicOperator + " " + n + " " + path + " " + shapeNames.get(0); break;
		default:
			break;
		}
		return s;
	}
}
