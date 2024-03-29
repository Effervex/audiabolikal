package audiabolikal.equipment;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import audiabolikal.util.ProbabilityDistribution;

/**
 * An abstract class of items, which can be subclassed into equipped gear. Note
 * that this class of item defines the set of all items of that type, not an
 * individual soldier's item, which must be created through the
 * getInidvidualItem method.
 * 
 * @author Samuel J. Sarjant
 */
public abstract class Item {
	/** The gaussian range in which items are considered 'ordinary'. */
	private static final double ORDINARY = 0.1;

	/**
	 * The maximum SD value. This value results in the best/worst possibility
	 * being chosen at ~1/500.
	 */
	private static final double MAX_SD = 2.23;

	/** The amount of level variance per SD for a generated item. */
	private static final int LEVEL_VARIANCE = 2;

	/** The minimum gain per level for each attribute in an item. */
	private static final float MIN_GAIN_PER_LEVEL = 0.2f;

	/** The coefficient for the base value when levelling. */
	private static final float BASE_COEFFICIENT = 0.1f;

	/** The name of the class. */
	protected String className_;

	/** Each item has at least one colour. A normalised distribution */
	private ProbabilityDistribution<Color> colourDistribution_;

	/**
	 * An item can be representative of many genres. This mapping represents the
	 * strength it relates to each genre.
	 */
	private Map<String, Double> genres_;

	/** The value, or cost of an item. */
	private float baseValue_;

	private float baseAttack_;
	private float attackVariance_;
	private float baseDefense_;
	private float defenseVariance_;
	private float baseHit_;
	private float hitVariance_;
	private float baseEvasion_;
	private float evasionVariance_;

	/** The random number generator for the mould. */
	private Random random_;

	/** The mesh for male models representing this item. */
	private File maleMeshFile_;

	/** The mesh for female models representing this item. */
	private File femaleMeshFile_;

	/** The texture for male models representing this item. */
	private File maleTextureFile_;

	/** The texture for female models representing this item. */
	private File femaleTextureFile_;

	/** The rotation values for the mesh. */
	private float[] rotation_;

	/** The scale values for the mesh. */
	private float[] scale_;

	/** The name of the item. */
	private String name_;

	/**
	 * A variable determining whether this class represents an individual item,
	 * or a general mould.
	 */
	private boolean individual_;

	/** The level of this item. */
	private int level_;

	private float attack_;
	private float defense_;
	private float hit_;
	private float evasion_;

	/** The value of the item. */
	private int value_;

	/** The fixed colour of an individual item. */
	private Color colour_;

	/**
	 * Basic constructor loads the class name.
	 */
	public Item() {
		className_ = this.getClass().getSimpleName();
	}

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
	 * @param baseAttack
	 *            The base hit for the item.
	 * @param attackVariance
	 *            The amount of variance the attack can have.
	 * @param attackVariance
	 *            The amount of variance the hit can have.
	 * @param baseDefense
	 *            The base defense of the item.
	 * @param baseDefense
	 *            The base evasion of the item.
	 * @param defenseVariance
	 *            The amount of variance the defense can have.
	 * @param defenseVariance
	 *            The amount of variance the evasion can have.
	 * @param maleMeshFile
	 *            The mesh file for the male model.
	 * @param femaleMeshFile
	 *            The mesh file for the female model.
	 * @param maleTextureFile
	 *            The texture file for the male model.
	 * @param rotationX
	 *            The x rotation of the model.
	 * @param rotationY
	 *            The y rotation of the model.
	 * @param rotationZ
	 *            The z rotation of the model.
	 * @param scaleX
	 *            The x scaling of the model.
	 * @param scaleY
	 *            The y scaling of the model.
	 * @param scaleZ
	 *            The z scaling of the model.
	 * @param femaleTexttureFile
	 *            The texture file for the female model.
	 */
	public void initialiseMouldItem(String name, Map<String, Double> genres,
			ProbabilityDistribution<Color> itemColors, float valueMod,
			float baseAttack, float attackVariance, float baseDefense,
			float defenseVariance, float baseHit, float hitVariance,
			float baseEvasion, float evasionVariance, File maleMeshFile,
			File femaleMeshFile, File maleTextureFile, File femaleTextureFile,
			float rotationX, float rotationY, float rotationZ, float scaleX,
			float scaleY, float scaleZ) {
		individual_ = false;
		name_ = name;
		genres_ = genres;
		colourDistribution_ = itemColors;
		colourDistribution_.normaliseProbs();
		baseValue_ = valueMod;
		baseAttack_ = baseAttack;
		baseDefense_ = baseDefense;
		attackVariance_ = attackVariance;
		defenseVariance_ = defenseVariance;
		baseHit_ = baseHit;
		baseEvasion_ = baseEvasion;
		hitVariance_ = hitVariance;
		evasionVariance_ = evasionVariance;
		maleMeshFile_ = maleMeshFile;
		femaleMeshFile_ = femaleMeshFile;
		maleTextureFile_ = maleTextureFile;
		femaleTextureFile_ = femaleTextureFile;
		rotation_ = new float[3];
		rotation_[0] = rotationX;
		rotation_[1] = rotationY;
		rotation_[2] = rotationZ;
		scale_ = new float[3];
		scale_[0] = scaleX;
		scale_[1] = scaleY;
		scale_[2] = scaleZ;
		random_ = new Random();
	}

