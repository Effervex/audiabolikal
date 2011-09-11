package audiabolikal.terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

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
	private static final Vector2f[] DIRECTIONS = { new Vector2f(-1, 0),
			new Vector2f(1, 0), new Vector2f(0, 1), new Vector2f(0, -1) };
	private static final float BUMPINESS_SD = .4f;
	/**
	 * The number of times a new feature is attempted to be added before giving
	 * up.
	 */
	private static final int FEATURE_ADD_TIMEOUT = 3;
	private static final double FEATURES_PER_AREA_MEAN = 0.25;
	private static final double FEATURES_PER_AREA_SD = 0.1;
	private static final int MAP_SIZE_MEAN = 16;
	private static final int MAP_SIZE_SD = 3;
	private static final float ORIGIN_BUFFER = 250;
	private static final double SLOPE_CURVE_EXP_SD = .005;
	private static final double SLOPE_MEAN = Math.PI / 3;
	private static final double SLOPE_SD = Math.PI / 24;
	private static final double SLOPE_ROLL_SD = Math.PI / 6;
	private static final double VALLEY_SIZE_MEAN = 0;
	private static final double VALLEY_SIZE_SD = .05;
	private static final double VALLEY_CURVE_MAX = Math.PI / 8;
	/** The points in the valley to generate new slopes for [1-4]. */
	private static final int MAX_VALLEY_POINTS = 6;
	private static final double WATER_LEVEL_MEAN = 2;
	private static final double WATER_LEVEL_SD = 2;
	private static final float ISLAND_HEIGHT_MEAN = 7;
	private static final float ISLAND_HEIGHT_SD = 1f;
	private static final float ISLAND_SLOPE_SD = .8f;
	private static final float ISLAND_ORIGIN_BUFFER = 1.25f;
	private static final float MIN_ISLANDS = .25f;
	private static final float MAX_ISLANDS = .35f;
	private static final float ISLAND_SPREAD = 2;
	/** The ratio of tile size to tile height. */
	public static final float TILE_RATIO = 3;
	private static final double CLIFF_FACE_SD = Math.PI / 36;
	private static final double CLIFF_FACE_VARIATION_SD = Math.PI / 36;
	private static final float CLIFF_SPLIT_SD = .1f;
	private static final float CANYON_WIDTH = 0.333f;

	/** The features of the terrain. */
	private TerrainFeature[] features_;

	/** The terrain of the level. */
	private int[][] terrain_;

	/** The lowest point of the terrain. */
	private int lowestPoint_;

	private TerrainTexture[][] terrainTextures_;

	/** The rendered theme of the level. */
	private TerrainTheme theme_;

	/** The height of the water on this level. */
	private int waterHeight_;

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
	 *            The average bumpiness shift for each square from the origin.
	 */
	private int applyBumpiness(int[][] terrain, int sizeX, int sizeZ,
			float bumpiness) {
		Vector2f origin = new Vector2f(Globals.random_.nextInt(sizeX),
				Globals.random_.nextInt(sizeZ));
		LinkedList<Vector2f> points = new LinkedList<Vector2f>();
		points.add(origin);
		Float[][] bumpMap = new Float[sizeX][sizeZ];

		// Loop through every point.
		while (!points.isEmpty()) {
			Vector2f point = points.pop();
			// Note neighbour values and add neighbours to the points.
			float average = 0;
			int numAdjacent = 0;
			for (Vector2f dir : DIRECTIONS) {
				Vector2f adjacent = point.add(dir);
				if (adjacent.x >= 0 && adjacent.x < sizeX && adjacent.y >= 0
						&& adjacent.y < sizeZ) {
					if (bumpMap[(int) adjacent.x][(int) adjacent.y] == null) {
						if (!points.contains(adjacent))
							points.add(adjacent);
					} else {
						average += bumpMap[(int) adjacent.x][(int) adjacent.y];
						numAdjacent++;
					}
				}
			}

			// Apply the bumpiness
			if (numAdjacent > 0)
				average /= numAdjacent;
			bumpMap[(int) point.x][(int) point.y] = average
					+ Globals.randomCoeff() * bumpiness;
		}

		// Apply the bumpiness to the terrain.
		int minValue = Integer.MAX_VALUE;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				terrain[x][z] += Math.round(bumpMap[x][z]);
				minValue = Math.min(terrain[x][z], minValue);
			}
		}
		return minValue;
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
		case ISLANDS:
			lowestPoint_ = generateIslands(sizeX, sizeZ, terrain);
			break;
		case CLIFF:
			lowestPoint_ = generateCliff(sizeX, sizeZ, terrain);
			break;
		case CANYON:
			lowestPoint_ = generateCanyon(sizeX, sizeZ, terrain);
			break;
		}

		// Determine the bumpiness
		float bumpiness = Globals.randomGaussian() * BUMPINESS_SD;
		lowestPoint_ = Math.min(lowestPoint_,
				applyBumpiness(terrain, sizeX, sizeZ, bumpiness));
		// Water level
		waterHeight_ = lowestPoint_
				+ (int) (WATER_LEVEL_MEAN + Globals.randomGaussian()
						* WATER_LEVEL_SD);

		// Applies the texture types to the terrain (roads, different ground
		// types, etc.)
		applyTextureTypes(terrain, waterHeight_);

		// Checks that the terrain is valid and all reachable.
		checkTerrain(terrain, lowestPoint_);

		return terrain;
	}

	/**
	 * Generates a canyon. Basically a two-sided cliff.
	 * 
	 * @param sizeX
	 *            The level x size.
	 * @param sizeZ
	 *            The level z size.
	 * @param terrain
	 *            The terrain to fill.
	 * @return The minimum value of the terrain.
	 */
	private int generateCanyon(int sizeX, int sizeZ, int[][] terrain) {
		double[] cliffFaceAngle = {
				Math.PI / 2 
						- Math.abs(Globals.randomGaussian() * CLIFF_FACE_SD),
				Math.PI / 2 
						- Math.abs(Globals.randomGaussian() * CLIFF_FACE_SD) };
		double[] slopeAngles = {
				SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD,
				SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD };
		double[] slopeCurves = {
				1 + Globals.randomGaussian() * SLOPE_CURVE_EXP_SD,
				1 + Globals.randomGaussian() * SLOPE_CURVE_EXP_SD };
		int joinPoint = (Globals.random_.nextBoolean()) ? 0 : sizeZ;
		int[] cliffSplitPoint = {
				Math.round(sizeX * CANYON_WIDTH + Globals.randomGaussian()
						* CLIFF_SPLIT_SD / 2 * sizeX),
				Math.round(sizeX * (1 - CANYON_WIDTH) + Globals.randomGaussian()
						* CLIFF_SPLIT_SD / 2 * sizeX) };
		Vector2f[] cliffLineOrigin = {
				new Vector2f(cliffSplitPoint[0], joinPoint),
				new Vector2f(cliffSplitPoint[1], joinPoint) };
		Vector2f cliffLineDir = new Vector2f(0, sizeZ);
		Vector2f slopeLineDir = new Vector2f(sizeX, 0);

		// Run through each cell
		int minValue = 0;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				Vector2f thisPoint = new Vector2f(x, z);

				// Two slopes (decreasing in the middle)
				double slopeAngle = 0;
				double slopeCurve = 0;
				boolean decreasing;
				if (x >= cliffSplitPoint[0] && x < cliffSplitPoint[1]) {
					slopeAngle = slopeAngles[0];
					slopeCurve = slopeCurves[0];
					decreasing = true;
				} else {
					slopeAngle = slopeAngles[1];
					slopeCurve = slopeCurves[1];
					decreasing = false;
				}

				float slopeDist = Math.abs(Globals.pointLineDist2f(thisPoint,
						cliffLineOrigin[0], slopeLineDir));
				double curveAngle = slopeAngle
						* Math.pow(slopeCurve, slopeDist);
				int coeff = (decreasing) ? -1 : 1;
				terrain[x][z] = (int) (coeff * Math.tan(curveAngle)
						* (slopeDist) / TILE_RATIO);

				// Update the minValue
				minValue = Math.min(minValue, terrain[x][z]);

				// Modify height from cliff face angle (if angle is less than 90
				// degrees
				for (int i = 0; i < 2; i++) {
					if (cliffFaceAngle[i] < Math.PI / 2) {
						float cliffDist = Globals.pointLineDist2f(
								thisPoint.addLocal(new Vector2f(.5f, 0f)),
								cliffLineOrigin[i], cliffLineDir);
						if (i == 0)
							cliffDist *= -1;
						int height = (int) Math.round(Math.tan(cliffFaceAngle[i]
								+ CLIFF_FACE_VARIATION_SD
								* Globals.randomGaussian())
								* cliffDist);
						// If the cliff face is less than the terrain either
						// side.
						if (Math.abs(height) < Math.abs(terrain[x][z])) {
							terrain[x][z] = height;
						}
					}
				}
			}
		}
		return minValue;
	}

	/**
	 * Generates a cliff running along the x axis. Cliffs are non-angled in
	 * orientation. Each cliff is made up of 2 angled segments, joined at an
	 * end. A cliff face may be angled (but not by much).
	 * 
	 * @param sizeX
	 *            The level x size.
	 * @param sizeZ
	 *            The level y size.
	 * @param terrain
	 *            The terrain to fill.
	 * @return The minimum value of the terrain.
	 */
	private int generateCliff(int sizeX, int sizeZ, int[][] terrain) {
		double cliffFaceAngle = Math.PI / 2
				- Math.abs(Globals.randomGaussian() * CLIFF_FACE_SD);
		double[] slopeAngles = {
				SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD,
				SLOPE_MEAN + Globals.randomGaussian() * SLOPE_SD };
		double[] slopeCurves = {
				1 + Globals.randomGaussian() * SLOPE_CURVE_EXP_SD,
				1 + Globals.randomGaussian() * SLOPE_CURVE_EXP_SD };
		int joinPoint = (Globals.random_.nextBoolean()) ? 0 : sizeZ;
		int cliffSplitPoint = Math.round(sizeX / 2f + Globals.randomGaussian()
				* CLIFF_SPLIT_SD * sizeX);
		Vector2f cliffLineOrigin = new Vector2f(cliffSplitPoint, joinPoint);
		Vector2f cliffLineDir = new Vector2f(0, sizeZ);
		Vector2f slopeLineDir = new Vector2f(sizeX, 0);

		// Run through each cell
		int minValue = 0;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				Vector2f thisPoint = new Vector2f(x, z);

				// Two slopes
				double slopeAngle = 0;
				double slopeCurve = 0;
				boolean decreasing;
				if (x < cliffSplitPoint) {
					slopeAngle = slopeAngles[0];
					slopeCurve = slopeCurves[0];
					decreasing = true;
				} else {
					slopeAngle = slopeAngles[1];
					slopeCurve = slopeCurves[1];
					decreasing = false;
				}

				float slopeDist = Math.abs(Globals.pointLineDist2f(thisPoint,
						cliffLineOrigin, slopeLineDir));
				double curveAngle = slopeAngle
						* Math.pow(slopeCurve, slopeDist);
				int coeff = (decreasing) ? -1 : 1;
				terrain[x][z] = (int) (coeff * Math.tan(curveAngle)
						* (slopeDist) / TILE_RATIO);

				// Update the minValue
				minValue = Math.min(minValue, terrain[x][z]);

				// Modify height from cliff face angle (if angle is less than 90
				// degrees
				if (cliffFaceAngle < Math.PI / 2) {
					float cliffDist = Globals.pointLineDist2f(
							thisPoint.addLocal(new Vector2f(.5f, 0f)),
							cliffLineOrigin, cliffLineDir);
					int height = (int) Math.round(Math.tan(cliffFaceAngle
							+ CLIFF_FACE_VARIATION_SD
							* Globals.randomGaussian())
							* cliffDist);
					// If the cliff face is less than the terrain either side.
					if (Math.abs(height) < Math.abs(terrain[x][z])) {
						terrain[x][z] = height;
					}
				}
			}
		}
		return minValue;
	}

	/**
	 * Generates a cluster of islands of variable height, shape and location
	 * 
	 * @param sizeX
	 *            The level x size.
	 * @param sizeZ
	 *            The level z size.
	 * @param terrain
	 *            The terrain to fill.
	 * @return The minimum value of the terrain.
	 */
	private int generateIslands(int sizeX, int sizeZ, int[][] terrain) {
		float avDimension = (sizeX + sizeZ) / 2f;
		int numIslands = (int) (avDimension * MIN_ISLANDS + Globals.random_
				.nextFloat() * (MAX_ISLANDS - MIN_ISLANDS));
		ArrayList<Vector2f> origins = new ArrayList<Vector2f>(numIslands);
		// Spread the origins of the islands enough
		double sumDistance = 0;
		do {
			sumDistance = 0;
			origins.clear();
			for (int i = 0; i < numIslands; i++) {
				Vector2f origin = new Vector2f(
						Globals.random_
								.nextInt((int) (sizeX * ISLAND_ORIGIN_BUFFER)),
						Globals.random_
								.nextInt((int) (sizeZ * ISLAND_ORIGIN_BUFFER)));
				origins.add(origin);
				if (i > 0)
					sumDistance += origin.distance(origins.get(i - 1));
			}
			sumDistance += origins.get(0).distance(origins.get(numIslands - 1));
		} while (sumDistance < avDimension * ISLAND_SPREAD || numIslands <= 1);

		// Generate different island points, building them up like bumpiness is
		// built by using a queue of points. Cease building if the landmass
		// drops below a given point.
		for (Vector2f origin : origins) {
			float[][] heightMap = new float[terrain.length * 2][terrain[0].length * 2];
			LinkedList<Vector2f> points = new LinkedList<Vector2f>();
			points.add(origin);

			// Continue to update terrain while height is above previous value.
			boolean isOrigin = true;
			while (!points.isEmpty()) {
				Vector2f point = points.pop();
				// Note neighbour values and add neighbours to the points.
				float average = 0;
				int numAdjacent = 0;
				Collection<Vector2f> adjacents = new LinkedList<Vector2f>();
				for (Vector2f dir : DIRECTIONS) {
					Vector2f adjacent = point.add(dir);
					if (adjacent.x >= 0
							&& adjacent.x < sizeX * ISLAND_ORIGIN_BUFFER
							&& adjacent.y >= 0
							&& adjacent.y < sizeZ * ISLAND_ORIGIN_BUFFER) {
						if (heightMap[(int) adjacent.x][(int) adjacent.y] > 0) {
							average += heightMap[(int) adjacent.x][(int) adjacent.y];
							numAdjacent++;
						} else if (heightMap[(int) adjacent.x][(int) adjacent.y] == 0
								&& !points.contains(adjacent)) {
							adjacents.add(adjacent);
						}
					}
				}

				// Add points to explore
				if (isOrigin || numAdjacent > 0)
					points.addAll(adjacents);

				// Create the downhill slope
				if (numAdjacent > 0) {
					average /= numAdjacent;
					float slopeDrop = Math.abs(Globals.randomGaussian())
							* ISLAND_SLOPE_SD;
					heightMap[(int) point.x][(int) point.y] = average
							- slopeDrop;
					if (heightMap[(int) point.x][(int) point.y] <= 0)
						heightMap[(int) point.x][(int) point.y] = Integer.MIN_VALUE;
				}

				if (isOrigin) {
					heightMap[(int) point.x][(int) point.y] = ISLAND_HEIGHT_MEAN
							+ Globals.randomGaussian() * ISLAND_HEIGHT_SD;
				}
				isOrigin = false;

				// Apply height map to terrain (making the conversion between
				// the larger map for the islands)
				float buffer = (ISLAND_ORIGIN_BUFFER - 1) / 2;
				if (point.x >= sizeX * buffer && point.x < sizeX * (1 + buffer)
						&& point.y >= sizeZ * buffer
						&& point.y < sizeZ * (1 + buffer)) {
					int shiftX = (int) (point.x - sizeX * buffer);
					int shiftY = (int) (point.y - sizeZ * buffer);
					terrain[shiftX][shiftY] = Math
							.max(terrain[shiftX][shiftY],
									(int) Math
											.round(heightMap[(int) point.x][(int) point.y]));
				}
			}
		}
		return 0;
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
		int numValleyPoints = Globals.random_.nextInt(MAX_VALLEY_POINTS - 1) + 2;
		Vector2f[] valleyPoints = new Vector2f[numValleyPoints];
		float[] valleyWidths = new float[numValleyPoints];
		float[] slopeAngle = new float[numValleyPoints * 2];
		float[] slopeCurves = new float[numValleyPoints * 2];
		// Valley direction
		float[] valleyDirection = new float[numValleyPoints];
		int valleyRidgeCoeff = (type == TerrainGeography.VALLEY) ? 1 : -1;
		Vector2f pointLength = new Vector2f(sizeX * 1f / (numValleyPoints - 1),
				sizeZ * 1f / (numValleyPoints - 1));

		// Initialise the valley points.
		for (int i = 0; i < numValleyPoints; i++) {
			valleyWidths[i] = (float) Math.max(0,
					(VALLEY_SIZE_MEAN + Globals.randomGaussian()
							* VALLEY_SIZE_SD)
							* (sizeX + sizeZ) / 2f);
			slopeAngle[i * 2] = (float) (SLOPE_MEAN + Globals.randomGaussian()
					* SLOPE_SD);
			slopeAngle[i * 2 + 1] = (float) (SLOPE_MEAN + Globals
					.randomGaussian() * SLOPE_SD);
			slopeCurves[i * 2] = (float) (1 + Globals.randomGaussian()
					* SLOPE_CURVE_EXP_SD);
			slopeCurves[i * 2 + 1] = (float) (1 + Globals.randomGaussian()
					* SLOPE_CURVE_EXP_SD);

			// Curving valley
			if (i == 0) {
				valleyDirection[i] = (float) (FastMath.TWO_PI * Globals.random_
						.nextDouble());
				valleyPoints[i] = new Vector2f(sizeX / 2, sizeZ / 2);
				valleyPoints[i].rotateAroundOrigin(valleyDirection[i], true);
				valleyPoints[i].addLocal(sizeX / 2, sizeZ / 2);
				valleyDirection[i] += FastMath.PI;
			} else {
				valleyDirection[i] = (float) (valleyDirection[i - 1] + Globals
						.randomCoeff() * VALLEY_CURVE_MAX);
				valleyPoints[i] = new Vector2f(pointLength);
				valleyPoints[i].rotateAroundOrigin(valleyDirection[i], true);
				valleyPoints[i].addLocal(valleyPoints[i - 1]);
			}
		}

		// Create the valley/ridge
		int minValue = 0;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				// First determine which points to integrate slope between
				Vector2f thisPoint = new Vector2f(x, z);
				int slopeOrigin = determineSlopeOrigin(thisPoint, valleyPoints);

				// Determine how far along the valley this point is (for valley
				// width)
				Vector2f lineDirection = valleyPoints[slopeOrigin + 1]
						.subtract(valleyPoints[slopeOrigin]);
				Vector2f lineOrigin = valleyPoints[slopeOrigin];
				float lineIntersect = Globals.pointLineIntersect(thisPoint,
						lineOrigin, lineDirection);
				float pointDist = Globals.pointLineDist2f(thisPoint,
						lineOrigin, lineDirection, lineIntersect);

				float interpolatedWidth = FastMath.interpolateLinear(
						lineIntersect, valleyWidths[slopeOrigin],
						valleyWidths[slopeOrigin + 1]);
				// If point close enough to line, it has no height difference.
				if (Math.abs(pointDist) > interpolatedWidth) {
					int whichSlope = (pointDist < 0) ? slopeOrigin
							: slopeOrigin + 1;
					float slopeDist = Math.abs(pointDist) - interpolatedWidth;
					float interpolatedSlope = FastMath.interpolateLinear(
							lineIntersect, slopeAngle[whichSlope],
							slopeAngle[whichSlope + 2]);
					terrain[x][z] = (int) (valleyRidgeCoeff
							* Math.tan(interpolatedSlope) * slopeDist / TILE_RATIO);

					// Curve the slope
					float interpolatedCurve = FastMath.interpolateLinear(
							lineIntersect, slopeCurves[whichSlope],
							slopeCurves[whichSlope + 2]);
					terrain[x][z] += (int) (valleyRidgeCoeff
							* Math.tan(interpolatedCurve) * slopeDist / TILE_RATIO);

					// Update the minValue
					minValue = Math.min(minValue, terrain[x][z]);
				}
			}
		}

		return 0;
	}

	/**
	 * Determines the origin point for calculating the valley slope from a given
	 * x and z coordinate.
	 * 
	 * @param thisPoint
	 *            The current point.
	 * @param valleyPoints
	 *            The valley points. If only two, just returns the first.
	 * @return The index of the origin point.
	 */
	private int determineSlopeOrigin(Vector2f thisPoint, Vector2f[] valleyPoints) {
		// If the valley is only of size 2, return the first one.
		if (valleyPoints.length == 2)
			return 0;

		// Run through each valley point, calculating the two adjacent points
		// nearest to this location.
		int closest = 0;
		double closestDistance = Integer.MAX_VALUE;
		for (int i = 0; i < valleyPoints.length - 1; i++) {
			double thisDist = thisPoint.distance(valleyPoints[i])
					+ thisPoint.distance(valleyPoints[i + 1]);
			if (thisDist < closestDistance) {
				closestDistance = thisDist;
				closest = i;
			}
		}
		return closest;
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
		Vector2f slopeLine = new Vector2f(Globals.random_.nextFloat() * sizeX,
				Globals.random_.nextFloat() * sizeZ).subtract(slopeOrigin);
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
				terrain[x][z] += (int) (Math.tan(slopeRoll) * distFromLine / TILE_RATIO);

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

	public int getWaterHeight() {
		return waterHeight_;
	}
}
