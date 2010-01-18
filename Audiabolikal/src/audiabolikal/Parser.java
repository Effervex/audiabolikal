package audiabolikal;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import audiabolikal.util.ProbabilityDistribution;

import net.roarsoftware.lastfm.*;

/**
 * This class handles all elements of parsing, from parsing artist and/or song
 * names, to parsing file data into appropriate attributes.
 */
public class Parser {
	/** The API key. */
	private static final String API_KEY = "58d47844ca662d856abf022d06802b4a";

	/** The API secret. */
	private static final String API_SECRET = "bf32b33d30ce07ea0776a8f18cdfb208";

	/** The number of tags to use for generation. */
	public static final int NUM_TAGS = 3;
	
	/** The random number seed. */
	private static final int SEED = 87465; 

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
		ProbabilityDistribution<String> topTags = new ProbabilityDistribution<String>(SEED);
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
	 * @param songFile The audio file which creates the attack.
	 * @return An attack, created from the data within the file.
	 */
	public static Attack parseAttack(AudioFile songFile) {
		
		return null;
	}
}
