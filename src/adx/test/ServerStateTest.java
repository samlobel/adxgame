package adx.test;

import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.server.ServerState;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.structures.Query;

public class ServerStateTest {

  @SuppressWarnings("serial")
  @Test (expected=AdXException.class) 
  public void testValidateBidBundle0() throws AdXException {
    // Test the validation of the bid bundle, case where an agent reports a campaign it does not owns.
    ServerState serverState = new ServerState(0);
    serverState.initStatistics();
    serverState.registerCampaign(new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
    BidBundle bidBundle0 = new BidBundle(0, new HashSet<BidEntry>() {{add(new BidEntry(1, null, 0, 0));}}, null, null);
    serverState.validateBidBundle(0, bidBundle0, "wrong agent");
  }
  
  @SuppressWarnings("serial")
  @Test (expected=AdXException.class)   
  public void testValidateBidBundle1() throws AdXException {
    // Test the validation of the bid bundle, case where an agent reports a campaign that does not exists.
    ServerState serverState = new ServerState(0);
    serverState.initStatistics();
    serverState.registerCampaign(new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
    BidBundle bidBundle0 = new BidBundle(0, new HashSet<BidEntry>() {{add(new BidEntry(10, null, 0, 0));}}, null, null);
    serverState.validateBidBundle(0, bidBundle0, "agent0");
  }
  
  @SuppressWarnings("serial")
  @Test 
  public void testValidateBidBundle2() throws AdXException {
    // Test the validation of the bid bundle, case when the agent report a campaign it owns.
    ServerState serverState = new ServerState(0);
    serverState.registerAgent("agent0");
    serverState.initStatistics();
    serverState.registerCampaign(new Campaign(1, 1 , 1, MarketSegment.FEMALE, 1), "agent0");
    BidBundle bidBundle0 = new BidBundle(1, new HashSet<BidEntry>() {{add(new BidEntry(1, new Query(), 0, 1));}}, null, null);
    try {
      serverState.validateBidBundle(1, bidBundle0, "agent0");
    } catch (AdXException e) {
      fail("Not suppose to throw AdXException");
    }
  }
  
  @SuppressWarnings("serial")
  @Test 
  public void testValidateBidBundle3() throws AdXException {
    // Test the validation of the bid bundle, case when the agent report various campaigns it owns.
    ServerState serverState = new ServerState(0);
    serverState.initStatistics();
    serverState.registerAgent("agent1");
    serverState.registerCampaign(new Campaign(208, 1, 1, MarketSegment.FEMALE, 1), "agent1");
    serverState.registerCampaign(new Campaign(350, 1, 1, MarketSegment.FEMALE, 1), "agent1");
    BidBundle bidBundle0 = new BidBundle(1, new HashSet<BidEntry>() {{
      add(new BidEntry(208, new Query(), 0, 1));
      add(new BidEntry(208, new Query(), 0, 1));
      add(new BidEntry(350, new Query(), 0, 1));}}, null, null);
    try {
      serverState.validateBidBundle(1, bidBundle0, "agent1");
    } catch (AdXException e) {
      fail("Not suppose to throw AdXException" + e.getMessage());
    }    
  }

  @Test (expected=AdXException.class) 
  public void testOverlappingRegistration() throws AdXException {
    ServerState serverState = new ServerState(0);
    serverState.initStatistics();
    serverState.registerCampaign(new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
    serverState.registerCampaign(new Campaign(1, 1, 1, MarketSegment.FEMALE, 1), "agent0");
  }
  
}
