package adx.structures;

import adx.exceptions.AdXException;
import adx.util.InputValidators;

/**
 * A bid entry is a constituent part of a BidBundle.
 * 
 * @author Enrique Areyan Viqueira
 */
public class BidEntry {

  /**
   * The campaign id to which this entry refers.
   */
  protected final int campaignId;

  /**
   * The query of this entry.
   */
  protected final Query query;

  /**
   * The bid placed on behalf of the campaign on this BidEntry's query.
   */
  protected final double bid;

  /**
   * A limit on expenditure by this campaign on this query.
   */
  protected final double limit;

  /**
   * Constructor.
   */
  public BidEntry() {
    super();
    this.campaignId = -1;
    this.query = null;
    this.bid = -1;
    this.limit = -1;
  }

  /**
   * Constructor.
   * 
   * @param campaignId
   * @param query
   * @param bid
   * @param limit
   * @throws AdXException
   */
  public BidEntry(int campaignId, Query query, double bid, double limit) throws AdXException {
    super();
    InputValidators.validateCampaignId(campaignId);
    this.campaignId = campaignId;
    InputValidators.validateNotNull(query);
    this.query = query;
    InputValidators.validateBid(bid);
    this.bid = bid;
    InputValidators.validateLimit(limit);
    this.limit = limit;
  }

  /**
   * Getter.
   * 
   * @return the campaign id.
   */
  public int getCampaignId() {
    return this.campaignId;
  }

  /**
   * Getter.
   * 
   * @return the query.
   */
  public Query getQuery() {
    return this.query;
  }

  /**
   * Getter.
   * 
   * @return the bid.
   */
  public double getBid() {
    return this.bid;
  }

  /**
   * Getter.
   * 
   * @return the limit expenditure.
   */
  public double getLimit() {
    return this.limit;
  }

  @Override
  public String toString() {
    return "[Campaign id = " + this.campaignId + ", query = " + this.query + ", bid = " + bid + ", limit = " + this.limit + "]";
  }

}
