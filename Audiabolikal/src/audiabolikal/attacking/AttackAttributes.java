package audiabolikal.attacking;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import audiabolikal.util.Pair;

/**
 * A structure for containing the special attributes an attack can have.
 * 
 * @author Samuel J. Sarjant
 */
public class AttackAttributes {
	// Attribute constants
	private static final int MAX_ATTRIBUTE_LEVEL = 10;
	private static final int LEVELLING_COEFFICIENT = 5;
	// Greater damage constants
	private static final float DAMAGE_PER_LEVEL = 0.6f;
	// Greater range constants
	private static final float RANGE_DAMAGE_PER_LEVEL = 0.2f;
	// Greater height constants
	private static final float HEIGHT_PER_LEVEL = 0.2f;
	// Rebuff constants
	private static final float REBUFF_CHANCE_PER_LEVEL = 0.1f;
	private static final int REBUFF_2_LEVEL = 6;
	private static final int REBUFF_3_LEVEL = 9;
	// Reposition constants
	private static final float REPOSITION_HEIGHT_PER_LEVEL = 0.2f;
	private static final int REPOSITION_2_LEVEL = 5;
	private static final int REPOSITION_3_LEVEL = 8;
	private static final int REPOSITION_4_LEVEL = 10;
	// Steal constants
	private static final float STEAL_CHANCE_1 = 0.02f;
	private static final float STEAL_CHANCE_2 = 0.025f;
	private static final float STEAL_CHANCE_3 = 0.025f;
	private static final float STEAL_CHANCE_4 = 0.02f;
	private static final int STEAL_2_LEVEL = 5;
	private static final int STEAL_3_LEVEL = 9;
	private static final int STEAL_4_LEVEL = 10;
	// Debuffing constants
	private static final float DEBUFF_CHANCE_PER_LEVEL = 0.02f;
	private static final int MIN_DEBUFF_TURNS = 3;
	private static final int MAX_DEBUFF_TURNS = 5;

	// Levels of the attack. Initially all at 0.
	/** Greater damage level. */
	private int greaterDamage_;

	/** Greater range level. */
	private int greaterRange_;

	/** When the range level is odd, a particular index must be selected. */
	private int rangeLevelIndex_;

	/** Greater height level. */
	private int greaterHeight_;

	/** Rebuff level. */
	private int rebuff_;

	/** Repositions level. */
	private int reposition_;

	/** Stealing level. */
	private int steal_;

	/** Poison level. */
	private int poison_;

	/** Weaken level. */
	private int weaken_;

	/** Corrode level. */
	private int corrode_;

	/** Blear level. */
	private int blear_;

	/** Slow level. */
	private int slow_;

	/** The type of attack this attack attribute is. */
	private AttackType attackType_;

	/** The attack points of the attack. */
	private Map<Point, DamageDetail> attackPoints_;

	public AttackAttributes(AttackType type) {
		attackType_ = type;
		updateAttackDetails();
	}

	/**
	 * Gets the details of this attack, using the attributes to form a mapping
	 * of damage details.
	 * 
	 * @return A mapping of points which are under the attack range, assuming
	 *         they fit the height requirements.
	 */
	public Map<Point, DamageDetail> getAttackDetails(Point soldierDirection) {
		if (attackPoints_ == null) {
			updateAttackDetails();
		}

		// Calculate the rotation
		double rotation = 0;
		if (soldierDirection.y == -1)
			rotation = Math.PI;
		else if (soldierDirection.x == -1)
			rotation = Math.PI / 2;
		else if (soldierDirection.x == 1)
			rotation = Math.PI * 1.5;

		// Apply rotation
		Map<Point, DamageDetail> rotatedPoints = attackPoints_;
		if (rotation != 0) {
			rotatedPoints = new HashMap<Point, DamageDetail>();
			for (Point point : attackPoints_.keySet()) {
				Point point2 = new Point((int) Math.round(Math.cos(rotation)
						* point.x - Math.sin(rotation) * point.y), (int) Math
						.round(Math.sin(rotation) * point.x
								+ Math.cos(rotation) * point.y));
				rotatedPoints.put(point2, attackPoints_.get(point));
			}
		}

		return rotatedPoints;
	}

