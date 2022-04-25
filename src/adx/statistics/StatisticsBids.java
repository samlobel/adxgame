package adx.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.structures.Query;
import adx.util.InputValidators;
import adx.util.Logging;
import adx.util.Pair;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class StatisticsBids {
  /**
   * The statistics object.
   */
  protected final Statistics statisticsObj;

  /**
   * A maps Day -> Impressions -> List of bids
   */
  // protected final Table<Integer, Query, List<Pair<String,Double>>> bids;
  /**
   * A table Day -> Query -> #Auction -> List of (Agent, Bid)
   */
  protected final Table<Integer, Query, Map<Integer, List<Pair<String, Double>>>> bids;

  /**
   * Constructor.
   * 
   * @param statisticsObj
   * @throws AdXException
   */
  public StatisticsBids(Statistics statisticsObj) throws AdXException {
    InputValidators.validateNotNull(statisticsObj);
    this.statisticsObj = statisticsObj;
    this.bids = HashBasedTable.create();
  }

  /**
   * 
   * @param bids
   */
  public void addBids(int day, Query query, List<Pair<String, Double>> bids) {
    if (this.bids.get(day, query) == null) {
      this.bids.put(day, query, new HashMap<Integer, List<Pair<String, Double>>>());
    }
    Map<Integer, List<Pair<String, Double>>> map = this.bids.get(day, query);
    map.put(map.size(), bids);
  }

  /**
   * Produce the contents of a csv file with a line for each auction where each line has all the bids placed for the auction.
   * 
   * @param day
   * @return
   */
  public String logBidsToCSV(int day) {
    String csvContents = "";
    for (Entry<Query, Map<Integer, List<Pair<String, Double>>>> x : this.bids.row(day).entrySet()) {
      for (Entry<Integer, List<Pair<String, Double>>> y : x.getValue().entrySet()) {
        String acum = "";
        for (Pair<String, Double> w : y.getValue()) {
          acum += w.getElement2() + ",";
        }
        csvContents += acum.substring(0, acum.length() - 1) + "\n";
      }
    }
    return csvContents;
  }

  /**
   * Printer
   * 
   * @param day
   */
  public void printBids(int day) {
    int totalBids = 0;
    Logging.log("Log of bids for day = " + day);
    for (Entry<Query, Map<Integer, List<Pair<String, Double>>>> x : this.bids.row(day).entrySet()) {
      Logging.log("\t " + x.getKey());
      for (Entry<Integer, List<Pair<String, Double>>> y : x.getValue().entrySet()) {
        Logging.log("\t\t" + y.getKey() + "->" + y.getValue());
        totalBids++;
      }
    }
    Logging.log("Total bids = " + totalBids);
  }

}
