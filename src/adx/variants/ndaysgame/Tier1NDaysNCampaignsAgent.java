package adx.variants.ndaysgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import adx.agent.AgentLogic;
import adx.agent.OnlineAgent;
import adx.exceptions.AdXException;
import adx.server.OfflineGameServer;
import adx.structures.Campaign;
import adx.structures.SimpleBidEntry;
import adx.util.AgentStartupUtil;
import adx.util.Logging;

public class Tier1NDaysNCampaignsAgent extends NDaysNCampaignsAgent {

	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param port
	 */
	public Tier1NDaysNCampaignsAgent() {
		super();
	}

	@Override
	protected Set<NDaysAdBidBundle> getAdBids() throws AdXException {
		// Log for which day we want to compute the bid bundle.
		Set<NDaysAdBidBundle> set = new HashSet<>();

		for (Campaign c : this.getActiveCampaigns()) {
			SimpleBidEntry entry = new SimpleBidEntry(c.getMarketSegment(),
					Math.max(0.1,  (c.getBudget() - this.getCumulativeCost(c)) / (c.getReach() - this.getCumulativeReach(c) + 0.0001)),
					Math.max(1.0, c.getBudget() - this.getCumulativeCost(c)));
			NDaysAdBidBundle bundle = new NDaysAdBidBundle(c.getId(), Math.max(1.0, c.getBudget() - this.getCumulativeCost(c)),
					Sets.newHashSet(entry));
			set.add(bundle);
		}
		return set;
	}

	@Override
	protected Map<Campaign, Double> getCampaignBids(Set<Campaign> campaignsForAuction) {
		Map<Campaign, Double> campaigns = new HashMap<>();
		for (Campaign c : campaignsForAuction) {
			campaigns.put(c, c.getReach() * (Math.random() * 0.9 + 0.1));
		}
		return campaigns;
	}

	@Override
	protected void onNewGame() {
		// TODO Auto-generated method stub
	}

}