	/**
	 * Updates the attack attributes.
	 */
	private void updateAttackDetails() {
		attackPoints_ = new HashMap<Point, DamageDetail>();

		int rangeLevel = (greaterRange_ + 1) / 2;
		int rangeIndex = 2;
		// Odd level
		if ((greaterRange_ & 1) != 0) {
			rangeIndex = rangeLevelIndex_;
		}
		Collection<Pair<Point, Float>> rawPoints = getRangedPoints(attackType_,
				rangeLevel, rangeIndex, RANGE_DAMAGE_PER_LEVEL);

		DamageDetail unmodifiedDD = createBaseDD();

		// For each point, apply the modifier and add it.
		for (Pair<Point, Float> point : rawPoints) {
			DamageDetail dd = unmodifiedDD.clone();
			dd.applyModifier(point.getB());

			attackPoints_.put(point.getA(), dd);
		}
	}

	/**
	 * Creates a base DamageDetail which contains all the attributes bar the
	 * range.
	 * 
	 * @return A DamageDetail object including all attributes bar the range.
	 */
	private DamageDetail createBaseDD() {
		DamageDetail dd = new DamageDetail();
		// Base Damage
		dd.setDamagePercent(1 + greaterDamage_ * DAMAGE_PER_LEVEL);

		// Height
		dd.setHeightMod(1 + greaterHeight_ * HEIGHT_PER_LEVEL);

		// Rebuff chance
		dd.setRebuffChance1(REBUFF_CHANCE_PER_LEVEL * rebuff_);
		dd.setRebuffChance2(REBUFF_CHANCE_PER_LEVEL
				* Math.max(rebuff_ - REBUFF_2_LEVEL + 1, 0));
		dd.setRebuffChance3(REBUFF_CHANCE_PER_LEVEL
				* Math.max(rebuff_ - REBUFF_3_LEVEL + 1, 0));

		// Reposition
		if (reposition_ >= REPOSITION_4_LEVEL) {
			dd.setRepositionMove(4);
		} else if (reposition_ >= REPOSITION_3_LEVEL) {
			dd.setRepositionMove(3);
		} else if (reposition_ >= REPOSITION_2_LEVEL) {
			dd.setRepositionMove(2);
		} else if (reposition_ >= 1) {
			dd.setRepositionMove(1);
		}
		dd.setRepositionHeight(REPOSITION_HEIGHT_PER_LEVEL * reposition_);

		// Steal
		dd.setStealChance1(STEAL_CHANCE_1 * steal_);
		dd.setStealChance2(STEAL_CHANCE_2
				* Math.max(steal_ - STEAL_2_LEVEL + 1, 0));
		dd.setStealChance3(STEAL_CHANCE_3
				* Math.max(steal_ - STEAL_3_LEVEL + 1, 0));
		dd.setStealChance4(STEAL_CHANCE_4
				* Math.max(steal_ - STEAL_4_LEVEL + 1, 0));

		// Poison
		Random random = new Random();
		dd.setPoisonChance(DEBUFF_CHANCE_PER_LEVEL * poison_);
		dd.setPoisonTurns(MIN_DEBUFF_TURNS
				+ random.nextInt(MAX_DEBUFF_TURNS - MIN_DEBUFF_TURNS + 1));

		// Weaken
		dd.setWeakenChance(DEBUFF_CHANCE_PER_LEVEL * weaken_);
		dd.setWeakenTurns(MIN_DEBUFF_TURNS
				+ random.nextInt(MAX_DEBUFF_TURNS - MIN_DEBUFF_TURNS + 1));

		// Corrode
		dd.setCorrodeChance(DEBUFF_CHANCE_PER_LEVEL * corrode_);
		dd.setCorrodeTurns(MIN_DEBUFF_TURNS
				+ random.nextInt(MAX_DEBUFF_TURNS - MIN_DEBUFF_TURNS + 1));

		// Blear
		dd.setBlearChance(DEBUFF_CHANCE_PER_LEVEL * blear_);
		dd.setBlearTurns(MIN_DEBUFF_TURNS
				+ random.nextInt(MAX_DEBUFF_TURNS - MIN_DEBUFF_TURNS + 1));

		// Slow
		dd.setSlowChance(DEBUFF_CHANCE_PER_LEVEL * slow_);
		dd.setSlowTurns(MIN_DEBUFF_TURNS
				+ random.nextInt(MAX_DEBUFF_TURNS - MIN_DEBUFF_TURNS + 1));

		return dd;
	}

