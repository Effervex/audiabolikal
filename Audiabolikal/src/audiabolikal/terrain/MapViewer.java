package audiabolikal.terrain;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.shader.Shader;

/**
 * View terrain.
 * 
 * @author Sam Sarjant
 */
public class MapViewer extends SimpleApplication implements ActionListener {
	public static final float TILE_SIZE = 1.5f;
	public static final float TILE_HEIGHT = TILE_SIZE / TacticalMap.TILE_RATIO;

	/** The terrain being viewed. */
	private TacticalMap terrain_;
	private boolean left = false, right = false, up = false, down = false;

	/**
	 * Map viewer is initialised with a map to view.
	 * 
	 * @param terrain
	 *            The map to view.
	 */
	public MapViewer(TacticalMap terrain) {
		terrain_ = terrain;
	}

	@Override
	public void simpleInitApp() {
		setUpBaseTerrain();
		setUpTerrainFeatures();

		// Light
		AmbientLight aLight = new AmbientLight();
		aLight.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(aLight);
		DirectionalLight dLight = new DirectionalLight();
		dLight.setColor(ColorRGBA.White);
		dLight.setDirection(new Vector3f(-2, -2, 3).normalizeLocal());
		rootNode.addLight(dLight);

		setUpKeys();
		setUpCam();
	}

	private void setUpCam() {
		cam.setLocation(new Vector3f(-5,5,-5));
		cam.setDirection(new Vector3f(1,-1,1));
		//cam.setAxes(Vector3f.UNIT_X, Vector3f.UNIT_Y, Vector3f.UNIT_Z);
	}

	/**
	 * Sets up the base terrain.
	 * 
	 * TODO Just does this simply by drawing cubes for every tile from array
	 */
	private void setUpBaseTerrain() {
		int[][] baseTerrain = terrain_.getTerrain();
		int lowestPoint = terrain_.getLowestPoint();
		for (int x = 0; x < baseTerrain.length; x++) {
			for (int z = 0; z < baseTerrain[x].length; z++) {
				// Draw cubes
				for (int y = lowestPoint; y <= baseTerrain[x][z]; y++) {
					Box box = new Box(
							new Vector3f(x * TILE_SIZE, (y - TILE_HEIGHT / 2)
									* TILE_HEIGHT, z * TILE_SIZE),
							TILE_SIZE / 2, TILE_HEIGHT / 2, TILE_SIZE / 2);
					Geometry tile = new Geometry("Tile[" + x + "," + y + ","
							+ z + "]", box);
					// Apply the texture theme
					Material mat1 = new Material(assetManager,
							"Common/MatDefs/Misc/Unshaded.j3md");
					float colour = 0.8f - (y - lowestPoint) * 0.035f;
					mat1.setColor("Color", new ColorRGBA(colour,
							colour, colour, 1f));
					tile.setMaterial(mat1);
					rootNode.attachChild(tile);
				}
			}
		}
	}

	/**
	 * Sets up the terrain features.
	 */
	private void setUpTerrainFeatures() {
		// TODO Auto-generated method stub

	}

	/**
	 * We over-write some navigational key mappings here, so we can add
	 * physics-controlled walking and jumping:
	 */
	private void setUpKeys() {
		inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "Lefts");
		inputManager.addListener(this, "Rights");
		inputManager.addListener(this, "Ups");
		inputManager.addListener(this, "Downs");
	}

	/**
	 * These are our custom actions triggered by key presses. We do not walk
	 * yet, we just keep track of the direction the user pressed.
	 */
	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("Lefts")) {
			left = value;
		} else if (binding.equals("Rights")) {
			right = value;
		} else if (binding.equals("Ups")) {
			up = value;
		} else if (binding.equals("Downs")) {
			down = value;
		}
	}

	/**
	 * Default main method.
	 * 
	 * @param args
	 *            The args (not needed).
	 */
	public static void main(String[] args) {
		MapViewer viewer = new MapViewer(new TacticalMap(
				TerrainGeography.SLOPE, 10, 10));
		viewer.start();
	}
}
