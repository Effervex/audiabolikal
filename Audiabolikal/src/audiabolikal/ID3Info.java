package audiabolikal;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * A class recording all the ID3 information about a song.
 * 
 * @author Samuel J. Sarjant
 */
public class ID3Info {
	/** The tag map. */
	private Map<String, Object> tags_;

	/**
	 * Creates a new ID3Info object by reading the given audio file.
	 * 
	 * @param songFile
	 *            The song file.
	 */
	public ID3Info(File songFile) {
		AudioFileFormat aff;
		try {
			aff = AudioSystem.getAudioFileFormat(songFile);
			if (aff instanceof TAudioFileFormat) {
				tags_ = ((TAudioFileFormat) aff).properties();
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return (String) tags_.get("title");
	}

	public String getArtist() {
		return (String) tags_.get("author");
	}

	public String getAlbum() {
		return (String) tags_.get("album");
	}

	public Long getDuration() {
		return (Long) tags_.get("duration");
	}
	
	public Integer getByteLength() {
		return (Integer) tags_.get("mp3.length.bytes");
	}
}
