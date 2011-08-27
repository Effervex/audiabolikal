package audiabolikal.terrain;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.math.FastMath;
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
	private static final float ORIGIN_BUFFER = 250;
	private static final double SLOPE_CURVE_EXP_SD = .005;
	private static final double SLOPE_MEAN = Math.PI / 3;
	private static final double SLOPE_SD = Math.PI / 24;
	private static final double SLOPE_ROLL_SD = Math.PI / 18;
	private static final double VALLEY_SIZE_MEAN = .3;
	private static final double VALLEY_SIZE_SD = .25;
	private static final double VALLEY_CURVE_MAX = Math.PI / 6;
	private static final double WATER_LEVEL_MEAN = -2;
	private static final double WATER_LEVEL_SD = 2;
	/** The ratio of tile size to tile height. */
	public static final float TILE_RATIO = 3;
	/** The points in the valley to generate new slopes for [1-4]. */
	private static final int VALLEY_POINTS = 2;

	/** The features of the terrain. */
	private TerrainFeature[] features_;

	/** The terrain of the level. */
	private int[][] terrain_;

	/** The lowest point of the terrain. */
	private int lowestPoint_;

	private TerrainTexture[][] terrainTextures_;

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
	 * Generates a new tactical map with a given geography, custom size and
	 * default theme.
	 * 
	 * @param customGeog
	 *            The custom geography.
	 * @param sizeX
	 *            The X size.
	 * @param sizeZ
	 *            The Z size.
	 */
	public TacticalMap(TerrainGeography customGeog, int sizeX, int sizeZ) {
		terrain_ = generateGeography(customGeog, sizeX, sizeZ);
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
	 * @param minValue
	 *            The minimum value of the terrain.
	 */
	private void checkTerrain(int[][] terrain, int minValue) {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds features to the terrain of the level.
	 */
	public void addFeatures(double averageAxis) {
		int numFeatures = (int) Math.round(averageAxis
				* (FEATURES_PER_AREA_MEAN + Globals.randomGaussian()
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

		int[][] terrain = new int[sizeX][sizeZ];
		// Set up the terrain
		switch (geography) {
		case PLAIN:
			// No need to do anything
			lowestPoint_ = 0;
			break;
		case SLOPE:
			lowestPoint_ = generateSlope(sizeX, sizeZ, midMap, terrain);
			break;
		case VALLEY:
			lowestPoint_ = generateValleyRidge(TerrainGeography.VALLEY, sizeX,
					sizeZ, midMap, terrain);
			break;
		case RIDGE:
			lowestPoint_ = generateValleyRidge(TerrainGeography.RIDGE, sizeX,
					sizeZ, midMap, terrain);
			break;
		}

		// Determine the bumpiness
		double bumpiness = Globals.randomGaussian() * BUMPINESS_SD;
		applyBumpiness(terrain, sizeX, sizeZ, bumpiness);
		// Water level
		int waterLevel = lowestPoint_
				+ (int) (WATER_LEVEL_MEAN + Globals.randomGaussian()
						* WATER_LEVEL_SD);

		// Applies the texture types to the terrain (roads, different ground
		// types, etc.)
		applyTextureTypes(terrain, waterLevel);

		// Checks that the terrain is valid and all reachable.
		checkTerrain(terrain, lowestPoint_);

		return terrain;
	}

	/**
	 * Generates a valley/ridge of variable grade, curvature, and direction.
	 * 
	 * @param type
	 *            The type: Valley or Ridge.
	 * @param sizeX
	 *            The level x size.
	 * @param sizeZ
	 *            The level z size.
	 * @param midMap
	 *            The middle of the map.
	 * @param terrain
	 *            The terrain to fill.
	 * @return The minimum value of the terrain.
	 */
	private int generateValleyRidge(TerrainGeography type, int sizeX,
			int sizeZ, Vector2f midMap, int[][] terrain) {
		// Valley/Ridge sizes
		double[] valleyWidths = new double[VALLEY_POINTS];
		double[] slopeAngle = new double[VALLEY_POINTS * 2];
		double[] slopeCurves = new double[VALLEY_POINTS * 2];
		// Valley direction
		float[] valleyDirection = new float[VALLEY_POINTS];

		// Initialise the valley points.
		for (int i = 0; i < VALLEY_POINTS; i++) {
			valleyWidths[i] = Math.max(1,
					VALLEY_SIZE_MEAN + Globals.randomGaussian()
							* VALLEY_SIZE_SD);
			slopeAngle[i] = SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD;
			slopeAngle[i * 2 + 1] = SLOPE_MEAN + Globals.randomGaussian()
					* SLOPE_SD;
			slopeCurves[i] = 1 + Math.abs(Globals.randomGaussian())
					* SLOPE_CURVE_EXP_SD;
			slopeCurves[i * 2 + 1] = 1 + Math.abs(Globals.randomGaussian())
					* SLOPE_CURVE_EXP_SD;
			// Curving valley
			if (i == 0)
				valleyDirection[i] = (float) (Math.PI * 2 * Globals.random_
						.nextDouble());
			else
				valleyDirection[i] = (float) (valleyDirection[i - 1] + (Globals.random_
						.nextDouble() - .5) * VALLEY_CURVE_MAX * 2);
		}

//		int minValue = Integer.MAX_VALUE;
//		for (int x = 0; x < sizeX; x++) {
//			for (int z = 0; z < sizeZ; z++) {
//				float distFromOrigin = Globals.distance(slopeOrigin.x,
//						slopeOrigin.y, x, z) - distBuffer;
//				double curveAngle = slopeAngle
//						* Math.pow(slopeCurve, distFromOrigin);
//				int coeff = (decreasing) ? 1 : -1;
//				terrain[x][z] = (int) (coeff * Math.tan(curveAngle)
//						* (distFromOrigin) / TILE_RATIO);
//
//				// Adjust the roll
//				float distFromLine = Globals.pointLineDist2f(
//						new Vector2f(x, z), slopeOrigin, slopeLine);
//				terrain[x][z] += (int) (Math.tan(slopeRoll) * distFromLine);
//
//				// Update the minValue
//				minValue = Math.min(minValue, terrain[x][z]);
//			}
//		}

		return 0;
	}

	/**
	 * Generates a slope of variable grade, curvature, roll and direction.
	 * 
	 * @param sizeX
	 *            The level x size.
	 * @param sizeZ
	 *            The level z size.
	 * @param midMap
	 *            The middle of the map.
	 * @param terrain
	 *            The terrain to fill.
	 * @return The minimum value of the terrain.
	 */
	private int generateSlope(int sizeX, int sizeZ, Vector2f midMap,
			int[][] terrain) {
		// Slope angle
		double slopeAngle = SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD;
		// Slope curve
		double slopeCurve = 1 + Globals.randomGaussian() * SLOPE_CURVE_EXP_SD;
		// Slope direction
		float slopeDirection = (float) (Math.PI * 2 * Globals.random_
				.nextDouble());

		// Slope can be exponential
		boolean decreasing = Globals.random_.nextBoolean();
		// Select an origin point of variable distance to modify curvature.
		Vector2f originBuffer = new Vector2f(Globals.random_.nextFloat()
				* ORIGIN_BUFFER * sizeX, Globals.random_.nextFloat()
				* ORIGIN_BUFFER * sizeZ);
		float distBuffer = originBuffer.length();
		Vector2f slopeOrigin = originBuffer.add(midMap);
		slopeOrigin.rotateAroundOrigin(slopeDirection, true);
		slopeOrigin.addLocal(sizeX / 2, sizeZ / 2);
		Vector2f slopeLine = new Vector2f(Globals.random_.nextFloat() * sizeX, Globals.random_.nextFloat() * sizeZ)
				.subtract(slopeOrigin);
		double slopeRoll = Globals.randomGaussian() * SLOPE_ROLL_SD;

		int minValue = Integer.MAX_VALUE;
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
				float distFromLine = Globals.pointLineDist2f(
						new Vector2f(x, z), slopeOrigin, slopeLine);
				terrain[x][z] += (int) (Math.tan(slopeRoll) * distFromLine);

				// Update the minValue
				minValue = Math.min(minValue, terrain[x][z]);
			}
		}
		return minValue;
	}

	/**
	 * The heart of the TacticalMap class. This method generates un-textured
	 * terrain from a given seed.
	 * 
	 * @param seed
	 *            The seed of the map.
	 */
	public void generateMap(int seed) {
		// TODO USE THE SEED!
		int sizeX = (int) Math.round(MAP_SIZE_MEAN + Globals.randomGaussian()
				* MAP_SIZE_SD);
		int sizeZ = (int) Math.round(MAP_SIZE_MEAN + Globals.randomGaussian()
				* MAP_SIZE_SD);
		// First, determine the biome of the map
		TerrainGeography geography = TerrainGeography.values()[(int) (Globals.random_
				.nextDouble() * TerrainGeography.values().length)];
		terrain_ = generateGeography(geography, sizeX, sizeZ);

		// Add features to it
		addFeatures((sizeX + sizeZ) / 2);
	}

	public int[][] getTerrain() {
		return terrain_;
	}

	public int getLowestPoint() {
		return lowestPoint_;
	}
}
