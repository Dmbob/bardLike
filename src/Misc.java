import javax.swing.JOptionPane;

/**
 * Purely static class that provides miscellaneous functions for the game.
 * @author alex
 * @since 5/23/13
 * @version 1
 */
public class Misc {
	public static final int TARGET_SIZE = 64;
	public static final int MAX_INVENTORY = 100;

	/**
	 * Pop-up dialog, replaces console println
	 * @param obj Any object to print to console.
	 */
	public static void showDialog(Object obj) {
		JOptionPane.showMessageDialog(null, obj.toString());
	}

	/**
	 * Linear interpolation.
	 * @param start Starting value.
	 * @param end Ending value.
	 * @param percent Percent between.
	 * @return Lerped value.
	 */
	public static float lerp(float start, float end, float percent) {
		return (start + percent*(end - start));
	}

	/**
	 * Clamps a value between an upper and lower value.
	 * @param value Value to clamp.
	 * @param low Lower value to clamp to.
	 * @param high Higher value to clamp to.
	 * @return Clamped value
	 */
	public static int clamp(int value, int low, int high) {
		if (value < low) 
			return low;
		if (value > high)
			return high;
		return value;
	}
	
	public static Vector getLocFromDir(int x, int y, Direction dir) {
		switch(dir) {
				case LEFT:
					return new Vector(x - 1, y);
				case RIGHT:
					return new Vector(x + 1, y);
				case UP:
					return new Vector(x, y - 1);
				case DOWN:
					return new Vector(x, y + 1);
		}
					/*
		if (dir == Direction.LEFT) {
			return new Vector(x - 1, y);
		} else if (dir == Direction.RIGHT) {
			return new Vector(x + 1, y);
		} else if (dir == Direction.UP) {
			return new Vector(x, y - 1);
		} else if (dir == Direction.DOWN) {
			return new Vector(x, y + 1);
		} else if (dir == Direction.LEFT_UP) {
			
		} else if (dir == Direction)
		*/
		return null;
	}

	/**
	 * Returns a random boolean!
	 * @return A random boolean.
	 */
	public static boolean randomBool() {
		if ((int) (Math.random() * 2) == 0) {
			return false;
		}
		return true;
	}
}
