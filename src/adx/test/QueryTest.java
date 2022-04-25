package adx.test;

import static org.junit.Assert.*;

import org.junit.Test;

import adx.exceptions.AdXException;
import adx.structures.MarketSegment;
import adx.structures.Query;

public class QueryTest {

  @Test
  public void testIsMatchingQuery() throws AdXException {
    Query q1 = new Query(MarketSegment.FEMALE);
    Query q2 = new Query(MarketSegment.MALE);
    Query q3 = new Query(MarketSegment.YOUNG);
    Query q4 = new Query(MarketSegment.OLD);
    Query q5 = new Query(MarketSegment.LOW_INCOME);
    Query q6 = new Query(MarketSegment.HIGH_INCOME);
    Query q7 = new Query(MarketSegment.FEMALE_YOUNG);
    Query q8 = new Query(MarketSegment.FEMALE_OLD);
    Query q9 = new Query(MarketSegment.FEMALE_LOW_INCOME);
    Query q10 = new Query(MarketSegment.FEMALE_HIGH_INCOME);
    Query q11 = new Query(MarketSegment.MALE_YOUNG);
    Query q12 = new Query(MarketSegment.MALE_OLD);
    Query q13 = new Query(MarketSegment.MALE_LOW_INCOME);
    Query q14 = new Query(MarketSegment.MALE_HIGH_INCOME);
    Query q15 = new Query(MarketSegment.YOUNG_LOW_INCOME);
    Query q16 = new Query(MarketSegment.YOUNG_HIGH_INCOME);
    Query q17 = new Query(MarketSegment.OLD_LOW_INCOME);
    Query q18 = new Query(MarketSegment.OLD_HIGH_INCOME);
    Query q19 = new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME);
    Query q20 = new Query(MarketSegment.FEMALE_OLD_LOW_INCOME);
    Query q21 = new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME);
    Query q22 = new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME);
    Query q23 = new Query(MarketSegment.MALE_YOUNG_LOW_INCOME);
    Query q24 = new Query(MarketSegment.MALE_OLD_LOW_INCOME);
    Query q25 = new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME);
    Query q26 = new Query(MarketSegment.MALE_OLD_HIGH_INCOME);
    /* True tests */
    assertTrue(q1.matchesQuery(q1));
    assertTrue(q1.matchesQuery(q7));
    assertTrue(q1.matchesQuery(q8));
    assertTrue(q1.matchesQuery(q9));
    assertTrue(q1.matchesQuery(q10));
    assertTrue(q1.matchesQuery(q19));
    assertTrue(q1.matchesQuery(q20));
    assertTrue(q1.matchesQuery(q21));
    assertTrue(q1.matchesQuery(q22));
    
    assertTrue(q2.matchesQuery(q12));
    assertTrue(q2.matchesQuery(q11));
    assertTrue(q2.matchesQuery(q12));
    assertTrue(q2.matchesQuery(q13));
    assertTrue(q2.matchesQuery(q14));
    assertTrue(q2.matchesQuery(q23));
    assertTrue(q2.matchesQuery(q24));
    assertTrue(q2.matchesQuery(q25));
    assertTrue(q2.matchesQuery(q26));
    
    assertTrue(q3.matchesQuery(q3));
    assertTrue(q3.matchesQuery(q7));
    assertTrue(q3.matchesQuery(q11));
    assertTrue(q3.matchesQuery(q15));
    assertTrue(q3.matchesQuery(q16));
    assertTrue(q3.matchesQuery(q19));
    assertTrue(q3.matchesQuery(q21));
    assertTrue(q3.matchesQuery(q23));
    assertTrue(q3.matchesQuery(q25));
    
    assertTrue(q4.matchesQuery(q4));
    assertTrue(q4.matchesQuery(q8));
    assertTrue(q4.matchesQuery(q12));
    assertTrue(q4.matchesQuery(q17));
    assertTrue(q4.matchesQuery(q18));
    assertTrue(q4.matchesQuery(q20));
    assertTrue(q4.matchesQuery(q22));
    assertTrue(q4.matchesQuery(q24));
    assertTrue(q4.matchesQuery(q26));
    
    assertTrue(q5.matchesQuery(q5));
    assertTrue(q5.matchesQuery(q9));
    assertTrue(q5.matchesQuery(q13));
    assertTrue(q5.matchesQuery(q15));
    assertTrue(q5.matchesQuery(q17));
    assertTrue(q5.matchesQuery(q19));
    assertTrue(q5.matchesQuery(q20));
    assertTrue(q5.matchesQuery(q23));
    assertTrue(q5.matchesQuery(q24));

    assertTrue(q6.matchesQuery(q6));
    assertTrue(q6.matchesQuery(q10));
    assertTrue(q6.matchesQuery(q14));
    assertTrue(q6.matchesQuery(q16));
    assertTrue(q6.matchesQuery(q18));
    assertTrue(q6.matchesQuery(q21));
    assertTrue(q6.matchesQuery(q22));
    assertTrue(q6.matchesQuery(q25));
    assertTrue(q6.matchesQuery(q26));
    
    /* False tests */
    assertFalse(q1.matchesQuery(q2));
    assertFalse(q17.matchesQuery(q12));
    assertFalse(q3.matchesQuery(q4));
    assertFalse(q11.matchesQuery(q7));
    assertFalse(q2.matchesQuery(q17));
    assertFalse(q18.matchesQuery(q3));
    assertFalse(q7.matchesQuery(q6));
    assertFalse(q6.matchesQuery(q24));
    
    /* Equality tests */
    assertTrue(new Query(MarketSegment.FEMALE).equals(new Query(MarketSegment.FEMALE)));    
    assertTrue(new Query(MarketSegment.MALE).equals(new Query(MarketSegment.MALE)));    
    assertTrue(new Query(MarketSegment.YOUNG).equals(new Query(MarketSegment.YOUNG)));    
    assertTrue(new Query(MarketSegment.OLD).equals(new Query(MarketSegment.OLD)));    
    assertTrue(new Query(MarketSegment.LOW_INCOME).equals(new Query(MarketSegment.LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.HIGH_INCOME).equals(new Query(MarketSegment.HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_YOUNG).equals(new Query(MarketSegment.FEMALE_YOUNG)));    
    assertTrue(new Query(MarketSegment.FEMALE_OLD).equals(new Query(MarketSegment.FEMALE_OLD)));    
    assertTrue(new Query(MarketSegment.FEMALE_LOW_INCOME).equals(new Query(MarketSegment.FEMALE_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_HIGH_INCOME).equals(new Query(MarketSegment.FEMALE_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME).equals(new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_OLD_LOW_INCOME).equals(new Query(MarketSegment.FEMALE_OLD_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME).equals(new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME).equals(new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_YOUNG).equals(new Query(MarketSegment.MALE_YOUNG)));    
    assertTrue(new Query(MarketSegment.MALE_OLD).equals(new Query(MarketSegment.MALE_OLD)));    
    assertTrue(new Query(MarketSegment.MALE_LOW_INCOME).equals(new Query(MarketSegment.MALE_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_HIGH_INCOME).equals(new Query(MarketSegment.MALE_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_YOUNG_LOW_INCOME).equals(new Query(MarketSegment.MALE_YOUNG_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_OLD_LOW_INCOME).equals(new Query(MarketSegment.MALE_OLD_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME).equals(new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.MALE_OLD_HIGH_INCOME).equals(new Query(MarketSegment.MALE_OLD_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.YOUNG_LOW_INCOME).equals(new Query(MarketSegment.YOUNG_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.YOUNG_HIGH_INCOME).equals(new Query(MarketSegment.YOUNG_HIGH_INCOME)));    
    assertTrue(new Query(MarketSegment.OLD_LOW_INCOME).equals(new Query(MarketSegment.OLD_LOW_INCOME)));    
    assertTrue(new Query(MarketSegment.OLD_HIGH_INCOME).equals(new Query(MarketSegment.OLD_HIGH_INCOME)));    

    assertFalse(new Query(MarketSegment.MALE).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.FEMALE).equals(new Query(MarketSegment.OLD_HIGH_INCOME)));    
    assertFalse(new Query(MarketSegment.MALE_YOUNG).equals(new Query(MarketSegment.FEMALE_OLD)));    
    assertFalse(new Query(MarketSegment.MALE_OLD_HIGH_INCOME).equals(new Query(MarketSegment.FEMALE_YOUNG_LOW_INCOME)));    
    assertFalse(new Query(MarketSegment.HIGH_INCOME).equals(new Query(MarketSegment.LOW_INCOME)));    
    assertFalse(new Query(MarketSegment.YOUNG).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.MALE).equals(new Query(MarketSegment.MALE_YOUNG_LOW_INCOME)));    
    assertFalse(new Query(MarketSegment.LOW_INCOME).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.MALE).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.FEMALE_YOUNG_HIGH_INCOME).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.FEMALE_OLD).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.HIGH_INCOME).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.LOW_INCOME).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.FEMALE_OLD).equals(new Query(MarketSegment.FEMALE)));    
    assertFalse(new Query(MarketSegment.FEMALE_OLD_HIGH_INCOME).equals(new Query(MarketSegment.YOUNG_HIGH_INCOME)));    
    assertFalse(new Query(MarketSegment.MALE_YOUNG_HIGH_INCOME).equals(new Query(MarketSegment.OLD_HIGH_INCOME)));    
    assertFalse(new Query(MarketSegment.MALE_OLD_LOW_INCOME).equals(new Query(MarketSegment.FEMALE)));    
  }

}
