package adx.structures;

import java.util.Objects;

import adx.exceptions.AdXException;
import adx.util.InputValidators;

/**
 * A query represents a market segment together with all possible attributes such as: publisher device and adtype.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Query {

  /**
   * This query's market segment.
   */
  private final MarketSegment marketSegment;

  public Query() {
    super();
    this.marketSegment = null;
  }

  /**
   * Constructor.
   * 
   * @param marketSegment
   * @throws AdXException 
   */
  public Query(MarketSegment marketSegment) throws AdXException {
    InputValidators.validateNotNull(marketSegment);
    this.marketSegment = marketSegment;
  }

  /**
   * Getter.
   * 
   * @return this query's market segment.
   */
  public MarketSegment getMarketSegment() {
    return this.marketSegment;
  }

  @Override
  public String toString() {
    if (this.marketSegment != null)
      return this.marketSegment.name();
    else
      return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof Query)) {
      return false;
    } else {
      Query query = (Query) o;
      return Objects.equals(this.marketSegment, query.marketSegment);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.marketSegment);
  }

  /**
   * This function determines whether this query matches a given query for auction selection purposes.
   * 
   * @param q
   * @return
   * @throws AdXException
   */
  public boolean matchesQuery(Query q) throws AdXException {
    return MarketSegment.marketSegmentSubset(this.marketSegment, q.getMarketSegment());
  }

}
