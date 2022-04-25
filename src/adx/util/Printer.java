package adx.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.structures.Campaign;

/**
 * A class that implements static methods for string representation of complicated object.
 * 
 * @author Enrique Areyan Viqueira
 *
 */
public class Printer {

  /**
   * String representation of a list of campaigns.
   * 
   * @param listOfCampaigns
   * @return
   */
  public static String printNiceListMyCampaigns(List<Campaign> listOfCampaigns) {
    String ret = "";
    if (listOfCampaigns != null && listOfCampaigns.size() > 0) {
      for (Campaign c : listOfCampaigns) {
        ret += "\n\t\t" + c;
      }
    } else {
      ret += "No campaigns in the given list.";
    }
    return ret;
  }

  /**
   * String representation of statistics.
   * 
   * @param stats
   * @return
   */
  public static String getNiceStatsTable(Map<Integer, Pair<Integer, Double>> stats) {
    String ret = "";
    if (stats != null) {
      for (Entry<Integer, Pair<Integer, Double>> x : stats.entrySet()) {
        ret += "\n\tCampaign " + x.getKey() + ", Total Reach = " + x.getValue().getElement1() + ", Total Cost " + x.getValue().getElement2();
      }
    } 
    return ret;
  }

  /**
   * String representation of statistics.
   * 
   * @param stats
   * @return
   */
  public static String getNiceStatsTable(int day, Map<Integer, Pair<Integer, Double>> stats) {
    String ret = "";
    for (Entry<Integer, Pair<Integer, Double>> x : stats.entrySet()) {
      ret += "\n\tCampaign " + x.getKey() + ", Total Reach On Day " + day + " = " + x.getValue().getElement1() + ", Total Cost On Day " + day + "="
          + x.getValue().getElement2();
    }
    return ret;
  }

  /**
   * Prints a nice representation of the profits.
   * 
   * @param agentProfitPairList
   * @param day
   * @return
   */
  public static String getNiceProfitTable(List<Pair<String, Double>> agentProfitPairList, int day) {
    NumberFormat formatter = new DecimalFormat("#0.00");
    String ret = "\n\t################# RESULT ##################";
    ret += "\n\t####      Agent \t#  Profit \t###";
    ret += "\n\t###########################################";
    if (agentProfitPairList.size() > 0) {
      for (Pair<String, Double> x : agentProfitPairList) {
        ret += "\n\t### " + String.format("%12s", x.getElement1()) + " \t# " + String.format("%8s", formatter.format(x.getElement2())) + " \t###";
      }
      ret += "\n\t###########################################";
    } else {
      ret += "Currently, no profits are registered for day " + day;
    }
    return ret;
  }

}
