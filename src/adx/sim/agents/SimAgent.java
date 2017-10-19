package adx.sim.agents;

import java.util.List;

import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.util.Logging;

/**
 * Abstract class to be extended by a simulated agent.
 * 
 * @author Enrique Areyan Viqueira
 */
public abstract class SimAgent {

  /**
   * A name for the simAgent
   */
  protected String simAgentName;

  /**
   * The agent's campaign.
   */
  protected Campaign myCampaign;

  /**
   * The other agent's campaigns.
   */
  protected List<Campaign> othersCampaigns;
  
  /**
   * The reserve price.
   */
  protected final double reserve;
  
  /**
   * Number of impressions.
   */
  protected final int numberOfImpressions;

  /**
   * Constructor.
   * 
   * @param simAgentName
   */
  public SimAgent(String simAgentName, double reserve, int numberOfImpressions) {
    this.simAgentName = simAgentName;
    this.reserve = reserve;
    this.numberOfImpressions = numberOfImpressions;
    Logging.log("[Agent] " + simAgentName + ", (r, m) = (" + this.reserve + "," + this.numberOfImpressions + ")");
  }

  /**
   * Getter.
   * 
   * @return the simAgent name.
   */
  public String getName() {
    return this.simAgentName;
  }

  /**
   * Setter.
   * 
   * @param campaign
   */
  public void setCampaigns(Campaign campaign, List<Campaign> othersCampaigns) {
    this.myCampaign = campaign;
    this.othersCampaigns = othersCampaigns;
    // Logging.log("[Agent] " + simAgentName + ", (r, m) = (" + this.reserve + "," + this.numberOfImpressions + "), " + "Segment = " + this.myCampaign.getMarketSegment() + " Reach = " + this.myCampaign.getReach() + " Reward = " + this.myCampaign.getBudget());
    // Logging.log(this.simAgentName);
    // Logging.log(this.myCampaign);
    // Logging.log(this.othersCampaigns);
  }

  /**
   * The main method to be implemented by a SimAgent.
   * 
   * @return a BidBundle
   */
  public abstract BidBundle getBidBundle();
  
}
