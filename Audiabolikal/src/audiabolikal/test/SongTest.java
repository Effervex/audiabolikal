package audiabolikal.test;

import static org.junit.Assert.*;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.ID3Info;
import audiabolikal.Song;

public class SongTest {
	private Song sut_;

	@Before
	public void setUp() {
		File file = new File(
				"D:\\My Music\\Final Fantasy\\FFX\\Final Fantasy - Otherworld.mp3");
		sut_ = new Song(file);
	}

	@Test
	public void testSong() {
		ID3Info id3 = sut_.getID3Info();
		assertEquals("Final Fantasy", id3.getArtist());
		assertEquals("Otherworld", id3.getTitle());
		assertEquals("FFX", id3.getAlbum());
		assertEquals(192836000, (long) id3.getDuration());
		assertEquals(3080192, (int) id3.getByteLength());
	}

	@Test
	public void testReadSpectrumData() {
		double[][] specData = sut_.readSpectrumData(32, 5, 0);
		assertEquals(specData.length, 5);
		assertEquals(specData[0].length, 8);
	}

	@Test
	public void testTest() throws Exception {
		AudioInputStream ais = AudioSystem.getAudioInputStream(sut_.getFile());
		AudioFormat af = ais.getFormat();
		System.out.println("Channels: " + af.getChannels());
		System.out.println("Encoding: " + af.getEncoding());
		System.out.println("Frame Rate: " + af.getFrameRate());
		System.out.println("Frame Size: " + af.getFrameSize());
		System.out.println("Sample Rate: " + af.getSampleRate());
		System.out.println("Sample Size: " + af.getSampleSizeInBits());
		System.out.println("Big Endian? " + af.isBigEndian());
		System.out.println("NOT SPECIFIED: " + AudioSystem.NOT_SPECIFIED);

		System.out.println("Frame Length: " + ais.getFrameLength());
	}

	@Test
	public void testPlay() throws Exception {
		//sut_.play();
	}
}
