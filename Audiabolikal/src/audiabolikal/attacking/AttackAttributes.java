package audiabolikal.attacking;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
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

	/**
	 * Gets the details of this attack, using the attributes to form a mapping
	 * of damage details.
	 * 
	 * @return A mapping of points which are under the attack range, assuming
	 *         they fit the height requirements.
	 */
	public Map<Point, DamageDetail> getAttackDetails(AttackType type,
			Point soldierDirection) {
		Map<Point, DamageDetail> attackPoints = new HashMap<Point, DamageDetail>();

		int rangeLevel = greaterRange_ / 2;
		int rangeIndex = 2;
		// Odd level
		if ((greaterRange_ & 1) != 0) {
			rangeIndex = rangeLevelIndex_;
		}
		Collection<Pair<Point, Float>> rawPoints = RangePoints.getRangedPoints(
				type, rangeLevel, rangeIndex);

		// Calculate the rotation
		double rotation = 0;
		if (soldierDirection.y == -1)
			rotation = Math.PI;
		else if (soldierDirection.x == -1)
			rotation = Math.PI / 2;
		else if (soldierDirection.x == 1)
			rotation = Math.PI * 1.5;

		DamageDetail unmodifiedDD = createBaseDD();

		// For each point, apply the modifier and add it.
		for (Pair<Point, Float> point : rawPoints) {
			DamageDetail dd = unmodifiedDD.clone();
			dd.applyModifier(point.getB());

			// Rotate the point
			Point point2 = point.getA();
			if (rotation != 0) {
				point2 = new Point((int) (Math.cos(rotation) * point2.x
						- Math.sin(rotation) * point2.y), (int) (Math.sin(rotation)
						* point2.x + Math.cos(rotation) * point2.y));
			}
			
			attackPoints.put(point2, dd);
		}

		return attackPoints;
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
		dd.setDamagePercent(1 + greaterHeight_ * HEIGHT_PER_LEVEL);

		// Rebuff chance
		dd.setRebuffChance1(REBUFF_CHANCE_PER_LEVEL * rebuff_);
		dd.setRebuffChance2(REBUFF_CHANCE_PER_LEVEL
				* Math.max(rebuff_ - REBUFF_2_LEVEL, 0));
		dd.setRebuffChance3(REBUFF_CHANCE_PER_LEVEL
				* Math.max(rebuff_ - REBUFF_3_LEVEL, 0));

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
		dd
				.setStealChance2(STEAL_CHANCE_2
						* Math.max(steal_ - STEAL_2_LEVEL, 0));
		dd
				.setStealChance3(STEAL_CHANCE_3
						* Math.max(steal_ - STEAL_3_LEVEL, 0));
		dd
				.setStealChance4(STEAL_CHANCE_4
						* Math.max(steal_ - STEAL_4_LEVEL, 0));

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
		if (aaCurrentLevel == MAX_ATTRIBUTE_LEVEL)
			return -1;

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
		return true;
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
				return true;
			}
			break;
		case GREATER_RANGE:
			if (greaterRange_ < MAX_ATTRIBUTE_LEVEL) {
				greaterRange_++;
				return true;
			}
			break;
		case GREATER_HEIGHT:
			if (greaterHeight_ < MAX_ATTRIBUTE_LEVEL) {
				greaterHeight_++;
				return true;
			}
			break;
		case REBUFF:
			if (rebuff_ < MAX_ATTRIBUTE_LEVEL) {
				rebuff_++;
				return true;
			}
			break;
		case REPOSITION:
			if (reposition_ < MAX_ATTRIBUTE_LEVEL) {
				reposition_++;
				return true;
			}
			break;
		case STEAL:
			if (steal_ < MAX_ATTRIBUTE_LEVEL) {
				steal_++;
				return true;
			}
			break;
		case POISON:
			if (poison_ < MAX_ATTRIBUTE_LEVEL) {
				poison_++;
				return true;
			}
			break;
		case WEAKEN:
			if (weaken_ < MAX_ATTRIBUTE_LEVEL) {
				weaken_++;
				return true;
			}
			break;
		case CORRODE:
			if (corrode_ < MAX_ATTRIBUTE_LEVEL) {
				corrode_++;
				return true;
			}
			break;
		case BLEAR:
			if (blear_ < MAX_ATTRIBUTE_LEVEL) {
				blear_++;
				return true;
			}
			break;
		case SLOW:
			if (slow_ < MAX_ATTRIBUTE_LEVEL) {
				slow_++;
				return true;
			}
			break;
		}
		return false;
	}
}
