package ie.gmit.sw.ai.fuzzy;

import java.util.ArrayList;
import java.util.Random;
import ie.gmit.sw.ai.Sprite;
import ie.gmit.sw.ai.Player;
import ie.gmit.sw.ai.nn.CharacterNn;
import ie.gmit.sw.ai.traversers.AStarTraversator;
import ie.gmit.sw.ai.traversers.BestFirstSearch;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.RecursiveDFSTraversator;
import ie.gmit.sw.ai.traversers.Traversator;

/* This class is responsible for implementing a neural network and fuzzy logig in a game. 
 * 
 */ 
public class FuzzySpider extends Sprite implements Runnable {

	// Declare variables
	private int row; // Row value
	private int col; // Column value
	private int feature; // spider feature number
	private Node node = new Node(row, col, feature); // Creates a new Node object which passes row, column and spider
														// feature value.
	private Object lock; // Used for synchronization.
	private Node[][] maze; // sets the new position of a character.
	private Random random = new Random();
	private Node lastNode; // Sets the old position of a character
	private Player player; // Player object
	private Traversator traverse; // traverse object uses for heuristic search.
	private Node MoveNext; // Sets player next move
	private boolean canMove; // Checks if player is able to move
	private CharacterNn characterNn; // Object for characterNn
	private int choice; // Sets for neural network choice

	// Constructor
	public FuzzySpider(int row, int col, int feature, Object lock, Node[][] maze, Player player, CharacterNn f) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.feature = feature;
		// player
		this.player = player;
		this.lock = lock;
		this.maze = maze;
		this.characterNn = f;

		// Sets col, row and feature to node object.
		node.setCol(col);
		node.setRow(row);
		node.setType(feature);

		// Checks the spider feature(number).In this case checks if the spider is blue.
		if (feature == 7) {
			// Uses AStar search to follow the player
			traverse = new AStarTraversator(player);
		} else if (feature == 6) { // Checks the spider feature(number).In this case checks if the spider is black.
			// Uses AStar search to follow the player
			traverse = new AStarTraversator(player);
		}
		/*
		 * else if (feature == 13) { traverse = new RecursiveDFSTraversator(player); }
		 */

