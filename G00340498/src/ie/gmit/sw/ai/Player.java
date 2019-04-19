package ie.gmit.sw.ai;

import ie.gmit.sw.ai.traversers.Node;

public class Player extends Node{
	
	//Used for player health
	private Node[][] maze;
	private double playerHealth = 100;
	
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
