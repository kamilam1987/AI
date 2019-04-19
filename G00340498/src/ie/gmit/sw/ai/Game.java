package ie.gmit.sw.ai;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import ie.gmit.sw.ai.fuzzy.Fight;
import ie.gmit.sw.ai.fuzzy.Fuzzifier;
import ie.gmit.sw.ai.gui.GameOverJOptionPane;
import ie.gmit.sw.ai.nn.CharacterNn;
import ie.gmit.sw.ai.traversers.Node;


public class Game implements KeyListener {
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	public static boolean GAME_OVER = false;
	private static JFrame f;
	private ControlledSprite player;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	public int attack;
	private CharacterNn nfight = new CharacterNn();
	
	public Game() throws Exception {
		
		//train it and send to fuzzifier
		nfight.train();
		
		model = new Maze(MAZE_DIMENSION, nfight);
		view = new GameView(model);

		Sprite[] sprites = getSprites();

		view.setSprites(sprites);

		//Replaced inside updateView
		//placePlayer();
		updateView();
		//OstartSpiders();

		Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
		view.setPreferredSize(d);
		view.setMinimumSize(d);
		view.setMaximumSize(d);

		JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addKeyListener(this);
		f.getContentPane().setLayout(new FlowLayout());
		f.add(view);
		f.setSize(1000, 1000);
		f.setLocation(100, 100);
		f.pack();
		f.setVisible(true);
	}

//	private void placePlayer() {
//		currentRow = (int) (MAZE_DIMENSION * Math.random());
//		currentCol = (int) (MAZE_DIMENSION * Math.random());
//		model.set(currentRow, currentCol, '5'); // Player is at index 5
//		updateView();
//	}

	private void updateView() {
		//Sets the player
		view.setPlayer(model.getP());
		currentRow = model.getP().getRow();
		currentCol = model.getP().getCol();
		
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
			if (isValidMove(currentRow, currentCol + 1)) {
				player.setDirection(Direction.RIGHT);
				currentCol++;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
			if (isValidMove(currentRow, currentCol - 1)) {
				player.setDirection(Direction.LEFT);
				currentCol--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
			if (isValidMove(currentRow - 1, currentCol)) {
				player.setDirection(Direction.UP);
				currentRow--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
			if (isValidMove(currentRow + 1, currentCol)) {
				player.setDirection(Direction.DOWN);
				currentRow++;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			view.toggleZoom();
		} else {
			return;
		}

		updateView();
	}

	public void keyReleased(KeyEvent e) {
	} // Ignore

	private static void closeGame(){

        // close the game
        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
    }

	public void keyTyped(KeyEvent e) {
	} // Ignore

	private boolean isValidMove(int row, int col) {

		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getType() == -1) {
			model.set(currentRow, currentCol, model.get(row, col));
			model.set(row, col, model.getP());
			return true;
		}
//		} else if (row <= model.size() - 1 && col <= model.size() - 1
//				&& (model.get(row, col).getType() == 7 || model.get(row, col).getType() == 6)) {
//			attack = 65;
//			spider.fight(2, 2);
//			model.set(currentRow, currentCol, model.get(row, col));
//			//model.set(row, col, model);
//
//			Fight f = new Fight();
//
//			double health = player.getPlayerHealth();
//			double newHealth = f.PlayerHealth(50, attack);
//			double hello = health - newHealth;
//
//			player.setPlayerHealth(hello);
//			System.out.println(player.getPlayerHealth());
//			System.out.println(newHealth);
//			System.out.println("hit");
//
////			Fuzzifier fuzz = new Fuzzifier();
////			
////		
////			player.setPlayerHealth(fuzz.newHealth(attack,player));
//
//			if (player.getPlayerHealth() <= 0) {
//				gameOver(true);
//			}
//
//
//			return true;
//
//		} 
		else {
			return false; // Can't move
		}
	}

	
	//This method creates / starts the Monster objects in a new thread  
//		private void startSpiders() {
//			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
//			Random r = new Random();
//			for (int row = 0; row < model.size(); row++){
//				for (int col = 0; col < model.get(row, col); col++){
//					char ch = model.get(row, col); //Index 0 is a hedge
//
//					if(ch > '5'){
//						if(ch <= '7')
//							spider = new Spider(Math.round(r.nextDouble()*50), Math.round(r.nextDouble()*100), player, model);
//						else if(ch <= '9')
//							spider = new Spider(Math.round(r.nextDouble()*50), Math.round(r.nextDouble()*50), player, model);
//						else
//							spider = new Spider(Math.round(r.nextDouble()*50), Math.round(r.nextDouble()*100), player, model);
//						//executor.execute(spider);
//					}			
//				}					
//			}
//		}
	public static void gameOver(boolean playerDead) {
		GAME_OVER = true;

		if (playerDead) { // game over, player is dead
			// show player dead message
			GameOverJOptionPane.display("Player Died! Game Over!");

		} else { // game over, boss is dead
			// won game, show message
			GameOverJOptionPane.display("Boss Defeated! Game Over!");
		} // if

		// end game
		 //closeGame();

	} // setGameOver()
	private Sprite[] getSprites() throws Exception {
		// Read in the images from the resources directory as sprites. Note that each
		// sprite will be referenced by its index in the array, e.g. a 3 implies a
		// Bomb...
		// Ideally, the array should dynamically created from the images...

		player = new ControlledSprite(0 , "Main Player", 3, 500, "resources/images/player/d1.png",
				"resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png",
				"resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png",
				"resources/images/player/r2.png", "resources/images/player/r3.png");

		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite(0, "Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite(0, "Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite(0, "Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite(0, "Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite(0, "Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		sprites[5] = player;
		sprites[6] = new Sprite(70, "Black Spider", 2, "resources/images/spiders/black_spider_1.png",
				"resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite(60, "Blue Spider", 2, "resources/images/spiders/blue_spider_1.png",
				"resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite(30, "Brown Spider", 2, "resources/images/spiders/brown_spider_1.png",
				"resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite(20, "Green Spider", 2, "resources/images/spiders/green_spider_1.png",
				"resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite(40, "Grey Spider", 2, "resources/images/spiders/grey_spider_1.png",
				"resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite(50, "Orange Spider", 2, "resources/images/spiders/orange_spider_1.png",
				"resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite(45, "Red Spider", 2, "resources/images/spiders/red_spider_1.png",
				"resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite(55, "Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png",
				"resources/images/spiders/yellow_spider_2.png");
		return sprites;
	}
}