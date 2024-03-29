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
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		items.add(helm);
		
		Item face = new Face();
		face.initialiseMouldItem("angry", genres, itemColors, 5000, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		items.add(face);
		
		Item aura = new Aura();
		aura.initialiseMouldItem("ki", genres, itemColors, 100000, 0, 0, 20, 0, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		items.add(aura);
		
		Item attire = new Attire();
		attire.initialiseMouldItem("suit", genres, itemColors, 50, 0, 0, 80, 15, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		items.add(attire);
		
		Item footwear = new Footwear();
		footwear.initialiseMouldItem("sandals", genres, itemColors, 10, 0, 0, 10, 2, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		items.add(footwear);
		
		Item weapon = new TwoHanded();
		weapon.initialiseMouldItem("axe", genres, itemColors, 50, 40, 15, 0, 0, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
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
		Aura aura = sut_.generateAura("metal");
		assertNotNull(aura);
		assertEquals(aura.getName(), "ki");
		aura = sut_.generateAura("thrash metal");
		assertNotNull(aura);
		assertEquals(aura.getName(), "ki");
	}

	@Test
	public void testGenerateAttire() {
		// Basic test
		Attire attire = sut_.generateAttire("rock");
		assertNotNull(attire);
		assertEquals(attire.getName(), "suit");
		attire = sut_.generateAttire("hard rock");
		assertNotNull(attire);
		assertEquals(attire.getName(), "suit");
	}

	@Test
	public void testGenerateFootwear() {
		// Basic test
		Footwear footwear = sut_.generateFootwear("rock");
		assertNotNull(footwear);
		assertEquals(footwear.getName(), "sandals");
		footwear = sut_.generateFootwear("hard rock");
		assertNotNull(footwear);
		assertEquals(footwear.getName(), "sandals");
	}

	@Test
	public void testGenerateWeapon() {
		// Basic test
		Weapon weapon = sut_.generateWeapon("rock");
		assertNotNull(weapon);
		assertEquals(weapon.getName(), "axe");
		weapon = sut_.generateWeapon("hard rock");
		assertNotNull(weapon);
		assertEquals(weapon.getName(), "axe");
	}

}
