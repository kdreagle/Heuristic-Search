package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewController {
	
	@FXML
	AnchorPane gridWorld;
	
	@FXML
	AnchorPane root;
	
	@FXML
	TextField w;
	
	@FXML
	TextField w1;
	
	@FXML
	TextField w2;
	
	public static short startX, startY, goalX, goalY;
	
	static int[] hardX;

	static int[] hardY;
	
	static char[][] grid;
	
	String[] start, goal, hard;
	
	static final boolean UNIFORM_COST_SEARCH = true;
	
	static final boolean A_STAR_SEARCH = false;
	
	@FXML
	public void initialize() throws IOException {
		
		readGrid();
		
		Rectangle border = new Rectangle(195,15,1290,970);
		border.setFill(Color.BLACK);
		border.toBack();
		root.getChildren().add(border);
	    
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*8,y*8,8,8);
				if (x == startX && y == startY) {
					cell.setFill(Color.PINK);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.GREEN);
				} else if (grid[y][x] == '0') {
					cell.setFill(Color.BLACK);
				} else if (grid[y][x] == '1') {
					cell.setFill(Color.WHITE);
				} else if (grid[y][x] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (grid[y][x] == 'a') {
					cell.setFill(Color.RED);
				} else if (grid[y][x] == 'b') {
					cell.setFill(Color.ORANGE);
				} else if (grid[y][x] == 'c') {
					cell.setFill(Color.BLUE);
					//t = new Tooltip("x: " + x + "\ny: " + y + "\nf: " + path.f(new Vertex(x,y)) + "\ng: " + path.g(new Vertex(x,y)) + "\nh: " + path.h5(new Vertex(x,y)));
					//Tooltip.install(cell, t);
				}
				gridWorld.getChildren().add(cell);
			}
		}
		
		gridWorld.toFront();
		
	}
	
	@FXML
	private void aStar(ActionEvent event) throws IOException {
		int weight = Integer.parseInt(w.getText());
		
		readGrid();
		
		ShortestPath path = new ShortestPath(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), A_STAR_SEARCH, weight);
		
		double startTime = System.currentTimeMillis();
		char[][] pathGrid = path.AStar();
		double endTime = System.currentTimeMillis();
		
		System.out.println("Run time: " + (endTime - startTime) + " milliseconds");
		
		gridWorld.getChildren().clear();
		
		Tooltip t;
		
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*8,y*8,8,8);
				if (x == startX && y == startY) {
					cell.setFill(Color.PINK);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.GREEN);
				} else if (pathGrid[y][x] == '0') {
					cell.setFill(Color.BLACK);
				} else if (pathGrid[y][x] == '1') {
					cell.setFill(Color.WHITE);
				} else if (pathGrid[y][x] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (pathGrid[y][x] == 'a') {
					cell.setFill(Color.RED);
				} else if (pathGrid[y][x] == 'b') {
					cell.setFill(Color.ORANGE);
				} else if (pathGrid[y][x] == 'c') {
					cell.setFill(Color.BLUE);
					
					t = new Tooltip("x: " + x + "\ny: " + y + "\nf: " + path.f(new Vertex(x,y)) + "\ng: " + path.g(new Vertex(x,y)) + "\nh: " + path.h5(new Vertex(x,y)));
					Tooltip.install(cell, t);
				}
				gridWorld.getChildren().add(cell);
			}
		}
	}
	
	@FXML
	private void uniformCost(ActionEvent event) throws IOException {
		readGrid();
		
		ShortestPath path = new ShortestPath(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), UNIFORM_COST_SEARCH , 1);
		
		char[][] pathGrid = path.AStar();
		
		gridWorld.getChildren().clear();
		
		Tooltip t;
		
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*8,y*8,8,8);
				if (x == startX && y == startY) {
					cell.setFill(Color.PINK);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.GREEN);
				} else if (pathGrid[y][x] == '0') {
					cell.setFill(Color.BLACK);
				} else if (pathGrid[y][x] == '1') {
					cell.setFill(Color.WHITE);
				} else if (pathGrid[y][x] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (pathGrid[y][x] == 'a') {
					cell.setFill(Color.RED);
				} else if (pathGrid[y][x] == 'b') {
					cell.setFill(Color.ORANGE);
				} else if (pathGrid[y][x] == 'c') {
					cell.setFill(Color.BLUE);
					t = new Tooltip("x: " + x + "\ny: " + y + "\ng: " + path.g(new Vertex(x,y)) );
					Tooltip.install(cell, t);
				}
				gridWorld.getChildren().add(cell);
			}
		}
	}
	
	@FXML
	private void sequentialHeuristic(ActionEvent event) throws IOException {
		readGrid();
		
		int weight1 = Integer.parseInt(w1.getText());
		int weight2 = Integer.parseInt(w2.getText());
		
		SequentialHeuristic path = new SequentialHeuristic(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), weight1 , weight2);
		
		char[][] pathGrid = path.AStar();
		
		gridWorld.getChildren().clear();
		
		Tooltip t;
		
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*8,y*8,8,8);
				if (x == startX && y == startY) {
					cell.setFill(Color.PINK);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.GREEN);
				} else if (pathGrid[y][x] == '0') {
					cell.setFill(Color.BLACK);
				} else if (pathGrid[y][x] == '1') {
					cell.setFill(Color.WHITE);
				} else if (pathGrid[y][x] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (pathGrid[y][x] == 'a') {
					cell.setFill(Color.RED);
				} else if (pathGrid[y][x] == 'b') {
					cell.setFill(Color.ORANGE);
				} else if (pathGrid[y][x] == 'c') {
					cell.setFill(Color.BLUE);
					t = new Tooltip("x: " + x + "\ny: " + y);
					Tooltip.install(cell, t);
				}
				gridWorld.getChildren().add(cell);
			}
		}
	}
	
	@FXML
	private void newGrid(ActionEvent event) throws IOException {
		Main.generateFile();
		
		readGrid();
		
		gridWorld.getChildren().clear();
	    
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*8,y*8,8,8);
				if (x == startX && y == startY) {
					cell.setFill(Color.PINK);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.GREEN);
				} else if (grid[y][x] == '0') {
					cell.setFill(Color.BLACK);
				} else if (grid[y][x] == '1') {
					cell.setFill(Color.WHITE);
				} else if (grid[y][x] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (grid[y][x] == 'a') {
					cell.setFill(Color.RED);
				} else if (grid[y][x] == 'b') {
					cell.setFill(Color.ORANGE);
				} else if (grid[y][x] == 'c') {
					cell.setFill(Color.BLUE);
				}
				gridWorld.getChildren().add(cell);
			}
		}
	}
	
	@FXML
	private void ABenchmark(ActionEvent event) throws IOException {
		ShortestPath path;
		
		double averageDuration = 0;
		
		int weight = Integer.parseInt(w.getText());
		
		double averageMemoryUsage = 0;
		
		BufferedReader br = new BufferedReader(new FileReader("grids.txt"));
		
		try {
			
			String[] start, goal, hard;
			
			long startTime, endTime;
			
			for (int x = 0 ; x < 50 ; x++) {
				
			    start = br.readLine().split(" ");
			    
			    startX = Short.parseShort(start[0]);
			    startY = Short.parseShort(start[1]);
			    
			    goal = br.readLine().split(" ");
			    
			    goalX = Short.parseShort(goal[0]);
			    goalY = Short.parseShort(goal[1]);
			    
			    hardX = new int[8];
			    hardY = new int[8];
			    
			    for (int i = 0; i < 8; i++) {
			    	hard = br.readLine().split(" ");
			  
			    	hardX[i] = Short.parseShort(hard[0]);
					hardY[i] = Short.parseShort(hard[1]);
			    }
			    
			    grid = new char[120][160];
			
			    for (int i = 0; i < 120; i++) grid[i] = br.readLine().toCharArray();
			    
			    path = new ShortestPath(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), A_STAR_SEARCH, weight);
			    
			    
				startTime = System.currentTimeMillis();
				grid = path.AStar();
				endTime = System.currentTimeMillis();
				
				averageMemoryUsage += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
				
				averageDuration += endTime - startTime;
			}


		} finally {
		    br.close();
		}
		
		System.out.println("Average duration: " + averageDuration/50 + " milliseconds");
		System.out.println("Average path length: " + Main.averagePathLength/50);
		System.out.println("Average nodes expanded: " + Main.averageNodesExpanded/50);
		System.out.println("Average memory usage: " + averageMemoryUsage/(50 * 1000000) + " megabytes");
		
		Main.averageNodesExpanded = 0;
		Main.averagePathLength = 0;
	}
	
	@FXML
	private void SHBenchmark(ActionEvent event) throws IOException {
		SequentialHeuristic path;
		
		double averageDuration = 0;
		
		int weight1 = Integer.parseInt(w1.getText());
		int weight2 = Integer.parseInt(w2.getText());
		
		double averageMemoryUsage = 0;
		
		BufferedReader br = new BufferedReader(new FileReader("grids.txt"));
		
		try {
			
			String[] start, goal, hard;
			
			long startTime, endTime;
			
			for (int x = 0 ; x < 50 ; x++) {
				
			    start = br.readLine().split(" ");
			    
			    startX = Short.parseShort(start[0]);
			    startY = Short.parseShort(start[1]);
			    
			    goal = br.readLine().split(" ");
			    
			    goalX = Short.parseShort(goal[0]);
			    goalY = Short.parseShort(goal[1]);
			    
			    hardX = new int[8];
			    hardY = new int[8];
			    
			    for (int i = 0; i < 8; i++) {
			    	hard = br.readLine().split(" ");
			  
			    	hardX[i] = Short.parseShort(hard[0]);
					hardY[i] = Short.parseShort(hard[1]);
			    }
			    
			    grid = new char[120][160];
			
			    for (int i = 0; i < 120; i++) grid[i] = br.readLine().toCharArray();
			    
			    path = new SequentialHeuristic(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), weight1, weight2);
			    
			    
				startTime = System.currentTimeMillis();
				grid = path.AStar();
				endTime = System.currentTimeMillis();
				
				averageMemoryUsage += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
				
				averageDuration += endTime - startTime;
			}


		} finally {
		    br.close();
		}
		
		System.out.println("Average duration: " + averageDuration/50 + " milliseconds");
		System.out.println("Average path length: " + Main.averagePathLength/50);
		System.out.println("Average nodes expanded: " + Main.averageNodesExpanded/50);
		System.out.println("Average memory usage: " + averageMemoryUsage/(50 * 1000000) + " megabytes");
		
		Main.averageNodesExpanded = 0;
		Main.averagePathLength = 0;
	}
	
	
	private void readGrid() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("grids.txt"));
		
		start = br.readLine().split(" ");
	    
	    startX = Short.parseShort(start[0]);
	    startY = Short.parseShort(start[1]);
	    
	    goal = br.readLine().split(" ");
	    
	    goalX = Short.parseShort(goal[0]);
	    goalY = Short.parseShort(goal[1]);
	    
	    hardX = new int[8];
	    hardY = new int[8];
	    
	    for (int i = 0; i < 8; i++) {
	    	hard = br.readLine().split(" ");
	  
	    	hardX[i] = Short.parseShort(hard[0]);
			hardY[i] = Short.parseShort(hard[1]);
	    }
	    
	    grid = new char[120][160];
	
	    for (int i = 0; i < 120; i++) grid[i] = br.readLine().toCharArray();
	}
}
