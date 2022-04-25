package adx.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.MarketSegment;
import adx.structures.Query;

public class BidBundleTest {

  @SuppressWarnings("serial")
  public static BidBundle getBidBundle0() throws AdXException {
    return new BidBundle(1, new HashSet<BidEntry>() {
      {
        add(new BidEntry(1, new Query(MarketSegment.FEMALE), 24.0, 12000.0));
        add(new BidEntry(1, new Query(MarketSegment.MALE), 21.0, 21000.0));
      }
    }, new HashMap<Integer, Double>() {
      {
        put(1, 200.0);
      }
    }, new HashMap<Integer, Double>() {
      {
        put(1, 100.0);
        put(2, 38.7);
      }
    });

  }

  @SuppressWarnings("serial")
  public static BidBundle getBidBundle1() throws AdXException {
    return new BidBundle(1, new HashSet<BidEntry>() {
      {
        add(new BidEntry(2, new Query(MarketSegment.HIGH_INCOME), 600, 38000.0));
      }
    }, new HashMap<Integer, Double>() {
      {
        put(2, 3000.0);
      }
    }, new HashMap<Integer, Double>() {
      {
        put(1, 7.67);
        put(2, 23.5);
      }
    });
  }

  @SuppressWarnings("serial")
  public static BidBundle getBidBundle2() throws AdXException {
    return new BidBundle(1, new HashSet<BidEntry>() {
      {
        add(new BidEntry(3, new Query(MarketSegment.YOUNG), 600, 31200.0));
      }
    }, new HashMap<Integer, Double>() {
      {
        put(3, 400.0);
      }
    }, null);
  }

  @SuppressWarnings("serial")
  public static BidBundle getBidBundle3() throws AdXException {
    return new BidBundle(1, new HashSet<BidEntry>() {
      {
        add(new BidEntry(4, new Query(MarketSegment.YOUNG), 600, 31220.0));
        add(new BidEntry(4, new Query(MarketSegment.MALE_YOUNG), 312.2, 31220.0));
        add(new BidEntry(4, new Query(MarketSegment.FEMALE_LOW_INCOME), 312.2, 31220.0));
      }
    }, null, null);
  }
  
  @SuppressWarnings("serial")
  public static BidBundle getBidBundle4() throws AdXException {
    return new BidBundle(1, new HashSet<BidEntry>() {
      {
        add(new BidEntry(99, new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME), 600, 31220.0));
        add(new BidEntry(99, new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME), 600, 31220.0));
      }
    }, null, null);
  }

  public static Table<Integer, String, BidBundle> getTableBidBundles() throws AdXException {
    Table<Integer, String, BidBundle> bidBundles = HashBasedTable.create();
    bidBundles.put(0, "agent0", BidBundleTest.getBidBundle0());
    bidBundles.put(0, "agent1", BidBundleTest.getBidBundle1());
    bidBundles.put(0, "agent2", BidBundleTest.getBidBundle2());
    bidBundles.put(0, "agent3", BidBundleTest.getBidBundle3());
    return bidBundles;
  }

  @Test
  public void testBidBundles() throws AdXException {
    BidBundle bidBundle0 = BidBundleTest.getBidBundle0();
    assertEquals(bidBundle0.getBidEntries().size(), 2);
    assertEquals(bidBundle0.getCampaignLimit(1), new Double(200.0));

    BidBundle bidBundle1 = BidBundleTest.getBidBundle1();
    assertEquals(bidBundle1.getBidEntries().size(), 1);
    assertEquals(bidBundle1.getCampaignLimit(2), new Double(3000.0));

    BidBundle bidBundle2 = BidBundleTest.getBidBundle2();
    assertEquals(bidBundle2.getBidEntries().size(), 1);
    assertEquals(bidBundle2.getCampaignLimit(3), new Double(400.0));

    BidBundle bidBundle3 = BidBundleTest.getBidBundle3();
    assertEquals(bidBundle3.getBidEntries().size(), 3);
    assertEquals(bidBundle3.getCampaignLimit(4), null);

    assertEquals(BidBundleTest.getTableBidBundles().size(), 4);
  }

}
