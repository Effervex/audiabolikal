package audiabolikal.attacking;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;

import audiabolikal.util.Pair;

/**
 * Gets the higher ranged points complete with damage modifiers.
 * 
 * @author Samuel J. Sarjant
 */
public class RangePoints {
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
	 * @return A collection of points with their damage modifiers.
	 */
	public static Collection<Pair<Point, Float>> getRangedPoints(
			AttackType type, int level, int levelIndex) {
		Collection<Pair<Point, Float>> rangedPoints = new HashSet<Pair<Point, Float>>();

		// Each ranged point has the immediate point (0)
		rangedPoints.add(new Pair<Point, Float>(new Point(0, 1), 1f));

		// Next attack points depend on the attack type and the level (and
		// index).
		switch (type) {
		case DOWNWARD:
			rangedPoints.addAll(downwardAttack(level, levelIndex));
			break;
		case FOREHAND:
		case BACKHAND:
			rangedPoints.addAll(horizontalAttack(level, levelIndex));
			break;
		case TL_BR:
			rangedPoints.addAll(tlbrAttack(level, levelIndex, true));
			break;
		case TR_BL:
			rangedPoints.addAll(tlbrAttack(level, levelIndex, false));
			break;
		case SPINNING_LR:
			rangedPoints.addAll(spinningLRAttack(level, levelIndex, true));
			break;
		case SPINNING_RL:
			rangedPoints.addAll(spinningLRAttack(level, levelIndex, false));
			break;
		case THRUSTING:
			rangedPoints.addAll(thrustingAttack(level, levelIndex));
			break;
		case MAGICAL:
			rangedPoints.addAll(magicalAttack(level, levelIndex));
			break;
		case BLAST:
			rangedPoints.addAll(blastAttack(level, levelIndex));
			break;
		}

		return rangedPoints;
	}

	private static Collection<Pair<Point, Float>> downwardAttack(int level,
			int levelIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Collection<Pair<Point, Float>> horizontalAttack(int level,
			int levelIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Collection<Pair<Point, Float>> tlbrAttack(int level,
			int levelIndex, boolean tlbr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Collection<Pair<Point, Float>> spinningLRAttack(int level,
			int levelIndex, boolean lr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Collection<Pair<Point, Float>> thrustingAttack(int level,
			int levelIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Collection<Pair<Point, Float>> magicalAttack(int level,
			int levelIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Collection<Pair<Point, Float>> blastAttack(int level,
			int levelIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
