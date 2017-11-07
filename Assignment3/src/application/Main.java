package application;
	
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
	public static final int CELLSIZE = 8;
	
	public static int startX, startY, goalX, goalY;
	
	public static int[] hardX, hardY;
	
	public static char[][] grid;
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException {
			
		BorderPane root = new BorderPane();
		
		// only needs to be run once
		generateFile();
		
		// read from the file here

		for (int x = 0; x < 160 ; x++) {
			for (int y = 0; y < 120; y++) {
				Rectangle cell = new Rectangle(x*CELLSIZE,y*CELLSIZE,CELLSIZE,CELLSIZE);
				if (x == startX && y == startY) {
					cell.setFill(Color.BLUE);
				} else if (x == goalX && y == goalY) {
					cell.setFill(Color.BLUE);
				} else if (grid[x][y] == '0') {
					cell.setFill(Color.BLACK);
				} else if (grid[x][y] == '1') {
					cell.setFill(Color.WHITE);
				} else if (grid[x][y] == '2') {
					cell.setFill(Color.YELLOW);
				} else if (grid[x][y] == 'a') {
					cell.setFill(Color.RED);
				} else if (grid[x][y] == 'b') {
					cell.setFill(Color.ORANGE);
				}
				root.getChildren().add(cell);
			}
		}

		Scene scene = new Scene(root,160*CELLSIZE,120*CELLSIZE);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public static void generateFile() throws FileNotFoundException, UnsupportedEncodingException {
		int x,y;
		
		PrintWriter writer = new PrintWriter("grids.txt", "UTF-8");
		
		// write 5 grids to the
		for (int i = 0; i < 5; i++) {
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
		
		// generate the start cell
		int i = random.nextInt(4);
		if (i == 0) {
			do {
				startX = random.nextInt(20);
				startY = random.nextInt(120);
			} while(grid[startX][startY] == '0');
		} else if (i == 1) {
			do {
				startX = random.nextInt(160);
				startY = random.nextInt(20);
			} while(grid[startX][startY] == '0');
		} else if (i == 2) {
			do {
				startX = random.nextInt(20) + 140;
				startY = random.nextInt(120);
			} while(grid[startX][startY] == '0');
		} else if (i == 3) {
			do {
				startX = random.nextInt(160);
				startY = random.nextInt(20) + 100;
			} while(grid[startX][startY] == '0');
		}
		
		// generate the goal cell
		i = random.nextInt(4);
		if (i == 0) {
			do {
				goalX = random.nextInt(20);
				goalY = random.nextInt(120);
			} while(grid[goalX][goalY] == '0' && 
					Math.abs(startX - goalX) < 100 &&
							Math.abs(startY - goalY) < 100 && 
							Math.sqrt(Math.pow(startX - goalX, 2.0) - Math.pow((startY - goalY), 2.0)) < 100 );
		} else if (i == 1) {
			do {
				goalX = random.nextInt(160);
				goalY = random.nextInt(20);
			} while(grid[goalX][goalY] == '0' && 
					Math.abs(startX - goalX) < 100 &&
							Math.abs(startY - goalY) < 100 && 
							Math.sqrt(Math.pow(startX - goalX, 2.0) - Math.pow((startY - goalY), 2.0)) < 100 );
		} else if (i == 2) {
			do {
				goalX = random.nextInt(20) + 140;
				goalY = random.nextInt(120);
			} while(grid[goalX][goalY] == '0' && 
					Math.abs(startX - goalX) < 100 &&
							Math.abs(startY - goalY) < 100 && 
							Math.sqrt(Math.pow(startX - goalX, 2.0) - Math.pow((startY - goalY), 2.0)) < 100 );
		} else if (i == 3) {
			do {
				goalX = random.nextInt(160);
				goalY = random.nextInt(20) + 100;
			} while(grid[goalX][goalY] == '0' && 
					Math.abs(startX - goalX) < 100 &&
							Math.abs(startY - goalY) < 100 && 
							Math.sqrt(Math.pow(startX - goalX, 2.0) - Math.pow((startY - goalY), 2.0)) < 100 );
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}