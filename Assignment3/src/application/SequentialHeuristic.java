package application;

import java.util.ArrayList;
import java.util.HashMap;


public class SequentialHeuristic {
	char[][] grid;
	Vertex start, goal;
	float w1, w2;
	int n = 4;
	HashMap[] g;
	HashMap[] bp;
	Fringe[] fringe;
	ArrayList[] closed;
	
	float h = 0;
	
	public SequentialHeuristic(char[][] grid, Vertex start, Vertex goal,  float w1, float w2) {
		this.grid = grid;
		this.start = start;
		this.goal = goal;
		this.w1 = w1;
		this.w2 = w2;
		g = new HashMap[n];
		bp = new HashMap[n];
		fringe = new Fringe[n];
		closed = new ArrayList[n];
	}
	
	@SuppressWarnings("unchecked")
	public char[][] AStar() {
		int i;
		Vertex s;
		for (i = 0; i < n ; i++ ) {
			fringe[i] = new Fringe();
			closed[i] = new ArrayList<Vertex>();
			g[i] = new HashMap<Vertex,Float>();
			bp[i] = new HashMap<Vertex,Vertex>();
			
			g[i].put(start, 0f);
			g[i].put(goal, Float.POSITIVE_INFINITY);
			
			bp[i].put(start, null);
			bp[i].put(goal, null);
			
			fringe[i].insert(start, key(start,i));
		}
		
		while (fringe[0].minKey() < Float.POSITIVE_INFINITY) {
			for (i = 1; i < n; i++ ) {
				if (fringe[i].minKey() <= w2 * fringe[0].minKey()) {
					if ((float)g[i].get(goal) <= fringe[i].minKey()) {
						if ((float)g[i].get(goal) < Float.POSITIVE_INFINITY) return path();
					} else {
						s = fringe[i].top();
						expandStates(s,i);
						closed[i].add(s);
					}
				} else {
					if ((float)g[0].get(goal) <= fringe[0].minKey()) {
						if ((float)g[0].get(goal) < Float.POSITIVE_INFINITY) return path();
					} else {
						s = fringe[0].top();
						expandStates(s,0);
						closed[0].add(s);
					}
				}
			}
		}
		return null;
	}
	
	
	char[][] path() {
		char[][] pathGrid = grid;
		Vertex current = goal;
		while (current != start) {
			
			if (bp[0].get(current) != null) {
				current = (Vertex) bp[0].get(current);
				pathGrid[current.y][current.x] = 'c';
			}
			if (bp[1].get(current) != null) {
				current = (Vertex) bp[1].get(current);
				pathGrid[current.y][current.x] = 'c';
			}
			if (bp[2].get(current) != null) {
				current = (Vertex) bp[2].get(current);
				pathGrid[current.y][current.x] = 'c';
			}
			if (bp[3].get(current) != null) {
				current = (Vertex) bp[3].get(current);
				pathGrid[current.y][current.x] = 'c';
			}
		}

		return pathGrid;
	}
	
	float key(Vertex v, int i) {
		if (i == 0) return (float)g[i].get(v) + w1*h(v);
		if (i == 1) return (float)g[i].get(v) + w1*h2(v);
		if (i == 2) return (float)g[i].get(v) + w1*h3(v);
		if (i == 3) return (float)g[i].get(v) + w1*h4(v);
		return 0;
	}
	
	void expandStates(Vertex v, int i) {
		fringe[i].remove(v);
		for (Vertex s: succ(v)) {
			if (!g[i].containsKey(s)) {
				g[i].put(s, Float.POSITIVE_INFINITY);
				bp[i].put(s,null);
			}
			if ((float)g[i].get(s) > (float)g[i].get(v) + c(v,s)) {
				g[i].put(s, (float)g[i].get(v) + c(v,s));
				bp[i].put(s, v);
				if (!closed[i].contains(s))
					fringe[i].insert(s, key(s,i));
			}
		}
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
	
	float h(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		int min = Integer.min(dx, dy);
		int max = Integer.max(dx, dy);
		return (float) (Math.sqrt(2) * min + max - min);
	}
	
	/**
	 * Euclidean distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h2(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * Diagonal distance heuristic
	 * @param v Current vertex
	 * @return Distance from v to goal vertex
	 */
	float h3(Vertex v) {
		int dx = Math.abs(v.x - goal.x);
		int dy = Math.abs(v.y - goal.y);
		return (float) (dx + dy + (Math.sqrt(2) - 2) * Math.min(dx, dy));
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
	
	float h5(Vertex v) {
		int dx1 = v.x - goal.x;
		int dy1 = v.y - goal.y;
		int dx2 = start.x - goal.x;
		int dy2 = start.y - goal.y;
		int cross = Math.abs(dx1*dy2 - dx2*dy1);
		h += cross*0.001;
		return h;
	}
}
