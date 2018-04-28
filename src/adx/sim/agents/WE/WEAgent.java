package adx.sim.agents.WE;

import ilog.concert.IloException;

import java.util.HashSet;
import java.util.Set;

import structures.Bidder;
import structures.Market;
import structures.MarketAllocation;
import structures.exceptions.AllocationException;
import structures.exceptions.BidderCreationException;
import structures.exceptions.GoodsException;
import structures.exceptions.MarketAllocationException;
import structures.exceptions.MarketCreationException;
import structures.exceptions.MarketOutcomeException;
import adx.exceptions.AdXException;
import adx.sim.agents.GameGoods;
import adx.sim.agents.SimAgent;
import adx.sim.agents.SimAgentModel;
import adx.sim.agents.SimAgentModel.MarketModel;
import adx.structures.BidBundle;
import adx.structures.SimpleBidEntry;
import adx.util.Logging;
import adx.variants.onedaygame.OneDayBidBundle;
import algorithms.pricing.RestrictedEnvyFreePricesLPSolution;
import algorithms.pricing.RestrictedEnvyFreePricesLPWithReserve;
import algorithms.pricing.error.PrincingAlgoException;
import allocations.greedy.GreedyAllocation;
import allocations.greedy.GreedyAllocationFactory;

/**
 * Implements the Walrasian Equilibrium (WE) agent.
 * 
 * @author Enrique Areyan Viqueira
 */
public class WEAgent extends SimAgent {

  /**
   * Constructor.
   * 
   * @param simAgentName
   */
  public WEAgent(String simAgentName, double reserve, int numberOfImpressions) {
    super(simAgentName, reserve, numberOfImpressions);
  }

  @Override
  public BidBundle getBidBundle() {
    try {
      // Get the model.
      MarketModel marketModel = SimAgentModel.constructModel(this.myCampaign, this.othersCampaigns, this.numberOfImpressions);
      Market<GameGoods, Bidder<GameGoods>> market = marketModel.market;
      // Logging.log(market);
      // Print some useful info
      // this.printInfo(market);
      Bidder<GameGoods> myCampaignBidder = marketModel.mybidder;
      Set<GameGoods> myDemandSet = marketModel.mybidder.getDemandSet();
      // Get the market with the reserve price.
      GameMarketWithReserve mwrp = new GameMarketWithReserve(market, this.reserve);
      // Test if there are bidders in the market with reserve.
      if (mwrp.areThereBiddersInTheMarketWithReserve()) {
        // Run allocation algorithm in the market that respect reserve.
        // First, get the greedy allocation algorithm
    	GreedyAllocation<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> greedyAlloc = GreedyAllocationFactory.GreedyAllocation();
    	// Next, run the greedy allocation algorithm
    	MarketAllocation<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> allocForMarketWithReserve = greedyAlloc.Solve(mwrp.getMarketWithReservePrice());
        // Deduce a MarketAllocation for the original market.
        MarketAllocation<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> allocForOriginalMarket = mwrp.deduceAllocation(allocForMarketWithReserve);
        // Run pricing algorithm for the original market and its deduced allocation.
        RestrictedEnvyFreePricesLPWithReserve<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> restrictedEnvyFreePricesLP = new RestrictedEnvyFreePricesLPWithReserve<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>>(allocForOriginalMarket, this.reserve);
        restrictedEnvyFreePricesLP.createLP();
        RestrictedEnvyFreePricesLPSolution<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> prices = restrictedEnvyFreePricesLP.Solve();
        // Some prints.
        // greedyAllocation.printAllocation();
        // prices.printPrices();
        // Back-up bid from WE allocation and prices
        Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
        for (GameGoods demandedGood : myDemandSet) {
          // Logging.log("Price for: " + demandedGood + " is " + prices.getPrice(demandedGood));
          if (allocForOriginalMarket.getAllocation(demandedGood, myCampaignBidder) > 0) {
            // bidEntries.add(new SimpleBidEntry(demandedGood.getMarketSegment(), prices.getPrice(demandedGood), greedyAllocation.getAllocation(demandedGood,
            // myCampaignBidder) * prices.getPrice(demandedGood)));
            bidEntries.add(new SimpleBidEntry(demandedGood.getMarketSegment(), prices.getPrice(demandedGood), this.myCampaign.getBudget()));
          }
        }
        // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
        OneDayBidBundle WEBidBundle = new OneDayBidBundle(this.myCampaign.getId(), this.myCampaign.getBudget(), bidEntries);
        // Logging.log("\n:::::::WEBidBundle for campaign: " + this.myCampaign + " = " + WEBidBundle);
        return WEBidBundle;
      }
    } catch (AdXException | MarketCreationException | BidderCreationException | MarketAllocationException | AllocationException | GoodsException | PrincingAlgoException | IloException | MarketOutcomeException e) {
      Logging.log("Failed to create market model --> ");
      e.printStackTrace();
      return null;
    }
    return null;
  }
}
