package sim.agents;

import structures.Goods;
import structures.exceptions.GoodsCreationException;
import adx.structures.MarketSegment;

public class GameGoods extends Goods {

  private final MarketSegment marketSegment;

  public GameGoods(MarketSegment marketSegment, int supply) throws GoodsCreationException {
    super(supply);
    this.marketSegment = marketSegment;
  }
  
  public MarketSegment getMarketSegment() {
    return this.marketSegment;
  }

  @Override
  public String toString() {
    return "(" + this.marketSegment + "," + this.supply + "," + this.remainingSupply + ")";
  }

}