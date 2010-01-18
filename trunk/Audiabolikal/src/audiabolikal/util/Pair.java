package audiabolikal.util;

/**
 * A simple class for containing two elemnets of data.
 * 
 * @author Samuel J. Sarjant
 *
 * @param <T1> Element 1.
 * @param <T2> Element 2.
 */
public class Pair<T1, T2> {
	private T1 a_;
	private T2 b_;
	
	public Pair (T1 element1, T2 element2) {
		a_ = element1;
		b_ = element2;
	}

	public T1 getA() {
		return a_;
	}

	public void setA(T1 a) {
		this.a_ = a;
	}

	public T2 getB() {
		return b_;
	}

	public void setB(T2 b) {
		this.b_ = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a_ == null) ? 0 : a_.hashCode());
		result = prime * result + ((b_ == null) ? 0 : b_.hashCode());
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
		final Pair other = (Pair) obj;
		if (a_ == null) {
			if (other.a_ != null)
				return false;
		} else if (!a_.equals(other.a_))
			return false;
		if (b_ == null) {
			if (other.b_ != null)
				return false;
		} else if (!b_.equals(other.b_))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return a_.toString() + "," + b_.toString();
	}
}
