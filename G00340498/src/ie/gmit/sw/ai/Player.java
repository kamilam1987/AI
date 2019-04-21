package ie.gmit.sw.ai;

import ie.gmit.sw.ai.traversers.Node;

/*
 * Player class extends Node class, and is used to track player health.
 * Creates the player on the map.
 */
public class Player extends Node {

	// Declare variables
	private Node[][] maze; // Maze Node object
	private double playerHealth = 100; // Sets player health at the start of a game.

	// Constructor passes player position, type and maze.
	public Player(int row, int col, int type, Node[][] maze) {
		super(row, col, type);
		this.maze = maze;
	}

	// Setters and Getters
	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double playerHealth) {
		this.playerHealth = playerHealth;
	}

}
