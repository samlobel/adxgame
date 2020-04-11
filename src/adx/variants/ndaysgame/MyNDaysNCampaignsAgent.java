package adx.variants.ndaysgame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adx.structures.Campaign;
import adx.structures.SimpleBidEntry;

// Despite the name, you can assume the game lasts 10 rounds.
public class MyNDaysNCampaignsAgent extends NDaysNCampaignsAgent {
	public MyNDaysNCampaignsAgent() {
		// TODO: fill this in if necessary
	}

	@Override
	protected Set<NDaysAdBidBundle> getAdBids() {
		// TODO: fill this in
		
		Set<NDaysAdBidBundle> bundles = new HashSet<>();
		
		for (Campaign c : this.getActiveCampaigns()) {
			Set<SimpleBidEntry> bidEntries = new HashSet<>();
			// bidEntries.add(new SimpleBidEntry(marketSegment, bid, limit);
			// double campaignWideLimit = ...
			// bundles.add(new NDaysAdBidBundle(c.getId(), campaignWideLimit, bidEntries));
		}
		
		return bundles;
	}

	@Override
	protected Map<Campaign, Double> getCampaignBids(Set<Campaign> campaignsForAuction) {
		// TODO: fill this in
		
		Map<Campaign, Double> campaignBids = new HashMap<>();
		
		for (Campaign c : campaignsForAuction) {
			// double bid = ...
			// campaignBids.put(c, bid);
		}
		
		return campaignBids;
	}

	@Override
	protected void onNewGame() {
		// TODO: fill this in if necessary
		// this is called at the beginning of each game.
		// you can use it to initialize any data you're keeping on a game.
	}
}
