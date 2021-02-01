package model;

public class ShapeName {
	private String shapeName;
	//shape atom s(b) --> term b 
	private String term;
	private boolean isAdorned;
	private boolean isIDB;
	
	public ShapeName(String shapeName) {
		this.shapeName = shapeName;
		this.term = null;
		this.isIDB = true;
	}
	
	public ShapeName(String shapeName, boolean isIDB) {
		this.shapeName = shapeName;
		this.term = null;
		this.isIDB = isIDB;
	}
	
	public ShapeName(String shapeName, String term) {
		this.shapeName = shapeName;
		this.term = term;
		this.isIDB = true;
	}

	public String getShapeName() {
		return this.shapeName;
	}
	
	public ShapeName getMagicShape() {
		return new ShapeName("magic_" + this.shapeName);
	}
	
	public void setShapename(String shapeName) {
		this.shapeName = shapeName;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	public void setAdorned(boolean isAdorned) {
		this.isAdorned = isAdorned;
	}
	
	public void setIDB(boolean isIDB) {
		this.isIDB = isIDB;
	}
	
	public boolean isAdorned() {
		return isAdorned;
	}
	
	public boolean isIDB() {
		return isIDB;
	}
	
	public boolean hasTerm() {
		return term != null;
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public boolean equals(ShapeName shapeName) {
		return this.shapeName.equals((shapeName.getShapeName()));
	}
	
	public String toString() {
		if(shapeName.contains("#")) {
			String[] s = shapeName.split("#");	
			return s[s.length-1].replace(">]", "");
		}
		return shapeName;
	}
}