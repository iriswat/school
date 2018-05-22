import org.newdawn.slick.Input;
/**
 * <h1> Enemy </h1>
 * <h2> An abstract and supertype class of the four different enemy types  </h2>
 * 
 * Handles Enemy's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public abstract class Enemy extends Sprite {
	/** the number of milliseconds for which the enemy should take no action */
	public int delay;
	/** the timer that keeps track of time (in millisecond) since the an enemy is created */
	public int timer = 0;
	/** Enemy's moving speed */
	public float speed;
	
	/**
	 * Constructor for Enemy
	 * @param imageSrc Enemy image's file location
	 * @param x The x position of Enemy
	 * @param y The y position of Enemy
	 * @param delay The delay value of Enemy
	 */
	public Enemy(String imageSrc, float x, float y, int delay) {
		super(imageSrc, x, y);
		this.delay = delay;
	}
	
	/** 
	 * Update the Enemy's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		timer += 1;
		// enemy only move after the delay finishes 
		if (timer>delay) {
			move(0, this.speed);
		}
	}
}
