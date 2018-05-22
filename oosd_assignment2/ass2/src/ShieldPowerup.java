
public class ShieldPowerup extends Powerup{
	
	/**
	 * <h1> Shield Powerup </h1>
	 * <h2> A type of powerups that would gain player a shield for a period of time </h2>
	 * 
	 * Handles shield powerup's behaviours
	 * 
	 * Project 2B for SWEN20003: Object Oriented Software Development 2018
	 * @author Un Leng Wat (860803), University of Melbourne
	 * 
	 */
	
	private final static String SHOT_SPEED_POWERUP_PATH = "res/shield-powerup.png";
	
	/**
	 * Constructor for shield powerup
	 * @param x The x position of shield powerup
	 * @param y The y position of shield powerup
	 */
	public ShieldPowerup(float x, float y) {
		super(SHOT_SPEED_POWERUP_PATH, x, y);
	}
}
