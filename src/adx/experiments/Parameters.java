package adx.experiments;

import java.io.IOException;

import adx.exceptions.AdXException;
import adx.util.Logging;

public class Parameters {

  private final int numberOfGames;

  private final double demandDiscountFactor;

  private final int numberOfImpressions;

  private final int reserve;

  public Parameters(int numberOfGames, double demandDiscountFactor, int numberOfImpressions, int reserve) {
    this.numberOfGames = numberOfGames;
    this.demandDiscountFactor = demandDiscountFactor;
    this.numberOfImpressions = numberOfImpressions;
    this.reserve = reserve;
  }

  public int getNumberOfGames() {
    return this.numberOfGames;
  }

  public double getDemandDiscountFactor() {
    return this.demandDiscountFactor;
  }

  public int getNumberOfImpressions() {
    return this.numberOfImpressions;
  }

  public int getReserve() {
    return this.reserve;
  }
  
  /**
   * Main.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException
   */
  public static Parameters getParameters(String[] args) {
    int numberOfGames;
    double demandDiscountFactor;
    int numberOfImpressions;
    int reserve;

    if (args.length > 0) {
      // Running experiments via command line
      numberOfGames = Integer.parseInt(args[0]);
      Logging.log("Received numberOfGames via command line parameter: " + numberOfGames);
      int demandDiscountFactorCommandLine = Integer.parseInt(args[1]);
      double[] gridOfDemandFactor = { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0 };
      demandDiscountFactor = gridOfDemandFactor[demandDiscountFactorCommandLine];
      Logging.log("Received demandDiscountFactor via command line parameter: " + demandDiscountFactor);
      numberOfImpressions = Integer.parseInt(args[2]);
      Logging.log("Received numberOfImpressions via command line parameter: " + numberOfImpressions);
      reserve = Integer.parseInt(args[3]);
      Logging.log("Received reserve via command line parameter: " + reserve);
    } else {
      // Running experiments manually
      numberOfGames = 200;
      Logging.log("Endogenous numberOfGames: " + numberOfGames);
      demandDiscountFactor = 3.0;
      Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
      numberOfImpressions = 2000;
      Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
      reserve = 0;
      Logging.log("Endogenous reserve: " + reserve);
    }
    return new Parameters(numberOfGames, demandDiscountFactor, numberOfImpressions, reserve);
  }
}
