package audiabolikal.terrain.feature;

/**
 * The current features in enum form.
 * 
 * @author Sam Sarjant
 */
public enum TerrainFeatureEnum {
	// A mostly aesthetic feature - just an arch.
	ARCH(null),
	// A bridge running over a sizable drop in terrain to another point of
	// roughly equal height.
	BRIDGE(null),
	// A building of variable size and height.
	BUILDING(new Building()),
	// A column/tree type object of available radius.
	COLUMN(null),
	// A crater in the ground of variable size.
	CRATER(null),
	// A full map feature which creates many columns at variable density and
	// placement.
	FOREST(null),
	// A mound on the ground of variable size.
	MOUND(null),
	// A river which runs through the map, generally sticking the existing
	// terrain valleys.
	RIVER(null),
	// A wall of variable width and length running across the map.
	WALL(null);

	/** The terrain feature. */
	private TerrainFeature feature_;

	/**
	 * Basic constructor.
	 * 
	 * @param feature
	 *            The feature.
	 */
	private TerrainFeatureEnum(TerrainFeature feature) {
		feature_ = feature;
	}

	/**
	 * Gets the feature.
	 * 
	 * @return The feature.
	 */
	public TerrainFeature getFeature() {
		return feature_;
	}
}
