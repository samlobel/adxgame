package adx.variants.ndaysgame;

import java.util.HashSet;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.Campaign;
import adx.structures.SimpleBidEntry;
import adx.util.Logging;

public class SimpleNDaysNCampaignAgent extends NDaysNCampaignsAgent {

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public SimpleNDaysNCampaignAgent(String host, int port) {
    super(host, port);
  }

  @Override
  protected NDaysBidBundle getBidBundle(int day) {
    // Log for which day we want to compute the bid bundle.
    Logging.log("\t[-] GetBidBundle of day = " + day);

    try {
      // Bidding only on the exact market segment of the campaign.
      Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
      Campaign campaign = this.myCampaigns.get(day);
      bidEntries.add(new SimpleBidEntry(campaign.getMarketSegment(), campaign.getBudget() / (double) campaign.getReach(), campaign.getBudget()));
      Logging.log("[-] bidEntries = " + bidEntries);

      // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
      return new NDaysBidBundle(this.currentDay, campaign.getId(), campaign.getBudget(), bidEntries);
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle: " + e.getMessage());
      return null;
    }
  }

  /**
   * Agent's main method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SimpleNDaysNCampaignAgent agent = new SimpleNDaysNCampaignAgent("localhost", 9898);
    agent.connect("Agent2");
  }

}
