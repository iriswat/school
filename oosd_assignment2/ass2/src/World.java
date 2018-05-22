import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;



/**
 * <h1> World </h1>
 * <h2> where the game happens </h2>
 * 
 * Includes everything in the game: background, sprites, status board, and shield 
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 * Part of the code is from @author Eleanor McMurtry, University of Melbourne
 */

public class World {
	
	/** player's current number of lives*/
	public static int lives;
	/** player's initial number of lives at the beginning of the game*/
	public final static int PLAYER_INITIAL_LIVES = 3;
	/** the game background scrolling speed*/
	public static final float BACKGROUND_SCROLL_SPEED = 0.2f;
	
	private static final String BACKGROUND_PATH = "res/space.png";
	private final static int SHIELD_TIME_LIMIT = 3000;
	private static final int ENEMY_INITIAL_Y = -64;
	private float backgroundOffset = 0;
	private Image background;
	private boolean shieldPowerup = false;
	private boolean shotSpeedPowerup = false;
	private float shieldPowerupX;
	private float shieldPowerupY;
	private float shotSpeedPowerupX;
	private float shotSpeedPowerupY;
	private boolean shieldPowerupOn = false;
	private int shield_timer = 0;
	private static World world;
	private StatusBoard statusBoard;
	private Shield shield;
	private ArrayList<Sprite> sprites = new ArrayList<>();
	
	/**
	 * Getter that returns the world object; create a new one if it does not exist
	 * @return world object
	 */
	public static World getInstance() {
		if (world == null) {
			world = new World();
		}
		return world;
	}
	
	
	
