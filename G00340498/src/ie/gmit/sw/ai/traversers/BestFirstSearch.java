package ie.gmit.sw.ai.traversers;

import java.util.LinkedList;
import ie.gmit.sw.ai.Spider;

public class BestFirstSearch implements Traversator{
	LinkedList<Node> queue = new LinkedList<Node>();
	private LinkedList<Node> path = new LinkedList<Node>();
	private Node[][] maze;
	private boolean keepRunning = true;
	private Node goal;
	private Spider spider;
	
	public BestFirstSearch(Node goal){
		this.goal = goal;
	}
	
	public void traverse(Node[][] maze, Node node) {
		//this.maze = maze;
		queue.addLast(node);
		search(node);
		
	}
	
	public void search(Node node){
		

		while(!queue.isEmpty()){
			if (!keepRunning) return;

			if (node.isGoalNode()){
				path.add(node);

				while (node != null){			
					node = node.getParent();
					if (node != null){ 						
						path.add(node);
					}			
				}								
				spider.setPath(path);

				keepRunning = false;
				return;
			}else{
				Node[] children = node.children(maze);
				queue.removeFirst();//Remove the starting Node
				for (int i = 0; i < children.length; i++) {
					//for (int i = children.length - 1; i >= 0; i--) {			
					if ((children[i].getRow() <= maze.length - 1) && (children[i].getCol() <= maze[children[i].getRow()].length - 1)
							&& ((children[i].getType() == 5))){
						children[i].setParent(node);
						queue.addLast(children[i]);	
					}
				}
			}

			try {
				node = queue.getFirst();
			} catch (Exception e) {
			}
		}
	}


	@Override
	public Node getNextNode() {
		// TODO Auto-generated method stub
		return path.getFirst();
	}

	
}
