package ie.gmit.sw.ai.traversers;

import java.util.LinkedList;
import java.util.List;

import ie.gmit.sw.ai.Spider;

public class RecursiveDFSTraversator implements Traversator{
	private Node[][] maze;
	private boolean keepRunning = true;
	private Node goal;
	private LinkedList<Node> path = new LinkedList<Node>();
	private List<Node> fullPath = null;
	private Spider spider;
	
	
	public RecursiveDFSTraversator(Node goal){
		this.goal = goal;
	}
	
	public void traverse(Node[][] maze, Node node) {
		this.maze = maze;
		dfs(node);
	}
	
	private void dfs(Node node){
		if (!keepRunning) return;
		
		path.add(node);
		if (node.isGoalNode()){
			node.setGoalNode(true);
			while (node != null){			
				node = node.getParent();
				if (node != null){ 						
					path.add(node);
				}			
			}								
			spider.setPath(path);
			keepRunning = false;
			return;
		}
		
		
		else{
			if (!node.isVisited()){
				if (!node.isVisited()){
					node.setVisited(true);
					Node[] children = node.children(maze);
					for (int i = 0; i < children.length; i++) {
						if ((children[i].getRow() <= maze.length - 1) && (children[i].getCol() <= maze[children[i].getRow()].length - 1)
								&& ((children[i].getType() == 5))){
							children[i].setParent(node);
							dfs(children[i]);
						}
					}
				}
			}
		}
	}


	@Override
	public Node getNextNode() {
		// TODO Auto-generated method stub
		return path.getFirst();
	}
			


	
}