package ie.gmit.sw.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ie.gmit.sw.ai.fuzzy.FuzzySpider;
import ie.gmit.sw.ai.nn.CharacterNn;
import ie.gmit.sw.ai.traversers.Node;

/*
 * Maze class, is used to implement a spiders, player and other objects a game.
 */
public class Maze {

	// Declare variables
	private Node[][] maze; // An array does not lend itself to the type of mazge generation alogs we use in
							// the labs. There are no "walls" to carve...
	// Sets in in fuzzy spider class use to run the while loop in runnable.
	private Object lock = new Object();
	// automatically provides a pool of threads .
	private ExecutorService executor = Executors.newFixedThreadPool(50);
	// Object of player from player class.
	private Player p;
	// Initialize the neural network class and train the neural network
	private CharacterNn n;

	public Maze(int dimension, CharacterNn characterNn) {
		// Initialise
		this.n = characterNn;

		// Sets maze to node
		maze = new Node[dimension][dimension];
		init();
		buildMaze();

		// Changed to int instead of character
		int featureNumber = (int) ((dimension * dimension) * 0.01); // Change this value to control the number of
																	// objects
		addFeature(1, 0, featureNumber); // 1 is a sword, 0 is a hedge
		addFeature(2, 0, featureNumber); // 2 is help, 0 is a hedge
		addFeature(3, 0, featureNumber); // 3 is a bomb, 0 is a hedge
		addFeature(4, 0, featureNumber); // 4 is a hydrogen bomb, 0 is a hedge

		// Specify the amount of items in the maze
		featureNumber = (int) ((dimension * dimension) * 0.001); // Change this value to control the number of spiders

		// The player instantiated in game runner but changed to maze class
		player(5, -1);

		// Declared spiders
		addFeature(6, -1, featureNumber); // 6 is a Black Spider, 0 is a hedge
		addFeature(7, -1, featureNumber); // 7 is a Blue Spider, 0 is a hedge
		addFeature(8, -1, featureNumber); // 8 is a Brown Spider, 0 is a hedge
		addFeature(9, -1, featureNumber); // 9 is a Green Spider, 0 is a hedge
		// addFeature(10, -1, featureNumber); //: is a Grey Spider, 0 is a hedge
		// addFeature(11, -1, featureNumber); //; is a Orange Spider, 0 is a hedge
		// addFeature(12, -1, featureNumber); // < is a Red Spider, 0 is a hedge
		// addFeature(13, -1, featureNumber); // = is a Yellow Spider, 0 is a hedge
	}

	private void init() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				// maze with a value of 0
				maze[row][col] = new Node(row, col, 0);
			}
		}
	}

	// In addFeature function feature was change from character to integer.
	private void addFeature(int feature, int replace, int number) {

		int counter = 0;
		// Draws the amount of spiders and hedges
		while (counter < number) { // Keep looping until feature number of items have been added
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			//
			if (maze[row][col].getType() == replace) {

				// Sends spiders and player to thread pool class
				if (feature > 5 && feature < 14) {
					FuzzySpider f = new FuzzySpider(row, col, feature, lock, maze, p, this.n);
					// Execute thread
					executor.execute(f);
				}
				// Changes its type to the name of a spider.
				maze[row][col].setType(feature);
				counter++;
			}
		}
	}

	// Builds the maze
	private void buildMaze() {
		for (int row = 1; row < maze.length - 1; row++) {
			for (int col = 1; col < maze[row].length - 1; col++) {
				int num = (int) (Math.random() * 10);
				if (isRoom(row, col))
					continue;
				if (num > 5 && col + 1 < maze[row].length - 1) {
					// Space is set to type of -1
					maze[row][col + 1].setType(-1);
				} else {
					if (row + 1 < maze.length - 1)
						maze[row + 1][col].setType(-1);
				}
			}
		}
	}

	// Puts the payer into the maze
	public void player(int feature, int replace) {
		// Checks if player exist
		boolean placed = false;
		while (!placed) {
			// Gets random row and column values
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());

			// If row and column values are not -1 means its not empty and don't place it
			if (maze[row][col].getType() == replace) {
				// If row and column values are empty then place the player
				p = new Player(row, col, feature, maze);
				maze[row][col] = p;
				placed = true;
			}
		}
	}

	private boolean isRoom(int row, int col) { // Flaky and only works half the time, but reduces the number of rooms
		return row > 1 && maze[row - 1][col].getType() == -1 && maze[row - 1][col + 1].getType() == -1;
	}

	// Sets getters and setters for Node
	public Node[][] getMaze() {
		return this.maze;
	}

	// Gets number of rows and columns
	public Node get(int row, int col) {
		return this.maze[row][col];
	}

	// Sets Node to number of columns and rows
	public void set(int row, int col, Node node) {
		node.setCol(col);
		node.setRow(row);

		this.maze[row][col] = node;
	}

	// Size of the maze
	public int size() {
		return this.maze.length;
	}

	// Gets player object
	public Player getP() {
		return this.p;
	}

	// Sets player object to the position on maze
	public void setP(int row, int col) {
		this.maze[row][col] = this.p;
		this.p.setRow(row);
		this.p.setCol(col);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1)
					sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}