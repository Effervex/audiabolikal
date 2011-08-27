package audiabolikal;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import audiabolikal.attacking.Attack;
import audiabolikal.equipment.Item;
import audiabolikal.util.ProbabilityDistribution;

import net.roarsoftware.lastfm.*;

/**
 * This class handles all elements of parsing, from parsing artist and/or song
 * names, to parsing file data into appropriate attributes.
 */
public class Parser {
	/** The API key. */
	public static final String API_KEY = "58d47844ca662d856abf022d06802b4a";

	/** The API secret. */
	public static final String API_SECRET = "bf32b33d30ce07ea0776a8f18cdfb208";

	/** The number of tags to use for generation. */
	public static final int NUM_TAGS = 3;

	/** The random number seed. */
	private static final int SEED = 87465;

	/** The location of the Audiabolikal files. */
	private static File programLoc_;

	/**
	 * Parses a soldier from a file, using the selected file's track tags from
	 * Last.fm as the details for the soldier, but the artist as the soldier
	 * name. As such, different tracks may create slightly different looking
	 * soldiers, depending on the individual track tags.
	 * 
	 * This problem was a result of the API, which only returns a Collection of
	 * String when looking up an Artist's top tags.
	 * 
	 * @param songFile
	 *            The song file, from which a soldier is created.
	 * @return A newly created Soldier, determined by the file artist's tags.
	 */
	public static Soldier parseSoldier(Song song) throws Exception {
		// First, read the songFile data
		ID3Info id3 = song.getID3Info();
		String artist = id3.getArtist();
		String title = id3.getTitle();

		// Gets the tags from Last.fm regarding the artist.
		List<net.roarsoftware.lastfm.Tag> trackTags = Track.getTopTags(artist,
				title, null);

		// Create a probability distribution of the top X tags
		ProbabilityDistribution<String> topTags = new ProbabilityDistribution<String>(
				SEED);
		Iterator<net.roarsoftware.lastfm.Tag> iter = trackTags.iterator();
		int i = 0;
		while ((i < NUM_TAGS) && (iter.hasNext())) {
			net.roarsoftware.lastfm.Tag tag = iter.next();
			topTags.add(tag.getName(), tag.getCount());
			i++;
		}
		topTags.normaliseProbs();

		// Assemble the soldier
		return null;
	}

	/**
	 * Parses an attack from a file by reading data from the file itself and
	 * possibly from Last.fm to create an attributable attack.
	 * 
	 * @param songFile
	 *            The audio file which creates the attack.
	 * @return An attack, created from the data within the file.
	 */
	public static Attack parseAttack(Song song) {
		return null;
	}

