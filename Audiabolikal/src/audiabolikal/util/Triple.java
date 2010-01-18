package audiabolikal.util;

/**
 * A utility class for a triple of data.
 * 
 * @author Samuel J. Sarjant
 * 
 * @param <T1>
 *            Element 1.
 * @param <T2>
 *            Element 2.
 * @param <T3>
 *            Element 3.
 */
public class Triple<T1, T2, T3> {
	private T1 a_;
	private T2 b_;
	private T3 c_;

	public Triple(T1 element1, T2 element2, T3 element3) {
		a_ = element1;
		b_ = element2;
		c_ = element3;
	}

	public T1 getA_() {
		return a_;
	}

	public void setA_(T1 a_) {
		this.a_ = a_;
	}

	public T2 getB_() {
		return b_;
	}

	public void setB_(T2 b_) {
		this.b_ = b_;
	}

	public T3 getC_() {
		return c_;
	}

	public void setC_(T3 c_) {
		this.c_ = c_;
	}
}
