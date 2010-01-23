package audiabolikal.test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import audiabolikal.attacking.AttackAttributeEnum;
import audiabolikal.attacking.AttackAttributes;
import audiabolikal.attacking.AttackType;
import audiabolikal.attacking.DamageDetail;
import audiabolikal.util.Pair;

public class AttackAttributesTest {
	private AttackAttributes sut_;

	@Before
	public void setUp() throws Exception {
		sut_ = new AttackAttributes(AttackType.DOWNWARD);
	}

	@Test
	public void testGetAttackDetails() {
		// Basic test
		Map<Point, DamageDetail> mapping = sut_.getAttackDetails(
				new Point(0, 1));
		assertEquals(mapping.size(), 1);
		assertTrue(mapping.containsKey(new Point(0, 1)));
		assertEquals(1, mapping.get(new Point(0, 1)).getDamagePercent(), 0.001);
		assertEquals(1, mapping.get(new Point(0, 1)).getHeightMod(), 0.001);

		// Greater Range
		sut_.setLevel(AttackAttributeEnum.GREATER_RANGE, 1);
		sut_.setRangeIndex(0);
		mapping = sut_.getAttackDetails(new Point(0, 1));
		assertEquals(mapping.size(), 2);
		assertTrue(mapping.containsKey(new Point(0, 1)));
		assertEquals(1, mapping.get(new Point(0, 1)).getDamagePercent(), 0.001);
		assertEquals(1, mapping.get(new Point(0, 1)).getHeightMod(), 0.001);
		assertTrue(mapping.containsKey(new Point(-1, 1)));
		assertEquals(0.2, mapping.get(new Point(-1, 1)).getDamagePercent(),
				0.001);
		assertEquals(1, mapping.get(new Point(-1, 1)).getHeightMod(), 0.001);

		// Rotated case
		sut_.setLevel(AttackAttributeEnum.GREATER_RANGE, 4);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(mapping.size(), 5);
		assertTrue(mapping.containsKey(new Point(-1, 0)));
		assertEquals(1, mapping.get(new Point(-1, 0)).getDamagePercent(), 0.001);
		assertTrue(mapping.containsKey(new Point(-1, -1)));
		assertEquals(0.4, mapping.get(new Point(-1, -1)).getDamagePercent(),
				0.001);
		assertTrue(mapping.containsKey(new Point(-1, 1)));
		assertEquals(0.4, mapping.get(new Point(-1, 1)).getDamagePercent(),
				0.001);
		assertTrue(mapping.containsKey(new Point(-2, -1)));
		assertEquals(0.2, mapping.get(new Point(-2, -1)).getDamagePercent(),
				0.001);
		assertTrue(mapping.containsKey(new Point(-2, 1)));
		assertEquals(0.2, mapping.get(new Point(-2, 1)).getDamagePercent(),
				0.001);

		// Greater Damage
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 4);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(3.4, mapping.get(new Point(-1, 0)).getDamagePercent(),
				0.001);
		assertEquals(1.36, mapping.get(new Point(-1, -1)).getDamagePercent(),
				0.001);
		assertEquals(1.36, mapping.get(new Point(-1, 1)).getDamagePercent(),
				0.001);
		assertEquals(0.68, mapping.get(new Point(-2, -1)).getDamagePercent(),
				0.001);
		assertEquals(0.68, mapping.get(new Point(-2, 1)).getDamagePercent(),
				0.001);

		// Greater Height
		sut_.setLevel(AttackAttributeEnum.GREATER_HEIGHT, 3);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(1.6, mapping.get(new Point(-1, 0)).getHeightMod(), 0.001);
		assertEquals(1.6, mapping.get(new Point(-1, -1)).getHeightMod(), 0.001);
		assertEquals(1.6, mapping.get(new Point(-2, -1)).getHeightMod(), 0.001);

