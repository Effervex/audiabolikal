package audiabolikal.attacking;

/**
 * A class representing the details of damage that an attack can inflict.
 * Included is the amount of damage, the height levels, status effect chances,
 * and more.
 * 
 * @author Samuel J. Sarjant
 */
public class DamageDetail {
	// The various details of the damage.
	private float damagePercent_;
	private float heightMod_;
	private float rebuffChance1_;
	private float rebuffChance2_;
	private float rebuffChance3_;
	private int repositionMove_;
	private float repositionHeight_;
	private float stealChance1_;
	private float stealChance2_;
	private float stealChance3_;
	private float stealChance4_;
	private float poisonChance_;
	private int poisonTurns_;
	private float weakenChance_;
	private int weakenTurns_;
	private float corrodeChance_;
	private int corrodeTurns_;
	private float blearChance_;
	private int blearTurns_;
	private float slowChance_;
	private int slowTurns_;

	/**
	 * Basic constructor. Initialise values with default 1.
	 */
	public DamageDetail() {
		damagePercent_ = 1;
		heightMod_ = 1;
	}

	/**
	 * Perfectly clones this DamageDetail.
	 */
	public DamageDetail clone() {
		DamageDetail clone = new DamageDetail();
		// Copy all fields.
		clone.damagePercent_ = damagePercent_;
		clone.heightMod_ = heightMod_;
		clone.rebuffChance1_ = rebuffChance1_;
		clone.rebuffChance2_ = rebuffChance2_;
		clone.rebuffChance3_ = rebuffChance3_;
		clone.repositionMove_ = repositionMove_;
		clone.repositionHeight_ = repositionHeight_;
		clone.stealChance1_ = stealChance1_;
		clone.stealChance2_ = stealChance2_;
		clone.stealChance3_ = stealChance3_;
		clone.stealChance4_ = stealChance4_;
		clone.poisonChance_ = poisonChance_;
		clone.poisonTurns_ = poisonTurns_;
		clone.weakenChance_ = weakenChance_;
		clone.weakenTurns_ = weakenTurns_;
		clone.corrodeChance_ = corrodeChance_;
		clone.corrodeTurns_ = corrodeTurns_;
		clone.blearChance_ = blearChance_;
		clone.blearTurns_ = blearTurns_;
		clone.slowChance_ = slowChance_;
		clone.slowTurns_ = slowTurns_;
		return clone;
	}

	/**
	 * Applies a modifier to the attributes which are affected by the modifier.
	 * 
	 * @param modifier
	 *            The modifier. Should be > 0.
	 */
	public void applyModifier(float modifier) {
		if (modifier <= 0) {
			System.err.println("Invalid modifier: "+ modifier);
			return;
		}
		
		if (modifier != 1) {
			damagePercent_ *= modifier;
			rebuffChance1_ *= modifier;
			rebuffChance2_ *= modifier;
			rebuffChance3_ *= modifier;
			stealChance1_ *= modifier;
			stealChance2_ *= modifier;
			stealChance3_ *= modifier;
			stealChance4_ *= modifier;
			poisonChance_ *= modifier;
			weakenChance_ *= modifier;
			corrodeChance_ *= modifier;
			blearChance_ *= modifier;
			slowChance_ *= modifier;
		}
	}

	public float getDamagePercent() {
		return damagePercent_;
	}

	public void setDamagePercent(float damagePercent) {
		this.damagePercent_ = damagePercent;
	}

	public float getHeightMod() {
		return heightMod_;
	}

	public void setHeightMod(float heightMod) {
		this.heightMod_ = heightMod;
	}

	public float getRebuffChance1() {
		return rebuffChance1_;
	}

	public void setRebuffChance1(float rebuffChance1) {
		this.rebuffChance1_ = rebuffChance1;
	}

	public float getRebuffChance2() {
		return rebuffChance2_;
	}

	public void setRebuffChance2(float rebuffChance2) {
		this.rebuffChance2_ = rebuffChance2;
	}

	public float getRebuffChance3() {
		return rebuffChance3_;
	}

	public void setRebuffChance3(float rebuffChance3) {
		this.rebuffChance3_ = rebuffChance3;
	}

	public float getRepositionMove() {
		return repositionMove_;
	}

	public void setRepositionMove(int repositionMove) {
		this.repositionMove_ = repositionMove;
	}

	public float getRepositionHeight() {
		return repositionHeight_;
	}

	public void setRepositionHeight(float repositionHeight) {
		this.repositionHeight_ = repositionHeight;
	}

	public float getStealChance1() {
		return stealChance1_;
	}

	public void setStealChance1(float stealChance1) {
		this.stealChance1_ = stealChance1;
	}

	public float getStealChance2() {
		return stealChance2_;
	}

	public void setStealChance2(float stealChance2) {
		this.stealChance2_ = stealChance2;
	}

	public float getStealChance3() {
		return stealChance3_;
	}

	public void setStealChance3(float stealChance3) {
		this.stealChance3_ = stealChance3;
	}

	public float getStealChance4() {
		return stealChance4_;
	}

	public void setStealChance4(float stealChance4) {
		this.stealChance4_ = stealChance4;
	}

	public float getPoisonChance() {
		return poisonChance_;
	}

	public void setPoisonChance(float poisonChance) {
		this.poisonChance_ = poisonChance;
	}

	public int getPoisonTurns() {
		return poisonTurns_;
	}

	public void setPoisonTurns(int poisonTurns) {
		this.poisonTurns_ = poisonTurns;
	}

	public float getWeakenChance() {
		return weakenChance_;
	}

	public void setWeakenChance(float weakenChance) {
		this.weakenChance_ = weakenChance;
	}

	public int getWeakenTurns() {
		return weakenTurns_;
	}

	public void setWeakenTurns(int weakenTurns) {
		this.weakenTurns_ = weakenTurns;
	}

	public float getCorrodeChance() {
		return corrodeChance_;
	}

	public void setCorrodeChance(float corrodeChance) {
		this.corrodeChance_ = corrodeChance;
	}

	public int getCorrodeTurns() {
		return corrodeTurns_;
	}

	public void setCorrodeTurns(int corrodeTurns) {
		this.corrodeTurns_ = corrodeTurns;
	}

	public float getBlearChance() {
		return blearChance_;
	}

	public void setBlearChance(float blearChance) {
		this.blearChance_ = blearChance;
	}

	public int getBlearTurns() {
		return blearTurns_;
	}

	public void setBlearTurns(int blearTurns) {
		this.blearTurns_ = blearTurns;
	}

	public float getSlowChance() {
		return slowChance_;
	}

	public void setSlowChance(float slowChance) {
		this.slowChance_ = slowChance;
	}

	public int getSlowTurns() {
		return slowTurns_;
	}

	public void setSlowTurns(int slowTurns) {
		this.slowTurns_ = slowTurns;
	}

}
