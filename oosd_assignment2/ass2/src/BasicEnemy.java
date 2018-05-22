import org.newdawn.slick.Input;
/**
 * <h1> Basic Enemy </h1>
 * <h2> A type of enemy </h2>
 * 
 * Handles Basic Enemy's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 * Part of the code is from @author Eleanor McMurtry, University of Melbourne
 */

public class BasicEnemy extends Enemy {

	private final static String BASIC_ENEMY_PATH = "res/basic-enemy.png";
	private final static float BASIC_ENEMY_SPEED = 0.2f;

	/**
	 * Constructor for Basic Enemy 
	 * @param x The x position of Basic Enemy
	 * @param y The y position of Basic Enemy
	 * @param delay The delay value of Basic Enemy
	 */
	public BasicEnemy(float x, float y,int delay) {
		super(BASIC_ENEMY_PATH,x, y, delay);
		this.speed = BASIC_ENEMY_SPEED;
	}
	
	/** 
	 * Update the Basic Enemy's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
	}
	
}
