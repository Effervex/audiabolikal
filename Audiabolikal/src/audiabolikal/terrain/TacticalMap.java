package audiabolikal.terrain;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.math.Vector2f;

import audiabolikal.Globals;
import audiabolikal.terrain.feature.TerrainFeature;
import audiabolikal.terrain.feature.TerrainFeatureEnum;

/**
 * A tactical grid based map on which battle takes place.
 * 
 * @author Samuel J. Sarjant
 */
public class TacticalMap {
	private static final double BUMPINESS_SD = .4;
	/**
	 * The number of times a new feature is attempted to be added before giving
	 * up.
	 */
	private static final int FEATURE_ADD_TIMEOUT = 3;
	private static final double FEATURES_PER_AREA_MEAN = 0.25;
	private static final double FEATURES_PER_AREA_SD = 0.1;
	private static final int MAP_SIZE_MEAN = 16;
	private static final int MAP_SIZE_SD = 3;
	private static final int MAX_BUMP_ORIGINS = 5;
	private static final float ORIGIN_BUFFER = 10;
	private static final double SLOPE_CURVE_EXP_SD = .003;
	private static final double SLOPE_MEAN = Math.PI / 3;
	private static final double SLOPE_SD = Math.PI / 24;
	private static final double SLOPE_ROLL_SD = Math.PI / 18;
	private static final double VALLEY_SIZE_MEAN = .3;
	private static final double VALLEY_SIZE_SD = .25;
	/** The ratio of tile size to tile height. */
	public static final double TILE_RATIO = 3;

	/** The features of the terrain. */
	private TerrainFeature[] features_;

	/** The terrain of the level. */
	private int[][] terrain_;

	/** The rendered theme of the level. */
	private TerrainTheme theme_;

	/**
	 * Generates a new tactical map with default theme using the given seed.
	 * 
	 * @param seed
	 *            The seed value for the map.
	 */
	public TacticalMap(int seed) {
		generateMap(seed);
		theme_ = TerrainTheme.DEFAULT;
	}

	/**
	 * Applies bumpiness to terrain by selecting a number of focal points from
	 * which random bumpiness emanates.
	 * 
	 * @param terrain
	 *            The terrain to affect.
	 * @param sizeX
	 *            The x size.
	 * @param sizeZ
	 *            The z size.
	 * @param bumpiness
	 *            The bumpiness shift for each square from the origin.
	 */
	private void applyBumpiness(int[][] terrain, int sizeX, int sizeZ,
			double bumpiness) {
		// TODO Auto-generated method stub

	}

	/**
	 * Applies textured tiles to the terrain (to be filled in with whatever
	 * textures are available).
	 * 
	 * @param terrain
	 *            The terrain.
	 * @param waterLevel
	 *            The current water level.
	 */
	private void applyTextureTypes(int[][] terrain, int waterLevel) {
		// TODO Auto-generated method stub

	}

