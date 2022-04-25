package adx.test;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.statistics.Statistics;
import adx.statistics.StatisticsBids;
import adx.structures.MarketSegment;
import adx.structures.Query;
import adx.util.Pair;

public class StatisticsBidsTest {

  @Test
  public void test() throws AdXException {
    StatisticsBids bidsStatistics = new StatisticsBids(new Statistics(new HashSet<>(Arrays.asList("dummyagent"))));
    bidsStatistics.addBids(0, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 
        Arrays.asList(new Pair<String, Double>("agent 0", 1.0),
                      new Pair<String, Double>("agent 1", 13.0)));
    bidsStatistics.addBids(0, new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME), 
        Arrays.asList(new Pair<String, Double>("agent 1", 31.0),
                      new Pair<String, Double>("agent 3", 13.0)));
    bidsStatistics.addBids(0, new Query(MarketSegment.MALE_OLD_HIGH_INCOME), 
        Arrays.asList(new Pair<String, Double>("agent 11", 31231.0),
                      new Pair<String, Double>("agent 32", 11233.0)));
    bidsStatistics.printBids(0);
  }

}
