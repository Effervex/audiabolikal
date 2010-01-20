package audiabolikal.equipment;

/**
 * TODO
 * An ordered enumeration (from average to worst) of weak prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum WeakEvasionSuffixes {
	HESISTATION("Hesistation"),
	DAWDLING("Dawdling"),
	HEAVY_HANDEDNESS("Heavy-handedness"),
	WAITING("Waiting"),
	INEPTITUDE("Ineptitude"),
	STUMBLING("Stumbling"),
	GRACELESSNESS("Gracelessness"),
	CLUMSINESS("Clumsiness"),
	SLOTH("Sloth"),
	BLUNDERING("Blundering"),
	UNLUCKINESS("Unluckiness"),
	MASOCHISM("Masochism"),
	CALAMITY("Calamity"),
	CURSES("Curses");

	/** The suffix. */
	private String suffix_;
	
	/**
	 * A basic constructor.
	 * 
	 * @param suffix The suffix.
	 */
	private WeakEvasionSuffixes(String suffix) {
		suffix_ = suffix;
	}
	
	public String toString() {
		return suffix_;
	}
}
