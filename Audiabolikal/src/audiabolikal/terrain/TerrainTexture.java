package audiabolikal.terrain;

/**
 * General terrain textures which define the type of texture applied to the
 * terrain. The actual applied texture may not use each one individually.
 * 
 * @author Sam Sarjant
 */
public enum TerrainTexture {
	// The building material.
	BUILDING,
	// The column/tree texture.
	COLUMN,
	// Solid material constructed (Stone tiles, etc).
	SOLID_CONSTRUCT,
	// The natural solid material (natural stone, etc).
	SOLID_NATURAL,
	// The standard terrain of the map (grass, etc).
	STANDARD,
	// Waterside texture.
	WATERSIDE;
}
