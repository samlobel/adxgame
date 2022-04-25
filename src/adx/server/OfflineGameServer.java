package adx.server;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;

import adx.agent.OfflineAgent;
import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.statistics.EffectiveReach;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Parameters;
import adx.util.Printer;
import adx.util.Sampling;

/**
 * A concrete implementation of a game server.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OfflineGameServer extends OfflineGameServerAbstract {

	/**
	 * Constructor.
	 * 
	 * @param port
	 *          - on which the server will run.
	 * @throws IOException
	 *           in case the server could not be started.
	 * @throws AdXException
	 */
	public OfflineGameServer() throws IOException, AdXException {
		super();
	}

	/**
	 * Runs the game.
	 * 
	 * @throws AdXException
	 */
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
				this.setUpGame();
				this.sendEndOfDayMessage();
				int day = 0;
				// Play game for the specified number of days.
				while (day < Parameters.TOTAL_SIMULATED_DAYS) {
					// Time is up for the present day, stop accepting bids for this day and run corresponding auctions.
					// this.serverState.printServerState();
					this.serverState.advanceDay();
					// Run auction for the bids received the day before.
					synchronized (this.serverState) {
						synchronized (this) {
							try {
								this.serverState.runAdAuctions();
								this.serverState.runCampaignAuctions();
								this.serverState.updateDailyStatistics(Parameters.EFFECTIVE_REACH_TYPE);
							} catch (AdXException e) {
								Logging.log("[x] Error running some auction -> " + e.getMessage());
							}
						}
					}
					day++;
					this.sendEndOfDayMessage();
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
		} else {
			Logging.log("[x] There are no players, stopping the server at " + Instant.now());
		}
	}

	/**
	 * Sample initial campaigns.
	 */
	protected synchronized void setUpGame() {
		Logging.log("[-] Set Up game, sample initial campaigns:");
		for (String agent : this.connectionsToNames.values()) {
			try {
				Campaign c = Sampling.sampleInitialCampaign();
				this.serverState.registerCampaign(c, agent);
			} catch (AdXException e) {
				Logging.log("[x] Error trying to sample an initial campaign.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send the end of day message to all agents.
	 * 
	 * @throws AdXException
	 */
	protected synchronized void sendEndOfDayMessage() throws AdXException {
		Logging.log("[-] Sending end of day message. ");
		Instant timeEndOfDay = Instant.now().plusSeconds(Parameters.get_SECONDS_DURATION_DAY());
		this.serverState.currentDayEnd = timeEndOfDay;
		List<Campaign> listOfCampaigns = null;
		try {
			listOfCampaigns = this.serverState.generateCampaignsOpportunities();
		} catch (AdXException e) {
			Logging.log("[x] Error sampling list of campaigns for auction --> " + e.getMessage());
		}
		for (Entry<String, OfflineAgent> agent : this.namesToConnections.entrySet()) {
			String agentName = agent.getKey();
			try {
				agent.getValue().receiveMessage(new EndOfDayMessage(this.serverState.getCurrentDay() + 1, timeEndOfDay.toString(), this.serverState.getDailySummaryStatistic(agentName), listOfCampaigns, this.serverState.getWonCampaigns(agentName),
						this.serverState.getQualitScore(agentName), this.serverState.getProfit(agentName)));
			} catch (AdXException e) {
				Logging.log("[x] Error sending the end of day message -> " + e);
			}
		}
	}

	public static void initParams(String[] args) throws AdXException, IOException {
		if (args.length != 2) {
			throw new AdXException("To run an AdX game you need to pass two parameters. (1) the locaiton of the .ini file, and (2) the type of game from options" + Parameters.allowableGames);
		}
		// Initialize the parameters of the game.
		// args[0] should be the location of the .ini file.
		// args[1] should be the type of game: ONE-DAY-ONE-CAMPAIGN, TWO-DAYS-ONE-CAMPAIGN, or TWO-DAYS-TWO-CAMPAIGNS.
		System.out.println("Running from .ini file in " + args[0]);
		System.out.println("Type of game " + args[1]);
		Parameters.populateParameters(args[0], args[1]);
	}

	/**
	 * Main server method.
	 * 
	 * @param args
	 * @throws AdXException
	 */
	public static void main(String[] args) {
		try {
			System.out.println("GameServer");
			OfflineGameServer.initParams(args);
			new OfflineGameServer().runAdXGame();
		} catch (IOException | AdXException e) {
			Logging.log("Error initializing the server --> ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
