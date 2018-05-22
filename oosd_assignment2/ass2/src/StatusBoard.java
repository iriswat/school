import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
/**
 * <h1> Status Board </h1>
 * <h2> Show the player's lives and score </h2>
 * 
 * Show up-to-date player statistics (lives and score)
 * 
 * Project 2B for SWEN20003: Object Oriented Software Development 2018
 * @author Un Leng Wat (860803), University of Melbourne
 * 
 */
public class StatusBoard {
	private static final int LIVES_INITIAL_X = 20;
	private static final int LIVES_INITIAL_Y = 696;
	private static final int SCORE_INITIAL_X = 20;
	private static final int SCORE_INITIAL_Y = 738;
	
	private final static String LIVES_PATH = "res/lives.png";
	private Image lives_img;
	
	/** Player's score */
	public static int playerScore = 0;
	
	/**
	 * Constructor for status board
	 */
	public StatusBoard() {
		try {
			lives_img = new Image(LIVES_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Render the status board with up-to-date info
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g) {
		int lives_x = LIVES_INITIAL_X;
		int lives_y = LIVES_INITIAL_Y;
		
		
		for (int i=0; i<World.lives ;i++) {
			g.drawString("Score:"+playerScore, SCORE_INITIAL_X, SCORE_INITIAL_Y);
			lives_img.draw(lives_x, lives_y);
			lives_x += 40;
		}
	}
}
