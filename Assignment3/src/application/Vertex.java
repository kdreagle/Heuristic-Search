package application;

public class Vertex implements Comparable<Vertex> {
	short x, y;
	float f;
	
	public Vertex(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
	}
	
	public Vertex(int x, int y, float f) {
		this.x = (short) x;
		this.y = (short) y;
		this.f = f;
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
		if (this.f == v.f) return 0;
		if (this.f < v.f) return -1;
		else return 1;
	}
	
	public String toString() {
		return x + "," + y + "  =  " + f;
	}
}
