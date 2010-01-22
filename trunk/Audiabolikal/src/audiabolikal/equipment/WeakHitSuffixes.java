package audiabolikal.equipment;

/**
 * An ordered enumeration (from average to worst) of weak prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum WeakHitSuffixes {
	SKIMMING("Skimming"),
	GLANCING("Glancing"),
	GRAZING("Grazing"),
	FUMBLING("Fumbling"),
	LACKING("Lacking"),
	MISCALCULATIONS("Miscalculations"),
	MISSING("Missing"),
	CLUMSINESS("Clumsiness"),
	SHORTCOMINGS("Shortcomings"),
	UNLUCKINESS("Unluckiness"),
	BLINDNESS("Blindness"),
	MISTAKES("Mistakes");

	/** The suffix. */
	private String suffix_;
	
	/**
	 * A basic constructor.
	 * 
	 * @param suffix The suffix.
	 */
	private WeakHitSuffixes(String suffix) {
		suffix_ = suffix;
	}
	
	public String toString() {
		return suffix_;
	}
}
