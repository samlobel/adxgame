package adx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import adx.auctions.CampaignAuctions;
import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.util.Pair;

public class CampaignAuctionsTest {

  public List<Pair<String, Double>> getListOfBids0() {
    List<Pair<String, Double>> bids = new ArrayList<Pair<String, Double>>();
    bids.add(new Pair<String, Double>("agent3", 400.0));
    return bids;
  }

  public List<Pair<String, Double>> getListOfBids1() {
    List<Pair<String, Double>> bids = new ArrayList<Pair<String, Double>>();
    bids.add(new Pair<String, Double>("agent0", 100.0));
    bids.add(new Pair<String, Double>("agent3", 400.0));
    bids.add(new Pair<String, Double>("agent2", 300.0));
    bids.add(new Pair<String, Double>("agent1", 200.0));
    bids.add(new Pair<String, Double>("agent4", 100.0));
    return bids;
  }

  public List<Pair<String, Double>> getListOfBids2() {
    List<Pair<String, Double>> bids = new ArrayList<Pair<String, Double>>();
    bids.add(new Pair<String, Double>("agent30", 400.0));
    bids.add(new Pair<String, Double>("agent48", 100.0));
    bids.add(new Pair<String, Double>("agent52", 300.0));
    bids.add(new Pair<String, Double>("agent81", 200.0));
    return bids;
  }

  @Test(expected = AdXException.class)
  public void testNullWinnerDetermination() throws AdXException {
    CampaignAuctions.winnerDetermination(null);
  }

  @Test(expected = AdXException.class)
  public void testEmptyWinnerDetermination() throws AdXException {
    CampaignAuctions.winnerDetermination(new ArrayList<Pair<String, Double>>());
  }

//  @Test
//  public void testWinnerDetermination0() throws AdXException {
//    Pair<Double, List<Pair<String, Double>>> winnersList = CampaignAuctions.winnerDetermination(this.getListOfBids0());
//    assertEquals(winnersList.getElement2().size(), 1);
//    assertEquals(winnersList.getElement2().get(0).getElement2(), new Double(400.0));
//  }
//
//  @Test
//  public void testWinnerDetermination1() throws AdXException {
//    Pair<Double, List<Pair<String, Double>>> winnersList = CampaignAuctions.winnerDetermination(this.getListOfBids1());
//    assertEquals(winnersList.getElement2().size(), 2);
//    assertEquals(winnersList.getElement2().get(0).getElement2(), new Double(100.0));
//    assertEquals(winnersList.getElement2().get(1).getElement2(), new Double(100.0));
//  }
//
//  @Test
//  public void testWinnerDetermination2() throws AdXException {
//    Pair<Double, List<Pair<String, Double>>> winnersList = CampaignAuctions.winnerDetermination(this.getListOfBids2());
//    assertEquals(winnersList.getElement2().size(), 1);
//    assertEquals(winnersList.getElement2().get(0).getElement2(), new Double(100.0));
//  }
//
//  @Test
//  public void testRunCampaignAuction0() throws AdXException {
//    Pair<String, Double> winnerPair = CampaignAuctions.runCampaignAuction(this.getListOfBids0());
//    assertEquals(winnerPair.getElement1(), "agent3");
//    assertEquals(winnerPair.getElement2(), new Double(Double.MAX_VALUE));
//  }
//  
//  @Test
//  public void testRunCampaignAuction1() throws AdXException {
//    Pair<String, Double> winnerPair = CampaignAuctions.runCampaignAuction(this.getListOfBids1());
//    assertTrue(winnerPair.getElement1().equals("agent0") || winnerPair.getElement1().equals("agent4"));
//    assertEquals(winnerPair.getElement2(), new Double(100.0));
//  }
//
//  @Test
//  public void testRunCampaignAuction2() throws AdXException {
//    Pair<String, Double> winnerPair = CampaignAuctions.runCampaignAuction(this.getListOfBids2());
//    assertEquals(winnerPair.getElement1(), "agent48");
//    assertEquals(winnerPair.getElement2(), new Double(200.0));
//  }
//  
//  @Test(expected = AdXException.class)
//  public void testFilterBids0() throws AdXException {
//    Map<String, BidBundle> bidBundles = BidBundleTest.getTableBidBundles().row(0);
//    List<Pair<String, Double>> filteredBids = CampaignAuctions.filterBids(new Campaign(999, 1, 1, MarketSegment.FEMALE, 999), bidBundles, null);
//    assertEquals(filteredBids.size(), 0);
//  }
//
//  @Test
//  public void testFilterBids1() throws AdXException {
//    Map<String, BidBundle> bidBundles = BidBundleTest.getTableBidBundles().row(0);
//    Map<String, Double> qualityScores = new HashMap<String, Double>();
//    List<Pair<String, Double>> filteredBids = CampaignAuctions.filterBids(new Campaign(999, 1, 1, MarketSegment.FEMALE, 999), bidBundles, qualityScores);
//    assertEquals(filteredBids.size(), 0);
//  }
//  
//  @Test
//  public void testFilterBids2() throws AdXException {
//    Map<String, BidBundle> bidBundles = BidBundleTest.getTableBidBundles().row(0);
//    Map<String, Double> qualityScores = new HashMap<String, Double>();
//    qualityScores.put("agent0", 1.0);
//    qualityScores.put("agent1", 1.0);
//    qualityScores.put("agent2", 1.0);
//    qualityScores.put("agent3", 1.0);
//    List<Pair<String, Double>> filteredBids = CampaignAuctions.filterBids(new Campaign(1, 1, 1, MarketSegment.FEMALE, 999), bidBundles, qualityScores);
//    assertEquals(filteredBids.size(), 1);
//  }
//
//  @Test
//  public void testFilterBids3() throws AdXException {
//    Map<String, BidBundle> bidBundles = BidBundleTest.getTableBidBundles().row(0);
//    Map<String, Double> qualityScores = new HashMap<String, Double>();
//    qualityScores.put("agent0", 0.25);
//    qualityScores.put("agent1", 1.0);
//    qualityScores.put("agent2", 1.0);
//    qualityScores.put("agent3", 1.0);
//    List<Pair<String, Double>> filteredBids = CampaignAuctions.filterBids(new Campaign(1, 1, 1, MarketSegment.FEMALE, 1500), bidBundles, qualityScores);
//    assertEquals(filteredBids.size(), 0);
//  }
//
}
