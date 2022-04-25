package adx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.statistics.Statistics;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.structures.Query;

public class StatisticsAdsTest {

  @Test
  public void testAddStatistic0() throws AdXException {
    HashSet<String> agents = new HashSet<String>();
    agents.add("agent0");
    agents.add("agent1");
    Statistics adStatistics = new Statistics(agents);
    try {
      adStatistics.getStatisticsAds().getDailyStatistic(-1, null, 0, null);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }
    try {
      adStatistics.getStatisticsAds().getDailyStatistic(1, null, 0, null);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }

    try {
      adStatistics.getStatisticsAds().addStatistic(0, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME), 10, 100.0);
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }

    try {
      adStatistics.getStatisticsAds().getDailyStatistic(0, "WRONG AGENT", 0, null);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }

    try {
      adStatistics.getStatisticsAds().getDailyStatistic(0, "agent0", 0, null);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }

    adStatistics.getStatisticsCampaign().registerCampaign(0, new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME), 10, 100.0);

    adStatistics.getStatisticsCampaign().registerCampaign(0, new Campaign(2, 1, 2, MarketSegment.FEMALE, 1), "agent1");
    adStatistics.getStatisticsAds().addStatistic(1, "agent1", 2, new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME), 10, 100.0);

    assertEquals(adStatistics.getStatisticsAds().getDailyStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME)).getElement1(), new Integer(10));
    assertEquals(adStatistics.getStatisticsAds().getDailyStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME)).getElement2(), new Double(100.0));
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_OLD_LOW_INCOME), 20, 200.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 30, 300.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME), 40, 400.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_YOUNG_LOW_INCOME), 50, 500.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_OLD_LOW_INCOME), 60, 600.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME), 70, 700.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_OLD_HIGH_INCOME), 80, 800.0);
    adStatistics.getStatisticsAds().addStatistic(1, "agent1", 2, new Query(MarketSegment.MALE_OLD_HIGH_INCOME), 90, 900.0);
    adStatistics.getStatisticsAds().addStatistic(2, "agent1", 2, new Query(MarketSegment.MALE_OLD_HIGH_INCOME), 100, 1000.0);
    adStatistics.getStatisticsAds().addStatistic(2, "agent1", 2, new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME), 1000, 3000.0);
    assertEquals(adStatistics.getStatisticsAds().getDailyStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME)).getElement1(), new Integer(40));
    assertEquals(adStatistics.getStatisticsAds().getDailyStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME)).getElement2(), new Double(400.0));
    assertEquals(adStatistics.getStatisticsAds().getDailySummaryStatistic(1, "agent0", 1).getElement1(), new Integer(360));
    assertEquals(adStatistics.getStatisticsAds().getDailySummaryStatistic(1, "agent0", 1).getElement2(), new Double(3600.0));
    assertEquals(adStatistics.getStatisticsAds().getDailySummaryStatistic(2, "agent1", 2).getElement1(), new Integer(1100));
    assertEquals(adStatistics.getStatisticsAds().getDailySummaryStatistic(2, "agent1", 2).getElement2(), new Double(4000.0));

    try {
      adStatistics.getStatisticsAds().getDailySummaryStatistic(-1, null, 0);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }
    try {
      adStatistics.getStatisticsAds().getDailySummaryStatistic(1, null, 0);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }
    try {
      adStatistics.getStatisticsAds().getDailySummaryStatistic(0, "WRONG AGENT", 0);
      fail("Suppose to get AdXException");
    } catch (AdXException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail("Got wrong kind of exception");
    }
  }
  
  @Test(expected = AdXException.class)
  public void testAddStatistic1() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsAds().addStatistic(0, "agent0", 1, new Query(MarketSegment.MALE_OLD_LOW_INCOME), 3, 3.28);
  }

  @Test
  public void testAddStatistic2() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsCampaign().registerCampaign(0, new Campaign(1, 1, 2, MarketSegment.FEMALE_YOUNG_HIGH_INCOME, 1000), "agent0");
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.MALE_OLD_LOW_INCOME), 30, 3.28);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_OLD_LOW_INCOME), 3000, 3.28);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 5, 3.28);
    statistics.getStatisticsAds().addStatistic(1, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 75, 3.28);
    statistics.getStatisticsAds().addStatistic(2, "agent0", 1, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 75, 3.28);
    //Logging.log(statistics.getStatisticsAds().printNiceAdStatisticsTable());
    //Logging.log(statistics.getStatisticsAds().printNiceSummaryTable());
  }

}
