package adx.sim;

import java.util.ArrayList;
import java.util.List;

import adx.exceptions.AdXException;
import adx.server.ServerState;
import adx.sim.agents.SimAgent;
import adx.statistics.Statistics;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.util.Pair;
import adx.util.Parameters;
import adx.util.Sampling;

/**
 * This class simulates the game. This is used for experimental purposes.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Simulator {

  /**
   * A simulation has a fixed list of SimAgents.
   */
  private final List<SimAgent> agents;

  /**
   * Keep a server state
   */
  private final ServerState serverState;

  /**
   * Reserve price
   */
  private final double reserve;

  /**
   * Number of impressions
   */
  private final int numberOfImpressions;

  /**
   * Discount factor on the reach of each sampled campaign.
   */
  private final double demandDiscountFactor;

  /**
   * Constructor.
   * 
   * @param agents
   * @throws AdXException
   */
  public Simulator(List<SimAgent> agents, double reserve, int numberOfImpressions, double demandDiscountFactor) throws AdXException {
    this.agents = agents;
    this.serverState = new ServerState(0);
    for (SimAgent simAgent : agents) {
      this.serverState.registerAgent(simAgent.getName());
    }
    this.reserve = reserve;
    this.numberOfImpressions = numberOfImpressions;
    this.demandDiscountFactor = demandDiscountFactor;
    Sampling.resetUniqueCampaignId();
  }

  /**
   * Constructor. No reserve and default number of impressions.
   * 
   * @param agents
   * @throws AdXException
   */
  public Simulator(List<SimAgent> agents) throws AdXException {
    this(agents, 0.0, Parameters.POPULATION_SIZE, 1.0);
  }

  /**
   * Run the simulation.
   * 
   * @return the statistics of the simulation.
   * 
   * @throws AdXException
   */
  public Statistics run() throws AdXException {

    this.serverState.initStatistics();

    // Sample and distribute campaigns
    List<Campaign> allCampaigns = new ArrayList<Campaign>();
    for (int j = 0; j < this.agents.size(); j++) {
      allCampaigns.add(this.sampleSimulatorCampaign());
    }
    // Logging.log(allCampaigns);
    int i = 0;
    for (SimAgent agent : this.agents) {
      List<Campaign> otherCampaigns = new ArrayList<Campaign>(allCampaigns);
      Campaign agentCampaign = otherCampaigns.get(i);
      this.serverState.registerCampaign(agentCampaign, agent.getName());
      otherCampaigns.remove(agentCampaign);
      i++;
      agent.setCampaigns(agentCampaign, otherCampaigns);
    }
    // this.serverState.printServerState();
    // Ask for bids
    for (SimAgent agent : this.agents) {
      BidBundle bidBundle = agent.getBidBundle();
      // An agent could return a null bid, e.g., in case the reserve is too high.
      if (bidBundle != null) {
        Pair<Boolean, String> bidBundleAccept = this.serverState.addBidBundle(bidBundle.getDay(), agent.getName(), bidBundle);
        if (!bidBundleAccept.getElement1()) {
          throw new AdXException("Bid bundle not accepted. Server replied: " + bidBundleAccept.getElement2());
        }
      }
    }
    this.serverState.advanceDay();
    // Run auctions
    // Logging.log("[Simulator]: run auctions with reserve = " + this.reserve);
    this.serverState.runAdAuctions(this.reserve, this.numberOfImpressions);
    this.serverState.updateDailyStatistics();
    // Report results
    // this.serverState.printServerState();
    // Logging.log(this.serverState.getStatistics().getStatisticsAds().printNiceAdStatisticsTable());
    return this.serverState.getStatistics();
  }

  /**
   * Sample a campaign for the simulator. In this case the campaigns always last for exactly 1 day.
   * 
   * @return
   * @throws AdXException
   */
  private Campaign sampleSimulatorCampaign() throws AdXException {
    Pair<MarketSegment, Integer> randomMarketSegment = Sampling.sampleMarketSegment();
    MarketSegment marketSegment = randomMarketSegment.getElement1();
    Integer expectedMarketSegmentSize = randomMarketSegment.getElement2();
    Campaign campaign = new Campaign(Sampling.getUniqueCampaignId(), 1, 1, marketSegment, (int) Math.floor(this.demandDiscountFactor * expectedMarketSegmentSize * (1.0 / this.agents.size())));
    campaign.setBudget(expectedMarketSegmentSize);
    return campaign;
  }

}