	/**
	 * Add a sprite into the sprites array list
	 * @param sprite The sprite to be added in the array list 
	 */
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	
	/**
	 * Read the type, the x position, and the delay value of the enemies from a text file
	 * and add them in the sprites array list
	 */
	private void readEnemy () {
		try {
			List<String> wave_lines = Files.readAllLines(new File("res/waves.txt").toPath());
			for (String line: wave_lines) {
				// skip lines that start with # (comment lines)
				if (!Objects.equals(line.substring(0,1), "#")) {
					String[] parts = line.split(",");
					String EnemyType = parts[0];
					float EnemyX = Float.parseFloat(parts[1]);
					int EnemyDelay = Integer.parseInt(parts[2]);
					if(Objects.equals(EnemyType, "BasicEnemy")) {
						sprites.add(new BasicEnemy(EnemyX, ENEMY_INITIAL_Y, EnemyDelay));
					}
					else if (Objects.equals(EnemyType, "SineEnemy")) {
						sprites.add(new SineEnemy(EnemyX, ENEMY_INITIAL_Y, EnemyDelay));
					}
					else if (Objects.equals(EnemyType, "BasicShooter")) {
						sprites.add(new BasicShooter(EnemyX, ENEMY_INITIAL_Y,EnemyDelay));
					}
					else if (Objects.equals(EnemyType, "Boss")) {
						sprites.add(new Boss(EnemyX, ENEMY_INITIAL_Y,EnemyDelay));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 *  Constructor for the world class; create a world object
	 */
	public World() {
		try {
			background = new Image(BACKGROUND_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		world = this;
		sprites.add(new Player());
		lives = PLAYER_INITIAL_LIVES;
		statusBoard = new StatusBoard();
		shield = new Shield(Player.PLAYER_INITIAL_X,Player.PLAYER_INITIAL_Y);
		readEnemy();
	}
	
	/**
	 * Handles interactions between sprites, shield, and powerups
	 */
	private void collisionHandling() {
		for (Sprite sprite : sprites) {	
			// update the shield position so it moves along with the player
			if (sprite instanceof Player) {
				shield.update(sprite.getX()-48, sprite.getY()-48);
			}
		
			for (Sprite other : sprites) {
				// handle collisions between sprites
				if (sprite != other && sprite.getBoundingBox().intersects(other.getBoundingBox())) {
					sprite.contactSprite(other);
					
					// handle collision between player and powerups
					// special effects of the powerups are triggered when the player makes contact with powerups 
					if (sprite instanceof Player && other instanceof ShieldPowerup) {
						ShieldPowerup shieldPowerup = (ShieldPowerup) other;
						// activate the powerup and its special effect if there's no shield powerup in effect 
						if (!shieldPowerup.getPowerOn()) {
							shield.activate();
							this.shieldPowerupOn = true;
						}
					}
					
					
					// when player without shield makes contact with enemies or their bullets, player loses a life and activate shield
					if (sprite instanceof Player && (other instanceof Enemy || other instanceof EnemyLaserShot)) {
						if (!shield.getActive()) {
							if (lives > 1) {
								lives -= 1;
								shield.activate();
							}
							else {
								System.exit(0);
							}
						}
					}
					
					// when player's laser shot makes contact with enemy, a powerup might be created 
					if (sprite instanceof PlayerLaserShot && other instanceof Enemy && sprite.onScreen() && other.onScreen()) {
						// only 5% chance that a powerup will be dropped where the enemy was killed 
						if (Boss.getRandomIntegerBetweenRange(1, 100) <= 5) {
							// equal chance of dropping either type of powerup
							if (Boss.getRandomIntegerBetweenRange(1, 2) == 1) {
								shotSpeedPowerup = true;
								shotSpeedPowerupX = other.getX();
								shotSpeedPowerupY = other.getY();
								
							}
							else {
								shieldPowerup = true;
								shieldPowerupX = other.getX();
								shieldPowerupY = other.getY();
								
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Keep track of the time since the powerup in effect; deactivate the powerup and reset timer if the time is up
	 */
	private void PowerupInEffect() {
		
		if (shield.getActive()) {
			shield_timer += 1;
		}
		// different shield time limit with and without the shield powerup 
		if (!this.shieldPowerupOn) {
			if (shield_timer > SHIELD_TIME_LIMIT) {
				shield.deactivate();
				shield_timer = 0;
			}
		}
		else {
			if (shield_timer > Powerup.POWERUP_LIMIT_TIME) {
				shield.deactivate();
				shield_timer = 0;
				this.shieldPowerupOn = false;
			}
		}
	}

	/**
	 *  Add powerups into the sprites array list when player destroys enemy/enemies 
	 */
	private void addPowerups() {	
		if (shotSpeedPowerup) {
			sprites.add(new ShotSpeedPowerup(shotSpeedPowerupX, shotSpeedPowerupY));
			shotSpeedPowerup = false;
		}
		if (shieldPowerup) {
			sprites.add(new ShieldPowerup(shieldPowerupX, shieldPowerupY));
			shieldPowerup = false;
		}
	}

	/**
	 * Update the game state for a frame
	 * @param input The external input (from mouse, keyboard, and controller)
	 * @param delta Time passed since last frame (in milliseconds)
	 */
	public void update(Input input, int delta) {
		// Update all sprites
		for (int i = 0; i < sprites.size(); ++i) {
			sprites.get(i).update(input, delta);
		}
		
		// handle collision in the world
		collisionHandling();
		
		// add powerups into the sprites array list
		addPowerups();
		
		// keep track of all the powerups in effect 
		PowerupInEffect();		
		
		// Clean up inactive sprites
		for (int i = 0; i < sprites.size(); ++i) {
			if (sprites.get(i).getActive() == false) {
				sprites.remove(i);
				// decrement counter to make sure we don't miss any
				--i;
			}
		}

		// keep the background scrolling 
		backgroundOffset += BACKGROUND_SCROLL_SPEED*delta;
		backgroundOffset = backgroundOffset % background.getHeight();
	}

	
	/**
	 * Render everything in the world so it reflects the current game state 
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g) {
		// Tile the background image
		for (int i = 0; i < App.SCREEN_WIDTH; i += background.getWidth()) {
			for (int j = -background.getHeight() + (int)backgroundOffset; j < App.SCREEN_HEIGHT; j += background.getHeight()) {
				background.draw(i, j);
			}
		}
		
		// draw the shield and the status board 
		shield.render(g);
		statusBoard.render(g);
		
		// Draw all sprites
		for (Sprite sprite : sprites) {
			sprite.render();
		}		
	}
}
