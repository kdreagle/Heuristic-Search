package application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Fringe extends PriorityQueue<Vertex> {
	public Map<Vertex,Float> map;
	
	Fringe () {
		super();
		map = new HashMap<Vertex,Float>();
	}
	
	void insert(Vertex v, float f) {
		map.put(v, f);
		add(v);
	}
	
	Vertex pop() {
		float min = Float.POSITIVE_INFINITY;
		Vertex minVertex = null;
		for (Vertex v : map.keySet()) {
			if (map.get(v) <= min)  {
				min = map.get(v);
				minVertex = v;
			}
		}
		remove(minVertex);
		return minVertex;
	}
	
	void remove(Vertex v) {
		map.remove(v);
		super.remove(v);
	}
	
	float minKey() {
		return Collections.min(map.values());
	}
	
	// pop without the remove
	Vertex top() {
		float min = Float.POSITIVE_INFINITY;
		Vertex minVertex = null;
		for (Vertex v : map.keySet()) {
			if (map.get(v) <= min)  {
				min = map.get(v);
				minVertex = v;
			}
		}
		return minVertex;
	}
		
}