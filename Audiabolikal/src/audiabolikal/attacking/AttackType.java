package audiabolikal.attacking;

import java.awt.Point;

/**
 * The various types of attacks that can be performed. Essentially the same
 * outcome, apart from their range types.
 * 
 * @author Samuel J. Sarjant
 */
public enum AttackType {
	NORMAL(new int[0]),
	DOWNWARD(new int[]{-1,1,1,1,-1,2,1,2,0,2,0,3,-1,0,1,0,-1,3,1,3}),
	FOREHAND(new int[]{-1,1,1,1,-2,1,2,1,-1,0,1,0,-2,0,2,0,-3,1,3,1}),
	BACKHAND(new int[]{-1,1,1,1,-2,1,2,1,-1,0,1,0,-2,0,2,0,-3,1,3,1}),
	TL_BR(new int[]{-1,1,1,0,-1,2,0,2,-2,3,2,0,-2,2,1,1,-1,3,2,-1}),
	TR_BL(new int[]{1,1,-1,0,1,2,0,2,2,3,-2,0,2,2,-1,1,1,3,-2,-1}),
	SPINNING_LR(new int[]{1,1,1,0,1,-1,0,-1,-1,-1,-1,0,-1,1,2,0,0,-2,-2,0}),
	SPINNING_RL(new int[]{-1,1,-1,0,-1,-1,0,-1,1,-1,1,0,1,1,-2,0,0,-2,2,0}),
	THRUSTING(new int[]{0,2,1,1,0,3,-1,1,0,4,1,2,0,5,-1,2,2,1,-2,1}),
	MAGICAL(new int[]{-1,0,1,0,0,-1,0,2,-2,0,2,0,0,-2,0,3,-3,0,3,0}),
	BLAST(new int[]{-1,2,1,2,-2,3,2,3,0,2,0,3,-1,3,1,3,-1,4,1,4});
	
	/** The attack points of the attack. */
	private Point[] attackPoints_;
	
	/**
	 * Constructor.
	 * 
	 * @param attackPoints The attack points of the attack.
	 */
	private AttackType(int[] attackPoints) {
		attackPoints_ = new Point[attackPoints.length / 2];
		for (int i = 0; i < attackPoints_.length; i++) {
			attackPoints_[i] = new Point(attackPoints[i * 2], attackPoints[i * 2 + 1]);
		}
	}

	/**
	 * Gets the attack points.
	 * 
	 * @return The attack points.
	 */
	public Point[] getAttackPoints() {
		return attackPoints_;
	}
}
