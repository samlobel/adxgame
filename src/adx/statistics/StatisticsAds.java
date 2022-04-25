package adx.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.structures.MarketSegment;
import adx.structures.Query;
import adx.util.InputValidators;
import adx.util.Pair;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * A class that deals with ad statistics bookkeeping.
 * 
 * @author Enrique Areyan Viqueira
 */
public class StatisticsAds {
  /**
   * The statistics object.
   */
  protected final Statistics statisticsObj;
  /**
   * A table that maps: Days -> Agent -> IdCampaign -> Query -> (Win Count, Win Cost)
   */
  protected final Table<Integer, String, Table<Integer, Query, Pair<Integer, Double>>> dailyStatistics;

  /**
   * A table that maps: Days -> Agent -> IdCampaign > (Win Count, Win Cost)
   */
  protected final Table<Integer, String, Map<Integer, Pair<Integer, Double>>> dailySummary;

  /**
   * A table that maps: Days -> Agent -> IdCampaign > (Effective Win Count, Win Cost)
   */
  protected final Table<Integer, String, Map<Integer, Integer>> dailyEffectiveReach;

  /**
   * A map: Agent -> IdCampaign > (Total Effective Win Count, Total Win Cost)
   */
  protected final Map<String, Map<Integer, Pair<Integer, Double>>> summary;

  /**
   * A map: Day -> (Impressions allocated at reserve, Impressions allocated not at reserve)
   */
  protected final Map<Integer, Pair<Integer, Integer>> reserveAllocation;

  /**
   * A map: Day -> (Impressions not allocated because bidders do not meet reserve, Impressions not allocated because lack of bids)
   */
  protected final Map<Integer, Pair<Integer, Integer>> noAllocation;

  /**
   * Constructor.
   * 
   * @param statisticsObj
   * @throws AdXException
   */
  public StatisticsAds(Statistics statisticsObj) throws AdXException {
    InputValidators.validateNotNull(statisticsObj);
    this.statisticsObj = statisticsObj;
    this.dailyStatistics = HashBasedTable.create();
    this.dailySummary = HashBasedTable.create();
    this.dailyEffectiveReach = HashBasedTable.create();
    this.summary = new HashMap<String, Map<Integer, Pair<Integer, Double>>>();
    this.reserveAllocation = new HashMap<Integer, Pair<Integer, Integer>>();
    this.noAllocation = new HashMap<Integer, Pair<Integer, Integer>>();
  }

  /**
   * Given the agent and a campaign ID, returns a pair (TotalWinCount, TotalCost) for the agent's campaign.
   * 
   * @param agent
   * @param campaignId
   * @return a pair (TotalWinCount, TotalCost) for the agent's campaign.
   */
  public Pair<Integer, Double> getSummaryStatistic(String agent, int campaignId) {
    if (this.summary.containsKey(agent) && this.summary.get(agent).containsKey(campaignId)) {
      return this.summary.get(agent).get(campaignId);
    }
    return new Pair<Integer, Double>(0, 0.0);
  }

  /**
   * Returns a statistics.
   * 
   * @param day
   * @param agent
   * @param campaignId
   * @param query
   * @return a Tuple containing the winCount and the winCost.
   * @throws AdXException
   */
  public Pair<Integer, Double> getDailyStatistic(int day, String agent, int campaignId, Query query) throws AdXException {
    InputValidators.validateDay(day);
    InputValidators.validateNotNull(query);
    this.statisticsObj.checkAgentName(agent);
    this.statisticsObj.getStatisticsCampaign().checkCampaign(agent, campaignId);
    if (this.dailyStatistics.get(day, agent) == null || this.dailyStatistics.get(day, agent).get(campaignId, query) == null) {
      this.addStatistic(day, agent, campaignId, query, 0, 0.0);
    }
    return this.dailyStatistics.get(day, agent).get(campaignId, query);
  }

  /**
   * Returns summary statistics for the given day, agent and campaign id.
   * 
   * @param day
   * @param agent
   * @param campaignId
   * @param query
   * @return
   * @throws AdXException
   */
  public Pair<Integer, Double> getDailySummaryStatistic(int day, String agent, int campaignId) throws AdXException {
    InputValidators.validateDay(day);
    this.statisticsObj.checkAgentName(agent);
    this.statisticsObj.getStatisticsCampaign().checkCampaign(agent, campaignId);
    if (!this.dailySummary.contains(day, agent)) {
      HashMap<Integer, Pair<Integer, Double>> newStat = new HashMap<Integer, Pair<Integer, Double>>();
      this.dailySummary.put(day, agent, newStat);
    }
    if (!this.dailySummary.get(day, agent).containsKey(campaignId)) {
      this.dailySummary.get(day, agent).put(campaignId, new Pair<Integer, Double>(0, 0.0));
    }
    return this.dailySummary.get(day, agent).get(campaignId);
  }

