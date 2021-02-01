package model;

public class Path {
	private org.apache.jena.sparql.path.Path path; 
	private boolean inverse;
	
	public Path(org.apache.jena.sparql.path.Path path) {
		this.path = path;
		this.inverse = path.toString().contains("^");
	}
	
	public org.apache.jena.sparql.path.Path getPath() {
		return path;
	}
	
	public String toString() {
		String[] pathName = path.toString().replace(">", "").split("#");
		return (inverse ? "^" : "") + pathName[pathName.length-1];
	}
}
