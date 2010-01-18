package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
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
		item.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		System.out.println("Check item names");
		for (int i = 0; i < 10; i++) {
			Item unique = item.spawnIndividualItem();
			System.out.println("   " + unique);
			assertNotNull(unique);
			assertTrue(unique instanceof Headgear);
			assertTrue(unique.getName().contains("helm"));
			assertEquals(unique.getGenres(), genres.keySet());
			assertEquals(unique.getValue(), 2000, 666);
			assertEquals(unique.getAttack(), 0, 0);
			assertEquals(unique.getDefense(), 40, 12);
		}
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
	public void testGeneratePrefix() {
		// No variance
		assertEquals("Ordinary", Item.generatePrefix(40, 20, 0, 0));
		assertEquals("Ordinary", Item.generatePrefix(40, 20, 0, 3));

		// Extremes
		assertEquals("Godly", Item.generatePrefix(40, 20, 3, 0));
		assertEquals("Broken", Item.generatePrefix(40, 20, -3, 0));
		assertEquals("Godly", Item.generatePrefix(40, 80, 0, 3));
		assertEquals("Broken", Item.generatePrefix(40, 80, 0, -3));
	}

	@Test
	public void testGenerateSuffix() {
		fail("Not yet implemented");
	}
}
