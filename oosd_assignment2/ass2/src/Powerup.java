import org.newdawn.slick.Input;
/**
 * <h1> Powerup </h1>
 * <h2> Supertype class of shot-speed powerup class and shield powerup class </h2>
 * 
 * Handles powerup's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class Powerup extends Sprite{
	/** Powerup's in-effect time */
	public static final int POWERUP_LIMIT_TIME = 5000;
	
	// keep track of the time since the powerup is in effect 
	private int timer;
	private static final float POWERUP_SPEED = 0.1f;
	// true if the powerup is in effect 
	private boolean powerupOn = false; 
	
	/**
	 * Constructor for powerup
	 * @param imageSrc powerup's file location
	 * @param x The x position of powerup
	 * @param y The y position of powerup
	 */
	public Powerup(String imageSrc, float x, float y) {
		super(imageSrc, x, y);
	}
	
	/**
	 * Getter of powerOn
	 * @return true if powerOn is true
	 */
	public boolean getPowerOn () {
		return this.powerupOn;
	}

	
	/** 
     * Handle collision between powerup and player
	 * @param other Sprite that is other than powerup and makes contact with it
	 */
	public void contactSprite(Sprite other) {
		if (other instanceof Player) {
			deactivate();
			this.powerupOn = true;
		}
	}
	
	/**
	 * Update the powerup game state for a frame
	 * @param input The external input (from mouse, keyboard, and controller)
	 * @param delta Time passed since last frame (in milliseconds)
	 */
	@Override
	public void update(Input input, int delta) {
		move(0,POWERUP_SPEED);
		if(this.powerupOn) {
			timer += 1;
		}
		if (timer > POWERUP_LIMIT_TIME) {
			this.powerupOn = false;
		}
	}
}
