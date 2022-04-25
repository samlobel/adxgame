package adx.structures;

import adx.exceptions.AdXException;

/**
 * This class represents an entry of a one day bid bundle.
 * 
 * @author Enrique Areyan Viqueira
 */
public class SimpleBidEntry extends BidEntry {

  /**
   * Constructor.
   */
  public SimpleBidEntry() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param query
   * @param bid
   * @param limit
   * @throws AdXException
   */
  public SimpleBidEntry(MarketSegment marketSegment, double bid, double limit) throws AdXException {
    // The Integer.MAX_VALUE is just a place holder, this value will get replaced later.
    super(Integer.MAX_VALUE, new Query(marketSegment), bid, limit);
  }
  
  @Override
  public String toString() {
    return "SimpleBidEntry = [query = " + this.query + ", bid = " + bid + ", limit = " + this.limit + "]";
  }

}