	/**
	 * Checks that each square of the terrain is reachable and modifies it
	 * appropriately.
	 * 
	 * @param terrain
	 *            The terrain to check.
	 */
	private void checkTerrain(int[][] terrain) {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds features to the terrain of the level.
	 */
	public void addFeatures(double averageAxis) {
		int numFeatures = (int) Math.round(averageAxis
				* (FEATURES_PER_AREA_MEAN + Globals.random_.nextGaussian()
						* FEATURES_PER_AREA_SD));
		Collection<TerrainFeature> features = new ArrayList<TerrainFeature>();
		// Add the features
		for (int i = 0; i < numFeatures; i++) {
			int timeout = FEATURE_ADD_TIMEOUT;
			TerrainFeature feature = null;
			do {
				// Get a random feature
				feature = TerrainFeatureEnum.values()[Globals.random_
						.nextInt(TerrainFeatureEnum.values().length)]
						.getFeature();
				timeout--;

				// Attempt to apply the feature
			} while (feature.applyFeature(terrain_) && timeout >= 0);
			features.add(feature);
		}
		features_ = features.toArray(new TerrainFeature[features.size()]);
	}

	/**
	 * Generates the terrain using a biome.
	 * 
	 * @param geography
	 *            The geographical layout of the level.
	 * @param sizeX
	 *            The x size of the terrain.
	 * @param sizeZ
	 *            The z size of the terrain,
	 */
	public int[][] generateGeography(TerrainGeography geography, int sizeX,
			int sizeZ) {

		Vector2f midMap = new Vector2f(sizeX / 2, sizeZ / 2);
		// Slope angle
		double slopeAngle = SLOPE_MEAN + Globals.random_.nextGaussian()
				* SLOPE_SD;
		// Slope curve
		double slopeCurve = 1 + Globals.random_.nextGaussian()
				* SLOPE_CURVE_EXP_SD;
		// Slope direction
		float slopeDirection = (float) (Math.PI * 2 * Globals.random_
				.nextDouble());

		// Valley/Ridge sizes
		double sizeA = Math.max(1,
				VALLEY_SIZE_MEAN + Globals.random_.nextGaussian()
						* VALLEY_SIZE_SD);
		double sizeB = Math.max(1,
				VALLEY_SIZE_MEAN + Globals.random_.nextGaussian()
						* VALLEY_SIZE_SD);
		double sizeC = Math.max(1,
				VALLEY_SIZE_MEAN + Globals.random_.nextGaussian()
						* VALLEY_SIZE_SD);

		int[][] terrain = new int[sizeX][sizeZ];
		// Set up the terrain
		switch (geography) {
		case PLAIN:
			// No need to do anything
			break;
		case SLOPE:
			// Slope can be exponential
			boolean decreasing = Globals.random_.nextBoolean();
			// Select an origin point of variable distance to modify curvature.
			Vector2f originBuffer = new Vector2f(Globals.random_.nextFloat()
					* ORIGIN_BUFFER * sizeX, Globals.random_.nextFloat()
					* ORIGIN_BUFFER * sizeZ);
			float distBuffer = originBuffer.length();
			Vector2f slopeOrigin = originBuffer.add(midMap);
			// Vector2f slopeOrigin = new Vector2f(-sizeX / 2, -sizeZ / 2);
			//slopeOrigin.rotateAroundOrigin(slopeDirection, true);
			slopeOrigin.addLocal(sizeX / 2, sizeZ / 2);
			Vector2f slopeLine = new Vector2f(sizeX / 2, sizeZ / 2)
					.subtract(slopeOrigin);
			double slopeRoll = Globals.random_.nextGaussian() * SLOPE_ROLL_SD;

			for (int x = 0; x < sizeX; x++) {
				for (int z = 0; z < sizeZ; z++) {
					float distFromOrigin = Globals.distance(slopeOrigin.x,
							slopeOrigin.y, x, z) - distBuffer;
					double curveAngle = slopeAngle
							* Math.pow(slopeCurve, distFromOrigin);
					int coeff = (decreasing) ? 1 : -1;
					terrain[x][z] = (int) (coeff * Math.tan(curveAngle)
							* (distFromOrigin) / TILE_RATIO);

					// Adjust the roll
					float distFromLine = Globals.pointLineDist2f(new Vector2f(
							x, z), slopeOrigin, slopeLine);
					terrain[x][z] += (int) (Math.tan(slopeRoll) * distFromLine);
				}
			}
		}

		// Determine the bumpiness
		double bumpiness = Globals.random_.nextGaussian() * BUMPINESS_SD;
		applyBumpiness(terrain, sizeX, sizeZ, bumpiness);
		// Water level
		int waterLevel = 0;

		// Applies the texture types to the terrain (roads, different ground
		// types, etc.)
		applyTextureTypes(terrain, waterLevel);

		// Checks that the terrain is valid and all reachable.
		checkTerrain(terrain);

		return terrain;
	}

	/**
	 * The heart of the TacticalMap class. This method generates un-textured
	 * terrain from a given seed.
	 * 
	 * @param seed
	 *            The seed of the map.
	 */
	public void generateMap(int seed) {
		int sizeX = (int) Math.round(MAP_SIZE_MEAN
				+ Globals.random_.nextGaussian() * MAP_SIZE_SD);
		int sizeZ = (int) Math.round(MAP_SIZE_MEAN
				+ Globals.random_.nextGaussian() * MAP_SIZE_SD);
		// First, determine the biome of the map
		TerrainGeography geography = TerrainGeography.values()[(int) (Globals.random_
				.nextDouble() * TerrainGeography.values().length)];
		terrain_ = generateGeography(geography, sizeX, sizeZ);

		// Add features to it
		// addFeatures((sizeX + sizeZ) / 2);
	}
}
