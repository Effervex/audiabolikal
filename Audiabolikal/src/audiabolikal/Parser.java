package audiabolikal;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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
	public static Soldier parseSoldier(File songFile) throws Exception {
		// First, read the songFile data
		AudioFile af = AudioFileIO.read(songFile);

		Tag fileTag = af.getTag();
		String artist = fileTag.getFirst(FieldKey.ARTIST);
		String title = fileTag.getFirst(FieldKey.TITLE);

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
	public static Attack parseAttack(AudioFile songFile) {
		return null;
	}

	/**
	 * Parses an item from a string.
	 * 
	 * @param strItem
	 *            The item to be parsed.
	 * @return The item the string represents.
	 */
	public static Item parseItem(String strItem) {
		Pattern p = Pattern.compile("((?:\\w*?)|(?:\".*?\")|(?:\\d+\\+\\d+)),");
		Matcher m = p.matcher(strItem);
		
		// Type
		m.find();
		String type = m.group(1);
		// Name
		m.find();
		String name = m.group(1);
		
		return null;
	}

	/**
	 * Formats an item into a string representation. <Item
	 * Type>,"Item Name","genre:0.12,genre:1.0,...","RGB:0.4,RGB:0.6,...",<Value
	 * Mod>,<Base Attack>+<Attack Variance>,<Base Defense>+<Defense
	 * Variance>,<Base Hit>+<Hit Variance>,<Base Evasion>+<Evasion
	 * Variance>,"<male mesh file>","<female mesh file>"
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
		buffer.append(item.getClass().getSimpleName() + ',');
		// Name
		buffer.append('"' + item.getName() + '"' + ',');
		// Genres
		buffer.append('"');
		boolean first = true;
		for (String genre : item.getGenres()) {
			if (!first)
				buffer.append(',');
			double weight = item.getGenreWeight(genre);
			buffer.append(genre + ":" + weight);
			first = false;
		}
		buffer.append('"' + ',' + '"');
		// Colours
		first = true;
		ProbabilityDistribution<Color> colors = item.getColorDistribution();
		for (Color color : colors) {
			if (!first)
				buffer.append(',');
			double weight = colors.getProb(color);
			buffer.append(color.getRGB() + ":" + weight);
			first = false;
		}
		buffer.append('"' + ',');
		// Value mod
		buffer.append(item.getValueMod() + ',');
		// Attributes
		buffer.append(item.getBaseAttack() + "+" + item.getAttackVariance()
				+ ',');
		buffer.append(item.getBaseDefense() + "+" + item.getDefenseVariance()
				+ ',');
		buffer.append(item.getBaseHit() + "+" + item.getHitVariance() + ',');
		buffer.append(item.getBaseEvasion() + "+" + item.getEvasionVariance()
				+ ',');
		// Male + female mesh + texture files
		buffer.append('"' + item.getMaleMeshFile().getPath() + '"' + ',');
		buffer.append('"' + item.getFemaleMeshFile().getPath() + '"' + ',');
		buffer.append('"' + item.getMaleTextureFile().getPath() + '"' + ',');
		buffer.append('"' + item.getFemaleTextureFile().getPath() + '"' + ',');
		// Rotation values
		buffer.append(item.getRotation()[0] + ',');
		buffer.append(item.getRotation()[1] + ',');
		buffer.append(item.getRotation()[2] + ',');
		// Scale
		buffer.append(item.getScale()[0] + ',');
		buffer.append(item.getScale()[1] + ',');
		buffer.append(item.getScale()[2] + ',');

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
