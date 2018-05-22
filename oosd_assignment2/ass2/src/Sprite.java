 import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;
/**
 * <h1> Sprite </h1>
 * <h2> All the moving objects (player, enemies, powerups, laser shots) except shield </h2>
 * 
 * Handles All the sprites' behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 * Part of the code is from @author Eleanor McMurtry, University of Melbourne
 *
 */
public class Sprite {
	private Image image = null;
	private float x;
	private float y;
	private BoundingBox bb;
	private boolean active = true;

	
	/**
	 * Constructor for Sprite
	 * @param imageSrc Enemy image's file location
	 * @param x The x position of Sprite
	 * @param y The y position of Sprite
	 */
	public Sprite(String imageSrc, float x, float y) {
		try {
			image = new Image(imageSrc);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
		bb = new BoundingBox(image, x, y);
	}
	
	/**
	 * @return true whenever the sprite is on screen.
	 */
	public boolean onScreen() {
		return x >= 0 && x <= App.SCREEN_WIDTH - bb.getWidth()
			&& y >= 0 && y <= App.SCREEN_HEIGHT - bb.getHeight();
	}
	
	/**
	 *  Forces the sprite to remain on the screen
	 */
	public void clampToScreen() {
		x = Math.max(x, 0);
		x = Math.min(x, App.SCREEN_WIDTH);
		y = Math.max(y, 0);
		y = Math.min(y, App.SCREEN_HEIGHT);
	}
	
	/** 
	 * Update the sprite's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	public void update(Input input, int delta) {
		
	}
	
	
	/**
	 * Render the sprite
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render() {
		image.drawCentered(x, y);
	}
	
	/**
	 * Called whenever this Sprite makes contact with another.
	 */
	public void contactSprite(Sprite other) {
		 
	} 

	/** 
	 * Getter of the sprite's x position
	 * @return x X position of the sprite 
	 */
	public float getX() { return x; }
	
	/** 
	 * Set the sprite's x position 
	 * @param x Sprite's x position 
	 */
	public void setX(float x) {this.x = x;}
	
	/** 
	 * Getter of the sprite's y position
	 * @return y Y position of the sprite 
	 */
	public float getY() { return y; }
	
	/** 
	 * Set the sprite's y position 
	 * @param y Sprite's y position 
	 */
	public void setY(float y) {this.y = y;}
	
	/** 
	 * Getter of the sprite's active status
	 * @return true if the sprite is active
	 */
	public boolean getActive() { return active; }
	
	/** 
	 * Setter to make the sprite inactive
	 */
	public void deactivate() { active = false; }
	
	/** 
	 * Getter of the bounding box of a sprite
	 * @return the new bounding box object 
	 */
	public BoundingBox getBoundingBox() {
		return new BoundingBox(bb);
	}
	
	/** 
	 * Set the sprite and its bounding box's new x and y positions 
	 */
	public void move(float dx, float dy) {
		x += dx;
		y += dy;
		bb.setX(x);
		bb.setY(y);
	}
	
	
}

