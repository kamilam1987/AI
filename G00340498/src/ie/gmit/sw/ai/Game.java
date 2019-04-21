package ie.gmit.sw.ai;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import ie.gmit.sw.ai.gui.GameOverJOptionPane;
import ie.gmit.sw.ai.nn.CharacterNn;

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
	private CharacterNn characterNn = new CharacterNn();

	public Game() throws Exception {

		// Train neural network and sends to it and send to fuzzifier
		characterNn.train();

		// Sets Maze with fuzzy logic for fight
		model = new Maze(MAZE_DIMENSION, characterNn);
		view = new GameView(model);

		// Gets sprites
		Sprite[] sprites = getSprites();

		view.setSprites(sprites);

		// Replaced inside updateView
		// placePlayer();
		updateView();

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

	// Sets view and player
	private void updateView() {
		// Sets the player
		view.setPlayer(model.getP());
		currentRow = model.getP().getRow();
		currentCol = model.getP().getCol();
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

	// Navigate player using arrow keys
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

	private static void closeGame() {

		// close the game
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
	}

	public void keyTyped(KeyEvent e) {
	} // Ignore

	// Checks for player valid move on maze
	private boolean isValidMove(int row, int col) {

		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getType() == -1) {
			model.set(currentRow, currentCol, model.get(row, col));
			model.set(row, col, model.getP());
			return true;
		} else if (player.getPlayerHealth() <= 0) {
			gameOver(true);
			return true;
		} else {
			return false; // Can't move
		}
	}

	// Sets game over if player health is less then zero.
	public static void gameOver(boolean isAlive) {
		GAME_OVER = true;

		if (isAlive) { // game over, player is dead
			// show player dead message
			GameOverJOptionPane.display("Player Died! Game Over!");

		} else {
			// Won game, display message
			GameOverJOptionPane.display("Boss Defeated! Game Over!");
		} // if

		// end game
		closeGame();

	} // setGameOver()

	private Sprite[] getSprites() throws Exception {
		// Read in the images from the resources directory as sprites. Note that each
		// sprite will be referenced by its index in the array, e.g. a 3 implies a
		// Bomb...
		// Ideally, the array should dynamically created from the images...

		player = new ControlledSprite(0, "Main Player", 3, 500, "resources/images/player/d1.png",
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

		// First value sets angry level for spiders.
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