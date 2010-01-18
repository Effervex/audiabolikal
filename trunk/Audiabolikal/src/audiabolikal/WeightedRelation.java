package audiabolikal;

/**
 * A relation to another tag, weighted by a value between 0 and 1. Although a
 * weight of 0 makes the relation meaningless.
 * 
 * @author Samuel J. Sarjant
 */
public class WeightedRelation {
	/** The relation name. */
	private String relation_;

	/** The weight. */
	private double weight_;

	/**
	 * A constructor.
	 * 
	 * @param tag
	 *            The parent tag.
	 * @param weight
	 *            The weight of the relation.
	 */
	public WeightedRelation(String tag, double weight) {
		relation_ = tag;
		weight_ = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// Only compare by relation
		result = prime * result
				+ ((relation_ == null) ? 0 : relation_.hashCode());
		result = prime * result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WeightedRelation other = (WeightedRelation) obj;
		// Only compare by relation
		if (relation_ == null) {
			if (other.relation_ != null)
				return false;
		} else if (!relation_.equals(other.relation_))
			return false;
		return true;
	}

	/**
	 * Compares if two weighted relations have the same weights, rather than
	 * just the same relations.
	 * 
	 * @param wr
	 *            The comparable relation.
	 * @return True if the weighted relations are the same in all values.
	 */
	public boolean strictEquals(WeightedRelation wr) {
		if (!equals(wr))
			return false;
		double epsilon = 0.01;
		if ((wr.weight_ > weight_ - epsilon) && (wr.weight_ < weight_ + epsilon))
			return true;
		return false;
	}

	public String getRelation() {
		return relation_;
	}

	public void setRelation(String relation) {
		this.relation_ = relation;
	}

	public double getWeight() {
		return weight_;
	}

	public void setWeight(double weight) {
		this.weight_ = weight;
	}

	@Override
	public String toString() {
		return relation_ + ":" + weight_;
	}
}