  /**
   * Returns the daily summary statistic for the given day and agent.
   * 
   * @param day
   * @param agent
   * @return
   */
  public Map<Integer, Pair<Integer, Double>> getDailySummaryStatistic(int day, String agent) {
    return this.dailySummary.get(day, agent);
  }

  /**
   * Gets the effective reach.
   * 
   * @param day
   * @param agent
   * @param campaignId
   * @return
   */
  public int getDailyEffectiveReach(int day, String agent, int campaignId) {
    if (this.dailyEffectiveReach.get(day, agent) == null || this.dailyEffectiveReach.get(day, agent).get(campaignId) == null) {
      return 0;
    }
    return this.dailyEffectiveReach.get(day, agent).get(campaignId);
  }

  /**
   * Adds an statistic.
   * 
   * @param day
   * @param agent
   * @param campaignId
   * @param query
   * @param winCount
   * @param winCost
   * @throws AdXException
   */
  public void addStatistic(int day, String agent, int campaignId, Query query, int winCount, double winCost) throws AdXException {
    InputValidators.validateDay(day);
    InputValidators.validateNotNull(query);
    this.statisticsObj.checkAgentName(agent);
    this.statisticsObj.getStatisticsCampaign().checkCampaign(agent, campaignId);
    this.statisticsObj.getStatisticsCampaign().checkCampaignActive(day, campaignId);

    // Check if we have added an statistic for this day and this agent before.
    if (!this.dailyStatistics.contains(day, agent)) {
      this.dailyStatistics.put(day, agent, HashBasedTable.create());
      this.dailySummary.put(day, agent, new HashMap<Integer, Pair<Integer, Double>>());
      this.dailyEffectiveReach.put(day, agent, new HashMap<Integer, Integer>());
    }

    // Add daily statistic.
    if (!this.dailyStatistics.get(day, agent).contains(campaignId, query)) {
      this.dailyStatistics.get(day, agent).put(campaignId, query, new Pair<Integer, Double>(winCount, winCost));
    } else {
      Integer currentCount = this.dailyStatistics.get(day, agent).get(campaignId, query).getElement1();
      Double currentCost = this.dailyStatistics.get(day, agent).get(campaignId, query).getElement2();
      this.dailyStatistics.get(day, agent).put(campaignId, query, new Pair<Integer, Double>(currentCount + winCount, currentCost + winCost));
    }

    // Add daily summary.
    if (!this.dailySummary.get(day, agent).containsKey(campaignId)) {
      this.dailySummary.get(day, agent).put(campaignId, new Pair<Integer, Double>(winCount, winCost));
    } else {
      Pair<Integer, Double> total = this.dailySummary.get(day, agent).get(campaignId);
      this.dailySummary.get(day, agent).put(campaignId, new Pair<Integer, Double>(total.getElement1() + winCount, total.getElement2() + winCost));
    }

    // Compute effective reach provided by the impressions of this query.
    int effectiveReach = this.computeEffectiveReach(campaignId, query, winCount);

    // Add daily effective reach.
    if (!this.dailyEffectiveReach.get(day, agent).containsKey(campaignId)) {
      this.dailyEffectiveReach.get(day, agent).put(campaignId, effectiveReach);
    } else {
      Integer total = this.dailyEffectiveReach.get(day, agent).get(campaignId);
      this.dailyEffectiveReach.get(day, agent).put(campaignId, total + effectiveReach);
    }

    // Add the final summary.
    if (!this.summary.containsKey(agent)) {
      this.summary.put(agent, new HashMap<Integer, Pair<Integer, Double>>());
    }
    if (!this.summary.get(agent).containsKey(campaignId)) {
      this.summary.get(agent).put(campaignId, new Pair<Integer, Double>(effectiveReach, winCost));
    } else {
      Pair<Integer, Double> total = this.summary.get(agent).get(campaignId);
      this.summary.get(agent).put(campaignId, new Pair<Integer, Double>(total.getElement1() + effectiveReach, total.getElement2() + winCost));
    }
  }

