package adx.auctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import adx.exceptions.AdXException;
import adx.structures.BidEntry;
import adx.util.Pair;

/**
 * This class contains the data and logic associated with bids from the same
 * query.
 * 
 * @author Enrique Areyan Viqueira
 */
public class StandingBidsForQuery {

  /**
   * List of pair (agent name, bid).
   */
  private List<Pair<String, BidEntry>> standingBids;
  
  /**
   * Reserve price
   */
  private final double reserve;

  /**
   * Constructor. Sorts bids in decreasing order.
   * 
   * @param bids
   */
  public StandingBidsForQuery(List<Pair<String, BidEntry>> bids, double reserve) {
    this.standingBids = new ArrayList<Pair<String, BidEntry>>(bids);
    Collections.sort(this.standingBids, AdAuctions.bidComparator);
    this.reserve = reserve;
  }

  /**
   * Returns the cost that needs to be paid by the winner, a.k.a. the second
   * price (more specifically, the max between the reserve and the second price).
   * 
   * @return
   */
  protected double getWinnerCost() {
    if (this.standingBids.size() <= 1) {
      return this.reserve;
    } else {
      return Math.max(standingBids.get(1).getElement2().getBid(), this.reserve);
    }
  }

  /**
   * Returns a winner (chosen at random if there are ties).
   * 
   * @param reserve
   * @return
   * @throws AdXException
   */
  protected Pair<String, BidEntry> getWinner() throws AdXException {
    if (this.standingBids.size() == 0) {
      // If there are no more bids, return null.
      return null;
    } else if (this.standingBids.get(0).getElement2().getBid() < this.reserve) {
      // If the highest bid does not meet reserve, return null.
      return null;
    } else if (this.standingBids.size() == 1) {
      // If there is only one bid, return it.
      return this.standingBids.get(0);
    } else {
      // There are more than one bid. Construct a list with all winners and return one of them at random.
      List<Pair<String, BidEntry>> winnerList = new ArrayList<Pair<String, BidEntry>>();
      double winningBid = this.standingBids.get(0).getElement2().getBid();
      Iterator<Pair<String, BidEntry>> bidsListIterator = this.standingBids.iterator();
      Pair<String, BidEntry> currentBidder = null;
      // Keep adding bidders to the winnerList as long as their bids match the winning bid.
      while ((bidsListIterator.hasNext()) && ((currentBidder = bidsListIterator.next()) != null)
          && currentBidder.getElement2().getBid() == winningBid) {
        winnerList.add(currentBidder);
      }
      // At this point we should have at least one bidder or something went wrong.
      if (winnerList.size() == 0) {
        throw new AdXException("There has to be at least one winner.");
      }
      // Pick a random bidder from the list of winners.
      Collections.shuffle(winnerList);
      return winnerList.get(0);
    }
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
   * Removes all the bids that belong to the campaign whose campaign id is given
   * as a parameter. This methods creates a new list containing the bidEntries
   * for campaigns other than campaignId and assigns this list as the
   * standingBids. This is done to avoid concurrent exception that may arise by
   * deleting members of the standingBids list directly
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
