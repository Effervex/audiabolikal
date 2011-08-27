package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import audiabolikal.Parser;
import audiabolikal.equipment.Footwear;
import audiabolikal.equipment.Headgear;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

public class ParserTest {

	@Test
	public void testParseSoldier() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseAttack() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseItem() {
		Item parsed = Parser
				.parseItem("Headgear,\"Hat\",\"metal:0.2,rock:0.8\",\"-4231:0.1,-45256:0.9\","
						+ "40,20,1,1,2,10,2,3,4,\"meshes\\hat.mesh\",\"meshes\\hat1.mesh\","
						+ "\"textures\\hat2.tex\",\"textures\\hat3.tex\",1,10,2,3,1.5,4,");
		assertEquals(parsed.getClass().getSimpleName(), "Headgear");
		assertEquals(parsed.getName(), "Hat");
		Set<String> genres = new HashSet<String>();
		genres.add("metal");
		genres.add("rock");
		assertEquals(parsed.getGenres(), genres);
		assertEquals(parsed.getGenreWeight("metal"), 0.2, 0.001);
		assertEquals(parsed.getGenreWeight("rock"), 0.8, 0.001);
		ProbabilityDistribution<Color> colors = new ProbabilityDistribution<Color>();
		colors.add(new Color(-4231), 0.1);
		colors.add(new Color(-45256), 0.9);
		assertEquals(parsed.getColorDistribution(), colors);
		assertEquals(parsed.getValueMod(), 40, 0.001);
		assertEquals(parsed.getBaseAttack(), 20, 0.001);
		assertEquals(parsed.getAttackVariance(), 1, 0.001);
		assertEquals(parsed.getBaseDefense(), 1, 0.001);
		assertEquals(parsed.getDefenseVariance(), 2, 0.001);
		assertEquals(parsed.getBaseHit(), 10, 0.001);
		assertEquals(parsed.getHitVariance(), 2, 0.001);
		assertEquals(parsed.getBaseEvasion(), 3, 0.001);
		assertEquals(parsed.getEvasionVariance(), 4, 0.001);
		assertEquals(parsed.getMaleMeshFile(), new File("meshes\\hat.mesh"));
		assertEquals(parsed.getFemaleMeshFile(), new File("meshes\\hat1.mesh"));
		assertEquals(parsed.getMaleTextureFile(), new File("textures\\hat2.tex"));
		assertEquals(parsed.getFemaleTextureFile(), new File("textures\\hat3.tex"));
		assertTrue(Arrays.equals(parsed.getRotation(), new float[] { 1, 10, 2 }));
		assertTrue(Arrays.equals(parsed.getScale(), new float[] { 3, 1.5f, 4 }));
	}

	@Test
	public void testFormatItem() {
		Item empty = new Headgear();
		String emptyStr = Parser.formatItem(empty);
		assertEquals(
				emptyStr,
				"Headgear,\"null\",\"\",\"\",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,\"null\",\"null\",\"null\",\"null\",0.0,0.0,0.0,1.0,1.0,1.0,");

		Item footwear = new Footwear();
		Map<String, Double> genres = new HashMap<String, Double>();
		genres.put("rock", 1d);
		genres.put("pop", 0.9d);
		ProbabilityDistribution<Color> itemColors = new ProbabilityDistribution<Color>();
		itemColors.add(Color.WHITE, 1);
		itemColors.add(Color.GRAY, 0.5);
		itemColors.add(Color.BLACK, 0.5);
		float[] attributes = { 0, 0, 5, 1, 0, 0, 8, 2 };
		File[] modelFiles = { new File("shoeFile.mesh"),
				new File("shoeFile2.mesh"), new File("shoeFile.tex"),
				new File("shoeFile2.tex") };
		float[] rotation = { 1, 2, 3 };
		float[] scale = { 1, 1.2f, 1.1f };
		footwear.initialiseMouldItem("Sneakers", genres, itemColors, 5,
				attributes, modelFiles, rotation, scale);
		String footStr = Parser.formatItem(footwear);
		assertEquals(
				footStr,
				"Footwear,\"Sneakers\",\"pop:0.9,rock:1.0\",\"-1:0.5,-8355712:0.25,-16777216:0.25\","
						+ "5.0,0.0,0.0,5.0,1.0,0.0,0.0,8.0,2.0,\"shoeFile.mesh\",\"shoeFile2.mesh\",\"shoeFile.tex\",\"shoeFile2.tex\","
						+ "1.0,2.0,3.0,1.0,1.2,1.1,");
	}

}
