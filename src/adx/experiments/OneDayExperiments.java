package adx.experiments;

import java.io.IOException;

import adx.exceptions.AdXException;
import adx.util.Logging;

/**
 * Run one-day experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayExperiments {

  public static final String directoryPrefix = "/home/eareyanv/workspace/adxgame/data/results/";

  /**
   * Basic Experiments.
   * 
   * @param args
   * @throws AdXException
   * @throws IOException 
   */
  public static void basicExperiments(int numberOfGames, int numberOfImpressions, double demandDiscountFactor) throws AdXException, IOException {
    Logging.log("Basic Experiments");

    String resultsDirectory = directoryPrefix + numberOfGames + "/" + numberOfImpressions + "/" + demandDiscountFactor + "/";
    Logging.log("Results folder: " + resultsDirectory);
    
    int numberOfAgents = 30;
    Logging.log("numberOfAgents: " + numberOfAgents);

    for (int j = 1; j < numberOfAgents + 1; j++) {
      // Pure agents profiles, we are only interested in pure WE and WF.
      Logging.log("All WE agents (" + j + ")");
      Experiment allWEExperiment = ExperimentFactory.WEandWFAgents(j, 0, resultsDirectory, "/WEWF(" + j + "-0)", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
      allWEExperiment.runExperiment();
      Logging.log("All WF agents (" + j + ")");
      Experiment allWFExperiment = ExperimentFactory.WEandWFAgents(0, j, resultsDirectory, "/WEWF(0-" + j + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
      allWFExperiment.runExperiment();
      // Mix of agents.
      for (int l = 1; l < numberOfAgents + 1; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        Experiment SIandWEExperiment = ExperimentFactory.SIandWEAgents(j, l, resultsDirectory, "/SIWE(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWEExperiment.runExperiment();

        Logging.log("SI and WF agents (" + j + "," + l + ")");
        Experiment SIandWFExperiment = ExperimentFactory.SIandWFAgents(j, l, resultsDirectory, "/SIWF(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWFExperiment.runExperiment();

        Logging.log("WE and WF agents (" + j + "," + l + ")");
        Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(j, l, resultsDirectory, "/WEWF(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        WEandWFExperiment.runExperiment();
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

    String resultsDirectory = directoryPrefix + numberOfGames + "/" + "8-agents-varyreserve-" + demandDiscountFactor + "-" + numberOfImpressions + "/";
    Logging.log("Results folder: " + resultsDirectory);

    double reserve = 0.0;
    int count = 0;
    for (int r = 0; r < 131; r++) {
      for (int i = 0; i < 9; i++) {
        Logging.log("WEWF(" + (8 - i) + "-" + i + "), r = " + reserve);
        Experiment varyReserveExperiment = ExperimentFactory.WEandWFAgents(8 - i, i, resultsDirectory, "WEWF(" + (8 - i) + "-" + i + ")-r(" + r + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, reserve);
        varyReserveExperiment.runExperiment();
        count++;
      }
      reserve += 1.0 / 100.0;
    }
    Logging.log("Total Count " + count);
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
      demandDiscountFactor = Double.parseDouble(args[1]);
      Logging.log("Received demandDiscountFactor via command line parameter: " + demandDiscountFactor);
      numberOfImpressions = Integer.parseInt(args[2]);
      Logging.log("Received numberOfImpressions via command line parameter: " + numberOfImpressions);
    } else {
      // Running experiments manually
      numberOfGames = 100;
      Logging.log("Endogenous numberOfGames: " + numberOfGames);
      demandDiscountFactor = 0.25;
      Logging.log("Endogenous demandDiscountFactor: " + demandDiscountFactor);
      numberOfImpressions = 2000;
      Logging.log("Endogenous numberOfImpressions: " + numberOfImpressions);
    }
    // Experiments so far.
    OneDayExperiments.basicExperiments(numberOfGames, numberOfImpressions, demandDiscountFactor);
    //OneDayExperiments.eightWEWFAgentsVaryReserve(numberOfGames, numberOfImpressions, demandDiscountFactor);
  }

}
