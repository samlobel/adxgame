package sim.adx.experiments;

import java.io.IOException;

import adx.exceptions.AdXException;
import adx.experiments.Experiment;
import adx.experiments.ExperimentFactory;
import adx.experiments.Parameters;
import adx.util.Logging;

/**
 * Run one-day experiments.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayExperiments {

  /**
   * Data Directory Prefix
   */
  public static final String dataDirectoryPrefix = "/home/eareyanv/workspace/adxgame/data/";

  /**
   * Results Directory Prefix
   */
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
      Experiment allWEExperiment = ExperimentFactory.WEandWFAgents(j, 0, resultsDirectory, "/WEWF(" + j + "-0)", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
      allWEExperiment.runExperiment(true, true, true);
      Logging.log("All WF agents (" + j + ")");
      Experiment allWFExperiment = ExperimentFactory.WEandWFAgents(0, j, resultsDirectory, "/WEWF(0-" + j + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
      allWFExperiment.runExperiment(true, true, true);
      // Mix of agents.
      for (int l = 1; l < numberOfAgents + 1; l++) {
        Logging.log("SI and WE agents (" + j + "," + l + ")");
        Experiment SIandWEExperiment = ExperimentFactory.SIandWEAgents(j, l, resultsDirectory, "/SIWE(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWEExperiment.runExperiment(true, true, true);

        Logging.log("SI and WF agents (" + j + "," + l + ")");
        Experiment SIandWFExperiment = ExperimentFactory.SIandWFAgents(j, l, resultsDirectory, "/SIWF(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        SIandWFExperiment.runExperiment(true, true, true);

        Logging.log("WE and WF agents (" + j + "," + l + ")");
        Experiment WEandWFExperiment = ExperimentFactory.WEandWFAgents(j, l, resultsDirectory, "/WEWF(" + j + "-" + l + ")", numberOfGames, numberOfImpressions, demandDiscountFactor, 0.0);
        WEandWFExperiment.runExperiment(true, true, true);
      }
    }
  }
  
  public static void main(String[] args) throws AdXException, IOException {
    // Basic Experiments.
    Parameters parameters = Parameters.getParameters(args);
    OneDayExperiments.basicExperiments(parameters.getNumberOfGames(), parameters.getNumberOfImpressions(), parameters.getDemandDiscountFactor());    
  }

}
