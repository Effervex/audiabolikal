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
	}

	@Test
	public void testGetGenres() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
		fail("Not yet implemented");
	}

}
