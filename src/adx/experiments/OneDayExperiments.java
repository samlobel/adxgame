package adx.experiments;

import java.io.File;
import java.io.IOException;

import adx.exceptions.AdXException;
import adx.util.Logging;

/**
 * Run one-day experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayExperiments {

  public static final String dataDirectoryPrefix = "/home/eareyanv/workspace/adxgame/data/";

  public static final String resultsDirectoryPrefix = "/home/eareyanv/workspace/adxgame/data/results/";

  /**
   * Get the path of the results directory.
   * 
   * @param numberOfGames
   * @param numberOfImpressions
   * @param demandDiscountFactor
   * @return
   */
  public static String getResultsDirectory(String parentFolderName, int numberOfGames, int numberOfImpressions, double demandDiscountFactor) {
    return resultsDirectoryPrefix + parentFolderName + "/" + numberOfGames + "/" + numberOfImpressions + "/" + demandDiscountFactor + "/";
  }

  /**
   * Basic Experiments.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException
   */
  public static void basicExperiments(int numberOfGames, int numberOfImpressions, double demandDiscountFactor) throws AdXException, IOException {
    Logging.log("Basic Experiments");

    String resultsDirectory = OneDayExperiments.getResultsDirectory("all-agents-no-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Logging.log("Results folder: " + resultsDirectory);

    int numberOfAgents = 30;
    Logging.log("numberOfAgents: " + numberOfAgents);

    for (int j = 1; j < numberOfAgents + 1; j++) {
      // Pure agents profiles, we are only interested in pure WE and WF.
      Logging.log("All WE agents (" + j + ")");
      Experiment allWEExperiment = ExperimentFactory.WEandWFAgents(j, 0, resultsDirectory, "/WEWF(" + j + "-0)", numberOfGames, numberOfImpressions,
          demandDiscountFactor, 0.0);
      allWEExperiment.runExperiment(true, true);
      Logging.log("All WF agents (" + j + ")");
      Experiment allWFExperiment = ExperimentFactory.WEandWFAgents(0, j, resultsDirectory, "/WEWF(0-" + j + ")", numberOfGames, numberOfImpressions,
          demandDiscountFactor, 0.0);
      allWFExperiment.runExperiment(true, true);
      // Mix of agents.
      for (int l = 1; l < numberOfAgents + 1; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        Experiment SIandWEExperiment = ExperimentFactory.SIandWEAgents(j, l, resultsDirectory, "/SIWE(" + j + "-" + l + ")", numberOfGames,
            numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWEExperiment.runExperiment(true, true);

        Logging.log("SI and WF agents (" + j + "," + l + ")");
        Experiment SIandWFExperiment = ExperimentFactory.SIandWFAgents(j, l, resultsDirectory, "/SIWF(" + j + "-" + l + ")", numberOfGames,
            numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWFExperiment.runExperiment(true, true);

        Logging.log("WE and WF agents (" + j + "," + l + ")");
        Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(j, l, resultsDirectory, "/WEWF(" + j + "-" + l + ")", numberOfGames,
            numberOfImpressions, demandDiscountFactor, 0.0);
        WEandWFExperiment.runExperiment(true, true);
      }
    }
  }

  /**
   * Eight WE and WF agents, any combinations, varying reserve.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException
   */
  public static void eightWEWFAgentsVaryReserve(int numberOfGames, int numberOfImpressions, double demandDiscountFactor) throws AdXException, IOException {
    Logging.log("Eight Agents Vary Reserve Experiment");

    String resultsDirectory = OneDayExperiments.getResultsDirectory("8-agents-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Logging.log("Results folder: " + resultsDirectory);

    double reserve = 1000.0;
    int count = 0;
    //for (int r = 0; r < 131; r++) {
    int r = 1000;
      for (int i = 0; i < 9; i++) {
        Logging.log("WEWF(" + (8 - i) + "-" + i + "), r = " + reserve);
        File f = new File(resultsDirectory + "agents/WEWF(" + (8 - i) + "-" + i + ")-r(" + r + ").csv");
        if (f.exists() && !f.isDirectory()) {
          Logging.log("Experiment already ran, skipping");
        } else {
          Logging.log("Running Experiment");
          Experiment varyReserveExperiment = ExperimentFactory.WEandWFAgents(8 - i, i, resultsDirectory, "WEWF(" + (8 - i) + "-" + i + ")-r(" + r + ")",
              numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
          varyReserveExperiment.runExperiment(false, false);
        }
        count++;
      }
      //reserve += 1.0 / 100.0;
    //}
    Logging.log("Total Count " + count);
  }
  
  public static void eightWEWFAgentsFixedReserve(int numberOfGames, int numberWE, int numberWF, int numberOfImpressions, double demandDiscountFactor, int reserve) throws AdXException, IOException {
    String resultsDirectory = OneDayExperiments.getResultsDirectory("8-agents-fixed-reserve", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Experiment varyReserveExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "WEWF(" + numberWE + "-" + numberWF + ")-r(" + reserve + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserve * 0.01);
    varyReserveExperiment.runExperiment(false, true);
  }

  /**
   * Main.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException
   */
  public static void main(String[] args) throws AdXException, IOException {
    int numberOfGames;
    double demandDiscountFactor;
    int numberOfImpressions;

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
    } else {
      // Running experiments manually
      numberOfGames = 200;
      Logging.log("Endogenous numberOfGames: " + numberOfGames);
      demandDiscountFactor = 0.25;
      Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
      numberOfImpressions = 2000;
      Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
    }
    // Experiments so far.
    // OneDayExperiments.basicExperiments(numberOfGames, numberOfImpressions, demandDiscountFactor);
    // OneDayExperiments.eightWEWFAgentsVaryReserve(numberOfGames, numberOfImpressions, demandDiscountFactor);
    OneDayExperiments.eightWEWFAgentsFixedReserve(200, 0, 8, 2000, 0.25, 80);
  }

  public static void main2(String[] args) throws AdXException, IOException {
    int numberOfGames = 200;
    int numberOfImpressions = 2000;
    double demandDiscountFactor = 3.0;
    int numberWE = 0;
    int numberWF = 8;
    double reserve = 0.8361;
    String resultsDirectory = OneDayExperiments.getResultsDirectory("small-tests", numberOfGames, numberOfImpressions, demandDiscountFactor);
    Logging.log("Results folder: " + resultsDirectory);

    Experiment allWEExperiment = ExperimentFactory.WEandWFAgents(numberWE, numberWF, resultsDirectory, "/WEWF(" + numberWE + "-" + numberWF + ")-r(" + reserve
        + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
    allWEExperiment.runExperiment(true, true);
  }

}
