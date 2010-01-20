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
import audiabolikal.equipment.*;
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
		Collection<Item> items = new HashSet<Item>();
		
		Item helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0);
		items.add(helm);
		
		Item face = new Face();
		face.initialiseMouldItem("angry", genres, itemColors, 5000, 0, 0, 0, 0, 0, 0, 0, 0);
		items.add(face);
		
		Item aura = new Aura();
		aura.initialiseMouldItem("ki", genres, itemColors, 100000, 0, 0, 20, 0, 0, 0, 0, 0);
		items.add(aura);
		
		Item attire = new Attire();
		attire.initialiseMouldItem("suit", genres, itemColors, 50, 0, 0, 80, 15, 0, 0, 0, 0);
		items.add(attire);
		
		Item footwear = new Footwear();
		footwear.initialiseMouldItem("sandals", genres, itemColors, 10, 0, 0, 10, 2, 0, 0, 0, 0);
		items.add(footwear);
		
		Item weapon = new TwoHanded();
		weapon.initialiseMouldItem("axe", genres, itemColors, 50, 40, 15, 0, 0, 0, 0, 0, 0);
		items.add(weapon);
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
		// Basic test
		Face face = sut_.generateFace("metal");
		assertNotNull(face);
		assertEquals(face.getName(), "angry");
		face = sut_.generateFace("thrash metal");
		assertNotNull(face);
		assertEquals(face.getName(), "angry");
	}

	@Test
	public void testGenerateAura() {
		// Basic test
		Face face = sut_.generateFace("metal");
		assertNotNull(face);
		assertEquals(face.getName(), "angry");
		face = sut_.generateFace("thrash metal");
		assertNotNull(face);
		assertEquals(face.getName(), "angry");
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
