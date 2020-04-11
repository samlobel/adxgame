package adx.variants.ndaysgame;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.structures.SimpleBidEntry;
import adx.util.BidBundleHelper;

/**
 * A specialized object for the NDaysNCampaign game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class NDaysBidBundle extends BidBundle {

	/**
	 * A bid bundle is composed of bid entries.
	 */
	protected Set<BidEntry> bidEntries;

	/**
	 * Constructor.
	 */
	public NDaysBidBundle() {
		super();
	}

	/**
	 * Constructor for a bidbundle to play the NDaysNCampaign Game.
	 * 
	 * @param bidEntries
	 * @param campaignsLimits
	 * @throws AdXException
	 */
	public NDaysBidBundle(int day, int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries,
			Map<Campaign, Double> campaignBids) throws AdXException {
		super(day, BidBundleHelper.createBidEntries(campaignId, simpleBidEntries),
				BidBundleHelper.createLimits(campaignId, limit), BidBundleHelper.createCampaignBids(campaignBids));
		this.bidEntries = new HashSet<>(simpleBidEntries);
	}

	public NDaysBidBundle(int day, Set<NDaysAdBidBundle> adBids, Map<Campaign, Double> campaignBids)
			throws AdXException {
		super(day, accumulateBidEntries(adBids), accumulateLimits(adBids),
				BidBundleHelper.createCampaignBids(campaignBids));
		this.bidEntries = accumulateBidEntries(adBids);
	}

	private static Set<BidEntry> accumulateBidEntries(Set<NDaysAdBidBundle> adBids) throws AdXException {
		Set<BidEntry> result = new HashSet<>();
		for (NDaysAdBidBundle bundle : adBids) {
			result.addAll(BidBundleHelper.createBidEntries(bundle.getCampaignID(), bundle.getBidEntries()));
		}
		return result;
	}

	private static Map<Integer, Double> accumulateLimits(Set<NDaysAdBidBundle> adBids) throws AdXException {
		Map<Integer, Double> result = new HashMap<>();
		for (NDaysAdBidBundle bundle : adBids) {
			result.putAll(BidBundleHelper.createLimits(bundle.getCampaignID(), bundle.getLimit()));
		}
		return result;
	}

	@Override
	public String toString() {
		String ret = "\n\t NDaysBidBundle:";
		if (this.bidEntries != null && this.bidEntries.size() > 0) {
			for (BidEntry simpleBidEntry : this.bidEntries) {
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
