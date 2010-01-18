package audiabolikal.equipment;

import java.awt.Color;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import audiabolikal.util.ProbabilityDistribution;

/**
 * An abstract class of items, which can be subclassed into equipped gear. Note
 * that this class of item defines the set of all items of that type, not an
 * individual soldier's item, which must be created through the
 * getInidvidualItem method.
 * 
 * TODO Note that the individual item method could also set a weapons attack and
 * defense attributes, set near base values with SD.
 * 
 * @author Samuel J. Sarjant
 */
public abstract class Item {
	/** Each item has at least one colour. A normalised distribution */
	private ProbabilityDistribution<Color> colourDistribution_;

	/**
	 * An item can be representative of many genres. This mapping represents the
	 * strength it relates to each genre.
	 */
	private Map<String, Double> genres_;

	/** The value, or cost of an item. */
	private int baseValue_;

	/** The base attack for the item. */
	private float baseAttack_;

	/** The amount of variance in the attack. */
	private float attackVariance_;

	/** The base defense for the item. */
	private float baseDefense_;

	/** The amount of variance in the defense. */
	private float defenseVariance_;

	/** The random number generator for the mould. */
	private Random random_;

	/** The mesh for male models representing this item. */

	/** The mesh for female models representing this item. */

	/** The texture for male models representing this item. */

	/** The texture for female models representing this item. */

	/** The name of the item. */
	private String name_;

	/**
	 * A variable determining whether this class represents an individual item,
	 * or a general mould.
	 */
	private boolean individual_;

	/** The attack of this item. */
	private float attack_;

	/** The defense of this item. */
	private float defense_;

	/** The value of the item. */
	private int value_;

	/** The fixed colour of an individual item. */
	private Color colour_;

	/**
	 * Initialises a mould item - a general item generator of sorts. The
	 * arguments define what range of items are created.
	 * 
	 * @param name
	 *            The name of the item.
	 * @param genres
	 *            The genres the item covers.
	 * @param itemColors
	 *            The possible colours the item can be.
	 * @param valueMod
	 *            The value modifier of the item. Relates to attack and defense
	 *            values.
	 * @param baseAttack
	 *            The base attack for the item.
	 * @param attackVariance
	 *            The amount of variance the attack can have.
	 * @param baseDefense
	 *            The base defense of the item.
	 * @param defenseVariance
	 *            The amount of variance the defense can have.
	 */
	public void initialiseMouldItem(String name, Map<String, Double> genres,
			ProbabilityDistribution<Color> itemColors, int valueMod,
			float baseAttack, float attackVariance, float baseDefense,
			float defenseVariance) {
		individual_ = false;
		name_ = name;
		genres_ = genres;
		colourDistribution_ = itemColors;
		baseValue_ = valueMod;
		baseAttack_ = baseAttack;
		baseDefense_ = baseDefense;
		attackVariance_ = attackVariance;
		defenseVariance_ = defenseVariance;
		random_ = new Random();
	}

	/**
	 * A constructor for creating an individual item with a set colour, attack,
	 * defense, and value.
	 * 
	 * @param generalItem
	 *            The general item which spawned this item.
	 * @param color
	 *            The colour of this individual item.
	 * @param actualValue
	 *            The actual value of the item.
	 * @param attack
	 *            The attack of the item.
	 * @param defense
	 *            The defense of the item.
	 */
	private void initialiseIndividualItem(Item generalItem, Color color,
			int actualValue, float attack, float defense) {
		individual_ = true;
		name_ = generalItem.name_;
		genres_ = generalItem.genres_;
		attack_ = attack;
		defense_ = defense;
		value_ = actualValue;
	}