	/**
	 * Finds the attack experience required for the attribute's next level.
	 * 
	 * @param aa
	 *            The attack attribute in question.
	 * @return The amount of attack experience required for the next level, or
	 *         -1 if the level is maxed.
	 */
	public int attackExpRequired(AttackAttributeEnum aa) {
		// Formula for attack exp
		// Level * LEVELLING_COEFICIENT + Level-1 * LEVELLING_COEFFICIENT +
		// Level-2 * ...
		int aaCurrentLevel = lookupLevel(aa);
		if (aaCurrentLevel >= MAX_ATTRIBUTE_LEVEL)
			return -1;

		// Needs to be incremented for the formula to work
		aaCurrentLevel++;

		return LEVELLING_COEFFICIENT * aaCurrentLevel * (aaCurrentLevel + 1)
				/ 2;
	}

	/**
	 * Looks up the current level of the attribute in question.
	 * 
	 * @param aa
	 *            The attack attribute enum.
	 * @return The level of the attribute: between 0 and 10, or -1 if aa is
	 *         illegal.
	 */
	private int lookupLevel(AttackAttributeEnum aa) {
		switch (aa) {
		case GREATER_DAMAGE:
			return greaterDamage_;
		case GREATER_RANGE:
			return greaterRange_;
		case GREATER_HEIGHT:
			return greaterHeight_;
		case REBUFF:
			return rebuff_;
		case REPOSITION:
			return reposition_;
		case STEAL:
			return steal_;
		case POISON:
			return poison_;
		case WEAKEN:
			return weaken_;
		case CORRODE:
			return corrode_;
		case BLEAR:
			return blear_;
		case SLOW:
			return slow_;
		}
		return -1;
	}

	/**
	 * Sets the level of an attribute to a specified value (between 0 and 10).
	 * 
	 * @param aa
	 *            The attribute to set
	 * @param level
	 *            The level value to set, between 0 and 10.
	 * @return True if the level is withint he correct range.
	 */
	public boolean setLevel(AttackAttributeEnum aa, int level) {
		if ((level > MAX_ATTRIBUTE_LEVEL) || (level < 0))
			return false;
		switch (aa) {
		case GREATER_DAMAGE:
			greaterDamage_ = level;
			break;
		case GREATER_RANGE:
			greaterRange_ = level;
			break;
		case GREATER_HEIGHT:
			greaterHeight_ = level;
			break;
		case REBUFF:
			rebuff_ = level;
			break;
		case REPOSITION:
			reposition_ = level;
			break;
		case STEAL:
			steal_ = level;
			break;
		case POISON:
			poison_ = level;
			break;
		case WEAKEN:
			weaken_ = level;
			break;
		case CORRODE:
			corrode_ = level;
			break;
		case BLEAR:
			blear_ = level;
			break;
		case SLOW:
			slow_ = level;
			break;
		default:
			return false;
		}

		updateAttackDetails();
		return true;
	}

	/**
	 * Sets the range level.
	 * 
	 * @param index
	 *            The index chosen.
	 */
	public void setRangeIndex(int index) {
		rangeLevelIndex_ = index;
		updateAttackDetails();
	}

	/**
	 * Gets the range index.
	 * 
	 * @return The range index.
	 */
	public int getRangeIndex() {
		return rangeLevelIndex_;
	}

