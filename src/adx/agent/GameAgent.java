package adx.agent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.structures.Query;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Printer;

/**
 * This class represents a simple agent for the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class GameAgent extends AgentLogic {

  /**
   * Current simulated day.
   */
  protected int currentDay;
  /**
   * A map that keeps track of the campaigns owned by the agent.
   */
  protected List<Campaign> myCampaigns;

  /**
   * A map that keeps track of campaign opportunities.
   */
  protected List<Campaign> campaignOpportunity;

  /**
   * Keeps the current quality score received by the server.
   */
  protected Double currentQualityScore;

  /**
   * Keeps the statistics.
   */
  protected Map<Integer, Pair<Integer, Double>> statistics;

  /**
   * Constructor.
   * 
   * @param host - on which the agent will try to connect.
   * @param port - the agent will use for the connection.
   */
  public GameAgent() {
    super();
    this.init();
  }

  public void init() {
    this.myCampaigns = new ArrayList<Campaign>();
    this.statistics = new HashMap<Integer, Pair<Integer, Double>>();
  }

  /**
   * Return the list of active campaigns.
   * 
   * @return the list of active campaigns.
   */
  public List<Campaign> getActiveCampaigns() {
    List<Campaign> activeCampaigns = new ArrayList<Campaign>();
    for (Campaign c : this.myCampaigns) {
      if (this.currentDay >= c.getStartDay() && this.currentDay <= c.getEndDay()) {
        if (!this.statistics.containsKey(c.getId()) || (this.statistics.containsKey(c.getId()) && this.statistics.get(c.getId()).getElement1() < c.getReach())) {
          activeCampaigns.add(c);
        }
      }
    }
    return activeCampaigns;
  }

  /**
   * Update statistics from server.
   * 
   * @param statistics
   */
  public void updateStatistics(Map<Integer, Pair<Integer, Double>> statistics) {
    if (statistics != null) {
      for (Entry<Integer, Pair<Integer, Double>> x : statistics.entrySet()) {
        if (this.statistics.containsKey(x.getKey())) {
          Pair<Integer, Double> currentStats = this.statistics.get(x.getKey());
          Pair<Integer, Double> newStats = new Pair<Integer, Double>(currentStats.getElement1() + x.getValue().getElement1(), currentStats.getElement2()
              + x.getValue().getElement2());
          this.statistics.put(x.getKey(), newStats);
        } else {
          this.statistics.put(x.getKey(), x.getValue());
        }
      }
    }
  }

  /**
   * Parse the end of day message.
   */
  @Override
  public BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
    Logging.log("[-] handleEndOfDayMessage " + endOfDayMessage);
    Logging.log("[-] Current time = " + Instant.now());
    try {
      this.currentDay = endOfDayMessage.getDay();
      this.updateStatistics(endOfDayMessage.getStatistics());
      this.campaignOpportunity = endOfDayMessage.getCampaignsForAuction();
      if (endOfDayMessage.getCampaignsWon() != null) {
        this.myCampaigns.addAll(endOfDayMessage.getCampaignsWon());
      }
      this.currentQualityScore = endOfDayMessage.getQualityScore();
      BidBundle bidBundle = this.getAdBid();
      if (bidBundle != null) {
        return bidBundle;
      }
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle for day " + endOfDayMessage.getDay() + " -> " + e.getMessage());
    }
    Logging.log("[-] All my campaigns: " + Printer.printNiceListMyCampaigns(this.myCampaigns));
    Logging.log("[-] Statistics: " + this.statistics);
    return null;
  }

  /**
   * Computes the campaign bids.
   * 
   * @return a map with campaign bids.
   */
  protected Map<Integer, Double> getCampaignBids() {
    Map<Integer, Double> campaignBids = new HashMap<Integer, Double>();
    if (this.campaignOpportunity != null) {
      for (Campaign camp : this.campaignOpportunity) {
        campaignBids.put(camp.getId(), new Double((camp.getReach() * 0.1) / this.currentQualityScore));
      }
    } else {
      Logging.log("[-] No campaign opportunites present for day " + this.currentDay);
    }
    return campaignBids;
  }

  /**
   * Produces and sends a bidbundle for the given day.
   * 
   * @param day
   *          - the day for which we want a bid bundle
   * @throws AdXException
   *           in case something went wrong creating the bid bundle.
   */
  public BidBundle getAdBid() throws AdXException {
    // Get the list of active campaigns for this day.
    Set<BidEntry> bidEntries = new HashSet<BidEntry>();
    Map<Integer, Double> limits = new HashMap<Integer, Double>();
    // If I have a campaign, prepare and send bid bundle.
    List<Campaign> myActiveCampaigns = this.getActiveCampaigns();
    if (myActiveCampaigns != null && myActiveCampaigns.size() > 0) {
      Logging.log("[-] Preparing and sending Ad Bid for day " + this.currentDay);
      Logging.log("[-] Active campaigns are: " + Printer.printNiceListMyCampaigns(myActiveCampaigns));
      for (Campaign c : myActiveCampaigns) {
        // bidEntries.add(new BidEntry(c.getId(), new Query(c.getMarketSegment()), c.getBudget() / c.getReach(), c.getBudget()));
        bidEntries.add(new BidEntry(c.getId(), new Query(c.getMarketSegment()), 0.0, c.getBudget()));
        limits.put(c.getId(), c.getBudget());
      }
    } else {
      Logging.log("[-] No campaings present on day " + this.currentDay);
    }
    return new BidBundle(this.currentDay, bidEntries, limits, this.getCampaignBids());
  }

  /**
   * Agent's main method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    OnlineAgent agent = new OnlineAgent("localhost", 9898, new GameAgent());
    agent.connect("agent0", "123456");
  }

  @Override
  public String toString() {
    return "Agent: " + this.agentName + "\n\tMy Campaigns: " + Printer.printNiceListMyCampaigns(this.myCampaigns);
  }

}
