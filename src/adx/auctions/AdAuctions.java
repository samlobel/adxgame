package adx.auctions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.statistics.Statistics;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.structures.Query;
import adx.util.Pair;
import adx.util.Sampling;

public class AdAuctions {

  protected static CompareBidEntries bidComparator = new CompareBidEntries();

  /**
   * Given a Query and a map of BidBundles filter and return those bids that match the query. This method assumes the bid bundle reports campaigns owned by
   * agents, no impostors.
   * 
   * @param query
   * @param bidBundles
   * @return
   * @throws AdXException
   */
  public static List<Pair<String, BidEntry>> filterBids(Query query, Map<String, BidBundle> bidBundles, Statistics statistics) throws AdXException {
		
	    List<Pair<String, BidEntry>> bids = new ArrayList<Pair<String, BidEntry>>();
	    for (Entry<String, BidBundle> agentBid : bidBundles.entrySet()) {
	      for (BidEntry bidEntry : agentBid.getValue().getBidEntries()) {
	    	  Campaign c = statistics.getStatisticsCampaign().getCampaign(bidEntry.getCampaignId());
	    	  boolean marketSegmentValidForCampaign = MarketSegment.marketSegmentSubset(c.getMarketSegment(), bidEntry.getQuery().getMarketSegment());
	        // Check if the query matches.
	        if (bidEntry.getQuery().matchesQuery(query) && Double.isFinite(bidEntry.getBid()) && marketSegmentValidForCampaign) {
	          bids.add(new Pair<String, BidEntry>(agentBid.getKey(), bidEntry));
	        }
	      }
	    }
	    return bids;
	  }

  /**
   * Given a map Agent -> BidBundle, returns a map Campaign Id -> Limit
   * 
   * @param day
   * @param bidBundles
   * @return the map containing the daily limit of all campaigns.
   * @throws AdXException
   */
  public static Map<Integer, Double> getCampaingsDailyLimit(int day, Map<String, BidBundle> bidBundles) throws AdXException {
    Map<Integer, Double> limits = new HashMap<Integer, Double>();
    for (BidBundle bidBundle : bidBundles.values()) {
      if (bidBundle.getDay() != day) {
        throw new AdXException("Processing a bid bundle for the WRONG day");
      }
      for (BidEntry bidEntry : bidBundle.getBidEntries()) {
        if (bidBundle.getCampaignLimit(bidEntry.getCampaignId()) != null) {
          limits.put(bidEntry.getCampaignId(), bidBundle.getCampaignLimit(bidEntry.getCampaignId()));
        }
      }
    }
    return limits;
  }

