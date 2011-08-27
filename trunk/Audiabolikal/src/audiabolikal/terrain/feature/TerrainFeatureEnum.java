package audiabolikal.terrain.feature;

/**
 * The current features in enum form.
 * 
 * @author Sam Sarjant
 */
public enum TerrainFeatureEnum {
	// A mostly aesthetic feature - just an arch.
	ARCH(new Arch()),
	// A bridge running over a sizable drop in terrain to another point of
	// roughly equal height.
	BRIDGE(new Bridge()),
	// A building of variable size and height.
	BUILDING(new Building()),
	// A chasm of variable size, depth and orientation with a way to get out.
	CHASM(new Chasm()),
	// A column/tree type object of available radius.
	COLUMN(new Column()),
	// A crater in the ground of variable size.
	CRATER(new Crater()),
	// A full map feature which creates many columns at variable density and
	// placement.
	FOREST(new Forest()),
	// A mound on the ground of variable size.
	MOUND(new Mound()),
	// A river which runs through the map, generally sticking the existing
	// terrain valleys.
	RIVER(new River()),
	// A wall of variable width and length running across the map.
	WALL(new Wall());

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
