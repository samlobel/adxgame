package adx.test;

import static org.junit.Assert.*;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.structures.MarketSegment;

public class MarketSegmentTest {

  @Test
  public void testMarketSegmentSubset() throws AdXException {
    /* Exhaustive true tests. */
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.FEMALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.MALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.MALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.FEMALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.MALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.FEMALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.MALE_OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD, MarketSegment.FEMALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.MALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.FEMALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.MALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.FEMALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.MALE_OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.HIGH_INCOME, MarketSegment.FEMALE_OLD_HIGH_INCOME));
    
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_YOUNG, MarketSegment.FEMALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_YOUNG, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_YOUNG, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD, MarketSegment.FEMALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD, MarketSegment.FEMALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_LOW_INCOME, MarketSegment.FEMALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_LOW_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_HIGH_INCOME, MarketSegment.FEMALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_HIGH_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_HIGH_INCOME, MarketSegment.FEMALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_YOUNG_LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_YOUNG_HIGH_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD_LOW_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD_HIGH_INCOME, MarketSegment.FEMALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG, MarketSegment.MALE_YOUNG));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG, MarketSegment.MALE_YOUNG_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_OLD, MarketSegment.MALE_OLD));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_OLD, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_OLD, MarketSegment.MALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_LOW_INCOME, MarketSegment.MALE_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_LOW_INCOME, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_LOW_INCOME, MarketSegment.MALE_OLD_LOW_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_HIGH_INCOME, MarketSegment.MALE_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_HIGH_INCOME, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_HIGH_INCOME, MarketSegment.MALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG_LOW_INCOME, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG_HIGH_INCOME, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_OLD_LOW_INCOME, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.MALE_OLD_HIGH_INCOME, MarketSegment.MALE_OLD_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_LOW_INCOME, MarketSegment.YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_LOW_INCOME, MarketSegment.MALE_YOUNG_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_HIGH_INCOME, MarketSegment.YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_HIGH_INCOME, MarketSegment.MALE_YOUNG_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_HIGH_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_LOW_INCOME, MarketSegment.OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_LOW_INCOME, MarketSegment.MALE_OLD_LOW_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_LOW_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));

    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_HIGH_INCOME, MarketSegment.OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_HIGH_INCOME, MarketSegment.MALE_OLD_HIGH_INCOME));
    assertTrue(MarketSegment.marketSegmentSubset(MarketSegment.OLD_HIGH_INCOME, MarketSegment.FEMALE_OLD_HIGH_INCOME));
    
    /* Some false tests. */
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE, MarketSegment.MALE_OLD));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.FEMALE_OLD));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.OLD_HIGH_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.MALE_YOUNG_HIGH_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.FEMALE_OLD_LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_HIGH_INCOME, MarketSegment.YOUNG_LOW_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG_LOW_INCOME, MarketSegment.FEMALE_OLD_LOW_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.OLD_LOW_INCOME, MarketSegment.FEMALE_YOUNG_LOW_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.YOUNG, MarketSegment.MALE_OLD_HIGH_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.LOW_INCOME, MarketSegment.FEMALE_YOUNG_HIGH_INCOME));
    assertFalse(MarketSegment.marketSegmentSubset(MarketSegment.MALE, MarketSegment.FEMALE_OLD_HIGH_INCOME));
  }

}