	/**
	 * A shortened constructor.
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
	 * @param attributes
	 *            The attributes in 8 index array form: attribute, variance.
	 *            ATK, DEF, HIT, EVA.
	 * @param modelFiles
	 *            The files for the item models.
	 * @param rotation
	 *            The rotation values.
	 * @param scale
	 *            The scale values.
	 */
	public void initialiseMouldItem(String name, Map<String, Double> genres,
			ProbabilityDistribution<Color> itemColors, float valueMod,
			float[] attributes, File[] modelFiles, float[] rotation,
			float[] scale) {
		initialiseMouldItem(name, genres, itemColors, valueMod, attributes[0],
				attributes[1], attributes[2], attributes[3], attributes[4],
				attributes[5], attributes[6], attributes[7], modelFiles[0],
				modelFiles[1], modelFiles[2], modelFiles[3], rotation[0],
				rotation[1], rotation[2], scale[0], scale[1], scale[2]);
	}

	/**
	 * A constructor for creating an individual item with set attributes (for
	 * level 1). Items with higher levels are levelled appropriately from those
	 * values.
	 * 
	 * @param generalItem
	 *            The item that spawned this unique item.
	 * @param name
	 *            The name of the unique item, based on it's attack and defense.
	 * @param level
	 *            The level of this item.
	 * @param color
	 *            The colour of this individual item.
	 * @param attack
	 *            The attack of the item.
	 * @param defense
	 *            The defense of the item.
	 * @param hit
	 *            The hit of the item.
	 * @param evasion
	 *            The evasion of the item.
	 */
	private void initialiseIndividualItem(Item generalItem, String name,
			int level, Color color, float attack, float defense, float hit,
			float evasion) {
		individual_ = true;
		level_ = level;
		name_ = name;
		genres_ = generalItem.genres_;
		maleMeshFile_ = generalItem.maleMeshFile_;
		femaleMeshFile_ = generalItem.femaleMeshFile_;
		maleTextureFile_ = generalItem.maleTextureFile_;
		femaleTextureFile_ = generalItem.femaleTextureFile_;
		rotation_ = generalItem.rotation_;
		scale_ = generalItem.scale_;
		// Base attack now defines the levelling constant
		baseAttack_ = generalItem.baseAttack_;
		baseDefense_ = generalItem.baseDefense_;
		baseHit_ = generalItem.baseHit_;
		baseEvasion_ = generalItem.baseEvasion_;
		baseValue_ = generalItem.baseValue_;
		attack_ = Math.max(0, attack);
		defense_ = Math.max(0, defense);
		hit_ = Math.max(0, hit);
		evasion_ = Math.max(0, evasion);
		// Variance becomes the difference between base and actual, defining
		// level growth
		attackVariance_ = attack_ - baseAttack_;
		defenseVariance_ = defense_ - baseDefense_;
		hitVariance_ = hit_ - baseHit_;
		evasionVariance_ = evasion_ - baseEvasion_;

		// Apply the level to the base stats.
		attack_ = applyLevel(level - 1, attack, baseAttack_, attackVariance_);
		defense_ = applyLevel(level - 1, defense, baseDefense_,
				defenseVariance_);
		hit_ = applyLevel(level - 1, hit, baseHit_, hitVariance_);
		evasion_ = applyLevel(level - 1, evasion, baseEvasion_,
				evasionVariance_);

		// Calculate the end value
		value_ = calcItemValue(attack_, defense_, hit_, evasion_, baseValue_);
	}

