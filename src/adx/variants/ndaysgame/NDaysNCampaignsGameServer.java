package adx.variants.ndaysgame;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;

import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.server.OnlineGameServer;
import adx.statistics.EffectiveReach;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Parameters;
import adx.util.Printer;
import adx.util.Sampling;

/**
 * Implementation of the NDaysNCampaigns game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class NDaysNCampaignsGameServer extends OnlineGameServer {

  /**
   * Constructor.
   * 
   * @param port
   * @throws IOException
   * @throws AdXException
   */
  public NDaysNCampaignsGameServer(int port) throws IOException, AdXException {
    super(port);
  }

  @Override
  public void runAdXGame() throws AdXException {
    // First order of business is to accept connections for a fixed amount of time
    Instant deadlineForNewPlayers = Instant.now().plusSeconds(Parameters.get_SECONDS_WAIT_PLAYERS());
    Logging.log("[-] Accepting connections until " + deadlineForNewPlayers);
    while (Instant.now().isBefore(deadlineForNewPlayers));
    // Do not accept any new agents beyond deadline. Play with present agents.
    this.acceptingNewPlayers = false;
    this.serverState.initStatistics();
    // Check if there is at least one agent to play the game.
    if (this.namesToConnections.size() > 0) {
      while (this.gameNumber < Parameters.get_TOTAL_SIMULATED_GAMES() + 1) {
        Instant endTime = Instant.now().plusSeconds(Parameters.get_SECONDS_DURATION_DAY());
        this.setUpGame();
        this.sendEndOfDayMessage();
        int day = 0;
        // Play game for the specified number of days.
        while (day < Parameters.TOTAL_SIMULATED_DAYS) {
          if (Instant.now().isAfter(endTime)) {
            // Time is up for the present day, stop accepting bids for this day and run corresponding auctions.
            // this.serverState.printServerState();
            this.serverState.advanceDay();
            // Run auction for the bids received the day before.
            synchronized (this.serverState) {
              synchronized (this) {
                try {
                  this.serverState.runAdAuctions();
                  this.serverState.runCampaignAuctions();
                  this.serverState.updateDailyStatistics(EffectiveReach.SIGMOIDAL);
                } catch (AdXException e) {
                  Logging.log("[x] Error running some auction -> " + e.getMessage());
                }
              }
            }
            endTime = Instant.now().plusSeconds(Parameters.get_SECONDS_DURATION_DAY());
            day++;
            this.sendEndOfDayMessage();
          }
        }
        // Print results of the game.
        Logging.log("Results for game " + this.gameNumber);
        Logging.log(this.serverState.getStatistics().getNiceProfitScoresTable(day));

        // Prepare to start a new game
        this.gameNumber++;
        this.serverState.saveProfit();
        this.serverState.initServerState(this.gameNumber);
        this.serverState.initStatistics();
        Sampling.resetUniqueCampaignId();
      }
      Logging.log("\nGame ended, played " + (this.gameNumber - 1) + " games, final results are: ");
      Logging.log(Printer.getNiceProfitTable(this.serverState.getStatistics().orderProfits(this.serverState.getAverageAcumProfitOverAllGames(this.gameNumber - 1).entrySet()), -1));
      this.gameServer.stop();
    } else {
      Logging.log("[x] There are no players, stopping the server at " + Instant.now());
      this.gameServer.stop();
    }
  }

  /**
   * Main server method.
   * 
   * @param args
   * @throws AdXException
   */
  public static void main(String[] args) {
    try {
      System.out.println("NDaysNCampaigns Game");
      OnlineGameServer.initParams(args);
      new NDaysNCampaignsGameServer(Integer.parseInt(args[2])).runAdXGame();
    } catch (IOException | AdXException e) {
      Logging.log("Error initializing the server --> ");
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
