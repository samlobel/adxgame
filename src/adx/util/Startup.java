package adx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;

import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.messages.EndOfDayMessage;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.structures.Query;
import adx.structures.SimpleBidEntry;
import adx.variants.ndaysgame.NDaysBidBundle;
import adx.variants.onedaygame.OneDayBidBundle;
import adx.variants.twodaysgame.TwoDaysBidBundle;

/**
 * Handles common startup operations. In particular, register all classes to be sent as part of messages.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Startup {
  public static void start(Kryo kryo) {
    kryo.register(ConnectServerMessage.class);
    kryo.register(Campaign.class);
    kryo.register(MarketSegment.class);
    kryo.register(EndOfDayMessage.class);
    kryo.register(BidBundle.class);
    kryo.register(BidEntry.class);
    kryo.register(Query.class);
    kryo.register(HashSet.class);
    kryo.register(HashMap.class);
    kryo.register(ACKMessage.class);
    kryo.register(Pair.class);
    kryo.register(ArrayList.class);
    kryo.register(OneDayBidBundle.class);
    kryo.register(TwoDaysBidBundle.class);
    kryo.register(SimpleBidEntry.class);
    kryo.register(NDaysBidBundle.class);
  }
}