		/*
		 * else if (feature == 10) { traverse = new BestFirstSearch(player); }
		 */
	}

	// Spider follows the player
	private void followPlayer() {
		// TODO Auto-generated method stub
		if (MoveNext != null) {
			synchronized (lock) {
				// Checks all the nodes around.
				Node[] surroundingNodes = node.adjacentNodes(maze);
				// List of empty surrounding nodes.
				ArrayList<Node> emptySurroundingNodes = new ArrayList<>();
				// Checks for empty surrounding nodes.
				for (Node n : surroundingNodes) {
					if (n.getType() == -1) {
						emptySurroundingNodes.add(n);
					}
				}

				// Checks for empty surrounding nodes.
				for (Node n : emptySurroundingNodes) {
					// Checks for next movement.
					if (MoveNext.equals(n)) {
						// New position of the object.
						int newPosX, newPosY;
						// Old position of the object.
						int oldPosX = node.getRow(), oldPosY = node.getCol();

						// Sets new z and y position.
						newPosX = MoveNext.getRow();
						newPosY = MoveNext.getCol();

						// Sets new x and y position to node row and node column.
						node.setRow(newPosX);
						node.setCol(newPosY);

						// Sets new position of maze to a node
						maze[newPosX][newPosY] = node;
						// Create a new node and sets to old maze x, y position.
						maze[oldPosX][oldPosY] = new Node(oldPosX, oldPosY, -1);

						MoveNext = null;
						canMove = false;
						return;
					}
				}
				// Move to random in empty
				move();

				MoveNext = null;
				canMove = false;
				return;
			}
		} else {
			move();

			canMove = false;
		}
	}

	public void move() {

		synchronized (lock) {
			// Sets all the nodes around
			Node[] surroundingNodes = node.adjacentNodes(maze);
			// List of empty surrounding nodes
			ArrayList<Node> emptySurroundingNodes = new ArrayList<>();

			// Check if they are empty
			for (Node n : surroundingNodes) {
				if (n.getType() == -1 && n != lastNode) {
					emptySurroundingNodes.add(n);
				}
			}

			// Checks if the size of empty surrounding nodes it's greater then 0.
			if (emptySurroundingNodes.size() > 0) {

				// Sets the position to get random empty surrounding nodes size.
				int position = random.nextInt(emptySurroundingNodes.size());

				// New position of the object.
				int newPosX, newPosY;
				// Old position of the object.
				int oldPosX = node.getRow(), oldPosY = node.getCol();
				newPosX = emptySurroundingNodes.get(position).getRow(); // Sets next new x position.
				newPosY = emptySurroundingNodes.get(position).getCol(); // Sets next new y position.
				node.setRow(newPosX);
				node.setCol(newPosY);

				// Sets last node to the new Node old x,y position.
				lastNode = new Node(oldPosX, oldPosY, -1);
				// Sets maze new x,y position to the node.
				maze[newPosX][newPosY] = node;
				// Sets maze old x,y position to the last.
				maze[oldPosX][oldPosY] = lastNode;
			}

		}

	}

	public void engage(int attack) {

		// Implement a fuzzy logic to the game.
		GameFuzzyLogic f = new GameFuzzyLogic();
		// Sets the current health value to the get player health.
		double currentHealth = player.getPlayerHealth();
		// Sets a new player health after spider attack.
		double newHealth = f.PlayerHealth(50, attack);
		// Decrease player health.
		double pHealth = currentHealth - newHealth;
		System.out.println(pHealth);
		// Sets a new player health.
		player.setPlayerHealth(pHealth);
		System.out.println("PLAYER HEALTH: " + player.getPlayerHealth());

	}

	// Uses for a heuristic search that tracks a player movement.
	public void traverse(int row, int col, Traversator t) {
		t.traverse(maze, maze[row][col]);
		MoveNext = t.getNextNode();
		if (MoveNext != null) {
			canMove = true;
		} else {
			canMove = false;
		}
	}

	// Uses a neural network to decrease player health when hits the enemy (spider)
	// without a weapon.
	public void nuralNetBattle(double health, double weapon, double enemies) {

		// Sets the player health
		if (health < 30) {
			health = 0;
		} else if (health > 30 && health < 60) {
			health = 1;
		} else if (health > 60) {
			health = 2;
		}

		// Sets the enemies anger value.
		if (enemies < 30) {
			enemies = 0;
		} else if (enemies > 30 && enemies < 60) {
			enemies = 1;
		} else if (enemies > 60) {
			enemies = 2;
		}

		try {

			// Sets choice to a neural network action.
			choice = characterNn.action(health, weapon, enemies);

			switch (choice) {

			case 1:
				panic();
				break;
			case 2:
				attack();
				break;
			case 3:
				hide();
				break;
			case 4:
				runAway();
				break;

			}

		} catch (Exception e) {
		}

	}

	// Functions for neural network
	private void panic() {
		System.out.println("PANIC");
		move();
	}

	private void attack() {
		System.out.println("ATTACK");
		followPlayer();
	}

	private void hide() {
		System.out.println("HIDE");
		move();
	}

	private void runAway() {
		System.out.println("RUN");
		move();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				// Runs a thread
				Thread.sleep(1000 * feature / 2);

				// Apply a search to a given spiders.
				if (feature >= 7 && feature <= 9) {

					if (feature == 7) {
						traverse(node.getRow(), node.getCol(), traverse);
					}

					// Implements the fuzzy logic
					// Detects collision between spider and a player.
					if (node.getHeuristic(player) <= 1) {
						System.out.println("COLLISION");
						// Gets the spider anger value.
						System.out.println(super.getAnger());
						// Apply a fuzzy logic.
						engage(super.getAnger());
					// Detect the player if it's 5 steeps away from a spider.
					} else if (canMove && node.getHeuristic(player) < 10) {
						System.out.println("FOLLOW THE PLAYER !!!");
						// Implement function to follow the player.
						followPlayer();
					} else {
						// Moves spider around the maze.
						move();
					}
				} else {
					// Implements neural network on black spider

					if (feature == 6) {
						traverse(node.getRow(), node.getCol(), traverse);
					}
					// Detects collision between spider and a player.
					if (node.getHeuristic(player) <= 1) {
						System.out.println("COLLISION");
						// Gets the spider anger value.
						System.out.println(super.getAnger());
						// Apply a fuzzy logic.
						engage(super.getAnger());
					// Detect the player if it's 5 steeps away from a spider.
					} else if (canMove && node.getHeuristic(player) < 5) {
						System.out.println("Neural Network Running on black spider");
						// Runs a neural network when player is 5 steeps away from a spider.
						nuralNetBattle(player.getPlayerHealth(), super.getAnger(), 1);
						// Spiders follows a player.
						followPlayer();
					} else {
						//Spiders moves around.
						move();
					}

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
