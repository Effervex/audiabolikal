package audiabolikal.terrain.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import audiabolikal.Globals;
import audiabolikal.terrain.TacticalMap;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A terrain feature which can be added to the tactical map.
 * 
 * @author Sam Sarjant
 */
public abstract class TerrainFeature {
	/** The number of attempts to randomly place */
	public static final int NUM_PLACEMENT_ATTEMPTS = 10;
	private static final float GROUND_FEATURE_MEAN = 0.5f;
	private static final float GROUND_FEATURE_SD = 0.05f;

	/**
	 * The location of the feature (where the location represents a corner of
	 * the structure).
	 */
	private Vector3f location_;
	/** The structure of the feature. */
	private Collection<Vector3f> structure_;
	/** The size of the terrain. */
	private Vector3f size_;
	/** The surface of the feature. */
	private Integer[][] surface_;
	/** The base (bottom) of the feature. */
	private Integer[][] base_;
	/** The highest point of the feature. */
	private int highestPoint_;
	/** The lowest point of the feature. */
	private int lowestPoint_;

	public TerrainFeature() {
		location_ = new Vector3f(0, 0, 0);
		// TODO Extract the other measurements from the structure
		updateStructureMeasurements(initialiseStructure());
	}

	/**
	 * Initialise the structure of the feature. TODO Probably need to change
	 * this to also include texture types.
	 * 
	 * @return The structure of the feature.
	 */
	protected abstract Collection<Vector3f> initialiseStructure();

	/**
	 * Update the measurements about the structure to this feature using the
	 * current structure.
	 * 
	 * @param structure
	 *            The structure to set.
	 */
	protected final void updateStructureMeasurements(
			Collection<Vector3f> structure) {
		structure_ = structure;
		// TODO Auto-generated method stub
		// Set size, surface, base, lowest point, highest point
	}

	/**
	 * Applies the feature to the current terrain, if possible.
	 * 
	 * @param tacticalMap
	 *            The current terrain map.
	 * @param features
	 *            The features already applied.
	 * @return True if the feature was integrated, false otherwise.
	 */
	public abstract boolean applyFeature(TacticalMap tacticalMap,
			Collection<TerrainFeature> features);

	/**
	 * Checks if the attempted placement of a feature is valid at the given
	 * location. All other members of this object are assumed to be defined upon
	 * calling this method.
	 * 
	 * @param tacticalMap
	 *            The current map.
	 * @param features
	 *            The features already applied.
	 * @param location
	 *            The location to test.
	 * @return True if the location is valid and the location is set.
	 */
	protected boolean setValidLocation(TacticalMap tacticalMap,
			Collection<TerrainFeature> features, Vector3f location) {
		if (withinMap((int) location.x, (int) location.z,
				tacticalMap.getTerrain())) {
			return false;
		}
		location_ = location;
		return true;
	}

	/**
	 * Checks if the x,z coords are within the tactical map.
	 * 
	 * @param x
	 *            The x location.
	 * @param z
	 *            The z location.
	 * @param tacticalMap
	 *            The tactical map.
	 * @return True if it is within the bounds of the map.
	 */
	public static boolean withinMap(int x, int z, int[][] tacticalMap) {
		if (x < 0 && x >= tacticalMap.length && z < 0
				&& z >= tacticalMap[0].length) {
			return false;
		}
		return true;
	}

	/**
	 * Grounds the current feature onto the tactical map given 2d coords. The
	 * map can be modified slightly to accommodate the feature.
	 * 
	 * @param location2d
	 *            The 2D location of the feature.
	 * @param tacticalMap
	 *            The current map.
	 * @return A Vector3f location where x and z are equal to the 2D location.
	 */
	protected Vector3f groundFeature(Vector2f location2d,
			TacticalMap tacticalMap) {
		// Find the median of the values that the lowest points of the object
		// cover
		List<Integer> baseTerrain = new ArrayList<Integer>();
		Collection<Vector2f> basePoints = new ArrayList<Vector2f>();
		int[][] terrain = tacticalMap.getTerrain();
		for (int x = 0; x < base_.length; x++) {
			for (int z = 0; z < base_[0].length; z++) {
				Integer baseVal = base_[x][z];
				// If this current point is lowest
				if (baseVal != null && baseVal == lowestPoint_) {
					int xLoc = x + (int) location_.x;
					int zLoc = z + (int) location_.z;
					if (withinMap(x, z, terrain)) {
						baseTerrain.add(baseVal);
						basePoints.add(new Vector2f(x, z));
					}
				}
			}
		}
		Collections.sort(baseTerrain);

		// Find the median value (if evenly sized collection, use the midpoint
		// between the halfway mark)
		float groundPoint = (GROUND_FEATURE_MEAN + Globals.randomGaussian()
				* GROUND_FEATURE_SD)
				* (baseTerrain.size() - 1);
		int median = (int) Math.round(FastMath.interpolateLinear(
				groundPoint % 1, baseTerrain.get((int) groundPoint),
				baseTerrain.get((int) Math.ceil(groundPoint))));

		// Place the object at that height (rounding down)
		Vector3f location = new Vector3f(location2d.x, median + 1, location2d.y);

		// Move any lower terrain up to the median value.
		for (Vector2f points : basePoints) {
			if (terrain[(int) points.x][(int) points.y] < median)
				terrain[(int) points.x][(int) points.y] = median;
		}

		return location;
	}

	/**
	 * Gets the origin point of the feature.
	 * 
	 * @return The origin point of the feature.
	 */
	public Vector3f getLocation() {
		return location_;
	}

	/**
	 * Gets the structure of the feature.
	 * 
	 * @return The structure of the feature.
	 */
	public Collection<Vector3f> getStructure() {
		return structure_;
	}

	/**
	 * Gets the surface height of the feature (where the origin is 0,0,0).
	 * 
	 * @return The surface heights.
	 */
	public int[][] getSurface() {
		// TODO Auto-generated method stub
		return null;
	}
}
