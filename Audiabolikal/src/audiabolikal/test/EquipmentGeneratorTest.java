package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.EquipmentGenerator;
import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

public class EquipmentGeneratorTest {
	private EquipmentGenerator sut_;

	@Before
	public void setUp() throws Exception {
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("metal", 1d);
		genres.put("rock", 0.5);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.GRAY, 1);
		Item helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		Collection<Item> items = new HashSet<Item>();
		items.add(helm);
		sut_ = EquipmentGenerator.initInstance(items);
	}

	@Test
	public void testGenerateHeadgear() {
		// Basic test
		Headgear headgear = sut_.generateHeadgear("metal");
		assertNotNull(headgear);
		assertEquals(headgear.getName(), "helm");
		headgear = sut_.generateHeadgear("thrash metal");
		assertNotNull(headgear);
		assertEquals(headgear.getName(), "helm");
	}

	@Test
	public void testGenerateFace() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateAura() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateAttire() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateFootwear() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateWeapon() {
		fail("Not yet implemented");
	}

}
