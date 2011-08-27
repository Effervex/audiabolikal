package audiabolikal.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.terrain.TacticalMap;
import audiabolikal.terrain.TerrainGeography;
import audiabolikal.terrain.feature.TerrainFeature;

public class TacticalMapTest {
	private TacticalMap sut_;

	@Before
	public void setUp() {
		sut_ = new TacticalMap(0);
	}

	@Test
	public void testGenerateMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddFeatures() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateGeography() {
		// Slope
		for (TerrainGeography geog : TerrainGeography.values()) {
			System.out.println(geog);
			for (int i = 0; i < 3; i++) {
				int[][] terrain = sut_.generateGeography(
						geog, 24, 24);
				for (int z = 0; z < 24; z++) {
					for (int x = 0; x < 24; x++) {
						System.out.print(terrain[x][z] + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
		}
	}
}
