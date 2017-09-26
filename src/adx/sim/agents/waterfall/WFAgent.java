package adx.sim.agents.waterfall;

import java.util.HashSet;
import java.util.Set;

import structures.Bidder;
import structures.Market;
import structures.exceptions.BidderCreationException;
import structures.exceptions.GoodsException;
import structures.exceptions.MarketCreationException;
import waterfall.Waterfall;
import waterfall.WaterfallSolution;
import adx.exceptions.AdXException;
import adx.sim.agents.GameGoods;
import adx.sim.agents.SimAgent;
import adx.sim.agents.SimAgentModel;
import adx.sim.agents.SimAgentModel.MarketModel;
import adx.structures.BidBundle;
import adx.structures.SimpleBidEntry;
import adx.variants.onedaygame.OneDayBidBundle;

/**
 * Implements a Waterfall agent.
 * 
 * @author Enrique Areyan Viqueira
 *
 */
public class WFAgent extends SimAgent {

  /**
   * Constructor.
   * 
   * @param simAgentName
   */
  public WFAgent(String simAgentName) {
    super(simAgentName);
  }

  @Override
  public BidBundle getBidBundle() {
    // Get the model.
    try {
      MarketModel marketModel = SimAgentModel.constructModel(this.myCampaign, this.othersCampaigns);
      Market<GameGoods, Bidder<GameGoods>> market = marketModel.market;
      // Print some useful info
      // this.printInfo("WF AGENT", market);
      Bidder<GameGoods> myCampaignBidder = marketModel.mybidder;
      Set<GameGoods> myDemandSet = marketModel.mybidder.getDemandSet();

      Waterfall<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> waterfall = new Waterfall<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>>(market);
      WaterfallSolution<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> waterfallSolution = waterfall.run();
      // waterfallSolution.printAllocationTable();
      // waterfallSolution.printPricesTable();
      // Back-up bid from waterfall allocation and prices
      Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
      for (GameGoods demandedGood : myDemandSet) {
        // Logging.log("Price for: " + demandedGood + " for bidder: " + myCampaignBidder + " is " + waterfallSolution.getPrice(demandedGood, myCampaignBidder));
        if (waterfallSolution.getAllocation(demandedGood, myCampaignBidder) > 0) {
          // bidEntries.add(new SimpleBidEntry(demandedGood.getMarketSegment(), waterfallSolution.getPrice(demandedGood, myCampaignBidder),
          // waterfallSolution.getAllocation(demandedGood, myCampaignBidder) * waterfallSolution.getPrice(demandedGood, myCampaignBidder)));
          bidEntries.add(new SimpleBidEntry(demandedGood.getMarketSegment(), waterfallSolution.getPrice(demandedGood, myCampaignBidder), this.myCampaign.getBudget()));
        }
      }
      // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
      OneDayBidBundle WEBidBundle = new OneDayBidBundle(this.myCampaign.getId(), this.myCampaign.getBudget(), bidEntries);
      // Logging.log("\n:::::::WEBidBundle = " + WEBidBundle);
      return WEBidBundle;

    } catch (BidderCreationException | AdXException | MarketCreationException | GoodsException e) {
      e.printStackTrace();
      return null;
    }
  }
}
