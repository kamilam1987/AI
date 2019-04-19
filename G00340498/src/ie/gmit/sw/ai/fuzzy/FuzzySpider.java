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

public class FuzzySpider extends Sprite implements Runnable {

	// variables
	private int row;
	private int col;
	private int feature;

	private Node node = new Node(row, col, feature);
	private Object lock;
	private Node[][] maze;

	private Random random = new Random();
	private Node lastNode;
	private Player player;
	private Traversator traverse;
	private Node MoveNext;
	private boolean canMove;
	private CharacterNn nnfight;
	private int choice;

	// searches
	public FuzzySpider(int row, int col, int feature, Object lock, Node[][] maze, Player player, CharacterNn f) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.feature = feature;
		// player
		this.player = player;

		node.setCol(col);
		node.setRow(row);
		node.setType(feature);

		this.lock = lock;
		this.maze = maze;

		//player.setPlayerHealth(100);

		this.nnfight = f;

		if (feature == 7) {
			// assign a search
			traverse = new AStarTraversator(player);
		} else if (feature == 6) {
			traverse = new AStarTraversator(player);
		}
		/*
		 * else if (feature == 13) { traverse = new RecursiveDFSTraversator(player); }
		 */

		/*
		 * else if (feature == 10) { traverse = new BestFirstSearch(player); }
		 */

	}

	
	// move around

	private void followPlayer() {
		// TODO Auto-generated method stub
		if (MoveNext != null) {
			synchronized (lock) {
				// Figure out all the nodes around
				Node[] surroundingNodes = node.adjacentNodes(maze);
				// List of empty surrounding nodes
				ArrayList<Node> emptySurroundingNodes = new ArrayList<>();
				// Check if they are empty
				for (Node n : surroundingNodes) {
					if (n.getType() == -1) {
						emptySurroundingNodes.add(n);
					}
				}

				// Check if they are empty
				for (Node n : emptySurroundingNodes) {
					if (MoveNext.equals(n)) {
						// New position of the object
						int newPosX, newPosY;
						// Previous position of the object
						int oldPosX = node.getRow(), oldPosY = node.getCol();

						System.out.println();
						newPosX = MoveNext.getRow();
						newPosY = MoveNext.getCol();

						node.setRow(newPosX);
						node.setCol(newPosY);

						maze[newPosX][newPosY] = node;
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
			// Figure out all the nodes around
			Node[] surroundingNodes = node.adjacentNodes(maze);
			// List of empty surrounding nodes
			ArrayList<Node> emptySurroundingNodes = new ArrayList<>();

			// Check if they are empty
			for (Node n : surroundingNodes) {
				if (n.getType() == -1 && n != lastNode) {
					emptySurroundingNodes.add(n);
				}
			}

			if (emptySurroundingNodes.size() > 0) {

				int position = random.nextInt(emptySurroundingNodes.size());

				// New position of the object
				int newPosX, newPosY;
				// Previous position of the object
				int oldPosX = node.getRow(), oldPosY = node.getCol();
				newPosX = emptySurroundingNodes.get(position).getRow();// nextPosition.getRow();
				newPosY = emptySurroundingNodes.get(position).getCol();// nextPosition.getCol();
				node.setRow(newPosX);
				node.setCol(newPosY);

				lastNode = new Node(oldPosX, oldPosY, -1);
				maze[newPosX][newPosY] = node;
				maze[oldPosX][oldPosY] = lastNode;
			}
			// add else if
		}

	}

	public void engage(int attack) {

		GameFuzzyLogic f = new GameFuzzyLogic();

		double currentHealth = player.getPlayerHealth();
		double newHealth = f.PlayerHealth(50, attack);
		double pHealth = currentHealth - newHealth;
		System.out.println(pHealth);
		player.setPlayerHealth(pHealth);
		System.out.println("PLAYER HEALTH: " + player.getPlayerHealth());

	}

	public void traverse(int row, int col, Traversator t) {
		t.traverse(maze, maze[row][col]);
		MoveNext = t.getNextNode();
		if (MoveNext != null) {
			canMove = true;
		} else {
			canMove = false;
		}
	}

	public void fightNn(double health, double weapon, double enemies) {

		if (health < 30) {
			health = 0;
		} else if (health > 30 && health < 60) {
			health = 1;
		} else if (health > 60) {
			health = 2;
		}

		if (enemies < 30) {
			enemies = 0;
		} else if (enemies > 30 && enemies < 60) {
			enemies = 1;
		} else if (enemies > 60) {
			enemies = 2;
		}

		try {

			choice = nnfight.action(health, weapon, enemies);

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
				Thread.sleep(1000 * feature / 2);

				// fuzzy stuff
				if (feature >= 7 && feature <= 9) {

					if (feature == 7) {
						traverse(node.getRow(), node.getCol(), traverse);
					}

					// fuzzy stuff
					if (node.getHeuristic(player) <= 1) {
						System.out.println("COLLISION");
						System.out.println(super.getAnger());
						engage(super.getAnger());
					} else if (canMove && node.getHeuristic(player) < 10) {
						System.out.println("Follow the player");
						followPlayer();
					} else {
						move();
					}
				} else {
					// neural net stuff

					if (feature == 6) {
						traverse(node.getRow(), node.getCol(), traverse);
					}
					if (node.getHeuristic(player) <= 1) {
						System.out.println("COLLISION");
						System.out.println(super.getAnger());
						engage(super.getAnger());
					} else if (canMove && node.getHeuristic(player) < 5) {
						System.out.println("Neural Network Running on black spider");
						fightNn(player.getPlayerHealth(), super.getAnger(), 1);

					} else {
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
