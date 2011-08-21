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
	public static final double EPSILON = 0.001;
	
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
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.8d, EPSILON);
		
		// Higher case (1 child)
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8f, EPSILON);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.56d, EPSILON);
		
		// Top case (many children + grandchildren...)
		genres = new HashMap<String, Double>();
		genres.put("metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("metal", helm), 0.8d, EPSILON);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8d, EPSILON);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.56d, EPSILON);
		
		// Alt case, more than one genre
		genres = new HashMap<String, Double>();
		genres.put("melodic thrash metal", 0.8d);
		genres.put("progressive rock", 0.2d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.8d, EPSILON);
		assertEquals(sut_.getProbability("progressive rock", helm), 0.2d, EPSILON);
		
		// Alt case, lifted genre
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		genres.put("progressive rock", 0.2d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8d, EPSILON);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 0.56d, EPSILON);
		assertEquals(sut_.getProbability("progressive rock", helm), 0.2d, EPSILON);
		
		// Alt case, same child
		genres = new HashMap<String, Double>();
		genres.put("metal", 1d);
		genres.put("rock", 0.5d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("metal", helm), 1d, EPSILON);
		assertEquals(sut_.getProbability("rock", helm), 0.5d, EPSILON);
		assertEquals(sut_.getProbability("progressive metal", helm), 0.8 * 1 + 0.2 * 0.5, EPSILON);
		
		// Overlapping genres
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		genres.put("melodic thrash metal", 0.9d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		assertEquals(sut_.getProbability("thrash metal", helm), 0.8d, EPSILON);
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 1.46d, EPSILON);
	}

	@Test
	public void testNormaliseAll() {
		// Over 1 case
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("melodic thrash metal", 10d);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.GRAY, 1);
		Item helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.insertItem(helm);
		sut_.normaliseAll();
		assertEquals(sut_.getProbability("melodic thrash metal", helm), 1d, EPSILON);
		
		// Normalised case
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.normaliseAll();
		assertEquals(sut_.getProbability("thrash metal", helm), 1, EPSILON);
		
		// 2 items
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		Headgear hat = new Headgear();
		hat.initialiseMouldItem("hat", genres, itemColors, 20, 0, 0, 10, 2, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.insertItem(hat);
		sut_.normaliseAll();
		assertEquals(sut_.getProbability("thrash metal", helm), 0.5d, EPSILON);
		assertEquals(sut_.getProbability("thrash metal", hat), 0.5d, EPSILON);
		
		// Imbalanced items
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.6d);
		hat = new Headgear();
		hat.initialiseMouldItem("hat", genres, itemColors, 20, 0, 0, 10, 2, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.insertItem(hat);
		sut_.normaliseAll();
		assertEquals(sut_.getProbability("thrash metal", helm), 0.625d, EPSILON);
		assertEquals(sut_.getProbability("thrash metal", hat), 0.375d, EPSILON);
	}

	@Test
	public void testSample() {
		// Basic case
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("melodic thrash metal", 1d);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.GRAY, 1);
		Item helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.insertItem(helm);
		sut_.normaliseAll();
		Item sample = sut_.sample("melodic thrash metal", "Headgear");
		assertEquals(helm, sample);
		sample = sut_.sample("metal", "Headgear");
		assertNull(sample);
		
		// Parent case
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 0.8d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.normaliseAll();
		sample = sut_.sample("thrash metal", "Headgear");
		assertEquals(helm, sample);
		sample = sut_.sample("melodic thrash metal", "Headgear");
		assertEquals(helm, sample);
		
		// Multiple items
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		Item hat = new Headgear();
		hat.initialiseMouldItem("hat", genres, itemColors, 20, 0, 0, 10, 2, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.insertItem(hat);
		sut_.normaliseAll();
		double helmCount = 0;
		double hatCount = 0;
		for (int i = 0; i < 1000; i++) {
			sample = sut_.sample("thrash metal", "Headgear");
			assertTrue(sample.equals(helm) || sample.equals(hat));
			if (sample.equals(helm))
				helmCount++;
			else if (sample.equals(hat))
				hatCount++;
		}
		// The samples should be roughly 50/50.
		assertEquals(helmCount, 500, 50);
		assertEquals(hatCount, 500, 50);
		
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 9d);
		helm = new Headgear();
		helm.initialiseMouldItem("helm", genres, itemColors, 50, 0, 0, 40, 5, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		genres = new HashMap<String, Double>();
		genres.put("thrash metal", 1d);
		hat = new Headgear();
		hat.initialiseMouldItem("hat", genres, itemColors, 20, 0, 0, 10, 2, 0, 0, 0, 0, null, null, null, null, 0, 0, 0, 1f, 1f, 1f);
		sut_.clear();
		sut_.insertItem(helm);
		sut_.insertItem(hat);
		sut_.normaliseAll();
		helmCount = 0;
		hatCount = 0;
		for (int i = 0; i < 1000; i++) {
			sample = sut_.sample("thrash metal", "Headgear");
			assertTrue(sample.equals(helm) || sample.equals(hat));
			if (sample.equals(helm))
				helmCount++;
			else if (sample.equals(hat))
				hatCount++;
		}
		// The samples should be roughly 50/50.
		assertEquals(helmCount, 900, 50);
		assertEquals(hatCount, 100, 50);
	}

}
