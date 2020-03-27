package adx.variants.ndaysgame;

import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
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
  protected Set<SimpleBidEntry> bidEntries;

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
  public NDaysBidBundle(int day, int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries) throws AdXException {
    super(day, BidBundleHelper.createBidEntries(campaignId, simpleBidEntries), BidBundleHelper.createLimits(campaignId, limit), null);
    this.bidEntries = simpleBidEntries;
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
