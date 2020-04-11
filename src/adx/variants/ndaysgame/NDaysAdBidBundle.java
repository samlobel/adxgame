package adx.variants.ndaysgame;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.Campaign;
import adx.structures.SimpleBidEntry;
import adx.util.BidBundleHelper;

/**
 * A specialized object for the NDaysNCampaign game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class NDaysAdBidBundle {

	/**
	 * A bid bundle is composed of bid entries.
	 */
	private Set<SimpleBidEntry> bidEntries;
	private int campaignID;
	private double limit;

	/**
	 * Constructor.
	 */
	public NDaysAdBidBundle() {
		this.bidEntries = new HashSet<>();
		this.campaignID = -1;
		this.limit = -1.0;
	}

	/**
	 * Constructor for a bidbundle to play the NDaysNCampaign Game.
	 * 
	 * @param bidEntries
	 * @param campaignsLimits
	 * @throws AdXException
	 */
	public NDaysAdBidBundle(int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries)
			throws AdXException {
		this.campaignID = campaignId;
		this.limit = limit;
		this.bidEntries = simpleBidEntries;
	}

	public double getLimit() {
		return limit;
	}

	public Set<SimpleBidEntry> getBidEntries() {
		return bidEntries;
	}

	public int getCampaignID() {
		return campaignID;
	}

	@Override
	public String toString() {
		String ret = "\n\t NDaysBidBundle:";
		if (this.bidEntries != null && this.bidEntries.size() > 0) {
			for (SimpleBidEntry simpleBidEntry : this.bidEntries) {
				if (simpleBidEntry != null) {
					ret += "\n\t\t" + simpleBidEntry;
				}
			}
		} else {
			ret += " [EMPTY] ";
		}
		return ret;
	}
}
