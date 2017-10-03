package adx.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;

/**
 * This class represents the different market segments in the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public enum MarketSegment {
  // These are all the possible market segments.
  MALE, FEMALE, YOUNG, OLD, LOW_INCOME, HIGH_INCOME,
  MALE_YOUNG, MALE_OLD, MALE_LOW_INCOME, MALE_HIGH_INCOME, 
  FEMALE_YOUNG, FEMALE_OLD, FEMALE_LOW_INCOME, FEMALE_HIGH_INCOME, 
  YOUNG_LOW_INCOME, YOUNG_HIGH_INCOME, 
  OLD_LOW_INCOME, OLD_HIGH_INCOME, 
  MALE_YOUNG_LOW_INCOME, MALE_YOUNG_HIGH_INCOME, MALE_OLD_LOW_INCOME, MALE_OLD_HIGH_INCOME, 
  FEMALE_YOUNG_LOW_INCOME, FEMALE_YOUNG_HIGH_INCOME, FEMALE_OLD_LOW_INCOME, FEMALE_OLD_HIGH_INCOME;

  // Contains a list that maps from market segments to their proportion.
  public static final Map<MarketSegment, Integer> proportionsMap;

  // Same as before but as a list.
  public static final List<Entry<MarketSegment, Integer>> proportionsList;

  static {
    HashMap<MarketSegment, Integer> initMap = new HashMap<MarketSegment, Integer>();
    /*initMap.put(MALE, 4956);
    initMap.put(FEMALE, 5044);
    initMap.put(YOUNG, 4589);
    initMap.put(OLD, 5411);
    initMap.put(LOW_INCOME, 8012);
    initMap.put(HIGH_INCOME, 1988);*/
    initMap.put(MALE_YOUNG, 2353);
    initMap.put(MALE_OLD, 2603);
    initMap.put(MALE_LOW_INCOME, 3631);
    initMap.put(MALE_HIGH_INCOME, 1325);
    initMap.put(FEMALE_YOUNG, 2236);
    initMap.put(FEMALE_OLD, 2808);
    initMap.put(FEMALE_LOW_INCOME, 4381);
    initMap.put(FEMALE_HIGH_INCOME, 663);
    initMap.put(YOUNG_LOW_INCOME, 3816);
    initMap.put(YOUNG_HIGH_INCOME, 773);
    initMap.put(OLD_LOW_INCOME, 4196);
    initMap.put(OLD_HIGH_INCOME, 1215);
    initMap.put(MALE_YOUNG_LOW_INCOME, 1836);
    initMap.put(MALE_YOUNG_HIGH_INCOME, 517);
    initMap.put(MALE_OLD_LOW_INCOME, 1795);
    initMap.put(MALE_OLD_HIGH_INCOME, 808);
    initMap.put(FEMALE_YOUNG_LOW_INCOME, 1980);
    initMap.put(FEMALE_YOUNG_HIGH_INCOME, 256);
    initMap.put(FEMALE_OLD_LOW_INCOME, 2401);
    initMap.put(FEMALE_OLD_HIGH_INCOME, 407);
    proportionsMap = Collections.unmodifiableMap(initMap);
    proportionsList = new ArrayList<Entry<MarketSegment, Integer>>(proportionsMap.entrySet());
  }

  /**
   * Takes in two market segments, m1 and m2, and returns true in case the first
   * market segment is a subset of the second.
   * 
   * @param m1 first market segment
   * @param m2 second market segment
   * @return true if m1 is a subset of m2, false otherwise.
   * @throws AdXException 
   */
  public static boolean marketSegmentSubset(MarketSegment m1, MarketSegment m2) throws AdXException {
    switch (m1) {
    case FEMALE:
      switch (m2) {
      case FEMALE:
      case FEMALE_HIGH_INCOME:
      case FEMALE_LOW_INCOME:
      case FEMALE_OLD:
      case FEMALE_OLD_HIGH_INCOME:
      case FEMALE_OLD_LOW_INCOME:
      case FEMALE_YOUNG:
      case FEMALE_YOUNG_HIGH_INCOME:
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case FEMALE_HIGH_INCOME:
      switch (m2) {
      case FEMALE_HIGH_INCOME:
      case FEMALE_OLD_HIGH_INCOME:
      case FEMALE_YOUNG_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case FEMALE_LOW_INCOME:
      switch (m2) {
      case FEMALE_LOW_INCOME:
      case FEMALE_OLD_LOW_INCOME:
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case FEMALE_OLD:
      switch (m2) {
      case FEMALE_OLD:
      case FEMALE_OLD_LOW_INCOME:
      case FEMALE_OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case FEMALE_OLD_HIGH_INCOME:      
      switch (m2) {
      case FEMALE_OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      }    
    case FEMALE_OLD_LOW_INCOME:
      switch (m2) {
      case FEMALE_OLD_LOW_INCOME:
        return true;
      default:
        return false;
      }  
    case FEMALE_YOUNG:
      switch (m2) {
      case FEMALE_YOUNG:
      case FEMALE_YOUNG_LOW_INCOME: 
      case FEMALE_YOUNG_HIGH_INCOME: 
        return true;
      default:
        return false;
      }        
    case FEMALE_YOUNG_HIGH_INCOME:
      switch (m2) {
      case FEMALE_YOUNG_HIGH_INCOME:
        return true;
      default:
        return false;
      }      
    case FEMALE_YOUNG_LOW_INCOME:
      switch (m2) {
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case HIGH_INCOME:
      switch (m2) {
      case HIGH_INCOME:
      case FEMALE_HIGH_INCOME:
      case MALE_HIGH_INCOME:
      case FEMALE_YOUNG_HIGH_INCOME:
      case FEMALE_OLD_HIGH_INCOME:
      case MALE_YOUNG_HIGH_INCOME:
      case MALE_OLD_HIGH_INCOME:
      case YOUNG_HIGH_INCOME:
      case OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      }      
    case LOW_INCOME:
      switch (m2) {
      case LOW_INCOME:
      case MALE_LOW_INCOME:
      case FEMALE_LOW_INCOME:
      case YOUNG_LOW_INCOME:
      case OLD_LOW_INCOME:
      case MALE_OLD_LOW_INCOME:
      case MALE_YOUNG_LOW_INCOME:
      case FEMALE_OLD_LOW_INCOME:
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case MALE:
      switch (m2) {
      case MALE:
      case MALE_HIGH_INCOME:
      case MALE_LOW_INCOME:
      case MALE_OLD:
      case MALE_OLD_HIGH_INCOME:
      case MALE_OLD_LOW_INCOME:
      case MALE_YOUNG:
      case MALE_YOUNG_HIGH_INCOME:
      case MALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case MALE_HIGH_INCOME:
      switch (m2) {
      case MALE_HIGH_INCOME:
      case MALE_OLD_HIGH_INCOME:
      case MALE_YOUNG_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case MALE_LOW_INCOME:
      switch (m2) {
      case MALE_LOW_INCOME:
      case MALE_OLD_LOW_INCOME:
      case MALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case MALE_OLD:
      switch (m2) {
      case MALE_OLD:
      case MALE_OLD_LOW_INCOME:
      case MALE_OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case MALE_OLD_HIGH_INCOME:
      switch (m2) {
      case MALE_OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      } 
    case MALE_OLD_LOW_INCOME:
      switch (m2) {
      case MALE_OLD_LOW_INCOME:
        return true;
      default:
        return false;
      } 
    case MALE_YOUNG:
      switch (m2) {
      case MALE_YOUNG:
      case MALE_YOUNG_LOW_INCOME: 
      case MALE_YOUNG_HIGH_INCOME: 
        return true;
      default:
        return false;
      }
    case MALE_YOUNG_HIGH_INCOME:
      switch (m2) {
      case MALE_YOUNG_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case MALE_YOUNG_LOW_INCOME:
      switch (m2) {
      case MALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case OLD:
      switch (m2) {
      case OLD:
      case OLD_HIGH_INCOME:
      case OLD_LOW_INCOME:
      case MALE_OLD:
      case MALE_OLD_HIGH_INCOME:
      case MALE_OLD_LOW_INCOME:
      case FEMALE_OLD:
      case FEMALE_OLD_HIGH_INCOME:
      case FEMALE_OLD_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case OLD_HIGH_INCOME:
      switch (m2) {
      case OLD_HIGH_INCOME:
      case MALE_OLD_HIGH_INCOME:
      case FEMALE_OLD_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case OLD_LOW_INCOME:
      switch (m2) {
      case OLD_LOW_INCOME:
      case MALE_OLD_LOW_INCOME:
      case FEMALE_OLD_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case YOUNG:
      switch (m2) {
      case YOUNG:
      case YOUNG_HIGH_INCOME:
      case YOUNG_LOW_INCOME:
      case MALE_YOUNG:
      case MALE_YOUNG_HIGH_INCOME:
      case MALE_YOUNG_LOW_INCOME:
      case FEMALE_YOUNG:
      case FEMALE_YOUNG_HIGH_INCOME:
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    case YOUNG_HIGH_INCOME:
      switch (m2) {
      case YOUNG_HIGH_INCOME:
      case MALE_YOUNG_HIGH_INCOME:
      case FEMALE_YOUNG_HIGH_INCOME:
        return true;
      default:
        return false;
      }
    case YOUNG_LOW_INCOME:
      switch (m2) {
      case YOUNG_LOW_INCOME:
      case MALE_YOUNG_LOW_INCOME:
      case FEMALE_YOUNG_LOW_INCOME:
        return true;
      default:
        return false;
      }
    }
    throw new AdXException("Unknown market segment membership " + m1 + ", " + m2);
  }
}
