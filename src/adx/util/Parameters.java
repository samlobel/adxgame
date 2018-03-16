package adx.util;

import com.google.common.collect.ImmutableList;

/**
 * Class with all the parameters of the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Parameters {

  
  // For TwoDaysOneCampaign,  POPULATION_SIZE = 5000,  and CAMPAIGN_DURATIONS = ImmutableList.of(2)
  // For TwoDaysTwoCampaigns, POPULATION_SIZE = 10000, and CAMPAIGN_DURATIONS = ImmutableList.of(1)
  
  public static final int SECONDS_DURATION_DAY = 2;

  public static final int POPULATION_SIZE = 10000;

  public static final int NUMBER_AUCTION_CAMPAINGS = 1;

  //public static final double QUALITY_SCORE_LEARNING_RATE = 0.6;
  public static final double QUALITY_SCORE_LEARNING_RATE = 1.0;

  // public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(1, 3, 5, 8);
  //public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(1);
  public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(1);

  public static final int TOTAL_SIMULATED_DAYS = 1;

  public static final int TOTAL_SIMULATED_GAMES = 15;

}
