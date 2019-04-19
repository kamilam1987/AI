package ie.gmit.sw.ai;

import ie.gmit.sw.ai.traversers.Node;

public class Player extends Node{
	
	//Used for player health
	private Node[][] maze;
	private double playerHealth;
	
	public Player(int row, int col, int type, Node[][] maze) {
		super(row, col, type);
		this.maze = maze;
	}
	
	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double playerHealth) {
		this.playerHealth = playerHealth;
	}

}
