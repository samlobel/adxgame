package adx.sim.agents.WE;

import java.util.ArrayList;

import structures.Bidder;
import structures.Market;
import structures.exceptions.BidderCreationException;
import structures.exceptions.MarketCreationException;
import structures.factory.reserve.MarketWithReservePrice;
import adx.sim.agents.GameGoods;

/**
 * Class to handle the model of the market with a reserve price.
 * 
 * @author Enrique Areyan Viqueira
 */
public class GameMarketWithReserve extends MarketWithReservePrice<Market<GameGoods, Bidder<GameGoods>>, GameGoods, Bidder<GameGoods>> {

  /**
   * Constructor.
   * 
   * @param market
   * @param reserve
   * @throws BidderCreationException
   * @throws MarketCreationException
   */
  public GameMarketWithReserve(Market<GameGoods, Bidder<GameGoods>> market, double reserve) throws BidderCreationException, MarketCreationException {
    super(market, reserve);
  }

  @Override
  protected Market<GameGoods, Bidder<GameGoods>> createMarketWithReserve() throws MarketCreationException {
    ArrayList<Bidder<GameGoods>> newBidders = new ArrayList<Bidder<GameGoods>>();
    for (Bidder<GameGoods> B : this.bidderToBidderMap.keySet()) {
      newBidders.add(B);
    }
    ArrayList<GameGoods> newGameGoods = new ArrayList<GameGoods>(this.market.getGoods());
    return new Market<GameGoods, Bidder<GameGoods>>(newGameGoods, newBidders);
  }

}
