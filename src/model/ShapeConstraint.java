package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.shacl.parser.Shape;

public class ShapeConstraint {
	private ShapesGraph shapesGraph;
	private ShapeName shapeName;
	private ShapeExpression shapeExpression;
	
	public ShapeConstraint(ShapesGraph shapesGraph, ShapeName shapeName, ShapeExpression shapeExpression) {
		this.shapesGraph = shapesGraph;
		this.shapeName = shapeName;
		this.shapeExpression = shapeExpression;
	}
	
	public ShapeConstraint(Shape shape) {
		this.shapeName = new ShapeName(shape.getShapeNode().getLocalName(), true);
	}
	
	public ShapeConstraint(ShapeConstraint r) {
		this.shapesGraph = r.shapesGraph;
		this.shapeName = new ShapeName(r.shapeName.getShapeName());
		this.shapeExpression = new ShapeExpression(r.shapeExpression);
	}

	public ShapeName getShapeName() {
		return shapeName;
	}
	public void setShapeName(ShapeName shapeName) {
		this.shapeName = shapeName;
	}
	
	public ShapeExpression getShapeExpression() {
		return shapeExpression;
	}
	
	public boolean containsExpression(ShapeName shapeName) {
		return getShapeExpression().getShapes().contains(shapeName);
	}
	
	//add a new shape name to the expression, if the shape expression has not AND as its logic operator this method returns a new shape constraint (to preserve the normal form)
	public ShapeConstraint addToShapeExpression(ShapeName shapeName) {
		if(this.shapeExpression.contains(LogicOperator.AND))
			this.shapeExpression.addShapeName(shapeName);
		else {
			ShapeExpression shapeExpression = new ShapeExpression(LogicOperator.AND);
			shapeExpression.addShapeName(this.shapeName);
			shapeExpression.addShapeName(shapeName);
			return new ShapeConstraint(shapesGraph, shapesGraph.getNewShapeName(true), shapeExpression);
		}
		return null;
	}
	
	public void adorn() {
		shapeName.setAdorned(true);
		
		for (ShapeName shapeName : shapeExpression.getShapes()) {
			if(shapeName != null && shapeName.isIDB() && !shapeName.isAdorned()) {
				shapesGraph.addAdornedShape(shapeName);
				shapeName.setAdorned(true);
			}
		}
	}
	
	public List<ShapeConstraint> generate() {
		List<ShapeConstraint> magicShapes = new ArrayList<>();
		ShapeExpression magicExpression = new ShapeExpression(shapeName.getMagicShape());
		
		for (ShapeName shapeName : shapeExpression.getShapes()) {
			if(shapeName != null && shapeName.isAdorned())	
				magicShapes.add(new ShapeConstraint(shapesGraph, shapeName.getMagicShape(), magicExpression));
		}
		
		return magicShapes;
	}
	
	public List<ShapeConstraint> modify() {
		List<ShapeConstraint> modifed = new ArrayList<>();
		removeAdornments();
		
		//if the shape constraint has not AND as its logic operator it will return a new constraint to preserve the normal form 
		ShapeConstraint sc = addToShapeExpression(shapeName.getMagicShape());
		if (sc != null)
			modifed.add(sc);
		modifed.add(this);
		return modifed;
	}
	
	public void swap(ShapeName s) {
		shapeExpression.replaceShapeName(s, shapeName);
		shapeName = s;
	}
	
	public void removeAdornments() {
		this.shapeName.setAdorned(false);
		for (ShapeName shapeName : shapeExpression.getShapes()) {
			shapeName.setAdorned(false);
		}
	}
	
	public String toString() {
		return shapeName + " :- " + shapeExpression + "; \n";
	}
}
