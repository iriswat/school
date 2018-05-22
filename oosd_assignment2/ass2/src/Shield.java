import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
/**
 * <h1> Shield </h1>
 * <h2> Protects player from losing lives when encounter to enemy/ememy shot </h2>
 * 
 * Handles shield's functionality
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */

public class Shield {
	private final static String SHIELD_PATH = "res/shield.png";
	private float x;
	private float y;
	private boolean active;
	private Image shield_img;
	
	/**
	 * Constructor for shield
	 * @param x The x position of shield
	 * @param y The y position of shield
	 */
	public Shield(float x, float y) {
		try {
			shield_img = new Image(SHIELD_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
		this.active = true;
	}
	
	/**
	 * Update the shield game state for a frame
	 * @param x Latest x position of the shield 
	 * @param y Latest y position of the shield 
	 */
	public void update(float x, float y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * Deactivate the shield's active status 
	 */
	public void deactivate() {
		this.active = false;
	}
	
	/**
	 * Activate the shield's active status 
	 */
	public void activate() {
		this.active = true;
	}
	
	/**
	 * Set the shield's x position
	 * @param x Shield's x position
	 */
	public void setX (float x) {
		this.x = x;
	}
	
	/**
	 * Set the shield's y position
	 * @param y Shield's y position
	 */
	public void setY (float y) {
		this.y = y;
	}
	
	/**
	 * Getter of the shield's active status
	 * @return Shield's active status
	 */
	public boolean getActive() {
		return this.active;
	}
	
	/** Render the most updated shield
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g) {
		if (this.active) {
			shield_img.draw(x, y);
			
		}
		
	}

}
