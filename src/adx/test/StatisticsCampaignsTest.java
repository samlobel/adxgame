package adx.test;

import static org.junit.Assert.*;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.statistics.Statistics;
import adx.structures.Campaign;
import adx.structures.MarketSegment;

public class StatisticsCampaignsTest {

  @Test(expected = AdXException.class)
  public void testRegisterCampaign0() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsCampaign().registerCampaign(1, new Campaign(), "agent0");
  }

  @Test(expected = AdXException.class)
  public void testRegisterCampaign1() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsCampaign().registerCampaign(1, new Campaign(1, 1, 1, MarketSegment.HIGH_INCOME, 1), "WRONG AGENT");
  }

  @Test(expected = AdXException.class)
  public void testRegisterCampaign2() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsCampaign().registerCampaign(0, new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
    statistics.getStatisticsCampaign().registerCampaign(0, new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent1");
  }

  @Test
  public void testRegisterCampaign3() throws AdXException {
    Statistics statistics = StatisticsTest.getStatistics();
    statistics.getStatisticsCampaign().registerCampaign(1, new Campaign(1, 1, 1, MarketSegment.YOUNG_HIGH_INCOME, 1), "agent0");
    assertTrue(statistics.getStatisticsCampaign().isOwner(1, "agent0"));
    assertTrue(statistics.getStatisticsCampaign().campaignExists(1));
    assertFalse(statistics.getStatisticsCampaign().campaignExists(999));
  }

}
