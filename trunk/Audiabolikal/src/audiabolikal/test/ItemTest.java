package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
import audiabolikal.equipment.StrongPrefixes;
import audiabolikal.equipment.WeakPrefixes;
import audiabolikal.util.ProbabilityDistribution;

public class ItemTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSpawnIndividualItem() {
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("metal", 1d);
		genres.put("rock", 0.5);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.GRAY, 1);
		Item item = new Headgear();
		item.initialiseMouldItem("Helm", genres, itemColors, 50, 0, 0, 40, 5,
				10, 2, 5, 1);
		System.out.println("Check item names");
		for (int i = 0; i < 10; i++) {
			Item unique = item.spawnIndividualItem(1, true);
			System.out.println(unique);
			assertNotNull(unique);
			assertTrue(unique instanceof Headgear);
			assertTrue(unique.getName().contains("Helm"));
			assertEquals(unique.getGenres(), genres.keySet());
			assertEquals(unique.getValue(), 2750, 892);
			assertEquals(unique.getAttack(), 0, 0);
			assertEquals(unique.getDefense(), 40, 14);
			assertEquals(unique.getLevel(), 1, 0);
			System.out.println();
		}
		
		System.out.println("Increasing levels with variance");
		for (int i = 1; i <= 100; i += 5) {
			Item unique = item.spawnIndividualItem(i, false);
			System.out.println(unique);
			System.out.println();
		}
	}
	
	@Test
	public void testApplyLevel() {
		// Base levelling
		float result = Item.applyLevel(1, 10, 10, 0);
		assertEquals(result, 11);
		result = Item.applyLevel(5, 10, 10, 0);
		assertEquals(result, 15);
		
		// Variance levelling
		result = Item.applyLevel(1, 10, 12, 2);
		assertEquals(result, 11.3);
		result = Item.applyLevel(5, 10, 12, 2);
		assertEquals(result, 16.5);
		
		// Negative
		result = Item.applyLevel(1, 10, 7, -3);
		assertEquals(result, 10.55);
		result = Item.applyLevel(5, 10, 7, -3);
		assertEquals(result, 12.75);
		
		// Minimum levelling
		result = Item.applyLevel(1, 10, 4, -6);
		assertEquals(result, 10.2);
		result = Item.applyLevel(1, 0, 0, 0);
		assertEquals(result, 0.2);
	}

	@Test
	public void testGetGenres() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateDescriptor() {
		// No variance
		assertEquals("Ordinary", Item.generateDescriptor(40, 20, 0, 0,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));
		assertEquals("Ordinary", Item.generateDescriptor(40, 20, 0, 3,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));

		// Extremes
		assertEquals("Godly", Item.generateDescriptor(40, 20, 3, 0,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));
		assertEquals("Broken", Item.generateDescriptor(40, 20, -3, 0,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));
		assertEquals("Godly", Item.generateDescriptor(40, 80, 0, 3,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));
		assertEquals("Broken", Item.generateDescriptor(40, 80, 0, -3,
				StrongPrefixes.values(), WeakPrefixes.values(), "Ordinary", 0.1));
	}

	@Test
	public void testGenerateSuffix() {
		fail("Not yet implemented");
	}
}
