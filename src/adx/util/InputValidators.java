package adx.util;

import adx.exceptions.AdXException;

/**
 * A centralized class that validates for different inputs.
 * 
 * @author Enrique Areyan Viqueira
 */
public class InputValidators {

  /**
   * Checks if the given object is null.
   * 
   * @param object
   * @throws AdXException
   */
  public static void validateNotNull(Object object) throws AdXException {
    if (object == null) {
      throw new AdXException("The object cannot be null.");
    }
  }

  /**
   * Checks if the day is in the valid range.
   * 
   * @param day
   * @throws AdXException
   */
  public static void validateDay(int day) throws AdXException {
    if (day < 0) {
      throw new AdXException("The day cannot be a negative integer.");
    }
  }

  /**
   * Checks if the id of a campaign is in the valid range.
   * 
   * @param campaignId
   * @throws AdXException
   */
  public static void validateCampaignId(int campaignId) throws AdXException {
    if (campaignId <= 0) {
      throw new AdXException("The id of a campaign must be a positive integer.");
    }
  }

  /**
   * Checks if the id of a campaign is in the valid range.
   * 
   * @param campaignId
   * @throws AdXException
   */
  public static void validateCampaignDuration(int startDay, int endDay) throws AdXException {
    if (startDay <= 0 || endDay <= 0 || startDay > endDay) {
      throw new AdXException("Invalid campaign duration. A campaign cannot start on: " + startDay + " and end on: " + endDay);
    }
  }

  /**
   * Checks if the reach of a campaign is in the valid range.
   * 
   * @param reach
   * @throws AdXException
   */
  public static void validateCampaignReach(int reach) throws AdXException {
    if (reach <= 0) {
      throw new AdXException("The reach of a campaign must be a positive integer.");
    }
  }

  /**
   * Checks if the reach of a campaign is in the valid range.
   * 
   * @param budget
   * @throws AdXException
   */
  public static void validateCampaignBudget(double budget) throws AdXException {
    if (budget <= 0) {
      throw new AdXException("The budget of a campaign must be a positive double.");
    }
  }

  /**
   * Checks if a bid is in the valid range.
   * 
   * @param bid
   * @throws AdXException
   */
  public static void validateBid(double bid) throws AdXException {
    if (bid < 0) {
      throw new AdXException("The bid must be a non-negative double.");
    }
  }

  /**
   * Checks if a limit is in the valid range.
   * 
   * @param bid
   * @throws AdXException
   */
  public static void validateLimit(double limit) throws AdXException {
    if (limit <= 0) {
      throw new AdXException("The limit must be a positive double greater than zero.");
    }
  }

  /**
   * Checks if the supply is in the valid range.
   * 
   * @param supply
   * @throws AdXException
   */
  public static void validateSupply(int supply) throws AdXException {
    if (supply <= 0) {
      throw new AdXException("The supply of a query must be a positive integer.");
    }
  }

}
