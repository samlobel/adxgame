package adx.sim;

import java.util.ArrayList;
import java.util.List;

import adx.exceptions.AdXException;
import adx.server.ServerState;
import adx.sim.agents.SimAgent;
import adx.statistics.Statistics;
import adx.structures.BidBundle;
import adx.structures.Campaign;
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
   * Constructor.
   * 
   * @param agents
   * @throws AdXException
   */
  public Simulator(List<SimAgent> agents, double reserve, int numberOfImpressions) throws AdXException {
    this.agents = agents;
    this.serverState = new ServerState(0);
    for (SimAgent simAgent : agents) {
      this.serverState.registerAgent(simAgent.getName());
    }
    this.reserve = reserve;
    this.numberOfImpressions = numberOfImpressions;
  }
  
  /**
   * Constructor. 
   * 
   * @param agents
   * @throws AdXException
   */
  public Simulator(List<SimAgent> agents) throws AdXException {
    this(agents, 0.0, Parameters.POPULATION_SIZE);
  }

  /**
   * Run the simulation.
   * @return 
   * 
   * @throws AdXException
   */
  public Statistics run() throws AdXException {

    this.serverState.initStatistics();

    // Sample and distribute campaigns
    List<Campaign> allCampaigns = new ArrayList<Campaign>();
    for (int j = 0; j < this.agents.size(); j++) {
      allCampaigns.add(Sampling.sampleInitialCampaign());
    }
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
      Pair<Boolean, String> bidBundleAccept = this.serverState.addBidBundle(bidBundle.getDay(), agent.getName(), bidBundle);
      if (!bidBundleAccept.getElement1()) {
        throw new AdXException("Bid bundle not accepted. Server replied: " + bidBundleAccept.getElement2());
      }
    }
    this.serverState.advanceDay();
    // Run auctions
    this.serverState.runAdAuctions(this.reserve, this.numberOfImpressions);
    this.serverState.updateDailyStatistics();
    // Report results
    //this.serverState.printServerState();
    //Logging.log(this.serverState.getStatistics().getStatisticsAds().printNiceAdStatisticsTable());
    return this.serverState.getStatistics();
  }

}
