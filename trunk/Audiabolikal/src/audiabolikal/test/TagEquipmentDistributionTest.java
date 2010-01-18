package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.TagEquipmentDistribution;
import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

public class TagEquipmentDistributionTest {
	TagEquipmentDistribution sut_;

	@Before
	public void setUp() throws Exception {
		sut_ = new TagEquipmentDistribution();
	}

	@Test
	public void testInsertItem() {
		// Base leaf case (no children)
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("melodic thrash metal", 0.8d);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.GRAY, 1);
		Item helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.8d);
		
		// Higher case (1 child)
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8f);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.32d);
		
		// Top case (many children + grandchildren...)
		genres = new HashMap<String, Double>();
		genres.put("metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("metal", helm), 0.8f);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8f);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.32d);
		
		// Alt case, more than one genre
		genres = new HashMap<String, Double>();
		genres.put("melodic thrash metal", 0.8d);
		genres.put("progressive rock", 0.2d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.8d);
		assertEquals(sut_.getProbability("progressive rock", helm), 0.2d);
		
		// Alt case, lifted genre
		
		// Alt case, same child
		genres = new HashMap<String, Double>();
		genres.put("metal", 1d);
		genres.put("rock", 0.5d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("metal", helm), 1d);
		assertEquals(sut_.getProbability("rock", helm), 0.5d);
		assertEquals(sut_.getProbability("progressive metal", helm), 0.8 * 1 + 0.2 * 0.5);
	}

	@Test
	public void testNormaliseAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSample() {
		fail("Not yet implemented");
	}

}
