package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ShortestPath {
	Vertex start, goal;
	char[][] grid;
	char[][] pathGrid;
	HashMap<Vertex,Float> g;
	HashMap<Vertex,Vertex> parent;
	Stack<Vertex> closed;
	boolean uniformCostSearch;
	float w;
	
	static final float root2 = (float) Math.sqrt(2);
	
	public ShortestPath(char[][] grid, Vertex start, Vertex goal, boolean uniformCostSearch, float w) {
		this.grid = grid;
		this.start = start;
		this.goal = goal;
		this.uniformCostSearch = uniformCostSearch;
		this.w = w;
		g = new HashMap<Vertex,Float>();
		parent = new HashMap<Vertex,Vertex>();
		closed = new Stack<Vertex>();
	}
	
	/**
	 * Project description implementation of A*
	 * @return
	 */
	public char[][] AStar() {
		
		Vertex currentVertex;
		
		Fringe fringe = new Fringe();
		
		g.put(start, 0.0f);
		
		if (uniformCostSearch) 
			start.f = g(start);
		else
			start.f = f(start);
		
		fringe.add(start);
		
		parent.put(start, start);
		
		while (!fringe.isEmpty()) {
			currentVertex = fringe.pop();
			
			if (currentVertex.equals(goal))  {
				Main.averageNodesExpanded += closed.size();
				return path(currentVertex);
			}
			
			if (!closed.contains(currentVertex)) 
				closed.push(currentVertex);
			
			for (Vertex s : succ(currentVertex)) {
				if (!closed.contains(s)) { 
					if (!fringe.contains(s)){
						g.put(s, Float.POSITIVE_INFINITY);
						parent.put(s, null);
					}
					// update vertex
					if (g(currentVertex) + c(currentVertex,s) < g(s)) {
						g.put(s, g(currentVertex)+c(currentVertex,s));
						parent.put(s, currentVertex);
						if (fringe.contains(s)) 
							fringe.remove(s);
						if (uniformCostSearch)
							s.f = g(s);
						else
							s.f = f(s);
						fringe.add(s);
					}
				}
			}
		}
		
		return null;
	}
	
	char[][] path(Vertex current) {
		pathGrid = grid;
		int pathLength = 0;
		while (parent.containsKey(current)) {
			pathLength++;
			pathGrid[current.y][current.x] = 'c';
			current = parent.get(current);
			if (current.equals(start)) break;
		}
		
		Main.averagePathLength += pathLength*pathLength/(h3(start));

		return pathGrid;
	}
	
	ArrayList<Vertex> succ(Vertex v) {
		short x = v.x;
		short y = v.y;
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
	
	float c(Vertex v, Vertex s) {
		if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == '1') {
			return (float) Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == '2') {
			return (float) (2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == '2') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == '1') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == 'a') {
			return (float) (0.25*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '1') {
			return (float) Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'a') {
			return (float) Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == 'b') {
			return (float) (0.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == 'b') {
			return (float) (0.375*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == 'a') {
			return (float) (0.375*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'a') {
			return (float) Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '1') {
			return (float) Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2));
		} else if (grid[v.y][v.x] == '1' && grid[s.y][s.x] == 'b') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == '1') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == 'a') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'a' && grid[s.y][s.x] == '2') {
			return (float) (1.5*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == '2' && grid[s.y][s.x] == 'b') {
			return (float) (2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		} else if (grid[v.y][v.x] == 'b' && grid[s.y][s.x] == '2') {
			return  (float) (2*Math.sqrt(Math.pow(v.x - s.x, 2) + Math.pow(v.y - s.y, 2)));
		}
		System.out.println("Error computing cost");
		return 10;
	}
	

	

	

	
	/**
	 * Diagonal distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h1(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) (dx + dy + (root2 - 2) * Math.min(dx, dy))*0.25f;
	}
	
	/**
	 * Diagonal distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h2(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) (dx + dy + (root2 - 2) * Math.min(dx, dy));
	}
	
	/**
	 * Euclidean distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h3(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Manhattan distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h4(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return dx + dy;
	}
	
	/**
	 * Squared Euclidean distance heuristic (seems like the best one)
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h5(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return dx*dx + dy*dy;
	}
	
	/**
	 * Euclidean distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h6(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) Math.sqrt(dx*dx + dy*dy) * 0.25f;
	}
	
	/**
	 * Example heuristic given in project description
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h7(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		int min = Integer.min(dx, dy);
		int max = Integer.max(dx, dy);
		return (float) (Math.sqrt(2) * min + max - min);
	}

	float g(Vertex v) {
		return g.get(v);
	}
	
	float f(Vertex v) {
		return g(v) + w*h3(v);
	}
	

}
