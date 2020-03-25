package sim.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import structures.Bidder;
import structures.Market;
import structures.exceptions.BidderCreationException;
import structures.exceptions.GoodsCreationException;
import structures.exceptions.MarketCreationException;
import adx.exceptions.AdXException;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.util.Logging;

import com.google.common.collect.ImmutableList;

public class SimAgentModel {

  /**
   * Maps int -> list of game goods. The idea is to compute the game goods for a given supply number only once!
   */
  public static final Map<Integer, ImmutableList<GameGoods>> mapOfGameGoods = new HashMap<Integer, ImmutableList<GameGoods>>();

  public static ImmutableList<GameGoods> getListOfGameGoods(int numberOfImpressions) {
    if (!SimAgentModel.mapOfGameGoods.containsKey(numberOfImpressions)) {
      // Logging.log("Creating list of goods for " + numberOfImpressions + " many impressions");
      // Create the market segments just once.
      List<MarketSegment> marketSegments = new ArrayList<MarketSegment>();
      marketSegments.add(MarketSegment.MALE_YOUNG_LOW_INCOME);
      marketSegments.add(MarketSegment.MALE_YOUNG_HIGH_INCOME);
      marketSegments.add(MarketSegment.MALE_OLD_LOW_INCOME);
      marketSegments.add(MarketSegment.MALE_OLD_HIGH_INCOME);
      marketSegments.add(MarketSegment.FEMALE_YOUNG_LOW_INCOME);
      marketSegments.add(MarketSegment.FEMALE_YOUNG_HIGH_INCOME);
      marketSegments.add(MarketSegment.FEMALE_OLD_LOW_INCOME);
      marketSegments.add(MarketSegment.FEMALE_OLD_HIGH_INCOME);
      List<GameGoods> gameGoods = new ArrayList<GameGoods>();
      for (MarketSegment marketSegment : marketSegments) {
        try {
          gameGoods.add(new GameGoods(marketSegment, (int) Math.ceil(MarketSegment.proportionsMap.get(marketSegment) * (numberOfImpressions / 10000.0))));
        } catch (GoodsCreationException e) {
          Logging.log("Error creating goods in the market model");
          e.printStackTrace();
        }
      }
      ImmutableList.Builder<GameGoods> gameMarketSegmentBuilder = ImmutableList.builder();
      gameMarketSegmentBuilder.addAll(gameGoods);
      SimAgentModel.mapOfGameGoods.put(numberOfImpressions, gameMarketSegmentBuilder.build());
    }
    return SimAgentModel.mapOfGameGoods.get(numberOfImpressions);
  }

  /**
   * Given an agent's campaign and a list of other agent's campaigns, build the market model.
   * 
   * @param myCampaign
   * @param listOfOthersCampaigns
   * @return
   * @throws BidderCreationException
   * @throws AdXException
   * @throws MarketCreationException
   */
  public static MarketModel constructModel(Campaign myCampaign, List<Campaign> listOfOthersCampaigns, int numberOfImpressions) throws BidderCreationException, AdXException, MarketCreationException {
    // Construct the model
    ImmutableList<GameGoods> listOfGameGoods = SimAgentModel.getListOfGameGoods(numberOfImpressions);
    ArrayList<Bidder<GameGoods>> listOfBidders = new ArrayList<Bidder<GameGoods>>();
    Set<GameGoods> myDemandSet = SimAgentModel.generateDemandSet(listOfGameGoods, myCampaign);
    Bidder<GameGoods> myCampaignBidder = new Bidder<GameGoods>(myCampaign.getReach(), myCampaign.getBudget(), myDemandSet);
    listOfBidders.add(myCampaignBidder);
    for (Campaign othersCampaigns : listOfOthersCampaigns) {
      listOfBidders.add(new Bidder<GameGoods>(othersCampaigns.getReach(), othersCampaigns.getBudget(), SimAgentModel.generateDemandSet(listOfGameGoods, othersCampaigns)));
    }
    Market<GameGoods, Bidder<GameGoods>> market = new Market<GameGoods, Bidder<GameGoods>>(listOfGameGoods, listOfBidders);
    return new SimAgentModel.MarketModel(market, myCampaignBidder);
  }

  /**
   * Given a campaign, generates its demand set.
   * 
   * @param campaign
   * @return
   * @throws AdXException
   */
  public static Set<GameGoods> generateDemandSet(ImmutableList<GameGoods> listOfGameGoods, Campaign campaign) throws AdXException {
    Set<GameGoods> demandSet = new HashSet<GameGoods>();
    for (GameGoods gameGood : listOfGameGoods) {
      if (MarketSegment.marketSegmentSubset(campaign.getMarketSegment(), gameGood.getMarketSegment())) {
        demandSet.add(gameGood);
      }
    }
    return demandSet;
  }

  /**
   * Auxiliary class to contain the market model and the bidder who owns the model.
   *
   */
  public static class MarketModel {
    public Market<GameGoods, Bidder<GameGoods>> market;
    public Bidder<GameGoods> mybidder;

    public MarketModel(Market<GameGoods, Bidder<GameGoods>> market, Bidder<GameGoods> mybidder) {
      this.market = market;
      this.mybidder = mybidder;
    }
  }

}
