package ie.gmit.sw.ai;

import java.util.LinkedList;

import ie.gmit.sw.ai.nn.CharacterNn;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.Traversator;

/* Spider class is used to set spider health, anger level which has different value for each enemy. Also uses path 
 * to track the spider for Heuristic search. CharacterNn object is passed to the spider class to be 
 * able implement a neural network.
 */
public class Spider {

	// Declare variables
	private double health = 100; // Spider health amount.
	private double anger; // Spider anger amount.
	private CharacterNn characterNn; // Object for characterNn neural network.
	private ControlledSprite player; // Object for player.
	private int result; // Result for neural network.
	private Node maze; // Object for maze.
	private LinkedList<Node> path = new LinkedList<Node>(); // Path for heuristic search.

	// Spider constructor which passes enemy health value, anger value player and
	// maze object.
	public Spider(double health, double anger, ControlledSprite player, Node maze) {
		this.health = health;
		this.anger = anger;
		this.player = player;
		this.maze = maze;
	}

	// Gets spider anger value and weapon.
	public void engage(double enemies, double weapon) {

		// Creates a new object of characterNn;
		characterNn = new CharacterNn();
		try {
			result = characterNn.action(0, 0, 0);
		} catch (Exception e) {
		}
	}

	// Use path to track the spider for Heuristic search
	public void setPath(LinkedList<Node> path) {
		this.path = path;
	}

	// Setter and getter for spider health.
	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	// Getter and setter for anger value.
	public double getAnger() {
		return anger;
	}

	public void setAnger(double anger) {
		this.anger = anger;
	}

	// Checks if spider is alive.
	public boolean isAlive() {
		if (health > 0)
			return true;
		else
			return false;
	}

}
