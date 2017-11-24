package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ShortestPath {
	Vertex start, goal;
	char[][] grid;
	HashMap<Vertex,Double> g;
	HashMap<Vertex,Vertex> parent;
	boolean uniformCostSearch;
	float w;
	
	class BinaryHeap {
		public BinaryHeap() {
			
		}
	}
	
	@SuppressWarnings("serial")
	class Fringe extends PriorityQueue<Vertex> {
		public Map<Vertex,Double> map;
		
		Fringe () {
			super();
			map = new HashMap<Vertex,Double>();
		}
		
		void insert(Vertex v, double f) {
			map.put(v, f);
			add(v);
		}
		
		Vertex pop() {
			double min = Double.POSITIVE_INFINITY;
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
			
	}
	
	public ShortestPath(char[][] grid, Vertex start, Vertex goal, boolean uniformCostSearch, float w) {
		this.grid = grid;
		this.start = start;
		this.goal = goal;
		this.uniformCostSearch = uniformCostSearch;
		this.w = w;
		g = new HashMap<Vertex,Double>();
		parent = new HashMap<Vertex,Vertex>();
	}
	
	/**
	 * Project description implementation of A*
	 * @return
	 */
	public char[][] AStar() {
		
		Vertex currentVertex;
		
		ArrayList<Vertex> closed = new ArrayList<Vertex>();
		
		Fringe fringe = new Fringe();
		
		g.put(start, 0.0);
		
		if (uniformCostSearch) 
			fringe.insert(start, g(start));
		else 
			fringe.insert(start, f(start));
		

		parent.put(start, start);
		
		while (!fringe.isEmpty()) {
			currentVertex = fringe.pop();
			
			
			if (currentVertex.equals(goal))  {
				Main.averageNodesExpanded += closed.size();
				return path(currentVertex);
			}
			
			if (!closed.contains(currentVertex)) 
				closed.add(currentVertex);
			
			for (Vertex s : succ(currentVertex)) {
				if (!closed.contains(s)) { 
					if (!fringe.contains(s)){
						g.put(s, Double.POSITIVE_INFINITY);
						parent.put(s, null);
					}
					// update vertex
					if (g(currentVertex) + c(currentVertex,s) < g(s)) {
						g.put(s, g(currentVertex)+c(currentVertex,s));
						parent.put(s, currentVertex);
						if (fringe.contains(s)) 
							fringe.remove(s);
						if (uniformCostSearch)
							fringe.insert(s, g(s));
						else
							fringe.insert(s, f(s));
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Wikipedia implementation of A*
	 * @param comment
	 * @return
	 */
	public char[][] AStar(String comment) {
		
		
		
		ArrayList<Vertex> closed = new ArrayList<Vertex>();
		ArrayList<Vertex> open = new ArrayList<Vertex>();
		
		parent.put(start, start);
		
		g.put(start, 0.0);
		
		open.add(start);
		
		Vertex current;
		
		while (!open.isEmpty()) {
			current = open.get(0);
			for (Vertex v: open) {
				if (f(v) < f(current))
					current = v;
			}
			
			if (current.equals(goal)) return path(current);
			
			open.remove(current);
			closed.add(current);
			
			for (Vertex s : succ(current)) {
				if (closed.contains(s)) continue;
				
				if (!open.contains(s)) open.add(s);
				
				if (g(current) + c(current, s) >= g(s)) continue;
				
				parent.put(s, current);
				g.put(s, g(current) + c(current, s));
			}
			
				
		}
		return null;
	}
	
	
	char[][] path(Vertex current) {
		char[][] pathGrid = grid;
		while (parent.containsKey(current)) {
			Main.averagePathLength++;
			pathGrid[current.y][current.x] = 'c';
			current = parent.get(current);
			if (current.equals(start)) break;
		}
		return pathGrid;
	}
	
	ArrayList<Vertex> succ(Vertex v) {
		int x = v.x;
		int y = v.y;
		ArrayList<Vertex> succs = new ArrayList<Vertex>(8);
		if ((x + 1 >= 0 && x + 1 < 160) && (y >= 0 && y < 120) && grid[y][x + 1] != '0') succs.add(new Vertex(x + 1, y));
		if ((x >= 0 && x < 160) && (y + 1 >= 0 && y + 1 < 120) && grid[y + 1][x] != '0') succs.add(new Vertex(x, y + 1));
		if ((x + 1 >= 0 && x + 1 < 160) && (y + 1 >= 0 && y + 1 < 120) && grid[y + 1][x + 1] != '0') succs.add(new Vertex(x + 1, y + 1));
		if ((x - 1 >= 0 && x - 1 < 160) && (y >= 0 && y < 120) && grid[y][x - 1] != '0') succs.add(new Vertex(x - 1, y));
		if ((x >= 0 && x < 160) && (y - 1 >= 0 && y - 1 < 120) && grid[y - 1][x] != '0') succs.add(new Vertex(x , y - 1));
		if ((x - 1 >= 0 && x - 1 < 160) && (y - 1 >= 0 && y - 1 < 120) && grid[y - 1][x - 1] != '0') succs.add(new Vertex(x - 1, y - 1));
		if ((x + 1 >= 0 && x + 1 < 160) && (y - 1 >= 0 && y - 1 < 120) && grid[y - 1][x + 1] != '0') succs.add(new Vertex(x + 1, y - 1));
		if ((x - 1 >= 0 && x - 1 < 160) && (y + 1 >= 0 && y + 1 < 120) && grid[y + 1][x - 1] != '0') succs.add(new Vertex(x - 1, y + 1));
		
		return succs;
	}
	
	double c(Vertex v, Vertex s) {
		if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == '1') {
			return Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == '2') {
			return 2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == '2') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == '1') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == 'a') {
			return 0.25*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '1') {
			return Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'a') {
			return Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == 'b') {
			return 0.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == 'b') {
			return 0.375*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == 'a') {
			return 0.375*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'a') {
			return Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '1') {
			return Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'b') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == '1') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == 'a') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '2') {
			return 1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == 'b') {
			return 2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == '2') {
			return 2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		}
		System.out.println("Error computing cost");
		return 10;
	}
	
	/**
	 * Example heuristic given in project description
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	double h(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		int min = Integer.min(dx, dy);
		int max = Integer.max(dx, dy);
		return Math.sqrt(2) * min + max - min;
	}
	
	/**
	 * Euclidean distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	double h2(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Diagonal distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	double h3(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return dx + dy + (Math.sqrt(2) - 2) * Math.min(dx, dy);
	}
	
	/**
	 * Manhattan distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	double h4(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return dx + dy;
	}
	
	double g(Vertex v) {
		return g.get(v);
	}
	
	double f(Vertex v) {
		return g(v) + w*h(v);
	}
	

}
