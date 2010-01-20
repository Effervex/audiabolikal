package audiabolikal.equipment;

/**
 * An ordered enumeration (from worst to best) of strong prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum StrongEvasionSuffixes {
	MOVING("Moving"),
	AVOIDING("Avoiding"),
	SHIFTING("Shifting"),
	PARRYING("Parrying"),
	DEFTNESS("Deftness"),
	ESCAPING("Escaping"),
	REACTION("Reaction"),
	DODGING("Dodging"),
	DEFLECTION("Deflection"),
	ELUSION("Elusion"),
	EVASION("Evasion"),
	AGILITY("Agility"),
	MISGUIDANCE("Misguidance"),
	DECEPTION("Deception"),
	SLIPPERYNESS("Slipperyness"),
	MISCONCEPTION("Misconception"),
	GREASE("Grease"),
	BLURRINESS("Blurriness"),
	TRICKERY("Trickery"),
	ILLUSION("Illusion"),
	PHANTASMS("Phantasms");
	
	/** The suffix. */
	private String suffix_;
	
	/**
	 * A basic constructor.
	 * 
	 * @param suffix The suffix.
	 */
	private StrongEvasionSuffixes(String suffix) {
		suffix_ = suffix;
	}
	
	public String toString() {
		return suffix_;
	}
}
