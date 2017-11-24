package application;

public class Vertex implements Comparable<Vertex> {
	int x, y;
	
	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (!(o instanceof Vertex)) return false;
		
		Vertex v = (Vertex) o;
		
		return (v.x == this.x) && (v.y == this.y);
	}
	
	@Override
	public int hashCode() {
	    int result = x;
	    result = 31 * result + y;
	    return result;
	}
	
	public boolean equals(Vertex v) {
		return (v.x == this.x) && (v.y == this.y);
	}

	@Override
	public int compareTo(Vertex v) {
		if ((v.x == this.x) && (v.y == this.y)) return 0;
		return 1;
	}
	
	public String toString() {
		return x + "," + y;
	}
}