		// Rebuff
		sut_.setLevel(AttackAttributeEnum.REBUFF, 9);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.9, mapping.get(new Point(-1, 0)).getRebuffChance1(),
				0.001);
		assertEquals(0.4, mapping.get(new Point(-1, 0)).getRebuffChance2(),
				0.001);
		assertEquals(0.1, mapping.get(new Point(-1, 0)).getRebuffChance3(),
				0.001);
		assertEquals(0.36, mapping.get(new Point(-1, -1)).getRebuffChance1(),
				0.001);
		assertEquals(0.16, mapping.get(new Point(-1, -1)).getRebuffChance2(),
				0.001);
		assertEquals(0.04, mapping.get(new Point(-1, -1)).getRebuffChance3(),
				0.001);
		assertEquals(0.18, mapping.get(new Point(-2, -1)).getRebuffChance1(),
				0.001);
		assertEquals(0.08, mapping.get(new Point(-2, -1)).getRebuffChance2(),
				0.001);
		assertEquals(0.02, mapping.get(new Point(-2, -1)).getRebuffChance3(),
				0.001);

		// Reposition
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 3);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(1, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(0.6, mapping.get(new Point(-1, 0)).getRepositionHeight(),
				0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 5);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(2, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(1, mapping.get(new Point(-1, 0)).getRepositionHeight(),
				0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 8);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(3, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(1.6, mapping.get(new Point(-1, 0)).getRepositionHeight(),
				0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 10);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(4, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(2, mapping.get(new Point(-1, 0)).getRepositionHeight(),
				0.001);

		// Steal
		sut_.setLevel(AttackAttributeEnum.STEAL, 3);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.06, mapping.get(new Point(-1, 0)).getStealChance1(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance2(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance3(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.024, mapping.get(new Point(-1, -1)).getStealChance1(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance2(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 6);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.12, mapping.get(new Point(-1, 0)).getStealChance1(),
				0.001);
		assertEquals(0.05, mapping.get(new Point(-1, 0)).getStealChance2(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance3(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.048, mapping.get(new Point(-1, -1)).getStealChance1(),
				0.001);
		assertEquals(0.02, mapping.get(new Point(-1, -1)).getStealChance2(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, -1)).getStealChance3(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 9);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.18, mapping.get(new Point(-1, 0)).getStealChance1(),
				0.001);
		assertEquals(0.125, mapping.get(new Point(-1, 0)).getStealChance2(),
				0.001);
		assertEquals(0.025, mapping.get(new Point(-1, 0)).getStealChance3(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.072, mapping.get(new Point(-1, -1)).getStealChance1(),
				0.001);
		assertEquals(0.05, mapping.get(new Point(-1, -1)).getStealChance2(),
				0.001);
		assertEquals(0.01, mapping.get(new Point(-1, -1)).getStealChance3(),
				0.001);
		assertEquals(0.0, mapping.get(new Point(-1, -1)).getStealChance4(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 10);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.2, mapping.get(new Point(-1, 0)).getStealChance1(),
				0.001);
		assertEquals(0.15, mapping.get(new Point(-1, 0)).getStealChance2(),
				0.001);
		assertEquals(0.05, mapping.get(new Point(-1, 0)).getStealChance3(),
				0.001);
		assertEquals(0.02, mapping.get(new Point(-1, 0)).getStealChance4(),
				0.001);
		assertEquals(0.08, mapping.get(new Point(-1, -1)).getStealChance1(),
				0.001);
		assertEquals(0.06, mapping.get(new Point(-1, -1)).getStealChance2(),
				0.001);
		assertEquals(0.02, mapping.get(new Point(-1, -1)).getStealChance3(),
				0.001);
		assertEquals(0.008, mapping.get(new Point(-1, -1)).getStealChance4(),
				0.001);

		// Poison
		sut_.setLevel(AttackAttributeEnum.POISON, 7);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.14, mapping.get(new Point(-1, 0)).getPoisonChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getPoisonTurns(), 1);
		assertEquals(0.056, mapping.get(new Point(-1, -1)).getPoisonChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getPoisonTurns(), 1);

		// Weaken
		sut_.setLevel(AttackAttributeEnum.WEAKEN, 10);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.2, mapping.get(new Point(-1, 0)).getWeakenChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getWeakenTurns(), 1);
		assertEquals(0.08, mapping.get(new Point(-1, -1)).getWeakenChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getWeakenTurns(), 1);

		// Corrode
		sut_.setLevel(AttackAttributeEnum.CORRODE, 1);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.02, mapping.get(new Point(-1, 0)).getCorrodeChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getCorrodeTurns(), 1);
		assertEquals(0.008, mapping.get(new Point(-1, -1)).getCorrodeChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getCorrodeTurns(), 1);

		// Blear
		sut_.setLevel(AttackAttributeEnum.BLEAR, 6);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.12, mapping.get(new Point(-1, 0)).getBlearChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getBlearTurns(), 1);
		assertEquals(0.048, mapping.get(new Point(-1, -1)).getBlearChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getBlearTurns(), 1);

		// Slow
		sut_.setLevel(AttackAttributeEnum.SLOW, 8);
		mapping = sut_.getAttackDetails(new Point(-1, 0));
		assertEquals(0.16, mapping.get(new Point(-1, 0)).getSlowChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getSlowTurns(), 1);
		assertEquals(0.064, mapping.get(new Point(-1, -1)).getSlowChance(),
				0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getSlowTurns(), 1);
	}

	@Test
	public void testAttackExpRequired() {
		// 0 case
		assertEquals(5, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 1 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 1);
		assertEquals(15, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 2 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 2);
		assertEquals(30, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 3 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 3);
		assertEquals(50, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 5 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 5);
		assertEquals(105, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 8 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 8);
		assertEquals(225, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));

		// 10 case
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 10);
		assertEquals(-1, sut_
				.attackExpRequired(AttackAttributeEnum.GREATER_DAMAGE));
	}

	@Test
	public void testGetSetLevel() {
		for (int i = 0; i <= 10; i++) {
			assertTrue(sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, i));
			assertEquals(i, sut_.getLevel(AttackAttributeEnum.GREATER_DAMAGE));
		}
	}

	@Test
	public void testGetSetRangeIndex() {
		for (int i = 0; i <= 1; i++) {
			sut_.setRangeIndex(i);
			assertEquals(i, sut_.getRangeIndex());
		}
	}

	@Test
	public void testIncrementLevel() {
		// 10 increments
		for (int i = 0; i < 10; i++) {
			assertTrue(sut_.incrementLevel(AttackAttributeEnum.GREATER_DAMAGE));
			assertEquals(i + 1, sut_
					.getLevel(AttackAttributeEnum.GREATER_DAMAGE));
		}
		assertFalse(sut_.incrementLevel(AttackAttributeEnum.GREATER_DAMAGE));
		assertEquals(10, sut_.getLevel(AttackAttributeEnum.GREATER_DAMAGE));
	}

	@Test
	public void testGetRangedPoints() {
		// Basic case
		Collection<Pair<Point, Float>> points = AttackAttributes
				.getRangedPoints(AttackType.NORMAL, 0, 2, 0.2f);
		assertEquals(points.size(), 1);
		assertTrue(points.contains(new Pair<Point, Float>(new Point(0, 1), 1f)));

		// Number of points check
		for (int i = 0; i <= 10; i++) {
			int subLevel = (i % 2 == 0) ? 2 : 0;
			points = AttackAttributes.getRangedPoints(AttackType.DOWNWARD,
					(i + 1) / 2, subLevel, 0.2f);
			assertEquals(i + 1, points.size());
			assertTrue(points.contains(new Pair<Point, Float>(new Point(0, 1),
					1f)));
		}

		// Each range vector check (visual inspection)
		// Downward
		for (AttackType at : AttackType.values()) {
			points = AttackAttributes.getRangedPoints(at, 5, 2, 0.2f);
			if (at.equals(AttackType.NORMAL))
				assertEquals(1, points.size());
			else
				assertEquals(11, points.size());
			printRange(at, points);
		}
	}

	/**
	 * Prints a 2D map of the points and their weights
	 * 
	 * @param type
	 *            The type of attack.
	 * @param points
	 *            The points of the attack.
	 */
	private void printRange(AttackType type,
			Collection<Pair<Point, Float>> points) {
		// A range map extending from -5 to 5 in both X and Y axes
		int maxRange = 5;
		float[][] rangeMap = new float[maxRange * 2 + 1][maxRange * 2 + 1];
		rangeMap[maxRange][maxRange] = -10;
		for (Pair<Point, Float> point : points) {
			Point p = point.getA();
			rangeMap[maxRange + p.x][maxRange + p.y] = point.getB();
		}

		// Printing it out
		StringBuffer buffer = new StringBuffer(type.toString() + "\n");
		for (int y = maxRange; y >= -maxRange; y--) {
			for (int x = -maxRange; x <= maxRange; x++) {
				buffer.append("[");
				float value = rangeMap[x + maxRange][y + maxRange];
				if (value == 0)
					buffer.append("   ");
				else if (value == -10)
					buffer.append(" X ");
				else
					buffer.append(new String(value + "").substring(0, 3));
				buffer.append("]");
			}
			buffer.append("\n");
		}

		System.out.println(buffer.toString());
	}
}
