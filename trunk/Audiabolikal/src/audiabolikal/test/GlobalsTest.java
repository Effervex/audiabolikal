package audiabolikal.test;

import static org.junit.Assert.*;

import org.junit.Test;

import audiabolikal.Globals;

import com.jme3.math.Vector2f;

public class GlobalsTest {

	@Test
	public void testDistance() {
		assertEquals(0, Globals.distance(0, 0, 0, 0), 0.0001);
		assertEquals(Math.sqrt(2), Globals.distance(1, 1, 0, 0), 0.0001);
	}

	@Test
	public void testPointLineDist2f() {
		assertEquals(new Vector2f(1, 1).length(), Math.sqrt(2), 0.0001);

		assertEquals(0, Globals.pointLineDist2f(Vector2f.ZERO, Vector2f.ZERO,
				new Vector2f(1, 1)), 0.0001);
		assertEquals(0, Globals.pointLineDist2f(new Vector2f(1, 1),
				Vector2f.ZERO, new Vector2f(1, 1)), 0.0001);
		assertEquals(-3, Globals.pointLineDist2f(new Vector2f(.5f, 3),
				Vector2f.ZERO, new Vector2f(1, 0)), 0.0001);
		assertEquals(-2, Globals.pointLineDist2f(new Vector2f(.5f, 3),
				new Vector2f(0, 1), new Vector2f(1, 0)), 0.0001);
		assertEquals(Math.sqrt(2) / 2, Globals.pointLineDist2f(new Vector2f(1, 0),
				new Vector2f(0, 0), new Vector2f(1, 1)), 0.0001);
		assertEquals(-Math.sqrt(2) / 2, Globals.pointLineDist2f(new Vector2f(0, 1),
				new Vector2f(0, 0), new Vector2f(1, 1)), 0.0001);
	}

}