  /**
   * Given a day and a boolean variable allocatedAtReserve, adds one to the counter of impressions allocated at reserve in case the variable is true, otherwise
   * adds one to the counter of impressions not allocated at reserve.
   * 
   * @param day
   * @param allocatedAtReserve
   */
  public void addReserveAllocation(int day, boolean allocatedAtReserve) {
    if (!this.reserveAllocation.containsKey(day)) {
      this.reserveAllocation.put(day, new Pair<Integer, Integer>(0, 0));
    }
    Pair<Integer, Integer> currentAllocation = this.reserveAllocation.get(day);
    this.reserveAllocation.put(day, new Pair<Integer, Integer>(currentAllocation.getElement1() + (allocatedAtReserve ? 1 : 0), currentAllocation.getElement2() + (!allocatedAtReserve ? 1 : 0)));
  }

  /**
   * Getter.
   * 
   * @param day
   * @return
   */
  public Pair<Integer, Integer> getReseveAllocation(int day) {
    if (!this.reserveAllocation.containsKey(day)) {
      this.reserveAllocation.put(day, new Pair<Integer, Integer>(0, 0));
    }
    return this.reserveAllocation.get(day);
  }

  /**
   * Given a day and a boolean variable reserveTooExpensive, adds one to the counter of impressions not allocated bue to the reserve being too high, otherwise
   * adds one to the impressions not beign allocated for other reasons (i.e., no bids were present OR bidders already exaushted their limits).
   * 
   * @param day
   * @param reserveTooExpensive
   */
  public void addNoAllocation(int day, boolean reserveTooExpensive) {
    if (!this.noAllocation.containsKey(day)) {
      this.noAllocation.put(day, new Pair<Integer, Integer>(0, 0));
    }
    Pair<Integer, Integer> current = this.noAllocation.get(day);
    this.noAllocation.put(day, new Pair<Integer, Integer>(current.getElement1() + (reserveTooExpensive ? 1 : 0), current.getElement2() + (!reserveTooExpensive ? 1 : 0)));
  }

  /**
   * Getter.
   * 
   * @param day
   * @return
   */
  public Pair<Integer, Integer> getNoAllocation(int day) {
    if (!this.noAllocation.containsKey(day)) {
      this.noAllocation.put(day, new Pair<Integer, Integer>(0, 0));
    }
    return this.noAllocation.get(day);
  }

  /**
   * Given a campaignId, a query, and a winCount, returns the effective win.
   * 
   * @param campaignId
   * @param query
   * @param winCount
   * @return
   * @throws AdXException
   */
  public int computeEffectiveReach(int campaignId, Query query, int winCount) throws AdXException {
    int effectiveWinCount = 0;
    if (MarketSegment.marketSegmentSubset(this.statisticsObj.getStatisticsCampaign().getCampaign(campaignId).getMarketSegment(), query.getMarketSegment())) {
      effectiveWinCount += winCount;
    }
    return effectiveWinCount;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the summary statistics table.
   */
  public String printNiceSummaryTable() {
    String ret = "";
    if (this.summary.size() > 0) {
      for (Entry<String, Map<Integer, Pair<Integer, Double>>> agent : this.summary.entrySet()) {
        ret += "\n\t\t Agent: " + agent.getKey();
        for (Entry<Integer, Pair<Integer, Double>> y : agent.getValue().entrySet()) {
          ret += "\n\t\t\t Campaign: " + y.getKey() + ", totals = " + y.getValue();
        }
      }
    } else {
      ret += "Currently, there are no summary statistics.";
    }
    return ret;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the ad statistics table.
   */
  public String printNiceAdStatisticsTable() {
    String ret = "";
    if (this.dailyStatistics.rowMap().entrySet().size() > 0) {
      for (Entry<Integer, Map<String, Table<Integer, Query, Pair<Integer, Double>>>> day : this.dailyStatistics.rowMap().entrySet()) {
        ret += "\n\t\t Day: " + day.getKey();
        for (Entry<String, Table<Integer, Query, Pair<Integer, Double>>> agent : day.getValue().entrySet()) {
          ret += "\n\t\t\t Agent: " + agent.getKey();
          for (Entry<Integer, Map<Query, Pair<Integer, Double>>> campaign : agent.getValue().rowMap().entrySet()) {
            ret += "\n\t\t\t\t Campaign: " + campaign.getKey() + ", total for this day "
                + this.dailySummary.get(day.getKey(), agent.getKey()).get(campaign.getKey()) + ", effective reach for this day "
                + this.dailyEffectiveReach.get(day.getKey(), agent.getKey()).get(campaign.getKey());
            for (Entry<Query, Pair<Integer, Double>> query : campaign.getValue().entrySet()) {
              ret += "\n\t\t\t\t\t Query: " + query.getKey() + ", (" + query.getValue().getElement1() + ", " + query.getValue().getElement2() + ")";
            }
          }
        }
      }
    } else {
      ret += "Currently, there are no statistics.";
    }
    return ret;
  }
}
