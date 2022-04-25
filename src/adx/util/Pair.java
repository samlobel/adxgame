package adx.util;

/**
 * A parameterizable pair class. All elements are immutable.
 * 
 * @author Enrique Areyan Viqueira
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {

  /**
   * First element.
   */
  private final T1 element1;

  /**
   * Second element.
   */
  private final T2 element2;
  
  /**
   * Empty constructor. 
   */
  public Pair() {
    this.element1 = null;
    this.element2 = null;
  }

  /**
   * Constructor.
   * 
   * @param element1
   * @param element2
   */
  public Pair(T1 element1, T2 element2) {
    this.element1 = element1;
    this.element2 = element2;
  }

  /**
   * Get first element.
   * 
   * @return the first element of the pair.
   */
  public T1 getElement1() {
    return this.element1;
  }

  /**
   * Get second element.
   * 
   * @return the second element of the pair.
   */
  public T2 getElement2() {
    return this.element2;
  }

  @Override
  public String toString() {
    return "(" + this.element1 + ", " + this.element2 + ")";
  }
}
