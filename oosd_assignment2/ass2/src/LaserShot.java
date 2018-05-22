import org.newdawn.slick.Input;
/**
 * <h1> Laser Shot </h1>
 * <h2> Abstract and supertype class of enemy laser shot class and player laser shot class </h2>
 * 
 * Handles laser shot's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public abstract class LaserShot extends Sprite {

	/**
	 * Constructor for laser shot
	 * @param imageSrc laser shot's file location
	 * @param x The x position of laser shot
	 * @param y The y position of laser shot
	 */
	public LaserShot(String imageSrc, float x, float y) {
		super(imageSrc, x, y);
	}
	
	/** the abstract method for updating the game state */
	public abstract void update(Input input, int delta);
	
	/** the abstract method for handling collision between laser shot and other sprites */
	public abstract void contactSprite(Sprite other);

}
