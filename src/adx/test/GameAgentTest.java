package adx.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import adx.agent.GameAgent;
import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.structures.MarketSegment;

public class GameAgentTest {

  @Test
  public void testGetAdBid() throws AdXException {
    GameAgent gameAgent = new GameAgent();
    Campaign c0 = new Campaign(1, 1, 2, MarketSegment.FEMALE_OLD, 1000);
    Campaign c1 = new Campaign(1, 2, 3, MarketSegment.FEMALE_OLD, 1000);
    Campaign c2 = new Campaign(2, 3, 4, MarketSegment.FEMALE_OLD, 1000);
    Campaign c3 = new Campaign(3, 5, 6, MarketSegment.FEMALE_OLD, 1000);
    Campaign c4 = new Campaign(4, 7, 8, MarketSegment.FEMALE_OLD, 1000);
    Campaign c5 = new Campaign(5, 7, 10, MarketSegment.FEMALE_OLD, 1000);
    
    c0.setBudget(1);
    c1.setBudget(1);
    c2.setBudget(1);
    c3.setBudget(1);
    c4.setBudget(1);
    c5.setBudget(1);
    
    List<Campaign> myCampaigns = new ArrayList<Campaign>();
    myCampaigns.add(c0);
    myCampaigns.add(c1);
    myCampaigns.add(c2);
    myCampaigns.add(c3);
    myCampaigns.add(c4);
    myCampaigns.add(c5);
    
    EndOfDayMessage eodMessage = new EndOfDayMessage(8, "", null, null, myCampaigns, 1.0, 3000.57);
    gameAgent.handleEndOfDayMessage(eodMessage);
    //Logging.log(gameAgent);
    //Logging.log(gameAgent.getAdBid());
    assertEquals(gameAgent.getAdBid().getBidEntries().size(), 2);
  }

}
