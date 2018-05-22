import org.newdawn.slick.Input;
/**
 * <h1> Sine Enemy </h1>
 * <h2> A type of enemy which move in a sine pattern</h2>
 * 
 * Handles Sine Enemy's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class SineEnemy extends Enemy{
	
	private final static String SINE_ENEMY_PATH = "res/sine-enemy.png";
	private final static float SINE_ENEMY_SPEED = 0.15f;
	// Sine Enemy's offset x value which creates the sine movement 
	private float sineEnemyXOffset; 
	// Sine Enemy's original x position
	private float initialX;
	
	/**
	 * Constructor for Sine Enemy 
	 * @param x The x position of Sine Enemy
	 * @param y The y position of Sine Enemy
	 * @param delay The delay value of Sine Enemy
	 */
	public SineEnemy(float x, float y, int delay) {
		super(SINE_ENEMY_PATH,x, y, delay);
		this.initialX = x;
		this.speed = SINE_ENEMY_SPEED;
	}
	
	/** 
	 * Update the Sine Enemy's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		setX(this.initialX);
		this.sineEnemyXOffset = (float) (96*Math.sin(Math.PI*2*(timer-delay)/1500));
		setX(this.initialX+sineEnemyXOffset);
	}
	
}
	