	/**
	 * Spawns an individual item from the general mould, after calculating
	 * values.
	 * 
	 * @param level
	 *            The level of the item being spawned.
	 * @param strictLevel
	 *            If the level is an approximate.
	 * @return An individual item.
	 */
	public Item spawnIndividualItem(int level, boolean strictLevel) {
		if (!individual_) {
			// Calculate the fixed values
			double defGauss = random_.nextGaussian();
			float defense = (float) (baseDefense_ + defenseVariance_ * defGauss);
			double atkGauss = random_.nextGaussian();
			float attack = (float) (baseAttack_ + attackVariance_ * atkGauss);
			double hitGauss = random_.nextGaussian();
			float hit = (float) (baseHit_ + hitVariance_ * hitGauss);
			double evasionGauss = random_.nextGaussian();
			float evasion = (float) (baseEvasion_ + evasionVariance_
					* evasionGauss);
			Color fixedColour = colourDistribution_.sample();
			String name = generateDescriptor(baseAttack_, baseDefense_,
					atkGauss, defGauss, StrongPrefixes.values(), WeakPrefixes
							.values(), "Ordinary", ORDINARY)
					+ " "
					+ name_
					+ generateSuffix(baseHit_, baseEvasion_, hitGauss,
							evasionGauss);

			// Modify the level if not fixed
			if (!strictLevel) {
				level += random_.nextGaussian() * LEVEL_VARIANCE;
				level = Math.max(1, level);
			}

			try {
				Item unique = this.getClass().newInstance();
				unique.initialiseIndividualItem(this, name, level, fixedColour,
						attack, defense, hit, evasion);
				return unique;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Applies levelling to an attribute, calculated using the base attribute
	 * and attribute variance. An attribute will always receive at least 0.2,
	 * even if it has 0 base and variance.
	 * 
	 * The formula for calculating level gain is as follows: 0.1 * (base +
	 * variance / 2).
	 * 
	 * @param levelsGained
	 *            The level of the item.
	 * @param attribute
	 *            The initial unlevelled attribute of the item.
	 * @param baseAttribute
	 *            The base attribute used to determine the attribute.
	 * @param attackVariance
	 *            The base variance used to determine the attribute.
	 * @return The new value of the attribute, at least 0.2 higher than before.
	 */
	public static float applyLevel(int levelsGained, float attribute,
			float baseAttribute, float attributeVariance) {
		// Apply boosting for every level gained
		float gainPerLevel = BASE_COEFFICIENT
				* (baseAttribute + attributeVariance / 2);
		gainPerLevel = Math.max(gainPerLevel, MIN_GAIN_PER_LEVEL);

		return attribute + gainPerLevel * levelsGained;
	}

	/**
	 * Calculates the value of the item, based on the items attributes.
	 * 
	 * @param attack
	 *            The attack value.
	 * @param defense
	 *            The defense value.
	 * @param hit
	 *            The hit value.
	 * @param evasion
	 *            The evasion value.
	 * @return The value of the item, based on the attack, defense, hit and
	 *         evasion.
	 */
	private int calcItemValue(float attack, float defense, float hit,
			float evasion, float valueMod) {
		// If attack and defense are 0, just use the value
		if ((attack + defense + hit + evasion) <= 0)
			return (int) valueMod;

		// Simply add and multiply.
		// TODO Investigate alternative methods. Perhaps exponential?
		return (int) ((attack + defense + hit + evasion) * valueMod);
	}

	/**
	 * Generates a descriptor, drawing a value from a list of values or, if the
	 * item is plain,
	 * 
	 * @param baseA
	 *            The base attack of the item.
	 * @param baseB
	 *            The base defense of the item.
	 * @param aGauss
	 *            The attack gaussian number.
	 * @param bGauss
	 *            The defense gaussian number.
	 * @param goodValues
	 *            The good values.
	 * @param badValues
	 *            The bad values.
	 * @param normal
	 *            The normal value.
	 * @return A string prefix corresponding to the dominant attribute gaussian
	 *         value.
	 */
	public static String generateDescriptor(float baseA, float baseB,
			double aGauss, double bGauss, Object[] goodValues,
			Object[] badValues, String normal, double normalVal) {
		// Attack is dominant
		double gauss = 0;
		if (baseA >= baseB)
			gauss = aGauss;
		else
			gauss = bGauss;

		// Choosing the prefix set.
		Object[] prefixes = null;
		if (gauss > normalVal)
			prefixes = goodValues;
		else if (gauss < -normalVal)
			prefixes = badValues;
		else
			return normal;

		// Normalise the value between 0 and 1 and use it as index lookup
		gauss = Math.abs(gauss);
		gauss -= ORDINARY;
		gauss /= MAX_SD;
		gauss = (gauss > 1) ? 1 : gauss;
		int index = (int) Math.floor((prefixes.length - 1) * gauss);
		return prefixes[index].toString();
	}

	/**
	 * Generates a suffix if the hit or evasion gaussian values are particularly
	 * good/bad.
	 * 
	 * @param baseHit
	 *            The base hit.
	 * @param baseEvasion
	 *            The base evasion
	 * @param hitGauss
	 *            The hit gauss.
	 * @param evasionGauss
	 *            The evasion gauss.
	 * @return A suffix, or "" if the values aren't strong enough.
	 */
	public String generateSuffix(float baseHit, float baseEvasion,
			double hitGauss, double evasionGauss) {
		// If hit and evasion aren't used
		if (baseHit == baseEvasion)
			return "";
		Object[] goodValues = null;
		Object[] badValues = null;

		if (baseHit > baseEvasion) {
			goodValues = StrongHitSuffixes.values();
			badValues = WeakHitSuffixes.values();
		} else {
			goodValues = StrongEvasionSuffixes.values();
			badValues = WeakEvasionSuffixes.values();
		}

		// Less chance of a suffix
		String suffix = generateDescriptor(baseHit, baseEvasion, hitGauss,
				evasionGauss, goodValues, badValues, "", ORDINARY * 5);
		if (suffix.equals(""))
			return "";
		return " of " + suffix;
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
	 * Gets the attack.
	 * 
	 * @return The attack.
	 */
	public float getAttack() {
		return attack_;
	}

	/**
	 * Gets the defense.
	 * 
	 * @return The defense.
	 */
	public float getDefense() {
		return defense_;
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
	 * Gets the base attack.
	 * 
	 * @return The base attack.
	 */
	public float getBaseAttack() {
		return baseAttack_;
	}

	/**
	 * Gets the base defense.
	 * 
	 * @return The base defense.
	 */
	public float getBaseDefense() {
		return baseDefense_;
	}

	/**
	 * Gets the base hit.
	 * 
	 * @return The base hit.
	 */
	public float getBaseHit() {
		return baseHit_;
	}

	/**
	 * Gets the base evasion.
	 * 
	 * @return The base evasion.
	 */
	public float getBaseEvasion() {
		return baseEvasion_;
	}

	/**
	 * Gets the attack variance.
	 * 
	 * @return The attack variance.
	 */
	public float getAttackVariance() {
		return attackVariance_;
	}

	/**
	 * Gets the defense variance.
	 * 
	 * @return The defense variance.
	 */
	public float getDefenseVariance() {
		return defenseVariance_;
	}

	/**
	 * Gets the hit variance.
	 * 
	 * @return The hit variance.
	 */
	public float getHitVariance() {
		return hitVariance_;
	}

	/**
	 * Gets the evasion variance.
	 * 
	 * @return The evasion variance.
	 */
	public float getEvasionVariance() {
		return evasionVariance_;
	}

	/**
	 * Gets the value mod for this item.
	 * 
	 * @return The value mod.
	 */
	public float getValueMod() {
		return baseValue_;
	}

	/**
	 * Gets the name of the item.
	 * 
	 * @return The item name.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Gets the ietm level.
	 * 
	 * @return The item level.
	 */
	public int getLevel() {
		return level_;
	}

	/**
	 * Gets the class name. Technically a static method, but it needs to be
	 * shared among subclasses.
	 * 
	 * @return The class name.
	 */
	public String getClassName() {
		return className_;
	}

	public Color getColor() {
		return colour_;
	}

	public ProbabilityDistribution<Color> getColorDistribution() {
		return colourDistribution_;
	}

	/**
	 * @return the maleMeshFile_
	 */
	public File getMaleMeshFile() {
		return maleMeshFile_;
	}

	/**
	 * @return the femaleMeshFile_
	 */
	public File getFemaleMeshFile() {
		return femaleMeshFile_;
	}

	/**
	 * @return the maleTextureFile_
	 */
	public File getMaleTextureFile() {
		return maleTextureFile_;
	}

	/**
	 * @return the femaleTextureFile_
	 */
	public File getFemaleTextureFile() {
		return femaleTextureFile_;
	}

	/**
	 * @return the rotation_
	 */
	public float[] getRotation() {
		return rotation_;
	}

	/**
	 * @return the scale_
	 */
	public float[] getScale() {
		return scale_;
	}

	/**
	 * Clones a general item mould.
	 * 
	 * @return A clone of the item.
	 */
	public Item cloneGeneral() {
		try {
			Item clone = this.getClass().newInstance();
			String name = name_;
			Map<String, Double> genres = new HashMap<String, Double>(genres_);
			ProbabilityDistribution<Color> itemColors = colourDistribution_.clone();
			float valueMod = baseValue_;
			float baseAttack = baseAttack_;
			float attackVariance = attackVariance_;
			float baseDefense = baseDefense_;
			float defenseVariance = defenseVariance_;
			float baseHit = baseHit_;
			float hitVariance = hitVariance_;
			float baseEvasion = baseEvasion_;
			float evasionVariance = evasionVariance_;
			File maleMeshFile = maleMeshFile_;
			File maleTextureFile = maleTextureFile_;
			File femaleMeshFile = femaleMeshFile_;
			File femaleTextureFile = femaleTextureFile_;
			float rotationX = rotation_[0];
			float rotationY = rotation_[1];
			float rotationZ = rotation_[2];
			float scaleX = scale_[0];
			float scaleY = scale_[1];
			float scaleZ = scale_[2];
			clone.initialiseMouldItem(name, genres, itemColors, valueMod,
					baseAttack, attackVariance, baseDefense, defenseVariance,
					baseHit, hitVariance, baseEvasion, evasionVariance,
					maleMeshFile, femaleMeshFile, maleTextureFile,
					femaleTextureFile, rotationX, rotationY, rotationZ, scaleX,
					scaleY, scaleZ);
			return clone;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(attackVariance_);
		result = prime * result + Float.floatToIntBits(attack_);
		result = prime * result + Float.floatToIntBits(baseAttack_);
		result = prime * result + Float.floatToIntBits(baseDefense_);
		result = prime * result + Float.floatToIntBits(baseEvasion_);
		result = prime * result + Float.floatToIntBits(baseHit_);
		result = prime * result + Float.floatToIntBits(baseValue_);
		result = prime * result
				+ ((className_ == null) ? 0 : className_.hashCode());
		result = prime
				* result
				+ ((colourDistribution_ == null) ? 0 : colourDistribution_
						.hashCode());
		result = prime * result + ((colour_ == null) ? 0 : colour_.hashCode());
		result = prime * result + Float.floatToIntBits(defenseVariance_);
		result = prime * result + Float.floatToIntBits(defense_);
		result = prime * result + Float.floatToIntBits(evasionVariance_);
		result = prime * result + Float.floatToIntBits(evasion_);
		result = prime * result
				+ ((femaleMeshFile_ == null) ? 0 : femaleMeshFile_.hashCode());
		result = prime
				* result
				+ ((femaleTextureFile_ == null) ? 0 : femaleTextureFile_
						.hashCode());
		result = prime * result + ((genres_ == null) ? 0 : genres_.hashCode());
		result = prime * result + Float.floatToIntBits(hitVariance_);
		result = prime * result + Float.floatToIntBits(hit_);
		result = prime * result + (individual_ ? 1231 : 1237);
		result = prime * result + level_;
		result = prime * result
				+ ((maleMeshFile_ == null) ? 0 : maleMeshFile_.hashCode());
		result = prime
				* result
				+ ((maleTextureFile_ == null) ? 0 : maleTextureFile_.hashCode());
		result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
		result = prime * result
				+ ((rotation_ == null) ? 0 : rotation_.hashCode());
		result = prime * result + ((scale_ == null) ? 0 : scale_.hashCode());
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
		Item other = (Item) obj;
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
		if (Float.floatToIntBits(baseEvasion_) != Float
				.floatToIntBits(other.baseEvasion_))
			return false;
		if (Float.floatToIntBits(baseHit_) != Float
				.floatToIntBits(other.baseHit_))
			return false;
		if (Float.floatToIntBits(baseValue_) != Float
				.floatToIntBits(other.baseValue_))
			return false;
		if (className_ == null) {
			if (other.className_ != null)
				return false;
		} else if (!className_.equals(other.className_))
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
		if (Float.floatToIntBits(evasionVariance_) != Float
				.floatToIntBits(other.evasionVariance_))
			return false;
		if (Float.floatToIntBits(evasion_) != Float
				.floatToIntBits(other.evasion_))
			return false;
		if (femaleMeshFile_ == null) {
			if (other.femaleMeshFile_ != null)
				return false;
		} else if (!femaleMeshFile_.equals(other.femaleMeshFile_))
			return false;
		if (femaleTextureFile_ == null) {
			if (other.femaleTextureFile_ != null)
				return false;
		} else if (!femaleTextureFile_.equals(other.femaleTextureFile_))
			return false;
		if (genres_ == null) {
			if (other.genres_ != null)
				return false;
		} else if (!genres_.equals(other.genres_))
			return false;
		if (Float.floatToIntBits(hitVariance_) != Float
				.floatToIntBits(other.hitVariance_))
			return false;
		if (Float.floatToIntBits(hit_) != Float.floatToIntBits(other.hit_))
			return false;
		if (individual_ != other.individual_)
			return false;
		if (level_ != other.level_)
			return false;
		if (maleMeshFile_ == null) {
			if (other.maleMeshFile_ != null)
				return false;
		} else if (!maleMeshFile_.equals(other.maleMeshFile_))
			return false;
		if (maleTextureFile_ == null) {
			if (other.maleTextureFile_ != null)
				return false;
		} else if (!maleTextureFile_.equals(other.maleTextureFile_))
			return false;
		if (name_ == null) {
			if (other.name_ != null)
				return false;
		} else if (!name_.equals(other.name_))
			return false;
		if (rotation_ == null) {
			if (other.rotation_ != null)
				return false;
		} else if (!rotation_.equals(other.rotation_))
			return false;
		if (scale_ == null) {
			if (other.scale_ != null)
				return false;
		} else if (!scale_.equals(other.scale_))
			return false;
		if (value_ != other.value_)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (!individual_) {
			buffer.append(name_ + " - ");
			buffer.append("ATK: " + (int) Math.round(baseAttack_) + " � "
					+ (int) Math.round(attackVariance_) + ", ");
			buffer.append("DEF: " + (int) Math.round(baseDefense_) + " � "
					+ (int) Math.round(defenseVariance_) + ", ");
			buffer.append("HIT: " + (int) Math.round(baseHit_) + " � "
					+ (int) Math.round(hitVariance_) + ", ");
			buffer.append("EVA: " + (int) Math.round(baseEvasion_) + " � "
					+ (int) Math.round(evasionVariance_));
		} else {
			buffer.append("Level " + level_ + " " + name_ + "\n");
			buffer.append("ATK: " + (int) Math.round(attack_) + ", ");
			buffer.append("DEF: " + (int) Math.round(defense_) + ", ");
			buffer.append("HIT: " + (int) Math.round(hit_) + ", ");
			buffer.append("EVA: " + (int) Math.round(evasion_));
		}
		return buffer.toString();
	}

	public void setName(String string) {
		name_ = string;
	}

}
