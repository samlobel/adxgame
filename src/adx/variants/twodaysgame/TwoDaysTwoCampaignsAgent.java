package adx.variants.twodaysgame;

import adx.agent.AgentLogic;
import adx.agent.OnlineAgent;
import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Printer;

/**
 * An abstract class to be implemented by an agent playing the
 * TwoCampaings-TwoDays game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class TwoDaysTwoCampaignsAgent extends AgentLogic {

	/**
	 * Campaign of day 1
	 */
	private Campaign firstCampaign;

	/**
	 * Campaign of day 2
	 */
	private Campaign secondCampaign;

	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param port
	 */
	public TwoDaysTwoCampaignsAgent() {
		super();
	}

	@Override
	protected BidBundle handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
		int currentDay = endOfDayMessage.getDay();
		if (currentDay == 1) {
			Logging.log("\n[-] Playing a new game!");
			// Start of the game, day 1
			this.firstCampaign = endOfDayMessage.getCampaignsWon().get(0);
			Logging.log("[-] My first campaign: " + this.firstCampaign);
			Logging.log("[-] End of Day 1, bid.");
			TwoDaysBidBundle bidBundleDay1 = null;
			try {
				bidBundleDay1 = this.getBidBundle(1);
			} catch (AdXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bidBundleDay1 != null) {
				return bidBundleDay1;
			} else {
				Logging.log("[-] WARNING! the bid bundle for day 1 was null!, nothing was sent to the server");
			}
		} else if (currentDay == 2) {
			// Day 2.
			this.secondCampaign = endOfDayMessage.getCampaignsWon().get(0);
			Logging.log("[-] My second campaign: " + this.secondCampaign);
			Logging.log("[-] Statistics from day 1: " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
			Logging.log("[-] Quality from day 1: " + endOfDayMessage.getQualityScore());
			Logging.log("[-] Profit from day 1: " + endOfDayMessage.getCumulativeProfit());
			Logging.log("[-] End of Day 2, bid.");
			TwoDaysBidBundle bidBundleDay2 = null;
			try {
				bidBundleDay2 = this.getBidBundle(2);
			} catch (AdXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bidBundleDay2 != null) {
				return bidBundleDay2;
			} else {
				Logging.log("[-] WARNING! the bid bundle for day 2 was null!, nothing was sent to the server");
			}
		} else {
			// End of Game.
			Logging.log("[-] Statistics from day 2: " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
			Logging.log("[-] Final Profit: " + endOfDayMessage.getCumulativeProfit());
			Logging.log("[-] Final Quality Score: " + endOfDayMessage.getQualityScore());
		}
		return null;
	}

	protected Campaign getFirstCampaign() {
		return this.firstCampaign;
	}

	protected Campaign getSecondCampaign() {
		return this.secondCampaign;
	}

	/**
	 * This is the only function that needs to be implemented by an agent playing
	 * the TwoDays Game.
	 * 
	 * @return the agent's bid bundle.
	 */
	abstract protected TwoDaysBidBundle getBidBundle(int day) throws AdXException;

}
