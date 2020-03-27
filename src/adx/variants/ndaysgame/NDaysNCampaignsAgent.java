package adx.variants.ndaysgame;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import adx.agent.Agent;
import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Printer;

abstract public class NDaysNCampaignsAgent extends Agent {

  /**
   * Current Game
   */
  protected int currentGame = 0;

  /**
   * Current simulated day.
   */
  protected int currentDay;

  /**
   * Keeps the current quality score received by the server.
   */
  protected Double currentQualityScore;

  /**
   * A map that keeps track of the campaigns owned by the agent each day.
   */
  protected Map<Integer, Campaign> myCampaigns;

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public NDaysNCampaignsAgent(String host, int port) {
    super(host, port);
  }

  /**
   * Connects the agent and registers its name. For simplicity, the password is fixed here.
   * 
   * @param name
   * @param password
   */
  protected void connect(String login) {
    super.connect(login, "123456");
  }

  /**
   * Initialize the state of the agent
   */
  private void init() {
    this.currentDay = -1;
    this.currentQualityScore = -1.0;
    this.myCampaigns = new HashMap<Integer, Campaign>();
    this.statistics = new HashMap<Integer, Pair<Integer, Double>>();
  }

  /**
   * Parse the end of day message.
   */
  @Override
  public void handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
    // Check if this is the start of a new game and initialize the agent.
    if (endOfDayMessage.getDay() == 1) {
      this.init();
      this.currentGame = this.currentGame + 1;
      Logging.log("\n\n[-] Starting a new Game, game #" + this.currentGame);
    }
    Logging.log("[-] handleEndOfDayMessage " + this.nDaysEndOfMessageString(endOfDayMessage));
    Logging.log("[-] Current time = " + Instant.now());
    try {
      // Read the EOD message and update the state of the agent.
      this.currentDay = endOfDayMessage.getDay();
      this.updateStatistics(endOfDayMessage.getStatistics());
      if (endOfDayMessage.getCampaignsWon().size() != 1) {
        throw new AdXException("Fatal Error: the agent should have received exactly one new campaign");
      }
      // There should always be exactly a single campaign given by the server which corresponds to the campaign assigned in the current day.
      Campaign assignedCampaign = endOfDayMessage.getCampaignsWon().get(0);
      this.myCampaigns.put(this.currentDay, assignedCampaign);

      // Store the current quality score.
      this.currentQualityScore = endOfDayMessage.getQualityScore();

      // Get the bid bundle and send it to the server.
      NDaysBidBundle bidBundle = this.getBidBundle(this.currentDay);
      if (bidBundle != null && this.getClient() != null && this.getClient().isConnected()) {
        this.getClient().sendTCP(bidBundle);
      }
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle for day " + endOfDayMessage.getDay() + " -> " + e.getMessage());
    }
    Logging.log("[-] Statistics: " + this.statistics);
  }

  /**
   * Get a string representation of the relevant parts of the EndOfDay Message for the NDaysNCampaigns game.
   * 
   * @param endOfDayMessage
   * @return
   */
  protected String nDaysEndOfMessageString(EndOfDayMessage endOfDayMessage) {
    return "\n EndOfDayMessage: \n\t Day: " + endOfDayMessage.getDay() + ",\n\t Statistics: " + endOfDayMessage.getStatistics() + ",\n\t New campaigns: " + Printer.printNiceListMyCampaigns(endOfDayMessage.getCampaignsWon())
        + "\n\t Quality Score = " + endOfDayMessage.getQualityScore() + "\n\t Cumulative Profit: " + endOfDayMessage.getCumulativeProfit();
  }

  /**
   * This is the only function that needs to be implemented by an agent playing the NDays Game.
   * 
   * @return the agent's bid bundle.
   */
  abstract protected NDaysBidBundle getBidBundle(int day);

}
