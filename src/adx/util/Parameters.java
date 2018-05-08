package adx.util;

import com.google.common.collect.ImmutableList;

/**
 * Class with all the parameters of the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Parameters {
  // Number of real-time seconds for a simulated day.
  public static final int SECONDS_DURATION_DAY = 2;
  // In case campaigns are auctioned, how many campaigns to auction.
  public static final int NUMBER_AUCTION_CAMPAINGS = 1;
  // The rate at which quality learning will happen. Only relevant for games longer than 1 day.
  public static final double QUALITY_SCORE_LEARNING_RATE = 1.0;
  // How many simulated days in a row.
  public static final int TOTAL_SIMULATED_GAMES = 10;
  
  /**
   * OneDayGame parameters follow. Default Configuration.
   
  // How many simulated days.
  public static final int TOTAL_SIMULATED_DAYS = 1;
  // Total population size.
  public static final int POPULATION_SIZE = 10000;
  // Campaigns can last several days. This list specifies the allowable number of days.
  public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(1);

   */
  /**
   * TwoDaysOneCampaign parameters follow.
   
  // How many simulated days.
  public static final int TOTAL_SIMULATED_DAYS = 2;
  // Total population size.
  public static final int POPULATION_SIZE = 10000;
  // Campaigns can last several days. This list specifies the allowable number of days.
  public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(2);
  */
  
  /**
   * TwoDaysTwoCampaigns parameters follow.
   */
  // How many simulated days.
  public static final int TOTAL_SIMULATED_DAYS = 2;
  // Total population size.
  public static final int POPULATION_SIZE = 10000;
  // Campaigns can last several days. This list specifies the allowable number of days.
  public static final ImmutableList<Integer> CAMPAIGN_DURATIONS = ImmutableList.of(1);
  
}
