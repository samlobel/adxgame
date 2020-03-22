package adx.variants.onedaygame;

import adx.agent.AgentLogic;
import adx.agent.OnlineAgent;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Printer;

/**
 * An abstract class to be implemented by an agent playing the OneDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class OneDayAgent extends AgentLogic {

  /**
   * In this game agents have only one campaign
   */
  protected Campaign myCampaign;

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public OneDayAgent() {
    super();
  }

  @Override
  protected BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
    int currentDay = endOfDayMessage.getDay();
    if (currentDay == 1) {
      this.myCampaign = endOfDayMessage.getCampaignsWon().get(0);
      Logging.log("\n[-] Playing a new game!");
      Logging.log("[-] My campaign: " + this.myCampaign);
      return this.getBidBundle();
    } else {
      Logging.log("[-] Statistics: " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
      Logging.log("[-] Final Profit: " + endOfDayMessage.getCumulativeProfit());
      return null;
    }
  }

  /**
   * This is the only function that needs to be implemented by an agent playing the OneDay Game.
   * 
   * @return the agent's bid bundle.
   */
  abstract protected OneDayBidBundle getBidBundle();

}
