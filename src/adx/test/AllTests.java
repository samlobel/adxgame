package adx.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MarketSegmentTest.class, QueryTest.class, ServerStateTest.class, StatisticsTest.class, SamplingTest.class, BidBundleTest.class,
    CampaignAuctionsTest.class, GameAgentTest.class, StatisticsAdsTest.class, StatisticsCampaignsTest.class, StatisticsBidsTest.class })
public class AllTests {

}
