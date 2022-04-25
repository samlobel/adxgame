package adx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.structures.Campaign;
import adx.structures.Query;
import adx.util.Logging;
import adx.util.Sampling;

public class SamplingTest {

  @Test
  public void testSampleAndBucketPopulation() throws AdXException {
    for (int i = 10; i < 1000; i++) {
      HashMap<Query, Integer> population = Sampling.sampleAndBucketPopulation(i);
      int total = 0;
      for(int n : population.values()) {
        total += n;
      }
      assertEquals(i, total);
    }
  }

  @Test
  public void testSampleInitialCampaign() throws AdXException {
    assertTrue(Sampling.sampleInitialCampaign() instanceof Campaign);
  }

  @Test
  public void testSampleCampaign() throws AdXException {
    assertTrue(Sampling.sampleCampaign(0) instanceof Campaign);
  }
  
  @Test
  public void testCampaignData() throws AdXException {
    //Logging.log(new Campaign(1, 1, 3, MarketSegment.FEMALE_OLD_HIGH_INCOME, 100));
    //Logging.log(Sampling.sampleCampaignOpportunityMessage(17, MarketSegment.proportionsList));
  }
  
  @Test
  public void testSamplePopulation() throws AdXException {
    List<Query> samplePopulation = Sampling.samplePopulation(100);
    Logging.log(samplePopulation);
    Logging.log("Size = " + samplePopulation.size());
    Logging.log(Sampling.cumulativeMarketSegments);
    HashMap<Query, Integer> population = Sampling.sampleAndBucketPopulation(100);
    Logging.log(population);
  }
}
