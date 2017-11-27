package application;
	
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
	static final int CELLSIZE = 8;
	
	static final int ITERATIONS = 50;
	
	static final boolean UNIFORM_COST_SEARCH = true;
	
	static final boolean A_STAR_SEARCH = false;
	
	public static short startX, startY, goalX, goalY;
	
	static int[] hardX;

	static int[] hardY;
	
	static char[][] grid;
	
	static float averageNodesExpanded = 0;
	
	static float averagePathLength = 0;
	
	//static ShortestPath path;
	
	static SequentialHeuristic path;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		AnchorPane root = FXMLLoader.load(getClass().getResource("view.fxml"));
		

		
		Scene scene = new Scene(root,1500,1000);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void start(Stage primaryStage,int hi) throws IOException {
		
		// only needs to be run once
		//generateFile();
		
		// read first grid from file
		BufferedReader br = new BufferedReader(new FileReader("grids.txt"));
		
		double averageDuration = 0;
		
		double averageMemoryUsage = 0;
		
		try {
			
			String[] start, goal, hard;
			
			long startTime, endTime;
			
			for (int x = 0 ; x < ITERATIONS ; x++) {
				
				System.out.println("iteration " + x);
				
				
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
			    
			    //path = new ShortestPath(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), UNIFORM_COST_SEARCH, 1);
			    path = new SequentialHeuristic(grid, new Vertex(startX,startY), new Vertex(goalX, goalY), 1.25f, 2f);
			    
				startTime = System.currentTimeMillis();
				grid = path.AStar();
				endTime = System.currentTimeMillis();
				
				averageMemoryUsage += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
				
				averageDuration += endTime - startTime;
			}


		} finally {
		    br.close();
		}
		
		System.out.println("Average duration: " + averageDuration/ITERATIONS + " milliseconds");
		System.out.println("Average path length: " + averagePathLength/ITERATIONS);
		System.out.println("Average nodes expanded: " + averageNodesExpanded/ITERATIONS);
		System.out.println("Average memory usage: " + averageMemoryUsage/(ITERATIONS * 1000000) + " megabytes");

		Scene scene = buildGUI(grid);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public static Scene buildGUI(char[][] grid) {
		
		BorderPane root = new BorderPane();
		
		Tooltip t;
		
		for (short y = 0; y < 120; y++){
			for (short x = 0; x < 160 ; x++) {
				Rectangle cell = new Rectangle(x*CELLSIZE,y*CELLSIZE,CELLSIZE,CELLSIZE);
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
				root.getChildren().add(cell);
			}
		}
		
		
		
		return new Scene(root,160*CELLSIZE,120*CELLSIZE);
	}
	
	public static void generateFile() throws FileNotFoundException, UnsupportedEncodingException {
		int x,y;
		
		PrintWriter writer = new PrintWriter("grids.txt", "UTF-8");
		
		// only needs to generate 5 grids, but for some reason the last grid is never completely written to the file
		for (int i = 0; i < 6; i++) {
			generateGrid();
			for (int j = 0; j < 10; j++) {
				generateStartGoal();
				writer.println(startX + " " + startY);
				writer.println(goalX + " " + goalY);
				for (int k = 0; k < hardX.length; k++)
					writer.println(hardX[k] + " " + hardY[k]);
				for (y = 0; y < 120; y++){
					for (x = 0; x < 160 ; x++) {
						writer.print(grid[x][y]+"");
					}
					writer.print('\n');
				}
			}
		}
		
		writer.close();
	}
	
	public static void generateGrid() {
		grid = new char[160][120];
		int x , y;
		
		// initialize the grid with regular unblocked cells
		for (x = 0; x < 160 ; x++) {
			for (y = 0; y < 120; y++) {
				grid[x][y] = '1';
			}
		}
		
		Random random = new Random();
		
		// generate the center of the 8 hard to traverse areas
		hardX = random.ints(0,160).distinct().limit(8).toArray();
		hardY = random.ints(0,120).distinct().limit(8).toArray();
		
		
		for (int i = 0; i < hardX.length; i++){	
			for (x = hardX[i] - 15; x < hardX[i] + 16; x++) {
				for (y = hardY[i] - 15; y < hardY[i] + 16; y++) {
					if (x >= 0 && x < 160 && y >= 0 && y < 120 && random.nextBoolean()) {
						grid[x][y] = '2';
					}
				}
			}
		}
		
		
		// generate the highways
		int len = 0, paths = 0, count = 0, restartCount = 0;
		char direction = 0;
		char[][] tempGrid = new char[160][120];
		char[][] tempPath = new char[160][120];
		boolean restart = false;
		while (paths < 4) {	
			len = 0;
			
			// first initialize the first 20 cells of the highway
			if (random.nextBoolean()) {
				y = random.nextInt(120);
				if (random.nextBoolean()) {
					x = 0;
					for (int j = 0; j < 20; j++) {
						tempPath[x][y] = 'a';
						direction = 's';
						len++;
						x++;
					}
				} else {
					x = 159;
					for (int j = 0; j < 20; j++) {
						tempPath[x][y] = 'a';
						direction = 'n';
						len++;
						x--;
					}
				}
			} else {
				x = random.nextInt(160);
				if (random.nextBoolean()) {
					y = 0;
					for (int j = 0; j < 20; j++) {
						tempPath[x][y] = 'a';
						direction = 'e';
						len++;
						y++;
					}
				} else {
					y = 119;
					for (int j = 0; j < 20; j++) {
						tempPath[x][y] = 'a';
						direction = 'w';
						len++;
						y--;
					}
				}
			}
			
			// keep expanding the highway until it hits the edge
			do {
				
				// randomly switch directions
				if (direction == 'n' || direction == 's') {
					if (random.nextInt(10) < 4) {
						if (random.nextBoolean()) {
							direction = 'e';
						} else {
							direction = 'w';
						}
					}
				} else if (direction == 'e' || direction == 'w') {
					if (random.nextInt(10) < 4) {
						if (random.nextBoolean()) {
							direction = 'n';
						} else {
							direction = 's';
						}
					}
				}
				
				// expand the highway in the correct direction, and reject the highway if it hits itself
				if (direction == 'n') {
					count = 0;

					while (y >= 0 && count <= 20) {
						if (tempPath[x][y] == 'a') {
							restart = true;
							len = 0;
							break;
						}
						tempPath[x][y] = 'a';
						count++;
						y--;
						len++;
					}
				} else if (direction == 's') {
					count = 0;

					while (y < 120 && count <= 20) {
						if (tempPath[x][y] == 'a') {
							restart = true;
							len = 0;
							break;
						}
						tempPath[x][y] = 'a';
						count++;
						y++;
						len++;
					}
				} else if (direction == 'e') {
					count = 0;

					while (x < 160 && count <= 20) {
						if (tempPath[x][y] == 'a') {
							restart = true;
							len = 0;
							break;
						}
						tempPath[x][y] = 'a';
						count++;
						x++;
						len++;
					}
				} else if (direction == 'w') {
					count = 0;

					while (x >= 0 && count <= 20) {
						if (tempPath[x][y] == 'a') {
							restart = true;
							len = 0;
							break;
						}
						tempPath[x][y] = 'a';
						count++;
						x--;
						len++;
					}
				}
				if (restart) break;
			} while (x >= 0 && x < 160 && y >= 0 && y < 120 );
			
			// accept the highway if it's at least 100 cells long
			if (len >= 100) {
				
				// check if the highway overlapped another highway, and reject it if it did
				for (x = 0; x < 160 ; x++) {
					for (y = 0; y < 120; y++) {
						if (tempGrid[x][y] == 'a' && tempPath[x][y] == 'a') {
							restartCount++;
							restart = true;
							break;
						}
					}
					if (restart) break;
				}
					
				// accept the highway
				if (!restart) {
					for (x = 0; x < 160 ; x++) {
						for (y = 0; y < 120; y++) {
							if (tempPath[x][y] == 'a')
								tempGrid[x][y] = 'a';
						}
					}
					paths++;
				}
			} else {
				restartCount++;
			}
			
			
			tempPath = new char[160][120];
			restart = false;
			
			// reject all the highways if it's having trouble making new highways
			if (restartCount > 50) {
				paths = 0;
				tempGrid = new char[160][120];
				restartCount = 0;
			}
		}
		
		
		// add the highways to the grid
		for (x = 0; x < 160 ; x++) {
			for (y = 0; y < 120; y++) {
				if (tempGrid[x][y] == 'a' && grid[x][y] == '2')
					grid[x][y] = 'b';
				else if (tempGrid[x][y] == 'a')
					grid[x][y] = 'a';
			}
		}
		
		// generate 3840 blocked cells
		count = 0;
		while (count < 3840) {
			x = random.nextInt(160);
			y = random.nextInt(120);
			char randomCell = grid[x][y];
			if (randomCell != 'a' && randomCell != 'b' && randomCell != '0') {
				grid[x][y] = '0';
				count++;
			}
		}
		
		
	}
	
	public static void generateStartGoal() {
		
		Random random = new Random();
		
		
		int i, dx, dy, counter;
		boolean restart = true;
		
		while (restart) {
			// generate the start cell
			i = random.nextInt(4);
			if (i == 0) {
				do {
					startX = (short) random.nextInt(20);
					startY = (short) random.nextInt(120);
				} while(grid[startX][startY] == '0');
			} else if (i == 1) {
				do {
					startX = (short) random.nextInt(160);
					startY = (short) random.nextInt(20);
				} while(grid[startX][startY] == '0');
			} else if (i == 2) {
				do {
					startX = (short) (random.nextInt(20) + 140);
					startY = (short) random.nextInt(120);
				} while(grid[startX][startY] == '0');
			} else if (i == 3) {
				do {
					startX = (short) random.nextInt(160);
					startY = (short) (random.nextInt(20) + 100);
				} while(grid[startX][startY] == '0');
			}
			
			// generate the goal cell
			i = random.nextInt(4);
			
			counter = 0;
			
			if (i == 0) {
				do {
					goalX = (short) random.nextInt(20);
					goalY = (short) random.nextInt(120);
					dx = startX - goalX;
					dy = startY - goalY;
					if (counter++ > 50 ) break;
				} while(grid[goalX][goalY] == '0' || Math.sqrt(dx*dx + dy*dy) < 100);
			} else if (i == 1) {
				do {
					goalX = (short) random.nextInt(160);
					goalY = (short) random.nextInt(20);
					dx = startX - goalX;
					dy = startY - goalY;
					if (counter++ > 50 ) break;
				} while(grid[goalX][goalY] == '0' || Math.sqrt(dx*dx + dy*dy) < 100);
			} else if (i == 2) {
				do {
					goalX = (short) (random.nextInt(20) + 140);
					goalY = (short) random.nextInt(120);
					dx = startX - goalX;
					dy = startY - goalY;
					if (counter++ > 50 ) break;
				} while(grid[goalX][goalY] == '0' || Math.sqrt(dx*dx + dy*dy) < 100);
			} else if (i == 3) {
				do {
					goalX = (short) random.nextInt(160);
					goalY = (short) (random.nextInt(20) + 100);
					dx = startX - goalX;
					dy = startY - goalY;
					if (counter++ > 50 ) break;
				} while(grid[goalX][goalY] == '0' || Math.sqrt(dx*dx + dy*dy) < 100);
			}
			
			if (counter <= 50) restart = false;
			counter = 0;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
