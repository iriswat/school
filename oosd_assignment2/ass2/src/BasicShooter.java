import java.util.Timer;

import org.newdawn.slick.Input;
/**
 * <h1> Basic Shooter </h1>
 * <h2> A type of enemy that is able to shoot the player</h2>
 * 
 * Handles Basic Shooter's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class BasicShooter extends Enemy{
	private final static String BASIC_SHOOTER_PATH = "res/basic-shooter.png";
	private   final static float BASIC_SHOOTER_SPEED = 0.2f;
	private final static int FIRE_INTERVAL = 3500;
	// whether it's first shoot or not
	private boolean firstShoot;
	private int fire_timer = 0;
	private int randomY;
	
	/**
	 * Constructor for Basic Shooter 
	 * @param x The x position of Basic Shooter
	 * @param y The y position of Basic Shooter
	 * @param delay The delay value of Basic Shooter
	 */
	public BasicShooter(float x, float y, int delay) {
		super(BASIC_SHOOTER_PATH,x, y, delay);
		this.firstShoot = true;
		//find a random value between 48 to 464
		this.randomY = Boss.getRandomIntegerBetweenRange(48,464);
		this.speed = BASIC_SHOOTER_SPEED;
	}
	
	// shoot every fire interval
	private void fire() {
		if (firstShoot) {
			World.getInstance().addSprite(new EnemyLaserShot(getX(), getY()));
			firstShoot = false;
		}
		else {
			if (fire_timer > FIRE_INTERVAL) {
				World.getInstance().addSprite(new EnemyLaserShot(getX(), getY()));
				fire_timer = 0;
			}
		}
	}
	
	/** 
	 * Update the Basic Shooter's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		// stop moving and start shooting when reaches the random value 
		if (getY() >= randomY) {
			// offset the background scrolling speed
			move(0, -World.BACKGROUND_SCROLL_SPEED);
			fire_timer += 1;
			fire();
		}
	}
}
