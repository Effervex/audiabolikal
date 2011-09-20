package audiabolikal.terrain.feature;

import java.util.Collection;

import audiabolikal.terrain.TacticalMap;

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
	/**
	 * The location of the feature (where the location represents a corner of
	 * the structure).
	 */
	private Vector3f location_;
	/** The structure of the feature. */
	protected Collection<Vector3f> structure_;
	/** The size of the terrain. */
	protected Vector3f size_;
	/** The direction that the feature is facing. */
	protected int direction_;

	public TerrainFeature() {
		location_ = new Vector3f(0, 0, 0);
		size_ = new Vector3f(0, 0, 0);
		direction_ = 0;
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
	protected boolean checkValidLocation(TacticalMap tacticalMap,
			Collection<TerrainFeature> features, Vector3f location) {
		if (location.x < 1 - size_.x
				&& location.x >= tacticalMap.getTerrain().length
				&& location.z < 1 - size_.z
				&& location.z >= tacticalMap.getTerrain()[0].length) {
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
		// Find the median of the values that the object covers
		
		// Place the object at that height (rounding down)
		
		// Move any lower terrain up to the median value.
		return null;
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
