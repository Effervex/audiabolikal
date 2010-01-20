package audiabolikal.equipment;

/**
 * An ordered enumeration (from worst to best) of strong prefix words.
 * 
 * @author Samuel J. Sarjant
 */
public enum StrongHitSuffixes {
	TARGETING("Targeting"),
	SKILL("Skill"),
	CLOBBERING("Clobbering"),
	VERITY("Verity"),
	SURENESS("Sureness"),
	SMACKING("Smacking"),
	STRIKING("Striking"),
	ACCURACY("Accuracy"),
	LUCKINESS("Luckiness"),
	DEFINITUDE("Definitude"),
	WALLOPING("Walloping"),
	WHOMPING("Whomping"),
	SURE_STRIKE("Sure-strike"),
	EXACTITUDE("Exactitude"),
	MASTERY("Mastery"),
	SUPREME_LUCK("Supreme Luck");
	
	/** The suffix. */
	private String suffix_;
	
	/**
	 * A basic constructor.
	 * 
	 * @param suffix The suffix.
	 */
	private StrongHitSuffixes(String suffix) {
		suffix_ = suffix;
	}
	
	public String toString() {
		return suffix_;
	}
}
