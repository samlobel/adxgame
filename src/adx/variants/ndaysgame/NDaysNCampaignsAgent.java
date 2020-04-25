package adx.variants.ndaysgame;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	protected int getCurrentDay() {
		return this.currentDay;
	}

	protected int currentGame() {
		return this.currentGame;
	}

	protected Set<Campaign> getActiveCampaigns() {
		return Collections.unmodifiableSet(this.myCampaigns);
	}

	protected int getCumulativeReach(Campaign c) {
		Pair<Integer, Double> stats = this.statistics.getOrDefault(c.getId(), null);
		if (stats == null) {
			return 0;
		}
		return stats.getElement1().intValue();
	}
	
	protected double getCumulativeCost(Campaign c) {
		Pair<Integer, Double> stats = this.statistics.getOrDefault(c.getId(), null);
		if (stats == null) {
			return 0.0;
		}
		return stats.getElement2().doubleValue();
	}

	protected double getCumulativeProfit() {
		return this.profit;
	}
	
	protected static double effectiveReach(int x, int R) {
		// Here's the quality score function if you want to use it
		return (2.0 / 4.08577) * (Math.atan(4.08577 * ((x + 0.0) / R) - 3.08577) - Math.atan(-3.08577));
	}
	
	protected boolean isValidCampaignBid(Campaign c, double bid) {
		return bid >= 0.1 * c.getReach() && bid <= c.getReach();
	}
	
	protected double clipCampaignBid(Campaign c, double bid) {
		return Math.max(0.1 * c.getReach(), Math.min(c.getReach(), bid));
	}
	
	protected double effectiveCampaignBid(double bid) {
		return bid / this.getQualityScore();
	}

	/**
	 * Parse the end of day message.
	 */
	@Override
	public BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
		// Check if this is the start of a new game and initialize the agent.
		if (endOfDayMessage.getDay() == 1) {
			this.init();
			this.onNewGame();
			this.currentGame++;
			Logging.log("\n\n[-] Starting a new Game, game #" + this.currentGame);
		}
		Logging.log("[-] handleEndOfDayMessage " + this.nDaysEndOfMessageString(endOfDayMessage));
		// Read the EOD message and update the state of the agent.
		this.currentDay = endOfDayMessage.getDay();
		this.updateStatistics(endOfDayMessage.getStatistics());
		this.profit = endOfDayMessage.getCumulativeProfit();

		// Remove inactive campaigns. Insert won campaigns.
		this.myCampaigns.removeIf(campaign -> campaign.getEndDay() < this.currentDay);
		if (endOfDayMessage.getCampaignsWon() != null) {
			this.myCampaigns.addAll(endOfDayMessage.getCampaignsWon());
		}

		// Store the current quality score.
		this.currentQualityScore = endOfDayMessage.getQualityScore();
		
		// Get the bid bundle and send it to the server.
		Map<Campaign, Double> campaignBids;
		try {
			campaignBids = this.getCampaignBids(Collections.unmodifiableSet(new HashSet<>(endOfDayMessage.getCampaignsForAuction())));
		} catch (AdXException e1) {
			// TODO Auto-generated catch block
			campaignBids = null;
			e1.printStackTrace();
		}
		Set<NDaysAdBidBundle> adBids;
		try {
			adBids = this.getAdBids();
		} catch (AdXException e1) {
			adBids = null;
			e1.printStackTrace();
		}
		if (adBids == null) {
			adBids = new HashSet<>();
		}
		try {
			return new NDaysBidBundle(this.currentDay, adBids, campaignBids);
		} catch (AdXException e) {
			return null;
		}
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
	abstract protected Set<NDaysAdBidBundle> getAdBids() throws AdXException;
	
	abstract protected Map<Campaign, Double> getCampaignBids(Set<Campaign> campaignsForAuction) throws AdXException;
	
	abstract protected void onNewGame();

}
