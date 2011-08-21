package audiabolikal;

import java.awt.Point;
import java.util.Random;

import com.jme3.math.Vector2f;

public class Globals {
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
		if (lineDir.equals(Vector2f.ZERO))
			throw new ArithmeticException("Line has no direction.");

		float u = ((point.x - lineOrigin.x) * (lineDir.x) + (point.y - lineOrigin.y)
				* (lineDir.y))
				/ lineDir.lengthSquared();
		Vector2f intersect = lineOrigin.add(lineDir.mult(u));
		float dist = point.distance(intersect);
		
		float v = (point.x - lineOrigin.x) * (lineDir.x) - (point.y - lineOrigin.y) * (lineDir.y);
		if (v < 0)
			return -dist;
		else
			return dist;
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
}
