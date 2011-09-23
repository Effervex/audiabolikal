package audiabolikal.terrain.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import audiabolikal.Globals;
import audiabolikal.terrain.TacticalMap;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * A building object of cuboid construction. Each building can be of multiple
 * storeys and contains one entrance wall (not able to be physically entered).
 * 
 * @author Sam Sarjant
 */
public class Building extends TerrainFeature {
	// BUILDING DIMENSIONS
	private final int MIN_DIMENSION = 3;
	private final int SIZE_MEAN = 5;
	private final int SIZE_SD = 1;
	private final double HEIGHT_SD = .25;
	private final int ONE_STOREY_TILES = (int) Math
			.round(3 / TacticalMap.TILE_HEIGHT);

	// BUILDING PLACEMENT
	private final int MAX_WALL_GAP = 4;
	private final int MIN_FRONT_GAP = 4;
	private final int MAX_FRONT_GAP = 8;

	public Building() {
		super();
		int sizeX = (int) Math.round(Math.max(MIN_DIMENSION, SIZE_MEAN
				+ Globals.random_.nextGaussian() * SIZE_SD));
		int sizeY = (int) (ONE_STOREY_TILES * Math.round(1
				+ Math.abs(Globals.randomGaussian()) * HEIGHT_SD));
		int sizeZ = (int) Math.round(Math.max(MIN_DIMENSION, SIZE_MEAN
				+ Globals.random_.nextGaussian() * SIZE_SD));
		size_ = new Vector3f(sizeX, sizeY, sizeZ);
	}

	@Override
	public boolean applyFeature(TacticalMap tacticalMap,
			Collection<TerrainFeature> features) {
		// Line the building up to an existing building (if any exist)
		List<Building> existingBuildings = new ArrayList<Building>();
		for (TerrainFeature tf : features) {
			if (tf instanceof Building)
				existingBuildings.add((Building) tf);
		}
		// If no existing buildings, just place the building.
		if (!existingBuildings.isEmpty()) {
			boolean result = placeBesideExistingBuilding(tacticalMap, features,
					existingBuildings);
			if (result)
				return true;
		}

		// If no existing buildings or no valid placements next to existing
		// buildings, choose a random location.
		for (int i = 0; i < TerrainFeature.NUM_PLACEMENT_ATTEMPTS; i++) {
			Vector2f location2d = new Vector2f(
					Globals.random_.nextInt(tacticalMap.getTerrain().length
							+ (int) size_.x - 1)
							- (1 - size_.x),
					Globals.random_.nextInt(tacticalMap.getTerrain().length
							+ (int) size_.z - 1)
							- (1 - size_.z));

			Vector3f location = groundFeature(location2d, tacticalMap);
			boolean result = setValidLocation(tacticalMap, features, location);
			if (result)
				return true;
		}

		return false;
	}

	/**
	 * Place this building beside an existing building.
	 * 
	 * @param tacticalMap
	 *            The current map.
	 * @param features
	 *            The existing features.
	 * @param existingBuildings
	 *            The existing buildings.
	 * @return True if the current building was able to be placed beside
	 *         another.
	 */
	private boolean placeBesideExistingBuilding(TacticalMap tacticalMap,
			Collection<TerrainFeature> features,
			List<Building> existingBuildings) {
		Collections.shuffle(existingBuildings, Globals.random_);
		// Randomise direction
		List<Integer> directions = new ArrayList<Integer>();
		for (int i = 0; i < TacticalMap.DIRECTIONS.length; i++)
			directions.add(i);
		Collections.shuffle(directions, Globals.random_);
		// If there are existing buildings, place the building to the side
		// of them if possible
		// Also, if there are existing buildings, ensure they are roughly
		// the same dimension
		for (Building existing : existingBuildings) {
			size_.x = existing.size_.x;
			size_.z = existing.size_.z;
			// Test each direction until a valid location is found
			for (int dir : directions) {
				// Find a range of spaces to test out for placement of the
				// building.
				List<Integer> space = new ArrayList<Integer>();
				int min = 0;
				int max = MAX_WALL_GAP;
				if (existing.direction_ == dir) {
					min = MIN_FRONT_GAP;
					max = MAX_FRONT_GAP;
				}
				for (int i = min; i < max; i++)
					space.add(i);
				Collections.shuffle(space, Globals.random_);

				// Test the position of the to-be-placed building
				Vector2f location2d = TacticalMap.DIRECTIONS[dir].clone();
				location2d.multLocal(new Vector2f(size_.x, size_.z));
				location2d.addLocal(existing.getLocation().x,
						existing.getLocation().z);

				Vector3f location = groundFeature(location2d, tacticalMap);
				if (setValidLocation(tacticalMap, features, location))
					return true;
			}
		}
		return false;
	}
}
