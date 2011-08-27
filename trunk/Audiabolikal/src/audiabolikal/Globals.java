package audiabolikal;

import java.awt.Point;
import java.util.Random;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

public class Globals {
	/** The cap to which gaussian numbers are generated. */
	public static final float SD_CAP = 3;
	public static Random random_ = new Random();

	/**
	 * Calculates the minimum distance of a point from a line and the side of
	 * the line the point is on.
	 * 
	 * @param point
	 *            The point.
	 * @param lineOrigin
	 *            The line origin.
	 * @param lineDir
	 *            The line direction.
	 * @return The minimum distance of the point from the line and the sign
	 *         indicates which side of the line the point is on (negative if
	 *         point is on right).
	 */
	public static float pointLineDist2f(Vector2f point, Vector2f lineOrigin,
			Vector2f lineDir) {
		return pointLineDist2f(point, lineOrigin, lineDir,
				pointLineIntersect(point, lineOrigin, lineDir));
	}

	/**
	 * Calculates the minimum distance of a point from a line and the side of
	 * the line the point is on given the line intersect point.
	 * 
	 * @param point
	 *            The point.
	 * @param lineOrigin
	 *            The line origin.
	 * @param lineDir
	 *            The line direction.
	 * @param u
	 *            The multiplier of the line.
	 * @return The minimum distance of the point from the line and the sign
	 *         indicates which side of the line the point is on (negative if
	 *         point is on right).
	 */
	public static float pointLineDist2f(Vector2f point, Vector2f lineOrigin,
			Vector2f lineDir, float u) {
		Vector2f intersect = lineOrigin.add(lineDir.mult(u));
		float dist = point.distance(intersect);

		float v = (point.x - lineOrigin.x) * (lineDir.y)
				- (point.y - lineOrigin.y) * (lineDir.x);
		if (v < 0)
			return -dist;
		else
			return dist;
	}

	/**
	 * Calculates the closest point along a line of a point.
	 * 
	 * @param point
	 *            The point.
	 * @param lineOrigin
	 * @param lineDir
	 * @return
	 */
	public static float pointLineIntersect(Vector2f point, Vector2f lineOrigin,
			Vector2f lineDir) {
		if (lineDir.equals(Vector2f.ZERO))
			throw new ArithmeticException("Line has no direction.");

		float u = ((point.x - lineOrigin.x) * (lineDir.x) + (point.y - lineOrigin.y)
				* (lineDir.y))
				/ lineDir.lengthSquared();
		return u;
	}

	/**
	 * Calculates the distance between two points.
	 * 
	 * @param x1
	 *            Point 1 x.
	 * @param y1
	 *            Point 1 y.
	 * @param x2
	 *            Point 2 x.
	 * @param y2
	 *            Point 2 y.
	 * @return The distance.
	 */
	public static float distance(float x1, float y1, int x2, int y2) {
		return (float) Point.distance(x1, y1, x2, y2);
	}

	/**
	 * Returns a random capped gaussian.
	 * 
	 * @return A random capped gaussian number.
	 */
	public static float randomGaussian() {
		float gaussian = (float) random_.nextGaussian();
		return FastMath.clamp(gaussian, -SD_CAP, SD_CAP);
	}
}
