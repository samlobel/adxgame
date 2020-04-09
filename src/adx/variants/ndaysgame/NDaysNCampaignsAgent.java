package adx.variants.ndaysgame;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adx.agent.Agent;
import adx.agent.AgentLogic;
import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Printer;

abstract public class NDaysNCampaignsAgent extends AgentLogic {

	/**
	 * Current Game
	 */
	private int currentGame;

	/**
	 * Current simulated day.
	 */
	private int currentDay;

	/**
	 * Keeps the current quality score received by the server.
	 */
	private Double currentQualityScore;

	/**
	 * A map that keeps track of the campaigns owned by the agent each day.
	 */
	private Set<Campaign> myCampaigns;

	private double profit;

	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param port
	 */
	public NDaysNCampaignsAgent() {
		super();
		this.currentGame = 0;
		this.init();
	}

	/**
	 * Initialize the state of the agent
	 */
	private void init() {
		this.currentDay = 0;
		this.currentQualityScore = 1.0;
		this.myCampaigns = new HashSet<Campaign>();
		this.statistics = new HashMap<Integer, Pair<Integer, Double>>();
		this.profit = 0.0;
	}

	protected double getQualityScore() {
		return this.currentQualityScore.doubleValue();
	}

	protected int getDay() {
		return this.currentDay;
	}

	protected int currentGame() {
		return this.currentGame;
	}

	protected Set<Campaign> getActiveCampaigns() {
		return new HashSet<>(this.myCampaigns);
	}

	protected int reachAchieved(Campaign c) {
		Pair<Integer, Double> stats = this.statistics.getOrDefault(c.getId(), null);
		if (stats == null) {
			return -1;
		}
		return stats.getElement1().intValue();
	}

	protected double getCumulativeProfit() {
		return this.profit;
	}

	/**
	 * Parse the end of day message.
	 */
	@Override
	public BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
		// Check if this is the start of a new game and initialize the agent.
		if (endOfDayMessage.getDay() == 1) {
			this.init();
			this.currentGame++;
			Logging.log("\n\n[-] Starting a new Game, game #" + this.currentGame);
		}
		Logging.log("[-] handleEndOfDayMessage " + this.nDaysEndOfMessageString(endOfDayMessage));
		Logging.log("[-] Current time = " + Instant.now());
		// Read the EOD message and update the state of the agent.
		this.currentDay = endOfDayMessage.getDay();
		this.updateStatistics(endOfDayMessage.getStatistics());
		this.profit = endOfDayMessage.getCumulativeProfit();

		// Remove inactive campaigns. Insert won campaigns.
		this.myCampaigns.removeIf(campaign -> campaign.getEndDay() < this.currentDay);
		this.myCampaigns.addAll(endOfDayMessage.getCampaignsWon());

		// Store the current quality score.
		this.currentQualityScore = endOfDayMessage.getQualityScore();

		// Get the bid bundle and send it to the server.
		NDaysBidBundle bidBundle = this.getBidBundle(this.currentDay);
		if (bidBundle != null) {
			Logging.log("[-] Statistics: " + this.statistics);
			return bidBundle;
		}
		return null;
	}

	/**
	 * Get a string representation of the relevant parts of the EndOfDay Message for
	 * the NDaysNCampaigns game.
	 * 
	 * @param endOfDayMessage
	 * @return
	 */
	private String nDaysEndOfMessageString(EndOfDayMessage endOfDayMessage) {
		return "\n EndOfDayMessage: \n\t Day: " + endOfDayMessage.getDay() + ",\n\t Statistics: "
				+ endOfDayMessage.getStatistics() + ",\n\t New campaigns: "
				+ Printer.printNiceListMyCampaigns(endOfDayMessage.getCampaignsWon()) + "\n\t Quality Score = "
				+ endOfDayMessage.getQualityScore() + "\n\t Cumulative Profit: "
				+ endOfDayMessage.getCumulativeProfit();
	}

	/**
	 * This is the only function that needs to be implemented by an agent playing
	 * the NDays Game.
	 * 
	 * @return the agent's bid bundle.
	 */
	abstract protected NDaysBidBundle getBidBundle(int day);

}
