package audiabolikal.terrain;

/**
 * The main geographical layout of the level.
 * 
 * @author Sam Sarjant
 */
public enum TerrainGeography {
	// A canyon is basically a 3 part cliff.
	CANYON,
	// A cliff is essentially a large vertical drop between the two levels.
	CLIFF,
	// Islands are a number of high points dotted about an otherwise flat surface.
	ISLANDS,
	// Plain is a flat level.
	PLAIN,
	// Ridge is a peaked line running across the level.
	RIDGE,
	// Slope is an inclined level.
	SLOPE,
	// Valley is a drop running through the level.
	VALLEY;
}