  /**
   * Comparator. Compares two bid entries by the bids. This is used to order a list of bid entries in descending order of bid.
   * 
   * @author Enrique Areyan Viqueira
   */
  public static class CompareBidEntries implements Comparator<Pair<String, BidEntry>> {
    @Override
    public int compare(Pair<String, BidEntry> o1, Pair<String, BidEntry> o2) {
      if (o1.getElement2().getBid() < o2.getElement2().getBid()) {
        return 1;
      } else if (o1.getElement2().getBid() > o2.getElement2().getBid()) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  /**
   * Wrapper to run all auctions with no reserve.
   * 
   * @param day
   * @param bidBundles
   * @param adStatistics
   * @throws AdXException
   */
  /*
   * public static void runAllAuctions(int day, Map<String, BidBundle> bidBundles, Statistics adStatistics) throws AdXException { AdAuctions.runAllAuctions(day,
   * bidBundles, adStatistics, 0.0); }
   */

  // public static void runAllAuctions(int day, Map<String, BidBundle> bidBundles, Statistics adStatistics)

  /**
   * Runs all the auctions for a given day.
   * 
   * @param day
   * @param bidBundles
   * @param statistics
   * @throws AdXException
   */
  public static void runAllAuctions(int day, Map<String, BidBundle> bidBundles, Statistics statistics, double reserve, int numberOfImpressions) throws AdXException {
    // Get the daily limits
    Map<Integer, Double> dailyLimits = AdAuctions.getCampaingsDailyLimit(day, bidBundles);
    // Collect bids.
    Map<Query, StandingBidsForQuery> allQueriesStandingBids = new HashMap<Query, StandingBidsForQuery>();
    for (MarketSegment marketSegment : Sampling.segmentsToSample.keySet()) {
      Query query = new Query(marketSegment);
      allQueriesStandingBids.put(query, new StandingBidsForQuery(day, query, AdAuctions.filterBids(query, bidBundles, statistics), reserve, statistics.getStatisticsBids()));
    }
    // Logging.log(allQueriesStandingBids);
    // Sample user population.
    List<Query> samplePopulation = Sampling.samplePopulation(numberOfImpressions);
    // Debug print.
    // Logging.log("NumberOfImpressions = " + numberOfImpressions);
    // Logging.log(allQueriesStandingBids);
    // Logging.log(dailyLimits);
    // Logging.log(samplePopulation);
    // For each user sampled, run the auction.
    for (Query query : samplePopulation) {
      StandingBidsForQuery bidsForCurrentQuery = allQueriesStandingBids.get(query);
      // Logging.log("Auction: \t " + query);
      // Logging.log("\t -> " + bidsForCurrentQuery);
      // Attempt to allocate the user. It could happen that the winner hits it limit and we need to find another winner.
      while (true) {
        // The getWinner() function returns a pair <Boolean, <Agent Name, Bid>>, where Boolean is the reason
        // in case a winner could not be found (and null otherwise), and <Agent Name, Bid> is the winner in case one exists.
        Pair<Boolean, Pair<String, BidEntry>> winnerDetermination = bidsForCurrentQuery.getWinner(day);
        if (winnerDetermination.getElement2() == null) {
          // A winner could not be determined. Record the reason.
          statistics.getStatisticsAds().addNoAllocation(day, winnerDetermination.getElement1());
          break;
        }
        Pair<String, BidEntry> winner = winnerDetermination.getElement2();
        String winnerName = winner.getElement1();
        BidEntry winnerBidEntry = winner.getElement2();
        double winCost = bidsForCurrentQuery.getWinnerCost();
        // Logging.log("Winner is: " + winner + ", pays " + winCost);
        double totalSpendSoFar = statistics.getStatisticsAds().getDailySummaryStatistic(day, winnerName, winnerBidEntry.getCampaignId()).getElement2();
        double querySpendSoFar = statistics.getStatisticsAds().getDailyStatistic(day, winnerName, winnerBidEntry.getCampaignId(), query).getElement2();
        double dailyLimit = (dailyLimits.containsKey(winnerBidEntry.getCampaignId())) ? dailyLimits.get(winnerBidEntry.getCampaignId()) : Double.MAX_VALUE;

        if (totalSpendSoFar + winCost > dailyLimit) {
          // In case the campaign already hit its daily limit, delete all bid entries matching this campaigns from ALL standing bids in ALL queries.
          for (StandingBidsForQuery queryStandingBids : allQueriesStandingBids.values()) {
            queryStandingBids.deleteBidFromCampaign(winnerBidEntry.getCampaignId());
          }
          // Logging.log("DELETED BID FROM ALL QUERIES");
        } else if (querySpendSoFar + winCost > winnerBidEntry.getLimit()) {
          // In case the campaign hit its query limit but not its daily limit, deleted only from the current query's standing bids.
          bidsForCurrentQuery.deleteBid(winner);
          // Logging.log("DELETED BID FROM QUERY: " + query);
        } else {
          // In case the campaign still has budget (both query and daily), allocate the user to this campaign.
          statistics.getStatisticsAds().addStatistic(day, winnerName, winnerBidEntry.getCampaignId(), query, 1, winCost);
          statistics.getStatisticsAds().addReserveAllocation(day, bidsForCurrentQuery.winnerPayedReserve());
          break;
        }
      }
    }
    // statistics.getStatisticsBids().printBids(day);
    // Logging.log(adStatistics.getStatisticsAds().printNiceAdStatisticsTable());
  }
}
