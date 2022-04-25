package adx.auctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.util.Pair;

/**
 * Methods to run the campaigns auctions.
 * 
 * @author Enrique Areyan Viqueira
 */
public class CampaignAuctions {

  private static CompareCampaignsBids campaignBidsComparator = new CompareCampaignsBids();

  /**
   * Given a list of pairs (Agent, Bid), returns a pair (Double, List) where Double is the cost of winning and List is the list of all winners, i.e., agents
   * with lowest bids
   * 
   * @param bids
   * @return
   * @throws AdXException
   */
  public static Pair<String, Double> winnerDetermination(List<Pair<String, Double>> bids) throws AdXException {
    if (bids == null) {
      throw new AdXException("Cannot run a campaign auction with null bids.");
    }
    if (bids.size() == 0) {
      throw new AdXException("Cannot run a campaign auction with no bids.");
    }
    Collections.sort(bids, CampaignAuctions.campaignBidsComparator);
    List<String> winnersList = new ArrayList<>();
    double winningBid = bids.get(0).getElement2();
    for (Pair<String, Double> agentBid : bids) {
      if (agentBid.getElement2() == winningBid) {
        winnersList.add(agentBid.getElement1());
      } else {
        break;
      }
    }
    if (winnersList.isEmpty()) {
    	return null;
    }
    Collections.shuffle(winnersList);
    String winner = winnersList.get(0);
    double winningCost = Double.MAX_VALUE;
    for (int i = 1; i < bids.size(); i++) {
    	Pair<String, Double> agentBid = bids.get(i);
    	if (!agentBid.getElement1().equals(winner)) {
    		winningCost = agentBid.getElement2();
    		break;
    	}
    }
    return new Pair<String, Double>(winner, winningCost);
  }

  /**
   * Given a list of pairs (Agent, Bid) runs the auction and returns a pair (Agent, WinningCost).
   * 
   * @param bids
   * @return
   * @throws AdXException
   */
  public static Pair<String, Double> runCampaignAuction(List<Pair<String, Double>> bids) throws AdXException {
    Pair<String, Double> result = CampaignAuctions.winnerDetermination(bids);
    if (result == null) {
      throw new AdXException("There must be at least one campaign auction winner");
    }
    return result;
  }

  /**
   * Given a campaignId and a map from agents->bidbundles, select and return
   * only those bids matching campaign Id.
   * 
   * @param campaignId
   * @param bidBundles
   * @throws AdXException
   */
  public static List<Pair<String, Double>> filterToEffectiveBids(Campaign campaign, Map<String, BidBundle> bidBundles, Map<String, Double> qualityScores) throws AdXException {
    if (campaign.getId() <= 0) {
      throw new AdXException("The id of a campaign must be a positive integer");
    }
    if (bidBundles == null) {
      throw new AdXException("The bid bundles must not be null");
    }
    if (qualityScores == null) {
      throw new AdXException("The quality score map must not be null");
    }
    List<Pair<String, Double>> bids = new ArrayList<Pair<String, Double>>();
    for (Entry<String, BidBundle> agentBid : bidBundles.entrySet()) {
      String agentName = agentBid.getKey();
      Double bidValue = agentBid.getValue().getCampaignBid(campaign.getId());
      if (bidValue != null) {
        if(!qualityScores.containsKey(agentName)) {
          throw new AdXException("Unable to find quality score for agent " + agentName);
        }
        Double qualityScore = qualityScores.get(agentName);
        if (bidValue >= (campaign.getReach() * 0.1) && bidValue <= campaign.getReach()) {
          bids.add(new Pair<String, Double>(agentName, bidValue / Math.max(qualityScore, 0.1)));
        }
      }
    }
    return bids;
  }

  /**
   * Comparator to compare campaigns bids.
   * 
   * @author Enrique Areyan Viqueira
   */
  public static class CompareCampaignsBids implements Comparator<Pair<String, Double>> {
    @Override
    public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
      if (o1.getElement2() < o2.getElement2()) {
        return -1;
      } else if (o1.getElement2() > o2.getElement2()) {
        return 1;
      } else {
        return 0;
      }
    }

  }
}
