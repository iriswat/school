import org.newdawn.slick.Input;
/**
 * <h1> Player Laser Shot </h1>
 * <h2> laser shot that used by the player and it can destroy enemy </h2>
 * 
 * Handles player laser shot's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */

public class PlayerLaserShot extends LaserShot{
	private final static String SHOT_SPRITE_PATH = "res/shot.png";
	private final float SHOT_SPEED = -3f;
	
	private final static int BASIC_ENEMY_SCORE = 50;
	private final static int BOSS_SCORE = 5000;
	private final static int SINE_ENEMY_SCORE = 100;
	private final static int BASIC_SHOOTER_SCORE = 200;
	
	/**
	 * Constructor for Player laser shot
	 * @param x The x position of player laser shot
	 * @param y The y position of player laser shot
	 */
	public PlayerLaserShot(float x, float y) {
		super(SHOT_SPRITE_PATH, x, y);
	}
	
	/** Update the player laser shot's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		if (!onScreen()) {
			deactivate();
		}
		move(0, SHOT_SPEED);
	}
	
	/**
	 * Handling collision between player laser shot and enemies 
	 * @param other Sprite other than player laser shot and makes contact with it
	 */
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof Enemy && onScreen() && other.onScreen()) {
			// only boss has multiple lives 
			if (!(other instanceof Boss)) {
				deactivate();
				other.deactivate();
				// update the score on the status board
				if (other instanceof BasicEnemy) {
					StatusBoard.playerScore += BASIC_ENEMY_SCORE;
				}
				
				if (other instanceof SineEnemy) {
					StatusBoard.playerScore += SINE_ENEMY_SCORE;
				}
				
				if (other instanceof BasicShooter) {
					StatusBoard.playerScore += BASIC_SHOOTER_SCORE;
				}
			}
			else {
				deactivate();
				Boss.bossLives -= 1;
				if (Boss.bossLives == 0) {
					other.deactivate();
					StatusBoard.playerScore += BOSS_SCORE;
				}
			}
		}

	}

	
}
