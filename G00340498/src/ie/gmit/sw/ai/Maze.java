package ie.gmit.sw.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.fuzzy.Fuzzifier;
import ie.gmit.sw.ai.nn.CharacterNn;
import ie.gmit.sw.ai.traversers.Node;

public class Maze {
	//first 
	private Node[][] maze; //An array does not lend itself to the type of mazge generation alogs we use in the labs. There are no "walls" to carve...
	// Sets in fuzzifier use to run the while loop in runnable
	private Object lock =  new Object();
	private ExecutorService executor = Executors.newCachedThreadPool();
	//Player class
	private Player p;
	//Initlialise the neural network class and train the neural network
	private CharacterNn f;
	
	public Maze(int dimension, CharacterNn nfight){
		// Initialise
		this.f= nfight;
		
		//second
		maze = new Node[dimension][dimension];
		init();
		buildMaze();
		
		// Changed to int instead of character
		int featureNumber = (int)((dimension * dimension) * 0.01); //Change this value to control the number of objects
//		addFeature('\u0031', '0', featureNumber); //1 is a sword, 0 is a hedge
//		addFeature('\u0032', '0', featureNumber); //2 is help, 0 is a hedge
//		addFeature('\u0033', '0', featureNumber); //3 is a bomb, 0 is a hedge
//		addFeature('\u0034', '0', featureNumber); //4 is a hydrogen bomb, 0 is a hedge
		
		
		addFeature(1, 0, featureNumber); //1 is a sword, 0 is a hedge
		addFeature(2, 0, featureNumber); //2 is help, 0 is a hedge
		addFeature(3, 0, featureNumber); //3 is a bomb, 0 is a hedge
		addFeature(4, 0, featureNumber); //4 is a hydrogen bomb, 0 is a hedge
		
		//Specify the amount of items in the maze
		featureNumber = (int)((dimension * dimension) * 0.001); //Change this value to control the number of spiders
//		addFeature('\u0036', '0', featureNumber); //6 is a Black Spider, 0 is a hedge
//		addFeature('\u0037', '0', featureNumber); //7 is a Blue Spider, 0 is a hedge
//		addFeature('\u0038', '0', featureNumber); //8 is a Brown Spider, 0 is a hedge
//		addFeature('\u0039', '0', featureNumber); //9 is a Green Spider, 0 is a hedge
//		addFeature('\u003A', '0', featureNumber); //: is a Grey Spider, 0 is a hedge
//		addFeature('\u003B', '0', featureNumber); //; is a Orange Spider, 0 is a hedge
//		addFeature('\u003C', '0', featureNumber); //< is a Red Spider, 0 is a hedge
//		addFeature('\u003D', '0', featureNumber); //= is a Yellow Spider, 0 is a hedge
		
		// The player instantiated in game runner but changed to maze class
		player(5, -1);
		
		//Declared spiders
		addFeature(6, -1, featureNumber); //6 is a Black Spider, 0 is a hedge
		//addFeature(7, -1, featureNumber); //7 is a Blue Spider, 0 is a hedge
		//addFeature(8, -1, featureNumber); //8 is a Brown Spider, 0 is a hedge
		//addFeature(9, -1, featureNumber); //9 is a Green Spider, 0 is a hedge
		//addFeature(10, -1, featureNumber); //: is a Grey Spider, 0 is a hedge
		//addFeature(11, -1, featureNumber); //; is a Orange Spider, 0 is a hedge
		addFeature(12, -1, featureNumber); //< is a Red Spider, 0 is a hedge
		addFeature(13, -1, featureNumber); //= is a Yellow Spider, 0 is a hedge
	}
	
	private void init(){
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				//maze[row][col] = '0'; //Index 0 is a hedge...
				// here at the start of the game we are going to init the whole
				// maze with a value of 0
				maze[row][col] = new Node(row, col, 0);
			}
		}
	}
	
	// Changed that takes integers instead of character
	private void addFeature(int feature, int replace, int number){
		
		int counter = 0;
		// this will draw the amount of spiders and hedges 
		while (counter < number){ //Keep looping until feature number of items have been added
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			// 
			if (maze[row][col].getType() == replace){
				
				// Sends spiders and player to thread pool class 
				if(feature > 5 && feature < 14) {
					Fuzzifier f = new Fuzzifier(row,col,feature,lock,maze, p, this.f);
					//Execute thread
					executor.execute(f);
				}
				//Changes its type to the name of a spider
				maze[row][col].setType(feature);
				counter++;
			}
		}
	}
	
	// Builds the maze
	private void buildMaze(){ 
		for (int row = 1; row < maze.length - 1; row++){
			for (int col = 1; col < maze[row].length - 1; col++){
				int num = (int) (Math.random() * 10);
				if (isRoom(row, col)) continue;
				if (num > 5 && col + 1 < maze[row].length - 1){
					//maze[row][col + 1] = '\u0020'; //\u0020 = 0x20 = 32 (base 10) = SPACE
					// Space is set to type of -1
					maze[row][col + 1].setType(-1);
				}else {
//					if (row + 1 < maze.length - 1) maze[row + 1][col] = '\u0020';
					if (row + 1 < maze.length - 1) maze[row + 1][col].setType(-1);
				}
			}
		}	
	}
	
	// Puts the payer into the maze
	public void player(int feature, int replace){
		// Checks if player exist
		boolean placed = false;
		while(!placed){
			// Gets random row and col values
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			// If row and col values are not -1 means  its not empty and don't place it
			if (maze[row][col].getType() == replace){
				// If row and col values are empty then place the player
				p = new Player(row,col,feature, maze);
				maze[row][col] = p;
				placed = true;
			}
		}
	}
	
	private boolean isRoom(int row, int col){ //Flaky and only works half the time, but reduces the number of rooms
//		return row > 1 && maze[row - 1][col] == '\u0020' && maze[row - 1][col + 1] == '\u0020';
		return row > 1 && maze[row - 1][col].getType() == -1 && maze[row - 1][col + 1].getType() == -1;
	}
	
	// sets getters and setters for Node
	public Node[][] getMaze(){
		return this.maze;
	}
	
	public Node get(int row, int col){
		return this.maze[row][col];
	}
	
	public void set(int row, int col, Node node){
		node.setCol(col);
		node.setRow(row);
		
		this.maze[row][col] = node;
	}
	
	public int size(){
		return this.maze.length;
	}
	
	public Player getP() {
		return this.p;
	}

	public void setP(int row, int col) {
		this.maze[row][col] = this.p;
		this.p.setRow(row);
		this.p.setCol(col);
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1) sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}