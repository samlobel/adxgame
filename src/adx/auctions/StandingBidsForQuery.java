package adx.auctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import adx.exceptions.AdXException;
import adx.statistics.StatisticsBids;
import adx.structures.BidEntry;
import adx.structures.Query;
import adx.util.Pair;

/**
 * This class contains the data and logic associated with bids from the same query.
 * 
 * @author Enrique Areyan Viqueira
 */
public class StandingBidsForQuery {

  /**
   * List of pair (agent name, bid).
   */
  private List<Pair<String, BidEntry>> standingBids;

  /**
   * Statistics for the bids
   */
  private final StatisticsBids statisticsBids;

  /**
   * Reserve price
   */
  private final double reserve;
  
  /**
   * Auction day
   */
  private final int day;
  
  /**
   * Auction Query
   */
  private final Query query;

  /**
   * Constructor. Sorts bids in decreasing order.
   * 
   * @param bids
   */
  public StandingBidsForQuery(int day, Query query, List<Pair<String, BidEntry>> bids, double reserve, StatisticsBids statisticsBids) {
    this.standingBids = new ArrayList<Pair<String, BidEntry>>(bids);
    Collections.sort(this.standingBids, AdAuctions.bidComparator);
    this.reserve = reserve;
    this.statisticsBids = statisticsBids;
    this.day = day;
    this.query = query;
  }

  /**
   * Returns the cost that needs to be paid by the winner, a.k.a. the second price (more specifically, the max between the reserve and the second price).
   * 
   * @return
   */
  protected double getWinnerCost() {
    if (this.standingBids.size() <= 1) {
      return this.reserve;
    } else {
      return Math.max(this.standingBids.get(1).getElement2().getBid(), this.reserve);
    }
  }

  /**
   * This function should only be called in case there is a winner, otherwise exceptions will be thrown.
   * 
   * @return true if the winner payed reserve
   * @throws AdXException
   */
  protected boolean winnerPayedReserve() throws AdXException {
    // Sanity check 1: there is actually a winner.
    if (this.standingBids.size() == 0) {
      throw new AdXException("Asking if an impression was allocated but there are no standing bids!");
    }
    // Sanity check 2: the winner can afford the reserve. Need not check the second price since the bids are already ordered.
    if (this.standingBids.get(0).getElement2().getBid() < this.reserve) {
      throw new AdXException("Inconsistency: asking if an impression was allocated when in fact the highest bidder cannot afford it!");
    }
    // If there is only one bidder, it by definition pays the reserve.
    if (this.standingBids.size() == 1) {
      return true;
    } else {
      // If there are multiple bidders, the winner pays reserve in case the second highest price is less than reserve.
      // Note we assume no ties. Or, in other words, if the second price is the reserve, we count it as the reserve.
      if (this.standingBids.get(1).getElement2().getBid() < this.reserve) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Returns a winner (chosen at random if there are ties). This function also returns a reason if a winner could not be found. The reason is boolean that is
   * true if the highest bidder could not afford the reserve, and false in case there are no more bids so that a winner could not be determined.
   * 
   * @param reserve
   * @return
   * @throws AdXException
   */
  protected Pair<Boolean, Pair<String, BidEntry>> getWinner(int day) throws AdXException {
    if (this.standingBids.size() == 0) {
      // If there are no more bids, return null.
      return new Pair<Boolean, Pair<String, BidEntry>>(false, null);
    } else {
      // There are bids, log them
      this.logBids();
      if (this.standingBids.get(0).getElement2().getBid() < this.reserve) {
        // If the highest bid does not meet reserve, return null.
        return new Pair<Boolean, Pair<String, BidEntry>>(true, null);
      } else if (this.standingBids.size() == 1) {
        // If there is only one bid, return it.
        return new Pair<Boolean, Pair<String, BidEntry>>(null, this.standingBids.get(0));
      } else {
        // There are more than one bid. Construct a list with all winners and return one of them at random.
        List<Pair<String, BidEntry>> winnerList = new ArrayList<Pair<String, BidEntry>>();
        double winningBid = this.standingBids.get(0).getElement2().getBid();
        Iterator<Pair<String, BidEntry>> bidsListIterator = this.standingBids.iterator();
        Pair<String, BidEntry> currentBidder = null;
        // Keep adding bidders to the winnerList as long as their bids match the winning bid.
        while ((bidsListIterator.hasNext()) && ((currentBidder = bidsListIterator.next()) != null) && currentBidder.getElement2().getBid() == winningBid) {
          winnerList.add(currentBidder);
        }
        // At this point we should have at least one bidder or something went wrong.
        if (winnerList.size() == 0) {
          throw new AdXException("There has to be at least one winner.");
        }
        // Pick a random bidder from the list of winners.
        Collections.shuffle(winnerList);
        return new Pair<Boolean, Pair<String, BidEntry>>(null, winnerList.get(0));
      }
    }
  }

  /**
   * Logs the current standing bids.
   * 
   * @param day
   */
  protected void logBids() {
    ArrayList<Pair<String, Double>> listOfBids = new ArrayList<Pair<String, Double>>();
    for (Pair<String, BidEntry> standingBid : this.standingBids) {
      listOfBids.add(new Pair<String, Double>(standingBid.getElement1(), standingBid.getElement2().getBid()));
    }
    this.statisticsBids.addBids(this.day, this.query, listOfBids);
  }

  /**
   * Removes a bid from the standing bids.
   * 
   * @param bid
   */
  protected void deleteBid(Pair<String, BidEntry> bid) {
    this.standingBids.remove(bid);
  }

  /**
   * Removes all the bids that belong to the campaign whose campaign id is given as a parameter. This methods creates a new list containing the bidEntries for
   * campaigns other than campaignId and assigns this list as the standingBids. This is done to avoid concurrent exception that may arise by deleting members of
   * the standingBids list directly
   * 
   * @param campaignId
   */
  protected void deleteBidFromCampaign(int campaignId) {
    ArrayList<Pair<String, BidEntry>> newStandingBids = new ArrayList<Pair<String, BidEntry>>();
    for (Pair<String, BidEntry> bid : this.standingBids) {
      if (bid.getElement2().getCampaignId() != campaignId) {
        newStandingBids.add(bid);
      }
    }
    this.standingBids = newStandingBids;
  }

  @Override
  public String toString() {
    String ret = "\n";
    for (Pair<String, BidEntry> x : this.standingBids) {
      ret += "\t" + x + "\n";
    }
    return ret;
  }

}
