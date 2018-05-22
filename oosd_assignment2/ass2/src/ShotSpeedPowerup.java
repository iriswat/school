
public class ShotSpeedPowerup extends Powerup{
	/**
	 * <h1> Shot Speed Powerup </h1>
	 * <h2> A type of powerups that would speed up player's firing interval </h2>
	 * 
	 * Handles shot speed powerup's behaviours
	 * 
	 * Project 2B for SWEN20003: Object Oriented Software Development 2018
	 * @author Un Leng Wat (860803), University of Melbourne
	 * 
	 */
	
	private final static String SHOT_SPEED_POWERUP_PATH = "res/shotspeed-powerup.png";
	
	
	/**
	 * Constructor for shot speed powerup
	 * @param x The x position of shot speed powerup
	 * @param y The y position of shot speed powerup
	 */
	public ShotSpeedPowerup(float x, float y) {
		super(SHOT_SPEED_POWERUP_PATH, x, y);
	}
	
	
}
