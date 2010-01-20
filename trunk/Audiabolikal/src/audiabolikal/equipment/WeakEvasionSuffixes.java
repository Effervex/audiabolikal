package audiabolikal.equipment;

/**
 * TODO
 * An ordered enumeration (from average to worst) of weak prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum WeakEvasionSuffixes {
	FADED("Faded"), GRUBBY("Grubby"), FATIGUED("Fatigued"), USED("Used"), WORN(
			"Worn"), DAMAGED("Damaged"), DULL("Dull"), WEAKENED("Weakened"), DECAYED(
			"Decayed"), POOR("Poor"), DELICATE("Delicate"), DETERIORATED(
			"Deteriorated"), OLD("Old"), FRAIL("Frail"), FLIMSY("Flimsy"), PUNY(
			"Puny"), FAULTY("Faulty"), RICKETY("Rickety"), DEGRADED("Degraded"), MAKESHIFT(
			"Makeshift"), FRAGILE("Fragile"), PATHETIC("Pathetic"), JOKE("Joke"), USELESS(
			"Useless"), BROKEN("Broken");

	/** The prefix. */
	private String prefix_;

	/**
	 * A basic constructor.
	 * 
	 * @param prefix
	 *            The prefix.
	 */
	private WeakEvasionSuffixes(String prefix) {
		prefix_ = prefix;
	}

	public String toString() {
		return prefix_;
	}
}
