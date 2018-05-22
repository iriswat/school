import org.newdawn.slick.Input;
/**
 * <h1> Boss </h1>
 * <h2> A type of enemy that is has multiple lives </h2>
 * 
 * Handles Boss's behaviours
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class Boss extends Enemy{
	
	private final static String BOSS_PATH = "res/boss.png";
	private final static int Y_BOUNDARY = 72;
	private int action_timer;
	private int wait_timer = 0;
	private int fire_timer;
	private int finalFiringTimer = 0;
	private final static int FIRE_INTERVAL = 200;
	private float random_x1;
	private float random_x2;
	
	private final static int BOSS_WAITING_TIME1 = 5000;
	private final static int BOSS_WAITING_TIME2 = 2000;

	private final static float BOSS_SPEED1 = 0.05f;
	private final static float BOSS_SPEED2 = 0.2f;
	private final static float BOSS_SPEED3 = 0.1f;
	
	/** Boss's number of lives*/
	public static int bossLives = 60;
	
	private final static int SHOT_OFFSET1 = -97;
	private final static int SHOT_OFFSET2= -74;
	private final static int SHOT_OFFSET3 = 74;
	private final static int SHOT_OFFSET4 = 97;
	private final static int STOP_TIME = 3000;
	
	/** Find a random value between min and max value 
	 * @param min The min value 
	 * @param max The max value
	 * @return random The random number between the min and max value
	 */
	public static int getRandomIntegerBetweenRange(int min, int max){
	    int random = (int)((Math.random()*((max-min)+1))+min);
	    return random;
	}
	
	/**
	 * Constructor for Boss 
	 * @param x The x position of Boss
	 * @param y The y position of Boss
	 * @param delay The delay value of Boss
	 */
	public Boss(float x, float y, int delay) {
		super(BOSS_PATH,x, y, delay);
		this.action_timer = 0;
		this.random_x1 = getRandomIntegerBetweenRange(128,896);
		this.random_x2 = getRandomIntegerBetweenRange(128,896);
		this.fire_timer = 0;
	}
	
	// shoot out the four-shot pattern bullets for every fire interval
	private void four_shots () {
		if (fire_timer == FIRE_INTERVAL) {
			fire_timer = 0;
		}
		if (fire_timer == 0) {
			float currentX = getX();
			World.getInstance().addSprite(new EnemyLaserShot(currentX+SHOT_OFFSET1, getY()));
			World.getInstance().addSprite(new EnemyLaserShot(currentX+SHOT_OFFSET2, getY()));
			World.getInstance().addSprite(new EnemyLaserShot(currentX+SHOT_OFFSET3, getY()));
			World.getInstance().addSprite(new EnemyLaserShot(currentX+SHOT_OFFSET4, getY()));
		}
	}
	
	/** 
	 * Update the Boss's game state for a frame.
     * @param input The external input (from mouse, keyboard, and controller)
     * @param delta Time passed since last frame (milliseconds)
     */
	@Override
	public void update(Input input, int delta) {
		timer += 1;
		if (timer >= delay) {
			if (getY() >= Y_BOUNDARY && action_timer < BOSS_WAITING_TIME1) {
				action_timer += 1;
				// wait 5000 ms after it reaches the Y_BOUNDARY
				move(0, 0);
			}
			// after 5000 ms, move to random_x1
			else if (getY() >= Y_BOUNDARY && action_timer == BOSS_WAITING_TIME1) {
				// stop when reaches the target position and wait for 2000 ms
				if ((int)getX() == this.random_x1) {
					move(0, 0);
					wait_timer += 1;
					if (wait_timer == BOSS_WAITING_TIME2) {
						action_timer = BOSS_WAITING_TIME1+BOSS_WAITING_TIME2;
					}
				}
				else if (getX() > this.random_x1) {
					move(-BOSS_SPEED3, 0);
				}
				else if (getX() < this.random_x1) {
					move(BOSS_SPEED3, 0);
				}				
				else {
					move(0, 0);	
				}
			}
			
			else if (getY() >= Y_BOUNDARY && action_timer == BOSS_WAITING_TIME1+BOSS_WAITING_TIME2) {
				four_shots();
				fire_timer += 1;
				finalFiringTimer += 1;
				if (finalFiringTimer > STOP_TIME) {
					fire_timer = -1;
				}
				if ((int)getX() == this.random_x2) {
					move(0, 0);
					
				}
				else if (getX() > this.random_x2) {
					move(-BOSS_SPEED2, 0);
				}
				else if (getX() < this.random_x2) {
					move(BOSS_SPEED2, 0);
				}
				else {
					move(0, 0);	
				}
			}
			// move at 0.05 before reaching the Y_BOUNDARY
			else {
				move(0, BOSS_SPEED1);
			}
			
		}
	}
		
}