	/**
	 * Increments the level of an attribute unless it is at max.
	 * 
	 * @param aa
	 *            The attribute to set
	 * @return True if the level being incremented isn't at already at max.
	 */
	public boolean incrementLevel(AttackAttributeEnum aa) {
		switch (aa) {
		case GREATER_DAMAGE:
			if (greaterDamage_ < MAX_ATTRIBUTE_LEVEL) {
				greaterDamage_++;
				updateAttackDetails();
				return true;
			}
			break;
		case GREATER_RANGE:
			if (greaterRange_ < MAX_ATTRIBUTE_LEVEL) {
				greaterRange_++;
				updateAttackDetails();
				return true;
			}
			break;
		case GREATER_HEIGHT:
			if (greaterHeight_ < MAX_ATTRIBUTE_LEVEL) {
				greaterHeight_++;
				updateAttackDetails();
				return true;
			}
			break;
		case REBUFF:
			if (rebuff_ < MAX_ATTRIBUTE_LEVEL) {
				rebuff_++;
				updateAttackDetails();
				return true;
			}
			break;
		case REPOSITION:
			if (reposition_ < MAX_ATTRIBUTE_LEVEL) {
				reposition_++;
				updateAttackDetails();
				return true;
			}
			break;
		case STEAL:
			if (steal_ < MAX_ATTRIBUTE_LEVEL) {
				steal_++;
				updateAttackDetails();
				return true;
			}
			break;
		case POISON:
			if (poison_ < MAX_ATTRIBUTE_LEVEL) {
				poison_++;
				updateAttackDetails();
				return true;
			}
			break;
		case WEAKEN:
			if (weaken_ < MAX_ATTRIBUTE_LEVEL) {
				weaken_++;
				updateAttackDetails();
				return true;
			}
			break;
		case CORRODE:
			if (corrode_ < MAX_ATTRIBUTE_LEVEL) {
				corrode_++;
				updateAttackDetails();
				return true;
			}
			break;
		case BLEAR:
			if (blear_ < MAX_ATTRIBUTE_LEVEL) {
				blear_++;
				updateAttackDetails();
				return true;
			}
			break;
		case SLOW:
			if (slow_ < MAX_ATTRIBUTE_LEVEL) {
				slow_++;
				updateAttackDetails();
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * Gets the range points for an attack at a set level and index, if
	 * necessary. This method assumes the soldier is facing north (point (0,1))
	 * is the default attack.
	 * 
	 * @param type
	 *            The attack type.
	 * @param level
	 *            The level of range, between 0 and 5.
	 * @param levelIndex
	 *            The index of the range, either 0, 1, or 2 (both).
	 * @param modifierPerLevel
	 *            The amount the modifier increases with level.
	 * @return A collection of points with their damage modifiers.
	 */
	public static Collection<Pair<Point, Float>> getRangedPoints(
			AttackType type, int level, int levelIndex, float modifierPerLevel) {
		Collection<Pair<Point, Float>> rangedPoints = new HashSet<Pair<Point, Float>>();

		// Each ranged point has the immediate point (0)
		rangedPoints.add(new Pair<Point, Float>(new Point(0, 1), 1f));

		if (type.equals(AttackType.NORMAL))
			return rangedPoints;

		// Next attack points depend on the attack type and the level (and
		// index).
		Point[] attackPoints = type.getAttackPoints();
		float modifier = modifierPerLevel;
		for (; level > 0; level--) {
			addPoints(levelIndex, rangedPoints, modifier,
					attackPoints[level * 2 - 2], attackPoints[level * 2 - 1]);
			modifier += modifierPerLevel;
			levelIndex = 2;
		}

		return rangedPoints;
	}

	/**
	 * Adds a point or points to the range points with an appropriate modifier,
	 * and increases the modifier for the next lower level.
	 * 
	 * @param levelIndex
	 *            The level index of the current level.
	 * @param rangePoints
	 *            The set of points being added to.
	 * @param modifier
	 *            The modifier to add the point/s at.
	 * @param point1
	 *            Index 0 of the points.
	 * @param point2
	 *            Index 1 of the points.
	 * @return The new modifier.
	 */
	private static void addPoints(int levelIndex,
			Collection<Pair<Point, Float>> rangePoints, float modifier,
			Point point1, Point point2) {
		if ((levelIndex == 0) || (levelIndex == 2))
			rangePoints.add(new Pair<Point, Float>(point1, modifier));
		if ((levelIndex == 1) || (levelIndex == 2))
			rangePoints.add(new Pair<Point, Float>(point2, modifier));
	}

	/**
	 * Gets the level of the attribute.
	 * 
	 * @param attribute
	 * @return The level of the attribute.
	 */
	public Object getLevel(AttackAttributeEnum attribute) {
		switch (attribute) {
		case GREATER_DAMAGE:
			return greaterDamage_;
		case GREATER_RANGE:
			return greaterRange_;
		case GREATER_HEIGHT:
			return greaterHeight_;
		case REBUFF:
			return rebuff_;
		case REPOSITION:
			return reposition_;
		case STEAL:
			return steal_;
		case POISON:
			return poison_;
		case WEAKEN:
			return weaken_;
		case CORRODE:
			return corrode_;
		case BLEAR:
			return blear_;
		case SLOW:
			return slow_;
		}
		return -1;
	}
}
