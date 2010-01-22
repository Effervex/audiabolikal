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
		sut_ = new AttackAttributes();
	}

	@Test
	public void testGetAttackDetails() {
		// Basic test
		Map<Point, DamageDetail> mapping = sut_.getAttackDetails(
				AttackType.NORMAL, new Point(0, 1));
		assertEquals(mapping.size(), 1);
		assertTrue(mapping.containsKey(new Point(0, 1)));
		assertEquals(1, mapping.get(new Point(0, 1)).getDamagePercent());
		assertEquals(1, mapping.get(new Point(0, 1)).getHeightMod());

		// Greater Range
		sut_.setLevel(AttackAttributeEnum.GREATER_RANGE, 1);
		sut_.setRangeIndex(0);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(0, 1));
		assertEquals(mapping.size(), 2);
		assertTrue(mapping.containsKey(new Point(0, 1)));
		assertEquals(1, mapping.get(new Point(0, 1)).getDamagePercent());
		assertEquals(1, mapping.get(new Point(0, 1)).getHeightMod());
		assertTrue(mapping.containsKey(new Point(-1, 1)));
		assertEquals(0.2, mapping.get(new Point(-1, 1)).getDamagePercent());
		assertEquals(1, mapping.get(new Point(-1, 1)).getHeightMod());
		
		// Rotated case
		sut_.setLevel(AttackAttributeEnum.GREATER_RANGE, 4);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(mapping.size(), 5);
		assertTrue(mapping.containsKey(new Point(-1, 0)));
		assertEquals(1, mapping.get(new Point(-1, 0)).getDamagePercent());
		assertTrue(mapping.containsKey(new Point(-1, -1)));
		assertEquals(0.4, mapping.get(new Point(-1, -1)).getDamagePercent());
		assertTrue(mapping.containsKey(new Point(-1, 1)));
		assertEquals(0.4, mapping.get(new Point(-1, 1)).getDamagePercent());
		assertTrue(mapping.containsKey(new Point(-2, -1)));
		assertEquals(0.2, mapping.get(new Point(-2, -1)).getDamagePercent());
		assertTrue(mapping.containsKey(new Point(-2, 1)));
		assertEquals(0.2, mapping.get(new Point(-2, 1)).getDamagePercent());
		
		// Greater Damage
		sut_.setLevel(AttackAttributeEnum.GREATER_DAMAGE, 4);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(3.4, mapping.get(new Point(-1, 0)).getDamagePercent(), 0.001);
		assertEquals(1.36, mapping.get(new Point(-1, -1)).getDamagePercent(), 0.001);
		assertEquals(1.36, mapping.get(new Point(-1, 1)).getDamagePercent(), 0.001);
		assertEquals(0.68, mapping.get(new Point(-2, -1)).getDamagePercent(), 0.001);
		assertEquals(0.68, mapping.get(new Point(-2, 1)).getDamagePercent(), 0.001);
		
		// Greater Height
		sut_.setLevel(AttackAttributeEnum.GREATER_HEIGHT, 3);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(1.6, mapping.get(new Point(-1, 0)).getHeightMod(), 0.001);
		assertEquals(1.6, mapping.get(new Point(-1, -1)).getHeightMod(), 0.001);
		assertEquals(1.6, mapping.get(new Point(-2, -1)).getHeightMod(), 0.001);
		
		// Rebuff
		sut_.setLevel(AttackAttributeEnum.REBUFF, 9);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.9, mapping.get(new Point(-1, 0)).getRebuffChance1(), 0.001);
		assertEquals(0.4, mapping.get(new Point(-1, 0)).getRebuffChance2(), 0.001);
		assertEquals(0.1, mapping.get(new Point(-1, 0)).getRebuffChance3(), 0.001);
		assertEquals(0.36, mapping.get(new Point(-1, -1)).getRebuffChance1(), 0.001);
		assertEquals(0.16, mapping.get(new Point(-1, -1)).getRebuffChance2(), 0.001);
		assertEquals(0.04, mapping.get(new Point(-1, -1)).getRebuffChance3(), 0.001);
		assertEquals(0.18, mapping.get(new Point(-2, -1)).getRebuffChance1(), 0.001);
		assertEquals(0.08, mapping.get(new Point(-2, -1)).getRebuffChance2(), 0.001);
		assertEquals(0.02, mapping.get(new Point(-2, -1)).getRebuffChance3(), 0.001);
		
		// Reposition
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 3);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(1, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(0.6, mapping.get(new Point(-1, 0)).getRepositionHeight(), 0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 5);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(2, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(1, mapping.get(new Point(-1, 0)).getRepositionHeight(), 0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 8);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(3, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(1.6, mapping.get(new Point(-1, 0)).getRepositionHeight(), 0.001);
		sut_.setLevel(AttackAttributeEnum.REPOSITION, 10);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(4, mapping.get(new Point(-1, 0)).getRepositionMove());
		assertEquals(2, mapping.get(new Point(-1, 0)).getRepositionHeight(), 0.001);
		
		// Steal
		sut_.setLevel(AttackAttributeEnum.STEAL, 3);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.06, mapping.get(new Point(-1, 0)).getStealChance1(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance2(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance3(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.024, mapping.get(new Point(-1, -1)).getStealChance1(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance2(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 6);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.12, mapping.get(new Point(-1, 0)).getStealChance1(), 0.001);
		assertEquals(0.05, mapping.get(new Point(-1, 0)).getStealChance2(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance3(), 0.0);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.048, mapping.get(new Point(-1, -1)).getStealChance1(), 0.001);
		assertEquals(0.02, mapping.get(new Point(-1, -1)).getStealChance2(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, -1)).getStealChance3(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 9);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.18, mapping.get(new Point(-1, 0)).getStealChance1(), 0.001);
		assertEquals(0.125, mapping.get(new Point(-1, 0)).getStealChance2(), 0.001);
		assertEquals(0.025, mapping.get(new Point(-1, 0)).getStealChance3(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, 0)).getStealChance4(), 0.0);
		assertEquals(0.072, mapping.get(new Point(-1, -1)).getStealChance1(), 0.001);
		assertEquals(0.05, mapping.get(new Point(-1, -1)).getStealChance2(), 0.001);
		assertEquals(0.01, mapping.get(new Point(-1, -1)).getStealChance3(), 0.001);
		assertEquals(0.0, mapping.get(new Point(-1, -1)).getStealChance4(), 0.0);
		sut_.setLevel(AttackAttributeEnum.STEAL, 10);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.2, mapping.get(new Point(-1, 0)).getStealChance1(), 0.001);
		assertEquals(0.15, mapping.get(new Point(-1, 0)).getStealChance2(), 0.001);
		assertEquals(0.05, mapping.get(new Point(-1, 0)).getStealChance3(), 0.001);
		assertEquals(0.02, mapping.get(new Point(-1, 0)).getStealChance4(), 0.001);
		assertEquals(0.08, mapping.get(new Point(-1, -1)).getStealChance1(), 0.001);
		assertEquals(0.06, mapping.get(new Point(-1, -1)).getStealChance2(), 0.001);
		assertEquals(0.02, mapping.get(new Point(-1, -1)).getStealChance3(), 0.001);
		assertEquals(0.008, mapping.get(new Point(-1, -1)).getStealChance4(), 0.001);
		
		// Poison
		sut_.setLevel(AttackAttributeEnum.POISON, 7);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.14, mapping.get(new Point(-1, 0)).getPoisonChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getPoisonTurns(), 1);
		assertEquals(0.056, mapping.get(new Point(-1, -1)).getPoisonChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getPoisonTurns(), 1);
		
		// Weaken
		sut_.setLevel(AttackAttributeEnum.WEAKEN, 10);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.2, mapping.get(new Point(-1, 0)).getWeakenChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getWeakenTurns(), 1);
		assertEquals(0.08, mapping.get(new Point(-1, -1)).getWeakenChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getWeakenTurns(), 1);
		
		// Corrode
		sut_.setLevel(AttackAttributeEnum.CORRODE, 1);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.02, mapping.get(new Point(-1, 0)).getCorrodeChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getCorrodeTurns(), 1);
		assertEquals(0.008, mapping.get(new Point(-1, -1)).getCorrodeChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getCorrodeTurns(), 1);
		
		// Blear
		sut_.setLevel(AttackAttributeEnum.BLEAR, 6);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.12, mapping.get(new Point(-1, 0)).getBlearChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getBlearTurns(), 1);
		assertEquals(0.048, mapping.get(new Point(-1, -1)).getBlearChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getBlearTurns(), 1);
		
		// Slow
		sut_.setLevel(AttackAttributeEnum.SLOW, 8);
		mapping = sut_.getAttackDetails(AttackType.DOWNWARD, new Point(-1, 0));
		assertEquals(0.16, mapping.get(new Point(-1, 0)).getSlowChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, 0)).getSlowTurns(), 1);
		assertEquals(0.064, mapping.get(new Point(-1, -1)).getSlowChance(), 0.001);
		assertEquals(4, mapping.get(new Point(-1, -1)).getSlowTurns(), 1);
	}

	@Test
	public void testAttackExpRequired() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetLevel() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncrementLevel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRangedPoints() {
		// Basic case
		Collection<Pair<Point, Float>> points = AttackAttributes
				.getRangedPoints(AttackType.NORMAL, 0, 2, 0.2f);
		assertTrue(points.isEmpty());
	}
}
