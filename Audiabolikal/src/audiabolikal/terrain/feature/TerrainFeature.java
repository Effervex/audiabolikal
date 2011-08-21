package audiabolikal.terrain.feature;

import java.util.Collection;

import com.jme3.math.Vector3f;

/**
 * A terrain feature which can be added to the tactical map.
 * 
 * @author Sam Sarjant
 */
public abstract class TerrainFeature {
	/** The origin location of the feature. */
	protected Vector3f location_;
	/** The structure of the feature. */
	protected Collection<Vector3f> structure_;

	public TerrainFeature() {
		location_ = new Vector3f(0, 0, 0);
	}

	/**
	 * Applies the feature to the current terrain, if possible.
	 * 
	 * @param terrain
	 *            The current terrain.
	 * @return True if the feature was integrated, false otherwise.
	 */
	public abstract boolean applyFeature(int[][] terrain);

	/**
	 * Gets the origin point of the feature.
	 * 
	 * @return The origin point of the feature.
	 */
	public abstract Vector3f getLocation();

	/**
	 * Gets the structure of the feature.
	 * 
	 * @return The structure of the feature.
	 */
	public abstract Collection<Vector3f> getStructure();

	/**
	 * Gets the surface height of the feature (where the origin is 0,0,0).
	 * 
	 * @return The surface heights.
	 */
	public abstract int[][] getSurface();
}
