package application;

import java.util.PriorityQueue;

@SuppressWarnings("serial")
public class Fringe extends PriorityQueue<Vertex> {
	
	Vertex pop() {
		Vertex minVertex = this.peek();
		remove(minVertex);
		return minVertex;
	}
	
	float minKey() {
		return peek().f;
	}
	
}