import org.newdawn.slick.Input;
/**
 * <h1> Enemy Laser Shot </h1>
 * <h2> laser shot that used by the enemies and it can make player lose a life </h2>
 * 
 * Handles enemy laser shot's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class EnemyLaserShot extends LaserShot{
	
	private final static String ENEMY_SHOT_PATH = "res/enemy-shot.png";
	private final float ENEMY_SHOT_SPEED = 0.7f;
	
	/**
	 * Constructor for Enemy laser shot
	 * @param x The x position of enemy laser shot
	 * @param y The y position of enemy laser shot
	 */
	public EnemyLaserShot(float x, float y) {
		super(ENEMY_SHOT_PATH, x, y);
	}
	
	/** Update the enemy laser shot's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		move(0, ENEMY_SHOT_SPEED);
		if (!onScreen()) {
			deactivate();
		}
	}
	
	/**
	 * Handling collision between enemy laser shot and player 
	 * @param other Sprite other than enemy laser shot and makes contact with it
	 */
	@Override
	public void contactSprite(Sprite other) {
		// as the collision between enemy laser shot and player involves the use of shield
		// it is handled in the World Class
	}
}
