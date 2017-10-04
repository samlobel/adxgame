package adx.sim.agents;

import java.util.HashSet;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.SimpleBidEntry;
import adx.util.Logging;
import adx.variants.onedaygame.OneDayBidBundle;

/**
 * Simple simAgent.
 * 
 * @author Enrique Areyan Viqueira
 */
public class SimpleSimAgent extends SimAgent {

  public SimpleSimAgent(String simAgentName, double reserve, int numberOfImpressions) {
    super(simAgentName, reserve, numberOfImpressions);
  }

  @Override
  public BidBundle getBidBundle() {
    try {
      // Bidding only on the exact market segment of the campaign.
      Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
      bidEntries.add(new SimpleBidEntry(this.myCampaign.getMarketSegment(), this.myCampaign.getBudget() / (double) this.myCampaign.getReach(), this.myCampaign.getBudget()));
      // Logging.log("[-] bidEntries = " + bidEntries);
      // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
      OneDayBidBundle SIBidBundle = new OneDayBidBundle(this.myCampaign.getId(), this.myCampaign.getBudget(), bidEntries);
      //Logging.log("\n:::::::SIBidBundle for campaign: " + this.myCampaign + " = " + SIBidBundle);
      //this.getMatchingMarketSegment(this.myCampaign.getMarketSegment());
      return SIBidBundle;
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle: " + e.getMessage());
      return null;
    }
  }

  /*public Set<MarketSegment> getMatchingMarketSegment(MarketSegment m) throws AdXException {
    for (Entry<MarketSegment, Integer> entry : MarketSegment.proportionsList) {
      //Logging.log(entry.getKey());
      if (MarketSegment.marketSegmentSubset(m, entry.getKey())) {
        //Logging.log("\t yes");
      }
    }
    return null;
  }*/

}