	/**
	 * Spawns an individual item from the general mould, after calculating
	 * values.
	 * 
	 * @return An individual item.
	 */
	public Item spawnIndividualItem() {
		if (!individual_) {
			// Calculate the fixed values
			float defense = (float) (baseDefense_ + defenseVariance_
					* random_.nextGaussian());
			float attack = (float) (baseAttack_ + attackVariance_
					* random_.nextGaussian());
			int calculatedValue = calcItemValue(attack, defense);
			Color fixedColour = colourDistribution_.sample();

			try {
				Item unique = this.getClass().newInstance();
				unique.initialiseIndividualItem(this, fixedColour,
						calculatedValue, attack, defense);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Calculates the value of the item, based on the attack and defense
	 * attributes.
	 * 
	 * @param attack
	 *            The attack value.
	 * @param defense
	 *            The defense value.
	 * @return The value of the item, based on the attack and defense.
	 */
	private int calcItemValue(float attack, float defense) {
		// Simply add and multiply.
		// TODO Investigate alternative methods. Perhaps exponential?
		return (int) ((attack + defense) * baseValue_);
	}

	/**
	 * Gets the genres associated with this item.
	 * 
	 * @return A set of genres associated with this item.
	 */
	public Set<String> getGenres() {
		return genres_.keySet();
	}

	/**
	 * Gets the weight of a genre this item is part of.
	 * 
	 * @param genre
	 *            The genre to look for.
	 * @return The weight of the genre, of 1 or less. 0 indicates this item does
	 *         not represent the genre.
	 */
	public double getGenreWeight(String genre) {
		Double value = genres_.get(genre);
		if (value != null)
			return value;
		return 0;
	}

	/**
	 * Gets the value of this item.
	 * 
	 * @return The value of the item.
	 */
	public int getValue() {
		return value_;
	}
	
	/**
	 * Gets the name of the item.
	 *  
	 * @return The item name.
	 */
	public String getName() {
		return name_;
	}
	
	@Override
	public String toString() {
		// Simple name for now.
		return name_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(attackVariance_);
		result = prime * result + Float.floatToIntBits(attack_);
		result = prime * result + Float.floatToIntBits(baseAttack_);
		result = prime * result + Float.floatToIntBits(baseDefense_);
		result = prime * result + baseValue_;
		result = prime
				* result
				+ ((colourDistribution_ == null) ? 0 : colourDistribution_
						.hashCode());
		result = prime * result + ((colour_ == null) ? 0 : colour_.hashCode());
		result = prime * result + Float.floatToIntBits(defenseVariance_);
		result = prime * result + Float.floatToIntBits(defense_);
		result = prime * result + ((genres_ == null) ? 0 : genres_.hashCode());
		result = prime * result + (individual_ ? 1231 : 1237);
		result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
		result = prime * result + value_;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Item other = (Item) obj;
		if (Float.floatToIntBits(attackVariance_) != Float
				.floatToIntBits(other.attackVariance_))
			return false;
		if (Float.floatToIntBits(attack_) != Float
				.floatToIntBits(other.attack_))
			return false;
		if (Float.floatToIntBits(baseAttack_) != Float
				.floatToIntBits(other.baseAttack_))
			return false;
		if (Float.floatToIntBits(baseDefense_) != Float
				.floatToIntBits(other.baseDefense_))
			return false;
		if (baseValue_ != other.baseValue_)
			return false;
		if (colourDistribution_ == null) {
			if (other.colourDistribution_ != null)
				return false;
		} else if (!colourDistribution_.equals(other.colourDistribution_))
			return false;
		if (colour_ == null) {
			if (other.colour_ != null)
				return false;
		} else if (!colour_.equals(other.colour_))
			return false;
		if (Float.floatToIntBits(defenseVariance_) != Float
				.floatToIntBits(other.defenseVariance_))
			return false;
		if (Float.floatToIntBits(defense_) != Float
				.floatToIntBits(other.defense_))
			return false;
		if (genres_ == null) {
			if (other.genres_ != null)
				return false;
		} else if (!genres_.equals(other.genres_))
			return false;
		if (individual_ != other.individual_)
			return false;
		if (name_ == null) {
			if (other.name_ != null)
				return false;
		} else if (!name_.equals(other.name_))
			return false;
		if (value_ != other.value_)
			return false;
		return true;
	}
}
