import org.newdawn.slick.Input;


/**
 * <h1> Player </h1>
 * <h2> the character that player controls </h2>
 * 
 * Handles player's movement, collision, and firing interval
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 * Part of the code is from @author Eleanor McMurtry, University of Melbourne
 */

public class Player extends Sprite {
	/** player's initial x position at the beginning of the game*/
	public final static int PLAYER_INITIAL_X = 512;
	/** player's initial y position at the beginning of the game*/
	public final static int PLAYER_INITIAL_Y = 688;
	
	private final static String PLAYER_SPRITE_PATH = "res/spaceship.png";
	private int powerupTimer = 0;
	private int timer;
	private boolean firstShoot;
	private final float SPEED = 0.5f;
	private final static int PLAYER_SHOOT_INTERVAL = 350;
	private final static int PLAYER_POWERUP_SHOOT_INTERVAL = 150;
	private int fireInterval = PLAYER_SHOOT_INTERVAL;
	

	
	
	/**
	 * Constructor for the player class
	 */
	public Player() {
		super(PLAYER_SPRITE_PATH, PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
		this.firstShoot = true;
	}

	/**
	 * Setter for the firing interval; usually be used when the player makes contact with a shot-speed powerup
	 */
	public void setSpecialFireInterval () {
		this.fireInterval = PLAYER_POWERUP_SHOOT_INTERVAL;
	}

	/** Update the player's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		timer += 1;
		doMovement(input, delta);
		doShooting(input);
		// keep track of the time when a shot speed powerup turns on
		if (this.fireInterval == PLAYER_POWERUP_SHOOT_INTERVAL) {
			powerupTimer += 1;
		}
		// change back to the normal shooting interval after the powerup time limit
		if (powerupTimer > Powerup.POWERUP_LIMIT_TIME) {
			this.fireInterval = PLAYER_SHOOT_INTERVAL;
			powerupTimer = 0;
		}
	}
	
	
	private void doShooting (Input input) {
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			if (firstShoot) {
				World.getInstance().addSprite(new PlayerLaserShot(getX(), getY()));
				firstShoot = false;
			}
			else {
				// no shooting before the interval is done 
				if (timer > this.fireInterval) {
					World.getInstance().addSprite(new PlayerLaserShot(getX(), getY()));
					timer = 0;
				}
			}
		}
	}
	
	private void doMovement(Input input, int delta) {
		// handle horizontal movement
		float dx = 0;
		if (input.isKeyDown(Input.KEY_LEFT)) {
			dx -= SPEED;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			dx += SPEED;
		}

		// handle vertical movement
		float dy = 0;
		if (input.isKeyDown(Input.KEY_UP)) {
			dy -= SPEED;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dy += SPEED;
		}
		
		move(dx * delta, dy * delta);
		clampToScreen();
	}
	
	
	
	/** 
	 * Handle collision between player and other sprites
	 * @param other The sprite that makes contact with the player
	 */
	@Override
	public void contactSprite(Sprite other) {
		if (other instanceof ShotSpeedPowerup) {
			ShotSpeedPowerup shotSpeedPowerup = (ShotSpeedPowerup) other;
			// change the firing interval if there's no shot-speed powerup in effect
			if (!shotSpeedPowerup.getPowerOn()) {
				setSpecialFireInterval();
			}
		}
	}
	
}
