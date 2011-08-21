package audiabolikal;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import audiabolikal.util.Pair;

public class Song {
	/** The rate of data transfer from music files. */
	private static final int TRANSFER_RATE = 4096;

	/** The number of buckets to read the data into. */
	private static final int SPECTRUM_BUCKETS = 32;

	/** The ID3 info about the song. */
	private ID3Info id3Info_;

	/** The file provided to get the song. */
	private File inputFile_;

	/** The format of the file. */
	private AudioFormat fileFormat_;

	/** The normalised frequencies of this particular frame. */
	private double[] frequencies_;

	/** The highest frequency value this frame. */
	private double maxFreq_;

	public Song(File songFile) {
		try {
			// Test this is a valid audio file.
			AudioInputStream in = AudioSystem.getAudioInputStream(songFile);
			inputFile_ = songFile;
			AudioInputStream din = null;
			// Determine the format
			AudioFormat baseFormat = in.getFormat();
			fileFormat_ = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);

			// Get the ID3 info
			id3Info_ = new ID3Info(songFile);
		} catch (IOException ioe) {
			System.err.println("File not found!");
			ioe.printStackTrace();
		} catch (UnsupportedAudioFileException uafe) {
			System.err.println("Unsupported Audio File!");
			uafe.printStackTrace();
		}
	}

	/**
	 * Reads the spectrum data for this song averaged to a given number of
	 * intervals. The numSpectraX controls the number of rows and the
	 * numIntervalsZ controls the number of columns.
	 * 
	 * @param numSpectraX
	 *            The number of spectra to sample (linearly interpolated).
	 * @param numIntervalsZ
	 *            The number of intervals to sample the song (averaged
	 *            sections).
	 * @param drift
	 *            The artificial drift along X to introduce to scramble the map.
	 * @return A 2D array [Z,X] containing the spectrum values.
	 */
	public double[][] readSpectrumData(int numSpectraX, int numIntervalsZ,
			float drift) {
		double[][] spectraValues = new double[numIntervalsZ][numSpectraX];
		frequencies_ = new double[numSpectraX];
		int totalBytes = id3Info_.getByteLength();
		int chunkBytes = totalBytes / numIntervalsZ;
		double normaliser = 1.0 * chunkBytes / TRANSFER_RATE;
		try {
			for (int z = 0; z < numIntervalsZ; z++) {
				byte[] data = new byte[TRANSFER_RATE];
				AudioInputStream ais = null;
				for (int i = 0; i < chunkBytes; i += TRANSFER_RATE) {
					int amountToRead = Math.min(chunkBytes - i, TRANSFER_RATE);
					Pair<AudioInputStream, Integer> pair = readStream(ais,
							data, amountToRead);
					process(data, amountToRead);
					for (int x = 0; x < numSpectraX; x++)
						spectraValues[z][x] += (frequencies_[x] / normaliser);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return spectraValues;
	}

	/**
	 * Reads data from an audio input stream and returns the stream.
	 * 
	 * @param ais
	 *            An optional existing audio input stream.
	 * @param data
	 *            The data array to read into.
	 * @param length
	 *            The number fo bytes to read in.
	 * @return The audio input stream (same as ais, if possible).
	 * @throws Exception
	 *             Should something go awry...
	 */
	private Pair<AudioInputStream, Integer> readStream(AudioInputStream ais,
			byte[] data, int length) throws Exception {
		if (ais == null) {
			AudioInputStream in = AudioSystem.getAudioInputStream(inputFile_);
			ais = AudioSystem.getAudioInputStream(fileFormat_, in);
		}
		int nBytesRead = ais.read(data, 0, length);

		return new Pair<AudioInputStream, Integer>(ais, nBytesRead);
	}

	public void play() throws Exception {
		// Open the audio stream
		AudioInputStream din = null;
		byte[] data = new byte[TRANSFER_RATE];
		SourceDataLine line = getLine(fileFormat_);
		if (line != null) {
			int nBytesRead = 0;
			while (nBytesRead != -1) {
				Pair<AudioInputStream, Integer> pair = readStream(din, data,
						data.length);
				din = pair.getA();
				nBytesRead = pair.getB();

				process(data, data.length);
				line.start();
				if (nBytesRead != -1) {
					line.write(data, 0, nBytesRead);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	/**
	 * This method intercepts the data and processes it as it is read in from
	 * the file.
	 * 
	 * @param data
	 *            The data to be processed
	 * @param length
	 *            The amount of the data array to process.
	 */
	private void process(byte[] data, int length) {
		if (frequencies_ == null)
			frequencies_ = new double[SPECTRUM_BUCKETS];
		frequencies_ = new double[frequencies_.length];
		maxFreq_ = 0;

		// The actual pass through the data
		double freqIncr = 1.0 / TRANSFER_RATE;
		int range = (Byte.MAX_VALUE + 1) * 2;
		for (int i = 0; i < TRANSFER_RATE; i++) {
			byte b = data[i];
			int freqIndex = (b + range / 2) / (range / frequencies_.length);
			frequencies_[freqIndex] += freqIncr;
			maxFreq_ = Math.max(frequencies_[freqIndex], maxFreq_);
		}

		// StringBuffer buffer = new StringBuffer();
		// for (double freq : frequencies_) {
		// char out = '"';
		// switch ((int) (freq / maxFreq_ * 8)) {
		// case 8:
		// out = '#';
		// break;
		// case 7:
		// out = '%';
		// break;
		// case 6:
		// out = '&';
		// break;
		// case 5:
		// out = '8';
		// break;
		// case 4:
		// out = '0';
		// break;
		// case 3:
		// out = 'o';
		// break;
		// case 2:
		// out = '|';
		// break;
		// case 1:
		// out = '_';
		// break;
		// case 0:
		// out = ' ';
		// break;
		// }
		// buffer.append(out);
		// }
		// System.out.println(buffer.toString());
	}

	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	/**
	 * Gets the ID3 info that is present in the mp3 song file.
	 * 
	 * @return The ID3 info present in the song.
	 */
	public ID3Info getID3Info() {
		return id3Info_;
	}

	public File getFile() {
		return inputFile_;
	}
}
