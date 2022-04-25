package adx.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.structures.Campaign;
import adx.util.InputValidators;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * A class that deals with campaign statistics bookkeeping.
 * 
 * @author Enrique Areyan Viqueira
 */
public class StatisticsCampaigns {
  /**
   * The statistics object.
   */
  protected final Statistics statisticsObj;

  /**
   * A map from IdCampaign -> Campaign
   */
  private final Map<Integer, Campaign> campaigns;

  /**
   * A map from IdCampaign -> Agent.
   */
  private final Map<Integer, String> campaignsOwnership;

  /**
   * A map from Day -> Agent -> List of Campaigns.
   */
  protected final Table<Integer, String, List<Campaign>> agentsWonCampaings;

  /**
   * A map from Agent -> List of Campaigns.
   */
  protected final Map<String, List<Campaign>> agentsCampaigns;

  /**
   * Constructor.
   * 
   * @throws AdXException
   */
  public StatisticsCampaigns(Statistics statisticsObj) throws AdXException {
    InputValidators.validateNotNull(statisticsObj);
    this.statisticsObj = statisticsObj;
    this.campaignsOwnership = new HashMap<Integer, String>();
    this.campaigns = new HashMap<Integer, Campaign>();
    this.agentsWonCampaings = HashBasedTable.create();
    this.agentsCampaigns = new HashMap<String, List<Campaign>>();
  }

  /**
   * Returns a campaign given its id.
   * 
   * @param campaignId
   * @return
   */
  public Campaign getCampaign(int campaignId) {
    return this.campaigns.get(campaignId);
  }

  /**
   * Given a day and an agent, returns a list of campaigns active for the day and the agent.
   * 
   * @param day
   * @param agent
   * @return
   */
  public List<Campaign> getAgentActiveCampaign(int day, String agent) {
    List<Campaign> listOfCampaigns = new ArrayList<Campaign>();
    if (this.agentsCampaigns.containsKey(agent)) {
      for (Campaign c : this.agentsCampaigns.get(agent)) {
        if (day >= c.getStartDay() && day <= c.getEndDay()) {
          listOfCampaigns.add(c);
        }
      }
    }
    return listOfCampaigns;
  }

  /**
   * Returns a list of all campaigns owned by the agent on the given day.
   * 
   * @param day
   * @param agent
   * @return a list of all campaigns owned by the agent on the given day.
   */
  public List<Campaign> getWonCampaigns(int day, String agent) {
    return this.agentsWonCampaings.get(day, agent);
  }

  /**
   * This function checks whether the given campaign exists AND if the campaign belongs to the agent.
   * 
   * @param agentName
   * @param campaignId
   * @throws AdXException
   */
  protected void checkCampaign(String agentName, int campaignId) throws AdXException {
    if (!this.campaignExists(campaignId)) {
      throw new AdXException("The campaign " + campaignId + ", does not exists");
    } else if (!this.isOwner(campaignId, agentName)) {
      throw new AdXException("The campaign " + campaignId + " is not owned by " + agentName);
    }
  }

  /**
   * Checks if the campaign is active on the given day.
   * 
   * @param day
   * @param campaignId
   * @throws AdXException
   */
  protected void checkCampaignActive(int day, int campaignId) throws AdXException {
    if (!this.campaignExists(campaignId) || day < this.campaigns.get(campaignId).getStartDay() || day > this.campaigns.get(campaignId).getEndDay()) {
      throw new AdXException("The campaign " + campaignId + " is not active on day " + day);
    }
  }

  /**
   * Register a campaign to an agent.
   * 
   * @param campaignId
   * @param agentName
   * @throws AdXException
   */
  public void registerCampaign(int day, Campaign campaign, String agentName) throws AdXException {
    this.statisticsObj.checkAgentName(agentName);
    int campaignId = campaign.getId();
    InputValidators.validateCampaignId(campaignId);
    if (this.campaignsOwnership.containsKey(campaignId)) {
      throw new AdXException("Campaign: " + campaignId + " has already been registered to: " + agentName);
    }
    this.campaignsOwnership.put(campaignId, agentName);
    if (!this.agentsWonCampaings.contains(day, agentName)) {
      this.agentsWonCampaings.put(day, agentName, new ArrayList<Campaign>());
    }
    this.agentsWonCampaings.get(day, agentName).add(campaign);
    this.campaigns.put(campaignId, campaign);
    if(!this.agentsCampaigns.containsKey(agentName)) {
      this.agentsCampaigns.put(agentName, new ArrayList<Campaign>());
    }
    this.agentsCampaigns.get(agentName).add(campaign);
  }

  /**
   * Given a campaign id, return true if that campaign has been registered and false otherwise.
   * 
   * @param campaignId
   * @return true if the campaign has been registered, false otherwise.
   */
  public boolean campaignExists(int campaignId) {
    return this.campaignsOwnership.containsKey(campaignId) && this.campaigns.containsKey(campaignId);
  }

  /**
   * Given a campaign Id and an agent name, return true if the agent owns the campaign.
   * 
   * @param campaignId
   * @param agentName
   * @return true if the agent owns the campaign.
   */
  public boolean isOwner(int campaignId, String agentName) {
    return this.campaignExists(campaignId) && this.campaignsOwnership.get(campaignId).equals(agentName);
  }

  /**
   * 
   * @return
   */
  public String printNiceCampaignTable() {
    String ret = "";
    if (this.campaigns.entrySet().size() > 0) {
      for (Entry<Integer, Campaign> x : this.campaigns.entrySet()) {
        ret += "\n\t\t" + x.getKey() + " -> " + x.getValue();
      }
    } else {
      ret += "Currently, no campaigns are registered";
    }
    return ret;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the map of campaign ownership.
   */
  public String printNiceCampaignOwnership() {
    String ret = "";
    if (this.campaignsOwnership.entrySet().size() > 0) {
      for (Entry<Integer, String> x : this.campaignsOwnership.entrySet()) {
        ret += "\n\t\t" + x.getKey() + " -> " + x.getValue();
      }
    } else {
      ret += "Currently, no campaign ownership is registered";
    }
    return ret;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the campaign table.
   */
  public String printNiceAgentCampaignTable() {
    String ret = "";
    if (this.agentsWonCampaings.rowMap().entrySet().size() > 0) {
      for (Entry<Integer, Map<String, List<Campaign>>> x : this.agentsWonCampaings.rowMap().entrySet()) {
        ret += "\n\t\t Day: " + x.getKey();
        for (Entry<String, List<Campaign>> y : x.getValue().entrySet()) {
          ret += "\n\t\t\t Agent: " + y.getKey();
          for (Campaign z : y.getValue()) {
            ret += "\n\t\t\t\t " + z;
          }
        }
      }
    } else {
      ret += "\n\t\t Currently, there are no campaigns.";
    }
    return ret;
  }
}
