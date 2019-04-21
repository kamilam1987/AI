package ie.gmit.sw.ai;

/*
 * ControlledSprite class extends Sprite class, and is used to track player health.
 * Checks if the player is alive.
 */
public class ControlledSprite extends Sprite {

	// Declare variables
	private double playerHealth; // Player health amount
	private boolean isAlive; // Checks if player is alive

	// Controller passes spider anger value, sprite name, number of frames, player
	// health amount and sprite image.
	public ControlledSprite(int anger, String name, int frames, double playerHealth, String... images)
			throws Exception {
		super(anger, name, frames, images);
		this.playerHealth = playerHealth;
	}

	public ControlledSprite() {
		super();
	}

	// Setters and getters for player heath.
	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double playerHealth) {
		this.playerHealth = playerHealth;
	}

	// Sets direction to controls sprite movements.
	public void setDirection(Direction d) {
		switch (d.getOrientation()) {
		case 0:
		case 1:
			super.setImageIndex(0); // UP or DOWN
			break;
		case 2:
			super.setImageIndex(1); // LEFT
			break;
		case 3:
			super.setImageIndex(2); // LEFT
		default:
			break; // Ignore...
		}
	}

	// Checks if player is alive by checking if his health value is greater then 0.
	public boolean isAlive() {
		if (playerHealth > 0)
			return true;
		else
			return false;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}