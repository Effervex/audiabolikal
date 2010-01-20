package audiabolikal.equipment;

/**
 * TODO
 * An ordered enumeration (from worst to best) of strong prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum StrongHitSuffixes {
	EFFECTIVE("Effective"),
	HARD("Hard"),
	WELL_MADE("Well-made"),
	ENDURING("Enduring"),
	TOUGH("Tough"),
	FORMIDABLE("Formidable"),
	STRAPPING("Strapping"),
	TENACIOUS("Tenacious"),
	DURABLE("Durable"),
	SOLID("Solid"),
	STRONG("Strong"),
	ROBUST("Robust"),
	REINFORCED("Reinforced"),
	UNYIELDING("Unyielding"),
	HEAVY_DUTY("Heavy-duty"),
	POTENT("Potent"),
	GREAT("Great"),
	MIGHTY("Mighty"),
	PERFECT("Perfect"),
	LEGENDARY("Legendary"),
	GODLY("Godly");
	
	/** The prefix. */
	private String prefix_;
	
	/**
	 * A basic constructor.
	 * 
	 * @param prefix The prefix.
	 */
	private StrongHitSuffixes(String prefix) {
		prefix_ = prefix;
	}
	
	public String toString() {
		return prefix_;
	}
}
