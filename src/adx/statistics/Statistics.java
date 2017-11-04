package adx.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.Campaign;
import adx.util.InputValidators;
import adx.util.Pair;
import adx.util.Parameters;
import adx.util.Printer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Maintains all of the game's statistics. The statistics are divided in two categories: campaigns and ads. Each category has its own handler.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Statistics {

  /**
   * Contains the permissible agents.
   */
  private final Set<String> agentsNames;

  /**
   * A map from Day -> Agent -> Quality Score.
   */
  private final Table<Integer, String, Double> qualityScores;

  /**
   * A map from Day -> Agent -> Profit
   */
  private final Table<Integer, String, Double> profit;

  /**
   * A handler to deal with campaign statistics.
   */
  private final StatisticsCampaigns campaignsStatisticsHandler;

  /**
   * A handler to deal with campaign statistics.
   */
  private final StatisticsAds adsStatisticsHandler;
  
  /**
   * A handler to deal with bid statistics.
   */
  private final StatisticsBids bidStatistics;

  /**
   * Constructor.
   * 
   * @throws AdXException
   */
  public Statistics(Set<String> agentsNames) throws AdXException {
    this.agentsNames = agentsNames;
    this.qualityScores = HashBasedTable.create();
    this.profit = HashBasedTable.create();
    for (String agent : this.agentsNames) {
      this.qualityScores.put(0, agent, 1.0);
      this.profit.put(0, agent, 0.0);
    }
    InputValidators.validateNotNull(agentsNames);
    this.campaignsStatisticsHandler = new StatisticsCampaigns(this);
    this.adsStatisticsHandler = new StatisticsAds(this);
    this.bidStatistics = new StatisticsBids(this);
  }

  /**
   * Getter.
   * 
   * @return the campaign statistics handler.
   */
  public StatisticsCampaigns getStatisticsCampaign() {
    return this.campaignsStatisticsHandler;
  }

  /**
   * Getter.
   * 
   * @return the ad statistics handler.
   */
  public StatisticsAds getStatisticsAds() {
    return this.adsStatisticsHandler;
  }
  
  /**
   * Getter.
   * 
   * @return the bid statistics handler.
   */
  public StatisticsBids getStatisticsBids() {
    return this.bidStatistics;
  }

  /**
   * Getter.
   * 
   * @return the map of quality scores.
   * @throws AdXException
   */
  public Double getQualityScore(int day, String agent) throws AdXException {
    if (!this.qualityScores.contains(day, agent)) {
      throw new AdXException("The quality score on day " + day + ", for agent " + agent + ", does not exists");
    }
    return this.qualityScores.get(day, agent);
  }

  /**
   * Getter.
   * 
   * @return the profit made on the given day by the given agent.
   * @throws AdXException
   */
  public Double getProfit(int day, String agent) throws AdXException {
    if (!this.profit.contains(day, agent)) {
      throw new AdXException("The profit on day " + day + ", for agent " + agent + ", does not exists");
    }
    return this.profit.get(day, agent);
  }

  /**
   * Getter.
   * 
   * @param day
   * @return the map of quality scores for the given day.
   */
  public Map<String, Double> getQualityScores(int day) {
    return this.qualityScores.row(day);
  }

  /**
   * Checks that the agent name is registered. Throws an exception if the agent name is not registered.
   * 
   * @param agentName
   * @throws AdXException
   */
  protected void checkAgentName(String agentName) throws AdXException {
    if (!this.agentsNames.contains(agentName)) {
      throw new AdXException("Unknown agent " + agentName);
    }
  }

  /**
   * Updates daily statistics, in this case quality score and profit.
   * 
   * @param day
   * @throws AdXException
   */
  public void updateDailyStatistics(int day) throws AdXException {
    for (String agent : this.agentsNames) {
      Pair<Double, Double> data = this.computeQualityScoreAndCumulativeProfit(day, agent);
      this.qualityScores.put(day, agent, data.getElement1());
      this.profit.put(day, agent, data.getElement2());
    }
  }

  /**
   * Computes the quality score and cumulative profit of an agent in a given day.
   * 
   * @param day
   * @param agent
   * @return
   * @throws AdXException
   */
  public Pair<Double, Double> computeQualityScoreAndCumulativeProfit(int day, String agent) throws AdXException {
    // First, we need to get all active campaigns for this agent.
    List<Campaign> campaigns = this.campaignsStatisticsHandler.getAgentActiveCampaign(day, agent);
    double qualityScore = this.getQualityScore(day - 1, agent);
    Double yesterdaysProfit = this.getProfit(day - 1, agent);
    double todaysProfit = 0.0;
    if (campaigns != null && campaigns.size() > 0) {
      for (Campaign c : campaigns) {
        int totalReachSoFar = this.adsStatisticsHandler.getSummaryStatistic(agent, c.getId()).getElement1();
        int todayEffectiveReach = this.adsStatisticsHandler.getDailyEffectiveReach(day, agent, c.getId());
        double todayCost = this.adsStatisticsHandler.getDailySummaryStatistic(day, agent, c.getId()).getElement2();
        // TODO: the profit and quality score should be a function of the effective reach only!
        todaysProfit += (this.computeEffectiveReachRatio(totalReachSoFar, c.getReach()) - this.computeEffectiveReachRatio(
            totalReachSoFar - todayEffectiveReach, c.getReach())) * c.getBudget() - todayCost;
        if (c.getEndDay() == day) {
          // The campaign ended today, update the quality score.
          qualityScore = (1 - Parameters.QUALITY_SCORE_LEARNING_RATE) * qualityScore + Parameters.QUALITY_SCORE_LEARNING_RATE
              * this.computeEffectiveReachRatio(totalReachSoFar, c.getReach());
        }
      }
    }
    return new Pair<Double, Double>(qualityScore, yesterdaysProfit + todaysProfit);
  }

  /**
   * Given a number of impressions x, a total reach and total budget, computes the Effective Reach Ratio.
   * 
   * @param x
   * @param reach
   * @param budget
   * @return the effective reach ratio for obtaining x impressions on a campaign with given reach and budget.
   */
  private double computeEffectiveReachRatio(double x, int reach) {
    // Logging.log("Compute ERR (x, reach) = (" + x + ", " + reach + ")");
    // NOTE: the following is a sigmoid effective reach ratio.
    // return (2 / 4.08577) * (Math.atan(4.08577 * (x / reach) - 3.08577) - Math.atan(-3.08577));
    // NOTE: this is a linear effective reach ratio with a cap equal to the total reach (no over reach)
    // return Math.min(x / (double) reach, 1.0);
    // NOTE: this is and all-or-nothing effective reach
    // Logging.log("All-or-nothing: " + ((x >= reach) ? 1.0 : 0.0));
    return (x >= reach) ? 1.0 : 0.0;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the quality scores.
   */
  public String getNiceQualityScoresTable() {
    String ret = "";
    if (this.qualityScores.size() > 0) {
      for (Entry<Integer, Map<String, Double>> x : this.qualityScores.rowMap().entrySet()) {
        ret += "\n\t\t Day: " + x.getKey();
        for (Entry<String, Double> y : x.getValue().entrySet()) {
          ret += "\n\t\t\t " + y.getKey() + " -> " + y.getValue();
        }
      }
    } else {
      ret += "Currently, no quality scores are registered";
    }
    return ret;
  }

  /**
   * Printer.
   * 
   * @return a human readable representation of the profits.
   */
  public String getNiceProfitScoresTable() {
    String ret = "";
    if (this.profit.size() > 0) {
      for (Entry<Integer, Map<String, Double>> x : this.profit.rowMap().entrySet()) {
        ret += "\n\t\t Day: " + x.getKey();
        for (Entry<String, Double> y : x.getValue().entrySet()) {
          ret += "\n\t\t\t " + y.getKey() + " -> " + y.getValue();
        }
      }
    } else {
      ret += "Currently, no profits are registered";
    }
    return ret;
  }

  /**
   * Printer.
   * 
   * @param day
   * @return
   */
  public String getNiceProfitScoresTable(int day) {
    return Printer.getNiceProfitTable(this.orderProfits(this.profit.row(day).entrySet()), day);
  }

  /**
   * Given a set of entries <Agent, Profit> return a list of ordered Pairs <Agent, Profit>
   * 
   * @param agentsProfits
   * @return
   */
  public List<Pair<String, Double>> orderProfits(Set<Entry<String, Double>> agentsProfits) {
    List<Pair<String, Double>> finalScores = new ArrayList<Pair<String, Double>>();
    for (Entry<String, Double> x : agentsProfits) {
      finalScores.add(new Pair<String, Double>(x.getKey(), x.getValue()));
    }
    Collections.sort(finalScores, new Comparator<Pair<String, Double>>() {
      @Override
      public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
        if (o1.getElement2() < o2.getElement2()) {
          return 1;
        } else if (o1.getElement2() > o2.getElement2()) {
          return -1;
        } else {
          return 0;
        }
      }
    });
    return finalScores;
  }

  /**
   * One Line Summary results of the game in a given day, from the point of view of the agents.
   * 
   * @param day
   * @throws AdXException
   */
  public String oneLineAgentsSummary(int day, int game) throws AdXException {
    String ret = "";
    for (String agent : this.agentsNames) {
      for (Campaign c : this.campaignsStatisticsHandler.agentsCampaigns.get(agent)) {
        Pair<Integer, Double> winCountCost = this.adsStatisticsHandler.getDailySummaryStatistic(day, agent, c.getId());
        ret += game + "," + agent + "," + c.getMarketSegment() + "," + c.getReach() + "," + c.getBudget() + "," + winCountCost.getElement1() + "," + winCountCost.getElement2() + "\n";
      }
    }
    return ret;
  }

  /**
   * One Line Summary results of the game in a given day, from the point of view of the market maker.
   * 
   * @param day
   * @param game
   * @return
   * @throws AdXException
   */
  public String oneLineMarketMakerSummary(int day, int game, int totalImpressions) throws AdXException {
    int reserveTooHigh = this.adsStatisticsHandler.getNoAllocation(day).getElement1();
    int noBids = this.adsStatisticsHandler.getNoAllocation(day).getElement2();
    int allocatedAtReserve = this.adsStatisticsHandler.getReseveAllocation(day).getElement1();
    int allocatedNotAtReserve = this.adsStatisticsHandler.getReseveAllocation(day).getElement2();
    if (reserveTooHigh + noBids + allocatedAtReserve + allocatedNotAtReserve != totalImpressions) {
      throw new AdXException("The count of allocated + not allocated impressions does not add to the total impression which are " + totalImpressions);
    }
    return game + "," + reserveTooHigh + "," + noBids + "," + allocatedAtReserve + "," + allocatedNotAtReserve + "\n";
  }

}
