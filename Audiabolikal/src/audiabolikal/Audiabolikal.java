package audiabolikal;

import com.jme3.app.Application;

/**
 * The main launch class, which handles the global running of the game. From
 * menu, to options, to new games, etc.
 * 
 * @author Samuel J. Sarjant
 */
public class Audiabolikal extends Application {
	/** The resources directory. */
	public static final String RESOURCE_DIR = "resources/";

	/**
	 * A constructor.
	 */
	public Audiabolikal() {
		initialise();
		loadResources();
	}
	
	/**
	 * Loads necessary resources.
	 */
	private void loadResources() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Initialises stuff.
	 */
	private void initialise() {
		
	}

	/**
	 * Runs the game.
	 */
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            The arguments given.
	 */
	public static void main(String[] args) {
		Audiabolikal audiabolikal = new Audiabolikal();
		audiabolikal.run();
	}
}
