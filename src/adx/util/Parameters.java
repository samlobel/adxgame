package adx.util;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import adx.exceptions.AdXException;

/**
 * Class with all the parameters of the game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class Parameters {

  // Number of seconds to wait for new players once the server starts.
  private static int SECONDS_WAIT_PLAYERS;

  // A flag to indicate whether the parameters have been read from the .ini file.
  private static boolean initialized = false;

  // Allowable games.
  public static final ImmutableSet<String> allowableGames = ImmutableSet.of("ONE-DAY-ONE-CAMPAIGN", "TWO-DAYS-ONE-CAMPAIGN", "TWO-DAYS-TWO-CAMPAIGNS");

  // Number of real-time seconds for a simulated day.
  private static int SECONDS_DURATION_DAY;

  // In case campaigns are auctioned, how many campaigns to auction.
  private static int NUMBER_AUCTION_CAMPAINGS;

  // The rate at which quality learning will happen. Only relevant for games longer than 1 day.
  private static double QUALITY_SCORE_LEARNING_RATE;

  // How many simulated days in a row.
  private static int TOTAL_SIMULATED_GAMES;

  // Total population size.
  public static int POPULATION_SIZE;

  // How many simulated days.
  public static int TOTAL_SIMULATED_DAYS;

  // Campaigns can last several days. This list specifies the allowable number of days.
  public static ImmutableList<Integer> CAMPAIGN_DURATIONS;

  /**
   * Checks if the parameters have been initialized. Throws exception in the negative case.
   * 
   * @throws AdXException
   */
  public static void safeguard() throws AdXException {
    if (!Parameters.initialized) {
      throw new AdXException("Parameters not initialized");
    }
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_SECONDS_WAIT_PLAYERS() throws AdXException {
    Parameters.safeguard();
    return Parameters.SECONDS_WAIT_PLAYERS;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_SECONDS_DURATION_DAY() throws AdXException {
    Parameters.safeguard();
    return Parameters.SECONDS_DURATION_DAY;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_NUMBER_AUCTION_CAMPAINGS() throws AdXException {
    Parameters.safeguard();
    return Parameters.NUMBER_AUCTION_CAMPAINGS;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static double get_QUALITY_SCORE_LEARNING_RATE() throws AdXException {
    Parameters.safeguard();
    return Parameters.QUALITY_SCORE_LEARNING_RATE;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_TOTAL_SIMULATED_GAMES() throws AdXException {
    Parameters.safeguard();
    return Parameters.TOTAL_SIMULATED_GAMES;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_POPULATION_SIZE() throws AdXException {
    Parameters.safeguard();
    return Parameters.POPULATION_SIZE;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static int get_TOTAL_SIMULATED_DAYS() throws AdXException {
    Parameters.safeguard();
    return Parameters.TOTAL_SIMULATED_DAYS;
  }

  /**
   * Getter.
   * 
   * @return
   * @throws AdXException
   */
  public static ImmutableList<Integer> get_CAMPAIGN_DURATIONS() throws AdXException {
    Parameters.safeguard();
    return Parameters.CAMPAIGN_DURATIONS;
  }

  /**
   * Populate parameters from .ini file.
   * 
   * @param ini_file_location
   * @throws InvalidFileFormatException
   * @throws IOException
   * @throws AdXException
   */
  public static void populateParameters(String ini_file_location, String type_of_game) throws InvalidFileFormatException, IOException, AdXException {
    // Implements a singleton design pattern.
    // The parameters can only be initialized once.
    if (!Parameters.initialized) {
      if (!Parameters.allowableGames.contains(type_of_game)) {
        throw new AdXException("The type of game " + type_of_game + " is not among the valid options " + Parameters.allowableGames);
      }
      // Read the .ini file.
      Ini ini = new Ini(new File(ini_file_location));

      // Global parameters
      Parameters.SECONDS_WAIT_PLAYERS = Integer.parseInt(ini.get("PARAMETERS").get("SECONDS_WAIT_PLAYERS"));
      Parameters.SECONDS_DURATION_DAY = Integer.parseInt(ini.get("PARAMETERS").get("SECONDS_DURATION_DAY"));
      Parameters.NUMBER_AUCTION_CAMPAINGS = Integer.parseInt(ini.get("PARAMETERS").get("NUMBER_AUCTION_CAMPAINGS"));
      Parameters.QUALITY_SCORE_LEARNING_RATE = Double.parseDouble(ini.get("PARAMETERS").get("QUALITY_SCORE_LEARNING_RATE"));
      Parameters.TOTAL_SIMULATED_GAMES = Integer.parseInt(ini.get("PARAMETERS").get("TOTAL_SIMULATED_GAMES"));
      Parameters.POPULATION_SIZE = Integer.parseInt(ini.get("PARAMETERS").get("POPULATION_SIZE"));

      // Game-specific parameters.
      Parameters.TOTAL_SIMULATED_DAYS = Integer.parseInt(ini.get(type_of_game).get("TOTAL_SIMULATED_DAYS"));

      // Compute an immutable list of integers corresponding to the allowable duration of campaigns.
      String[] possibleCampaingsDurations = ini.get(type_of_game).get("CAMPAIGN_DURATIONS").split(",");
      Integer[] temp = new Integer[possibleCampaingsDurations.length];
      for (int i = 0; i < possibleCampaingsDurations.length; i++) {
        temp[i] = Integer.parseInt(possibleCampaingsDurations[i]);
      }
      Parameters.CAMPAIGN_DURATIONS = ImmutableList.copyOf(temp);

      // Mark the parameters as initialized, thus implementing the singleton pattern.
      Parameters.initialized = true;
    } else {
      throw new AdXException("Parameters already initialized.");
    }
  }

  public static void main(String[] args) throws InvalidFileFormatException, IOException, AdXException {
    // Parameters.populateParameters("config/config.ini", "ONE-DAY-ONE-CAMPAIGN");
    // Parameters.populateParameters("config/config.ini", "TWO-DAYS-ONE-CAMPAIGN");
    // Parameters.populateParameters("config/config.ini", "TWO-DAYS-TWO-CAMPAIGNS");
    Parameters.populateParameters(args[0], args[1]);
    int s = Parameters.get_SECONDS_DURATION_DAY();
    int c = Parameters.get_NUMBER_AUCTION_CAMPAINGS();
    double d = Parameters.get_QUALITY_SCORE_LEARNING_RATE();
    int n = Parameters.get_TOTAL_SIMULATED_GAMES();
    int p = Parameters.get_POPULATION_SIZE();
    int days = Parameters.get_TOTAL_SIMULATED_DAYS();
    ImmutableList<Integer> camp = Parameters.get_CAMPAIGN_DURATIONS();
    System.out.println(s);
    System.out.println(c);
    System.out.println(d);
    System.out.println(n);
    System.out.println(p);
    System.out.println(days);
    System.out.println(camp);
  }
}