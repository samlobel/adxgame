package adx.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.statistics.EffectiveReach;
import adx.statistics.Statistics;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.structures.Query;
import adx.util.Parameters;

public class StatisticsTest {

  public static Statistics getStatistics() throws AdXException {
    HashSet<String> agents = new HashSet<String>();
    agents.add("agent0");
    agents.add("agent1");
    agents.add("agent2");
    agents.add("agent3");
    agents.add("agent4");
    return new Statistics(agents);
  }

  @Test
  public void testComputeQualityScore0() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(1.0));

    Campaign c = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c, "agent0");
    c.setBudget(100.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 90, 0.0);
    // This test only works with QUALITY_SCORE_LEARNING_RATE of 0.6
    assertEquals(Parameters.get_QUALITY_SCORE_LEARNING_RATE(), 0.6, 0.00000001);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(0.9261666020749232));
  }

  @Test
  public void testComputeQualityScore1() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c0 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c0, "agent0");

    c0.setBudget(100.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 90, 0.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 90, 0.0);
    // This test only works with QUALITY_SCORE_LEARNING_RATE of 0.6
    assertEquals(Parameters.get_QUALITY_SCORE_LEARNING_RATE(), 0.6, 0.00000001);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(1.1630636924254625));

  }

  @Test
  public void testComputeQualityScore2() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c0 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    Campaign c1 = new Campaign(2, 1, 1, MarketSegment.FEMALE, 200);
    statistics.getStatisticsCampaign().registerCampaign(0, c0, "agent0");
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");

    c0.setBudget(100.0);
    c1.setBudget(200.0);

    // This test only works with QUALITY_SCORE_LEARNING_RATE of 0.6
    assertEquals(Parameters.get_QUALITY_SCORE_LEARNING_RATE(), 0.6, 0.00000001);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 100, 0.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.FEMALE), 200, 0.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(0.9999673448059072));

  }

  @Test
  public void testComputeQualityScore3() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c0 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    Campaign c1 = new Campaign(2, 1, 1, MarketSegment.FEMALE, 200);
    statistics.getStatisticsCampaign().registerCampaign(0, c0, "agent0");
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");

    c0.setBudget(100.0);
    c1.setBudget(200.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 0, 0.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.FEMALE), 200, 0.0);
    // This test only works with QUALITY_SCORE_LEARNING_RATE of 0.6
    assertEquals(Parameters.get_QUALITY_SCORE_LEARNING_RATE(), 0.6, 0.00000001);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(0.7599766748613623));

  }

  @Test
  public void testComputeQualityScore4() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c0 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    Campaign c1 = new Campaign(2, 1, 1, MarketSegment.FEMALE, 200);
    statistics.getStatisticsCampaign().registerCampaign(0, c0, "agent0");
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");

    c0.setBudget(100.0);
    c1.setBudget(200.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 100, 0.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.FEMALE), 0, 0.0);
    // This test only works with QUALITY_SCORE_LEARNING_RATE of 0.6
    assertEquals(Parameters.get_QUALITY_SCORE_LEARNING_RATE(), 0.6, 0.00000001);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement1(), new Double(0.39999066994454496));

  }

  @Test
  public void testComputeProfit0() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(0.0));

    Campaign c0 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    Campaign c1 = new Campaign(2, 1, 1, MarketSegment.FEMALE, 200);
    statistics.getStatisticsCampaign().registerCampaign(0, c0, "agent0");
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");

    c0.setBudget(100.0);
    c1.setBudget(200.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 100, 0.0);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.FEMALE), 100, 0.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(144.1513113566575));

    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.FEMALE), 100, 0.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(299.9883374306812));
  }

  @Test
  public void testComputeProfit1() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c1 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");
    c1.setBudget(100.0);

    Campaign c2 = new Campaign(2, 1, 1, MarketSegment.MALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c2, "agent0");
    c2.setBudget(100.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.MALE_HIGH_INCOME), 80, 85.25);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(-114.94367301687879));
  }

  @Test
  public void testComputeProfit2() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c1 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");
    c1.setBudget(100.0);

    Campaign c2 = new Campaign(2, 1, 2, MarketSegment.MALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c2, "agent0");
    c2.setBudget(100.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 2, new Query(MarketSegment.MALE_HIGH_INCOME), 80, 85.25);

    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(-114.94367301687879));
    statistics.updateDailyStatistics(1, EffectiveReach.SIGMOIDAL);
    statistics.getStatisticsAds().addStatistic(2, "agent0", 2, new Query(MarketSegment.FEMALE), 60, 85.25);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(2, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(-114.94367301687879 - 85.25));
    // Logging.log(statistics.getStatisticsAds().printNiceSummaryTable());
  }

  @Test
  public void testComputeProfit3() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c1 = new Campaign(1, 1, 10, MarketSegment.YOUNG, 1000);
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");
    c1.setBudget(3500.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 150, 0.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(121.4532557060372));

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE), 9999, 99.9);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(1, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(121.4532557060372 - 99.9));

    statistics.updateDailyStatistics(1, EffectiveReach.SIGMOIDAL);

    statistics.getStatisticsAds().addStatistic(2, "agent0", 1, new Query(MarketSegment.MALE), 9999, 99.9);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(2, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(121.4532557060372 - 99.9 - 99.9));

    statistics.getStatisticsAds().addStatistic(2, "agent0", 1, new Query(MarketSegment.MALE_YOUNG_LOW_INCOME), 623, 0.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(2, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(2078.516182293361));

    statistics.updateDailyStatistics(2, EffectiveReach.SIGMOIDAL);

    statistics.getStatisticsAds().addStatistic(3, "agent0", 1, new Query(MarketSegment.FEMALE_OLD), 789, 24.0);
    assertEquals(statistics.computeQualityScoreAndCumulativeProfit(3, "agent0", EffectiveReach.SIGMOIDAL).getElement2(), new Double(2078.516182293361 - 24.0));

    // Logging.log(statistics.getStatisticsAds().printNiceSummaryTable());
    // Logging.log(statistics.getStatisticsAds().printNiceAdStatisticsTable());
  }

  @Test
  public void computeEffectiveReach() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    Campaign c1 = new Campaign(1, 1, 1, MarketSegment.FEMALE, 100);
    statistics.getStatisticsCampaign().registerCampaign(0, c1, "agent0");
    c1.setBudget(100.0);

    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_HIGH_INCOME), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.OLD), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.YOUNG_LOW_INCOME), 80, 85.25);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_OLD_HIGH_INCOME), 80, 85.25);
    int effectiveReach = statistics.getStatisticsAds().getDailyEffectiveReach(1, "agent0", c1.getId());
    Double cost = statistics.getStatisticsAds().getDailySummaryStatistic(1, "agent0", c1.getId()).getElement2();
    assertEquals(effectiveReach, 240);
    assertEquals(cost, new Double(596.75));
  }

}