	/**
	 * Parses an item from a string.
	 * 
	 * <Item Type>,"Item Name","genre:0.12,genre:1.0,..."
	 * ,"RGB:0.4,RGB:0.6,...",<Value Mod>,<Base Attack>+<Attack Variance>,<Base
	 * Defense>+<Defense Variance>,<Base Hit>+<Hit Variance>,<Base
	 * Evasion>+<Evasion Variance>,"<male mesh file>","<female mesh file>"
	 * ,"<male texture file>","<female texture file>"
	 * ,<RotationX>,<RotationY>,<RotationZ>,<ScaleX>,<ScaleY>,<ScaleZ>,
	 * 
	 * @param strItem
	 *            The item to be parsed.
	 * @return The item the string represents.
	 */
	public static Item parseItem(String strItem) {
		Pattern p = Pattern.compile("((?:[\\w\\.]+?)|(?:\".+?\")),");
		Matcher m = p.matcher(strItem);

		// Type
		m.find();
		String type = m.group(1);
		// Name
		m.find();
		String name = m.group(1).replaceAll("\"", "");
		// Genres
		m.find();
		String genreString = m.group(1).replaceAll("\"", "");
		String[] genresSplit = genreString.split(",");
		Map<String, Double> genres = new HashMap<String, Double>();
		for (String genreWeight : genresSplit) {
			int colonIndex = genreWeight.indexOf(':');
			genres.put(genreWeight.substring(0, colonIndex),
					Double.parseDouble(genreWeight.substring(colonIndex + 1)));
		}
		// Colours
		m.find();
		ProbabilityDistribution<Color> colorDistribution = new ProbabilityDistribution<Color>();
		String colourString = m.group(1).replaceAll("\"", "");
		String[] coloursSplit = colourString.split(",");
		for (String colourWeight : coloursSplit) {
			int colonIndex = colourWeight.indexOf(':');
			colorDistribution.add(
					new Color(Integer.parseInt(colourWeight.substring(0,
							colonIndex))), Double.parseDouble(colourWeight
							.substring(colonIndex + 1)));
		}
		// Value Mod
		m.find();
		float valueMod = Float.parseFloat(m.group(1));
		// Attributes
		m.find();
		float baseAttack = Float.parseFloat(m.group(1));
		m.find();
		float attackVariance = Float.parseFloat(m.group(1));
		m.find();
		float baseDefense = Float.parseFloat(m.group(1));
		m.find();
		float defenseVariance = Float.parseFloat(m.group(1));
		m.find();
		float baseHit = Float.parseFloat(m.group(1));
		m.find();
		float hitVariance = Float.parseFloat(m.group(1));
		m.find();
		float baseEvasion = Float.parseFloat(m.group(1));
		m.find();
		float evasionVariance = Float.parseFloat(m.group(1));
		// Male + female mesh + texture files
		m.find();
		String fileString = m.group(1).replaceAll("\"", "");
		File maleMeshFile = (fileString.equals("null")) ? null : new File(
				fileString);
		m.find();
		fileString = m.group(1).replaceAll("\"", "");
		File femaleMeshFile = (fileString.equals("null")) ? null : new File(
				fileString);
		m.find();
		fileString = m.group(1).replaceAll("\"", "");
		File maleTextureFile = (fileString.equals("null")) ? null : new File(
				fileString);
		m.find();
		fileString = m.group(1).replaceAll("\"", "");
		File femaleTextureFile = (fileString.equals("null")) ? null : new File(
				fileString);
		// Rotation
		float[] rotation = new float[3];
		m.find();
		rotation[0] = Float.parseFloat(m.group(1));
		m.find();
		rotation[1] = Float.parseFloat(m.group(1));
		m.find();
		rotation[2] = Float.parseFloat(m.group(1));
		// Scale
		float[] scale = new float[3];
		m.find();
		scale[0] = Float.parseFloat(m.group(1));
		m.find();
		scale[1] = Float.parseFloat(m.group(1));
		m.find();
		scale[2] = Float.parseFloat(m.group(1));

		try {
			Item item = (Item) Class.forName("audiabolikal.equipment." + type)
					.newInstance();
			item.initialiseMouldItem(name, genres, colorDistribution, valueMod,
					baseAttack, attackVariance, baseDefense, defenseVariance,
					baseHit, hitVariance, baseEvasion, evasionVariance,
					maleMeshFile, femaleMeshFile, maleTextureFile,
					femaleTextureFile, rotation[0], rotation[1], rotation[2],
					scale[0], scale[1], scale[2]);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Formats an item into a string representation.
	 * 
	 * <Item Type>,"Item Name","genre:0.12,genre:1.0,..."
	 * ,"RGB:0.4,RGB:0.6,...",<Value Mod>,<Base Attack>+<Attack Variance>,<Base
	 * Defense>+<Defense Variance>,<Base Hit>+<Hit Variance>,<Base
	 * Evasion>+<Evasion Variance>,"<male mesh file>","<female mesh file>"
	 * ,"<male texture file>","<female texture file>"
	 * ,<RotationX>,<RotationY>,<RotationZ>,<ScaleX>,<ScaleY>,<ScaleZ>,
	 * 
	 * @param item
	 *            The item being formatted.
	 * @return The string representation of the item.
	 */
	public static String formatItem(Item item) {
		if (item == null)
			return null;

		StringBuffer buffer = new StringBuffer();
		// Item type
		buffer.append(item.getClass().getSimpleName() + ",");
		// Name
		buffer.append('"' + item.getName() + '"' + ",");
		// Genres
		buffer.append('"');
		if (item.getGenres() != null) {
			boolean first = true;
			for (String genre : item.getGenres()) {
				if (!first)
					buffer.append(",");
				double weight = item.getGenreWeight(genre);
				buffer.append(genre + ":" + weight);
				first = false;
			}
		}
		buffer.append('"' + "," + '"');
		if (item.getColorDistribution() != null) {
			// Colours
			boolean first = true;
			ProbabilityDistribution<Color> colors = item.getColorDistribution();
			for (Color color : colors) {
				if (!first)
					buffer.append(",");
				double weight = colors.getProb(color);
				buffer.append(color.getRGB() + ":" + weight);
				first = false;
			}
		}
		buffer.append('"' + ",");
		// Value mod
		buffer.append(item.getValueMod() + ",");
		// Attributes
		buffer.append(item.getBaseAttack() + "," + item.getAttackVariance()
				+ ",");
		buffer.append(item.getBaseDefense() + "," + item.getDefenseVariance()
				+ ",");
		buffer.append(item.getBaseHit() + "," + item.getHitVariance() + ",");
		buffer.append(item.getBaseEvasion() + "," + item.getEvasionVariance()
				+ ",");
		// Male + female mesh + texture files
		String path = (item.getMaleMeshFile() != null) ? item.getMaleMeshFile()
				.getPath() : null;
		buffer.append('"' + path + '"' + ",");
		path = (item.getFemaleMeshFile() != null) ? item.getFemaleMeshFile()
				.getPath() : null;
		buffer.append('"' + path + '"' + ",");
		path = (item.getMaleTextureFile() != null) ? item.getMaleTextureFile()
				.getPath() : null;
		buffer.append('"' + path + '"' + ",");
		path = (item.getFemaleTextureFile() != null) ? item
				.getFemaleTextureFile().getPath() : null;
		buffer.append('"' + path + '"' + ",");
		// Rotation values
		buffer.append(item.getRotation()[0] + ",");
		buffer.append(item.getRotation()[1] + ",");
		buffer.append(item.getRotation()[2] + ",");
		// Scale
		buffer.append(item.getScale()[0] + ",");
		buffer.append(item.getScale()[1] + ",");
		buffer.append(item.getScale()[2] + ",");

		return buffer.toString();
	}

	/**
	 * Converts an absolute path to a relative path.
	 * 
	 * @param absolutePath
	 *            The absolute path.
	 * @param programLoc
	 *            The location of the program.
	 * @return The relative file path from the program location.
	 */
	private static char getRelativePath(String absolutePath, String programLoc) {

		return 0;
	}
}
