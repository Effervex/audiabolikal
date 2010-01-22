package audiabolikal;

import audiabolikal.attacking.Attack;
import audiabolikal.equipment.*;

/**
 * This class represents an individual soldier, created using an artist's name
 * and given attacks based from tracks/and or albums. The soldier's stance is
 * determined by its weapon.
 * 
 * @author Samuel J. Sarjant
 */
public class Soldier {
	/** The maximum number of attacks this soldier is allowed. */
	public static final int MAX_ATTACKS = 4;

	/* The appearance of the soldier. */
	private Headgear headgear_;
	private Face face_;
	private Aura aura_;
	private Attire attire_;
	private Footwear footwear_;

	/** The weapon of the soldier, determining its stance. */
	private Weapon weapon_;

	/** The soldier's attacks. */
	private Attack[] attacks_;
	
	
	
	/** The name of the soldier. */
	private String name_;
	
	/** The soldier's level. */
	private int level = 1;
	
	/** The soldier's experience. */
	private int exp_ = 0;

	/**
	 * A constructor for a new Soldier.
	 */
	public Soldier(Headgear headgear, Face face, Aura aura, Attire attire,
			Footwear footwear, Weapon weapon) {
		headgear_ = headgear;
		face_ = face;
		aura_ = aura;
		attire_ = attire;
		footwear_ = footwear;
		weapon_ = weapon;
	}
}
